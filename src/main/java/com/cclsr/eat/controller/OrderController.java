package com.cclsr.eat.controller;

import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.dto.OrdersDto;
import com.cclsr.eat.service.OrdersService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrdersService ordersService;

    /**
     * 提交订单
     * @param ordersDto
     * @return
     */
    @PostMapping("/submit")
    public R<String> submit(@RequestBody OrdersDto ordersDto)
    {
        ordersService.submit(ordersDto);
        return null;
    }
}
