package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.ShoppingCart;
import com.cclsr.eat.mapper.ShoppingCartMapper;
import com.cclsr.eat.service.ShoppingCartService;
import org.springframework.stereotype.Service;

@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart> implements ShoppingCartService {
}
