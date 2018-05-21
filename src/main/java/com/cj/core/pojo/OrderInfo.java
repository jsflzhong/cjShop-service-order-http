package com.cj.core.pojo;

import java.util.List;

/**
 * @author cj
 * @description orderDTO
 * @date 2018/5/21
 */
public class OrderInfo extends TbOrder {

    //继承TbOrder对象的所有字段之后,的额外第一个字段.
    //对应"tb_order_item" 订单明细表.
    //是个List集合.看接口文档中的JSON数据格式便知.
    //注意字段名,不要随意些,接口文档中的参数说明中的JSON数据中,已经指定了.
    private List<TbOrderItem> orderItems; //一对多.所以是个集合.
    //额外第二个字段.
    //对应"tb_order_shipping" 物流表.
    //是个pojo对象.看接口文档中的JSON数据格式便知.
    //注意字段名,不要随意些,接口文档中的参数说明中的JSON数据中,已经指定了.
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
