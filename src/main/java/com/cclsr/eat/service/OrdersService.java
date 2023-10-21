package com.cclsr.eat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cclsr.eat.entity.Orders;
import com.cclsr.eat.entity.dto.OrdersDto;
import org.springframework.stereotype.Service;

@Service
public interface OrdersService extends IService<Orders> {

    public void submit(OrdersDto ordersDtoß);
}
