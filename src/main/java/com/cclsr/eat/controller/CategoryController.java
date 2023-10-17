package com.cclsr.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize) {
        Page pageInfo = new Page(page, pageSize);
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        categoryService.page(pageInfo, queryWrapper);
        return R.success(pageInfo);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody Category category) {
        log.info("传入的参数：", category.toString());
        categoryService.save(category);
        return R.success("新增分类成功");
    }

    /**
     * 删除分类
     *
     * @param id
     * @return
     */
    @DeleteMapping
    public R<String> delete(Long id) {
        categoryService.remove(id);
        return R.success("删除分类成功");
    }

    /**
     * 更新分类
     *
     * @param category
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody Category category) {
        categoryService.updateById(category);
        return R.success("修改分类成功");
    }

    /**
     * 获取菜品分类
     *
     * @param category
     * @return
     */
    @GetMapping("/list")
    public R<List<Category>> list(Category category) {
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(category.getType() != null, Category::getType, category.getType());
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }
}
