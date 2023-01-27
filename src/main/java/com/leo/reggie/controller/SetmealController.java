package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leo.reggie.common.R;
import com.leo.reggie.dto.SetmealDto;
import com.leo.reggie.entity.Category;
import com.leo.reggie.entity.Setmeal;
import com.leo.reggie.entity.SetmealDish;
import com.leo.reggie.service.CategoryService;
import com.leo.reggie.service.SetmealDishService;
import com.leo.reggie.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/setmeal")
@Slf4j
public class SetmealController {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private SetmealDishService setmealDishService;


    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){

        Page<Setmeal> page1 = new Page<>(page,pageSize);
        Page<SetmealDto> setmealDtoPage = new Page<>();


        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Setmeal::getName,name);
        lambdaQueryWrapper.orderByAsc(Setmeal::getUpdateTime);
        setmealService.page(page1,lambdaQueryWrapper);

        BeanUtils.copyProperties(page1,setmealDtoPage,"records");
        List<Setmeal> records = page1.getRecords();
        List<SetmealDto> list = records.stream().map((item) -> {
            SetmealDto setmealDto = new SetmealDto();
            //copy properties from item to setmealDto
            BeanUtils.copyProperties(item, setmealDto);
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);
            }

            return setmealDto;


        }).collect(Collectors.toList());

        setmealDtoPage.setRecords(list);



        return R.success(setmealDtoPage);
    }


    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){

        log.info("setmeal info:{}",setmealDto);
        setmealService.saveWithDish(setmealDto);


        return R.success("insert successfully");
    }

    @DeleteMapping
    public R<String> delete(@RequestParam List<Long> ids){

        setmealService.deleteWithDish(ids);

        return R.success("delete successfully");
    }
}
