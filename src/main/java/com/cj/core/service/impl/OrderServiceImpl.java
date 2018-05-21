package com.cj.core.service.impl;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.mapper.TbOrderItemMapper;
import com.cj.core.mapper.TbOrderMapper;
import com.cj.core.mapper.TbOrderShippingMapper;
import com.cj.core.pojo.OrderInfo;
import com.cj.core.pojo.TbOrderItem;
import com.cj.core.pojo.TbOrderShipping;
import com.cj.core.service.OrderService;
import com.cj.core.utils.JedisClient;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author cj
 * @description 订单service
 * @date 2018/5/21
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;

    //注入JedisClient来生成订单号.
    @Autowired
    private JedisClient jedisClient;

    //从配置文件中注入#生成"订单id"在Redis中的key.
    @Value("${REDIS_ORDER_GEN_KEY}")
    private String REDIS_ORDER_GEN_KEY;
    //从配置文件中注入#订单号的初始值.(可以用字符串.因为Redis那边都是字符串,会自动转换).
    @Value("${ORDER_ID_BEGIN}")
    private String ORDER_ID_BEGIN;
    //"订单明细id"在Redis中的key.
    @Value("${REDIS_ORDER_DETAIL_GEN_KEY}")
    private String REDIS_ORDER_DETAIL_GEN_KEY;

    /**
     * "创建订单"服务
     *
     * @param orderInfo 自己写的用来封装请求数据的pojo
     * @return 封装了订单号的taotaoservice.
     * @author cj
     */
    @Override
    public TaotaoResult createOrder(OrderInfo orderInfo) {

        // 一、插入订单表
        // 1、接收数据OrderInfo
        // 2、生成"订单id"
        // 先从Redis中取出指定的订单号key并判断.
        // 如果这个订单号还不存在,那么就给它赋一个初始值.
        // 因为这个订单号如果还不存在的话,那么新生成的会默认是从1开始.
        // 作为订单号不合适,太小了.给用户体验不好.
        // 默认值也在配置文件中配置了.
        String id = jedisClient.get(REDIS_ORDER_GEN_KEY);
        //如果值为空,说明还不存在.
        if (StringUtils.isBlank(id)) {
            //如果订单号生成key不存在,就给它设置一个初始值.
            jedisClient.set(REDIS_ORDER_GEN_KEY, ORDER_ID_BEGIN);
            //上面这一块,在系统中只会执行一次.
            //一次后,就有值了.永远不会再进来.
        }
        //使用redis的incr命令生成订单号.需要jedis的客户端.以及spring配置中关于jedis的配置.
        //参数就是Redis中的key. 在上面定义的.
        Long orderId = jedisClient.incr(REDIS_ORDER_GEN_KEY);
        //3、补全字段
        //orderInfo对象是传入的.
        //补全主键订单号. 上面已经生成了.
        orderInfo.setOrderId(orderId.toString());
        //补全订单状态.
        //状态：1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderInfo.setStatus(1);
        Date date = new Date();
        //补全俩时间.
        orderInfo.setCreateTime(date);
        orderInfo.setUpdateTime(date);
        //payment_time字段,付款时间,不用补全了,因为不知道什么时候付款. 默认为空,就空吧.
        //`payment_time` datetime DEFAULT NULL COMMENT '付款时间',
        //`consign_time` datetime DEFAULT NULL COMMENT '发货时间',
        //`end_time` datetime DEFAULT NULL COMMENT '交易完成时间',
        //`close_time` datetime DEFAULT NULL COMMENT '交易关闭时间',
        //`buyer_nick` varchar(50) COLLATE utf8_bin DEFAULT NULL COMMENT '买家昵称',
        //`buyer_rate` int(2) DEFAULT NULL COMMENT '买家是否已经评价',
        //这些都不知道,就都默认为空好了.以后更新状态时再做.

        // 4、插入订单表
        /**
         注意,orderInfo是TbOrder的子类.
         虽然它也有自己额外的字段.但是确是可以直接插入到TbOrder这个pojo对应的表中的.
         多于的字段,有就有,插入时不取它们,没影响的.
         这个是新知识!!
         */
        orderMapper.insert(orderInfo);

        // 二、插入订单明细
        // 2、补全字段
        // 先从orderInfo对象中,取出来订单明细表的pojo集合(一对多). 是orderInfo的一个字段.
        List<TbOrderItem> orderItems = orderInfo.getOrderItems();
        //迭代.
        for (TbOrderItem orderItem : orderItems) {
            // 1、生成"订单明细id"，使用redis的incr命令生成。
            //注意,由于这次不是订单id,而是"订单明细id"了.
            //所以,在Redis中的key,要用一个新的,不要与"订单id"的key一样了!
            //配置中配置了个新的,上面注入了.
            //注意,订单明细id,不是给用户看的.所以不用设置大一些的初始值.让它默认从1开始即可.
            //在迭代循环内生成,每次迭代一个订单明细对象时,都会生成一个新的id.
            Long detailId = jedisClient.incr(REDIS_ORDER_DETAIL_GEN_KEY);
            //把生成的订单明细id,设置进订单明细对象里. 补全主键.
            orderItem.setId(detailId.toString());
            //补全订单明细pojo的订单号字段.
            orderItem.setOrderId(orderId.toString());
            //所有字段已经全部补全.其他字段都是由前端传过来的.
            // 3、插入数据
            /**
             注意逻辑.OrderItem作为OrderInfo对象的一个集合字段,
             在controller中已经接收了前端传过来的很多它自己的字段的值.
             然后在上面,从OrderInfo对象中把这个集合字段取出来,迭代出一个个的OrderItem对象.
             然后在迭代循环中,把他们的字段补全.
             最后在这里,用OrderItem自己的mapper,插入到OrderItem对应的订单明细表中.
             说白了:接值--拿出--迭代--补全--插入到数据库表.
             */
            orderItemMapper.insert(orderItem);
        }

        // 三、插入物流表
        //也是从OrderInfo中拿出来TbOrderShipping对象.
        //已经封装了前端传过来的很多字段中的数据.
        TbOrderShipping orderShipping = orderInfo.getOrderShipping();
        // 1、补全字段
        orderShipping.setOrderId(orderId.toString());
        orderShipping.setCreated(date);
        orderShipping.setUpdated(date);
        //其他字段,都从前端传入对象中了.
        // 2、插入数据
        //调用TbOrderShipping自己的mapper,把补全了字段的TbOrderShipping对象,插入到对应的物流表中.
        orderShippingMapper.insert(orderShipping);
        // 返回TaotaoResult,包装订单号。
        return TaotaoResult.ok(orderId);
    }
}
