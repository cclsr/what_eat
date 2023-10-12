package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.mapper.CategoryMapper;
import com.cclsr.eat.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
}
