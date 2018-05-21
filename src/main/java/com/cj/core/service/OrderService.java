package com.cj.core.service;

import com.cj.common.pojo.TaotaoResult;
import com.cj.core.pojo.OrderInfo;

/**
 * @author cj
 * @description 订单service
 * @date 2018/5/21
 */
public interface OrderService {

    TaotaoResult createOrder(OrderInfo orderInfo);
}
