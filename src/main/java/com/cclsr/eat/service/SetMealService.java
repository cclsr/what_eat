package com.cclsr.eat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.entity.dto.SetMealDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SetMealService extends IService<SetMeal> {
    // 新增套餐 同时保存套餐和菜品的关联关系
    public void saveWithSetMealDish(SetMealDto setMealDto);

    // 删除套餐 同时删除套餐和菜品的关联关系
    public void deleteWithSetMealDish(List<Long> ids);
}
