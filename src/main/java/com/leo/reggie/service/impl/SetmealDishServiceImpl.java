package com.leo.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.reggie.entity.SetmealDish;
import com.leo.reggie.mapper.SetmealDishMapper;
import com.leo.reggie.service.SetmealDishService;
import org.springframework.stereotype.Service;


@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
