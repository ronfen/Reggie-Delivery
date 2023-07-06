package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.leo.reggie.common.BaseContext;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.ShoppingCart;
import com.leo.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/shoppingCart")
@Slf4j
public class ShoppingCartController {

    private ShoppingCartService shoppingCartService;

    public ShoppingCartController(ShoppingCartService shoppingCartService) {
        this.shoppingCartService = shoppingCartService;
    }


    @PostMapping("/add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart){

        //set userId for the trolley
        shoppingCart.setUserId(BaseContext.getCurrentId());

        Long dishId = shoppingCart.getDishId();

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());

        if(dishId != null){
            wrapper.eq(ShoppingCart::getDishId, dishId);
        }else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart one = shoppingCartService.getOne(wrapper);

        //check if the trolley exists
        if(one!= null){
            Integer number = one.getNumber();
            one.setNumber(number+1);
            one.setCreateTime(LocalDateTime.now());
            shoppingCartService.updateById(one);

        }else {
            shoppingCart.setNumber(1);
            shoppingCart.setCreateTime(LocalDateTime.now());
            shoppingCartService.save(shoppingCart);
            one = shoppingCart;
        }

        return R.success(one);
    }


    @GetMapping("/list")
    public R<List<ShoppingCart>> list(){
        log.info("View Shopping Cart...");
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        wrapper.orderByAsc(ShoppingCart::getCreateTime);
        List<ShoppingCart> list = shoppingCartService.list(wrapper);

        return R.success(list);


    }

    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(wrapper);


        return R.success("Removed Successfully!");
    }

}
