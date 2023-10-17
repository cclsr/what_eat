package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.SetMealDish;
import com.cclsr.eat.entity.dto.SetMealDto;
import com.cclsr.eat.mapper.SetMealDishMapper;
import com.cclsr.eat.service.SetMealDishService;
import com.cclsr.eat.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class SetMealDishServiceImpl extends ServiceImpl<SetMealDishMapper, SetMealDish> implements SetMealDishService {
}
