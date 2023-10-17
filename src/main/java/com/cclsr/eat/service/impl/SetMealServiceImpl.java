package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.common.CustomException;
import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.entity.SetMealDish;
import com.cclsr.eat.entity.dto.SetMealDto;
import com.cclsr.eat.mapper.SetMealMapper;
import com.cclsr.eat.service.SetMealDishService;
import com.cclsr.eat.service.SetMealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, SetMeal> implements SetMealService {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private SetMealDishService setMealDishService;

    // 新增套餐 同时保存套餐和菜品的关联关系
    @Override
    @Transactional
    public void saveWithSetMealDish(SetMealDto setMealDto) {
        this.save(setMealDto);
        SetMealDish setMealDish = new SetMealDish();
        BeanUtils.copyProperties(setMealDto, setMealDish);

        List<SetMealDish> setMealDishList = setMealDto.getSetmealDishes();
        setMealDishList.stream().map((item) -> {
            item.setSetmealId(setMealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setMealDishService.saveBatch(setMealDishList);
    }

    @Override
    @Transactional
    public void deleteWithSetMealDish(List<Long> ids) {
        // 判断套餐是否停售
        LambdaQueryWrapper<SetMeal> setMealQueryWrapper = new LambdaQueryWrapper<>();
        setMealQueryWrapper.in(SetMeal::getId, ids);
        setMealQueryWrapper.eq(SetMeal::getStatus, 1);
        int count = this.count(setMealQueryWrapper);
        if(count > 0){
            throw new CustomException("套餐启售中，不能删除。。。");
        }
        this.removeByIds(ids);

        LambdaQueryWrapper<SetMealDish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.in(SetMealDish::getSetmealId, ids);
        setMealDishService.remove(dishLambdaQueryWrapper);
    }
}
