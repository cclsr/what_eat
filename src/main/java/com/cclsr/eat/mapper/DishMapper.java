package com.cclsr.eat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.cclsr.eat.entity.Dish;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface DishMapper extends BaseMapper<Dish> {
}
