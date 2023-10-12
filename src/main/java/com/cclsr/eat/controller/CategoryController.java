package com.cclsr.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    public CategoryService categoryService;
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByDesc(Category::getUpdateTime);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }
}
