package com.leo.reggie.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.reggie.common.CustomException;
import com.leo.reggie.dto.SetmealDto;
import com.leo.reggie.entity.Dish;
import com.leo.reggie.entity.Setmeal;
import com.leo.reggie.entity.SetmealDish;
import com.leo.reggie.mapper.SetmealMapper;
import com.leo.reggie.service.SetmealDishService;
import com.leo.reggie.service.SetmealService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDish(SetmealDto setmealDto) {
        this.save(setmealDto);
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.stream().map((item) ->{
           item.setSetmealId(setmealDto.getId());
           return item;
        }).collect(Collectors.toList());

        setmealDishService.saveBatch(setmealDishes);
    }

    /**
     * remove the set and the dishes in a relational table
     * @param ids
     */
    @Override
    @Transactional
    public void deleteWithDish(List<Long> ids) {

        LambdaQueryWrapper<Setmeal> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.in(Setmeal::getId,ids);
        lambdaQueryWrapper.eq(Setmeal::getStatus,1);

        int count = this.count(lambdaQueryWrapper);
        if(count > 0){
            throw new CustomException("Setmeal is on sale, cannot delete!");
        }

        this.removeByIds(ids);


        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper1 = new LambdaQueryWrapper<>();
        lambdaQueryWrapper1.in(SetmealDish::getSetmealId, ids);
        setmealDishService.remove(lambdaQueryWrapper1);

    }

    @Override
    public SetmealDto getWithSetDish(Long id) {
        Setmeal setMeal = this.getById(id);
        
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setMeal,setmealDto);



        LambdaQueryWrapper<SetmealDish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(SetmealDish::getSetmealId,setmealDto.getId());
        List<SetmealDish> list = setmealDishService.list(lambdaQueryWrapper);
        setmealDto.setSetmealDishes(list);


        return setmealDto;

    }
}
