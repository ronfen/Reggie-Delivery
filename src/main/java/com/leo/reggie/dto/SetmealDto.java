package com.leo.reggie.dto;

import com.leo.reggie.entity.Setmeal;
import com.leo.reggie.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
