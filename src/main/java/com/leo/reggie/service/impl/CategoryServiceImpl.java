package com.leo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.reggie.common.CustomException;
import com.leo.reggie.entity.Category;
import com.leo.reggie.entity.Dish;
import com.leo.reggie.entity.Setmeal;
import com.leo.reggie.mapper.CategoryMapper;
import com.leo.reggie.service.CategoryService;
import com.leo.reggie.service.DishService;
import com.leo.reggie.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;


    @Autowired
    private SetmealService setmealService;

    @Override
    public void removeById(Long id) {

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Dish::getCategoryId,id);
        int count1 = dishService.count(lambdaQueryWrapper);
        if(count1>0){
            throw new CustomException("Can't delete when there's a dish under this category!");
        }

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.eq(Setmeal::getCategoryId,id);
        int count2 = setmealService.count(lambdaQueryWrapper1);
        if(count2>0){
            throw new CustomException("Can't delete when there's a set under this category!");
        }



        super.removeById(id);


    }
}
