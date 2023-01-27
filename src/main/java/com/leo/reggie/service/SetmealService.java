package com.leo.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.reggie.dto.SetmealDto;
import com.leo.reggie.entity.Setmeal;
import org.springframework.stereotype.Service;

import java.util.List;


public interface SetmealService extends IService<Setmeal> {

    public void saveWithDish(SetmealDto setmealDto);

    public void deleteWithDish(List<Long> ids);
}
