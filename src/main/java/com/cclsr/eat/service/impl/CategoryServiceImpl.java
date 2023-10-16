package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.common.CustomException;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.entity.Dish;
import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.mapper.CategoryMapper;
import com.cclsr.eat.service.CategoryService;
import com.cclsr.eat.service.DishService;
import com.cclsr.eat.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    public DishService dishService;

    @Autowired
    public SetMealService setMealService;

    /**
     * 根据id删除分类，删除之前进行判断
     *
     * @param id
     */
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int dishCount = dishService.count(dishLambdaQueryWrapper);
        if (dishCount > 0) {
            throw new CustomException("当前分类下关联了菜品，不能删除");
        }

        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(SetMeal::getCategoryId, id);
        int setMealCount = setMealService.count(setMealLambdaQueryWrapper);
        if (setMealCount > 0) {
            throw new CustomException("当前分类下关联了套餐，不能删除");
        }

        super.removeById(id);
    }
}
