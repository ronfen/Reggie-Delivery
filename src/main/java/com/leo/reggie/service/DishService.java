package com.leo.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.reggie.dto.DishDto;
import com.leo.reggie.entity.Dish;
import org.springframework.stereotype.Service;



public interface DishService extends IService<Dish> {

    public void saveWithFlavor(DishDto dishDto);

    public DishDto getByIdWithFlavor(long id);

    public void updateWithFlavor(DishDto dishDto);
}
