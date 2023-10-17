package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.DishFlavor;
import com.cclsr.eat.mapper.DishFlavorMapper;
import com.cclsr.eat.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
