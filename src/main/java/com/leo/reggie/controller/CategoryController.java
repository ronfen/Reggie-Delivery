package com.leo.reggie.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.Category;
import com.leo.reggie.entity.Employee;
import com.leo.reggie.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @PostMapping
    public R<String> save(@RequestBody Category category){

        service.save(category);


        return R.success("add successfully!");
    }

    @GetMapping("/page")
    public R<Page> page(int page, int pageSize, String name){
        log.info("name is {}", name);
        Page<Category> page1 = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();
        lambdaQueryWrapper.orderByAsc(Category::getSort);
        service.page(page1, lambdaQueryWrapper);
        return R.success(page1);
    }

    @DeleteMapping
    public R<String> delete(long id){

        //service.removeById(id);

        service.removeById(id);
        return R.success("delete successfully");
    }

    @PutMapping
    public R<String> update(@RequestBody Category category){

        service.updateById(category);

        return R.success("update successfully");
    }


    @GetMapping("/list")
    public R<List<Category>> list(Category category){

        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(category.getType() !=null, Category::getType,category.getType() );
        lambdaQueryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);

        List<Category> list = service.list(lambdaQueryWrapper);
        return R.success(list);

    }
}
