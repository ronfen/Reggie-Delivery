package com.leo.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.leo.reggie.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;

public interface CategoryService extends IService<Category> {




    /**
     * custmoize a method to improve the code,
     *
     * check if there's other records related to it before removal.
     *
     * @param id
     */
    public void removeById(Long id);
}
