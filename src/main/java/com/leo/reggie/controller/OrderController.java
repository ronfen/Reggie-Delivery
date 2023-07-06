package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.leo.reggie.common.BaseContext;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.Order;
import com.leo.reggie.entity.ShoppingCart;
import com.leo.reggie.service.OrderService;
import com.leo.reggie.service.ShoppingCartService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@Slf4j
public class OrderController {

    private OrderService orderService;

    private ShoppingCartService shoppingCartService;

    public OrderController(OrderService orderService, ShoppingCartService shoppingCartService) {
        this.orderService = orderService;
        this.shoppingCartService = shoppingCartService;
    }


    @PostMapping("/submit")
    public R<String> submit(@RequestBody Order order){

        //after placing the order, clean up the trolley.
        LambdaUpdateWrapper<ShoppingCart> lambdaUpdateWrapper = new LambdaUpdateWrapper<>();
        lambdaUpdateWrapper.eq(ShoppingCart::getUserId, BaseContext.getCurrentId());
        shoppingCartService.remove(lambdaUpdateWrapper);

        return R.success("Order Successful!");
    }
}
