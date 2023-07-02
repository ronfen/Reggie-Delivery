package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leo.reggie.common.R;
import com.leo.reggie.dto.DishDto;
import com.leo.reggie.entity.Category;
import com.leo.reggie.entity.Dish;
import com.leo.reggie.entity.Employee;
import com.leo.reggie.service.CategoryService;
import com.leo.reggie.service.DishFlavorService;
import com.leo.reggie.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/dish")
@Slf4j
public class DishController {

    private DishService dishService;


    private DishFlavorService dishFlavorService;


    private CategoryService categoryService;

    public DishController(DishService dishService, DishFlavorService dishFlavorService, CategoryService categoryService) {
        this.dishService = dishService;
        this.dishFlavorService = dishFlavorService;
        this.categoryService = categoryService;
    }

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info(dishDto.toString());

        dishService.saveWithFlavor(dishDto);
        return R.success("add successfully");
    }

    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String name){
        Page<Dish> dishPage = new Page(page,pageSize);
        Page<DishDto> dishDtoPage = new Page<>();


        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.like(StringUtils.isNotEmpty(name),Dish::getName,name);
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage,lambdaQueryWrapper);

        BeanUtils.copyProperties(dishPage,dishDtoPage,"records");
        List<Dish> records = dishPage.getRecords();
        List<DishDto> list = records.stream().map((item) ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            Long categoryId =item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            String categoryName = category.getName();
            dishDto.setCategoryName(categoryName);

            return dishDto;
        }).collect(Collectors.toList());

        dishDtoPage.setRecords(list);

        return R.success(dishDtoPage);


    }

    @DeleteMapping
    public R<String> delete(long ids[]){


        for(int i = 0;i<ids.length;i++){
            dishService.removeById(ids[i]);
        }
        

        return R.success("delete successfully");
    }

    @GetMapping("/{id}")
    public R<DishDto> getById(@PathVariable long id){

        DishDto dishDto = dishService.getByIdWithFlavor(id);

        if(dishDto != null){
            return R.success(dishDto);
        }

        return R.error("No such dish.");
    }

    @PutMapping
    public R<String> updateById(@RequestBody DishDto dishDto){

        dishService.updateWithFlavor(dishDto);


        return R.success("update successfully.");


    }

    @PostMapping("/status/{status}")
    public R<String> updateStatusById(@PathVariable int status, @RequestParam List<Long> ids){
        log.info("status----"+ status);
        log.info("id-----"+ids);

        ids.forEach((id) ->{
            Dish dish = new Dish();
            dish.setId(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        });






        return R.success("update status successfully");
    }

    @GetMapping("/list")
    public R<List<Dish>> list(Dish dish) {

        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        lambdaQueryWrapper.eq(Dish::getStatus,1);
        lambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);

        List<Dish> list = dishService.list(lambdaQueryWrapper);
        return R.success(list);

    }

}
