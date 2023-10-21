package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.common.BaseContext;
import com.cclsr.eat.common.CustomException;
import com.cclsr.eat.entity.*;
import com.cclsr.eat.entity.dto.OrdersDto;
import com.cclsr.eat.mapper.OrdersMapper;
import com.cclsr.eat.service.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
public class OrdersServiceImpl extends ServiceImpl<OrdersMapper, Orders> implements OrdersService {

    @Autowired
    private ShoppingCartService shoppingCartService;

    @Autowired
    private OrderDetailService orderDetailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AddressBookService addressBookService;

    @Override
    @Transactional
    public void submit(OrdersDto ordersDto) {
        // 获取用户
        Long userId = BaseContext.getCurrentId();
        // 获取购物车数据
        LambdaQueryWrapper<ShoppingCart> cartLambdaQueryWrapper = new LambdaQueryWrapper<>();
        cartLambdaQueryWrapper.eq(ShoppingCart::getUserId, userId);
        cartLambdaQueryWrapper.orderByDesc(ShoppingCart::getCreateTime);
        List<ShoppingCart> cartList = shoppingCartService.list(cartLambdaQueryWrapper);
        if (cartList == null || cartList.size() == 0) {
            throw new CustomException("购物车为空，结算失败");
        }

        User user = userService.getById(userId);

        AddressBook address = addressBookService.getById(ordersDto.getAddressBookId());

        if (address == null) {
            throw new CustomException("地址信息错误");
        }
        // 保存订单
        ordersDto.setUserId(userId);
        ordersDto.setUserName(user.getName());
        ordersDto.setPhone(address.getPhone());
        ordersDto.setOrderTime(LocalDateTime.now());
        ordersDto.setCheckoutTime(LocalDateTime.now());
        long orderId = IdWorker.getId();
        ordersDto.setNumber(String.valueOf(orderId));
        ordersDto.setStatus(2);
        AtomicInteger amount = new AtomicInteger(0);
        List<OrderDetail> detailList = cartList.stream().map((item) -> {
            OrderDetail detail = new OrderDetail();
            BeanUtils.copyProperties(item, detail);
            detail.setOrderId(orderId);
            amount.addAndGet(detail.getAmount().multiply(new BigDecimal(detail.getNumber())).intValue());
            return detail;
        }).collect(Collectors.toList());
        ordersDto.setAmount(new BigDecimal(amount.get()));
        ordersDto.setUserName(user.getName());
        ordersDto.setConsignee(address.getConsignee());
        ordersDto.setAddress((address.getProvinceName() == null ? "" : address.getProvinceName()) + (address.getCityName() == null ? "" : address.getCityName()) + (address.getDistrictName() == null ? "" : address.getDistrictName()) + (address.getDetail() == null ? "" : address.getDetail()));
        this.save(ordersDto);
        // 保存订单详情
        orderDetailService.saveBatch(detailList);
        // 清空购物车数据
        shoppingCartService.remove(cartLambdaQueryWrapper);
    }
}
