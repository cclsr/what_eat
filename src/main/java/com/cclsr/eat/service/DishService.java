package com.cclsr.eat.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.cclsr.eat.entity.Dish;
import com.cclsr.eat.entity.dto.DishDto;
import org.springframework.stereotype.Service;

@Service
public interface DishService extends IService<Dish> {
    // 新增菜品，同时插入菜品对应的口味数据，需要操作两张表 dish dish_flavor
    public void saveWithFlavor(DishDto dishDto);

    // 根据菜品id 查询菜品信息和口味信息
    public DishDto getByIdWithFlavor(Long id);

    public void updateWithFlavor(DishDto dishDto);
}
