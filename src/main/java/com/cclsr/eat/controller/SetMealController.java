package com.cclsr.eat.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.cclsr.eat.common.R;
import com.cclsr.eat.entity.Category;
import com.cclsr.eat.entity.SetMeal;
import com.cclsr.eat.entity.dto.SetMealDto;
import com.cclsr.eat.service.CategoryService;
import com.cclsr.eat.service.SetMealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询套餐列表
     *
     * @param page
     * @param pageSize
     * @return
     */
    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name) {
        Page<SetMeal> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, SetMeal::getName, name);
        queryWrapper.orderByDesc(SetMeal::getUpdateTime);
        setMealService.page(pageInfo, queryWrapper);

        Page<SetMealDto> dtoPage = new Page<>();
        BeanUtils.copyProperties(pageInfo, dtoPage, "records");
        List<SetMeal> setMealList = pageInfo.getRecords();
        List<SetMealDto> list = setMealList.stream().map((item) -> {
            SetMealDto setMealDto = new SetMealDto();
            BeanUtils.copyProperties(item, setMealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setMealDto.setCategoryName(categoryName);
            }
            return setMealDto;
        }).collect(Collectors.toList());
        dtoPage.setRecords(list);
        return R.success(dtoPage);
    }

    /**
     * 保存套餐
     * @param setMealDto
     * @return
     */
    @PostMapping
    public R<String> save(@RequestBody SetMealDto setMealDto) {
        log.info("套餐信息：{}", setMealDto.toString());
        setMealService.saveWithSetMealDish(setMealDto);
        return R.success("套餐保存成功");
    }

    /**
     * 删除套餐
     * @param ids
     * @return
     */
    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids) {
        setMealService.deleteWithSetMealDish(ids);
        return R.success("套餐删除成功");
    }

    /**
     * 修改套餐状态
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R<String> status(@RequestParam List<Long> ids, @PathVariable Integer status) {
        LambdaQueryWrapper<SetMeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.in(SetMeal::getId, ids);
        SetMeal setMeal = new SetMeal();
        setMeal.setStatus(status);
        setMealService.update(setMeal, setMealLambdaQueryWrapper);
        return R.success("状态修改成功");
    }

    /**
     * 根据条件查询套餐
     * @param setMeal
     * @return
     */
    @GetMapping("/list")
    public R<List<SetMeal>> list(SetMeal setMeal){
        LambdaQueryWrapper<SetMeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setMeal.getCategoryId() != null, SetMeal::getCategoryId, setMeal.getCategoryId());
        queryWrapper.eq(setMeal.getStatus() != null, SetMeal::getStatus, setMeal.getStatus());
        queryWrapper.orderByDesc(SetMeal::getUpdateTime);
        List<SetMeal> list = setMealService.list(queryWrapper);
        return R.success(list);
    }
}
