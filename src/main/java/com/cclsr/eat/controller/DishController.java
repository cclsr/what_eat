package com.cclsr.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.entity.Dish;
import com.cclsr.eat.entity.DishFlavor;
import com.cclsr.eat.entity.dto.DishDto;
import com.cclsr.eat.service.CategoryService;
import com.cclsr.eat.service.DishFlavorService;
import com.cclsr.eat.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增菜品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto) {
        log.info(dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Dish::getName, name);
        queryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(pageInfo, queryWrapper);

        // 对象拷贝 忽略 record
        BeanUtils.copyProperties(pageInfo, dishDtoPage, "records");
        List<Dish> records = pageInfo.getRecords();
        List<DishDto> list = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            Long categoryId = item.getCategoryId();
            // 根据id查询分类对象
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            if (categoryName != null) {
                dishDto.setCategoryName(categoryName);
            }

            return dishDto;
        }).collect(Collectors.toList());
        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);
    }

    /**
     * 根据id查询菜品和对应的口味信息
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品更新成功");
    }

    //    @DeleteMapping
//    public R<String> delete(String ids) {
//        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
//        dishService.removeByIds(list);
//        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.in(DishFlavor::getDishId, list);
//        dishFlavorService.remove(queryWrapper);
//        return R.success("删除成功");
//    }
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        dishService.removeWithFlavor(ids);
        return R.success("删除成功");
    }

    @PostMapping("/status/{status}")
    public R<String> status(String ids, @PathVariable Integer status) {
        List<String> list = new ArrayList<>(Arrays.asList(ids.split(",")));
        Dish dish = new Dish();
        dish.setStatus(status);
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dish::getId, list);
        dishService.update(dish, queryWrapper);
        return R.success("菜品更新成功");
    }

    /**
     * 根据条件查询菜品列表
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish) {
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.like(dish.getName() != null, Dish::getName, dish.getName());
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> list = dishService.list(queryWrapper);
        // 封装口味数据
        List<DishDto> dishDtoList = list.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            LambdaQueryWrapper<DishFlavor> flavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
            flavorLambdaQueryWrapper.eq(DishFlavor::getDishId, item.getId());
            List<DishFlavor> flavors = dishFlavorService.list(flavorLambdaQueryWrapper);
            BeanUtils.copyProperties(item, dishDto);
            dishDto.setFlavors(flavors);
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtoList);
    }
}
