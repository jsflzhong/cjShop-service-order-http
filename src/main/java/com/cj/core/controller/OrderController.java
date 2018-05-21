package com.cj.core.controller;

import com.cj.common.pojo.TaotaoResult;
import com.cj.common.utils.ExceptionUtil;
import com.cj.core.pojo.OrderInfo;
import com.cj.core.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author cj
 * @description 订单的controller
 * @date 2018/5/21
 */
@Controller
public class OrderController {

    @Autowired
    private OrderService orderService;

    /**
     * "创建订单"服务
     * 请求数据为JSON.所以"形参上"得用@ResponseBody注解.
     *
     * @param orderInfo Order DTO
     * @return 2016年7月3日
     */
    @RequestMapping(value = "/order/create", method = RequestMethod.POST)
    @ResponseBody
    public TaotaoResult createOrder(@RequestBody OrderInfo orderInfo) {
        try {
            TaotaoResult result = orderService.createOrder(orderInfo);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return TaotaoResult.build(500, ExceptionUtil.getStackTrace(e));
        }
    }
}
