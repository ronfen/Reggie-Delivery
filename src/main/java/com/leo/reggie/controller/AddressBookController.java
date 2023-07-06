package com.leo.reggie.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.leo.reggie.common.BaseContext;
import com.leo.reggie.common.R;
import com.leo.reggie.entity.AddressBook;
import com.leo.reggie.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {

    private AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook){

        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        log.info("address book is saved");
        return R.success(addressBook);
    }

    @PutMapping("/default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook){
        log.info("address book: {}", addressBook);
        LambdaUpdateWrapper<AddressBook> lambdaQueryWrapper = new LambdaUpdateWrapper<>();
        lambdaQueryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.set(AddressBook::getIsDefault, 0);
        addressBookService.update(lambdaQueryWrapper);

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);

        return R.success(addressBook);
    }

    @GetMapping("/default")
    public R<AddressBook> getDefault(){
        log.info("get the default address.");
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(AddressBook::getIsDefault, 1);
        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        AddressBook addressBook = addressBookService.getOne(wrapper);

        return R.success(addressBook);
    }

    @GetMapping("/list")
    public R<List<AddressBook>> getAllAddressBooks(){

        LambdaQueryWrapper<AddressBook> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(null != BaseContext.getCurrentId(),AddressBook::getUserId, BaseContext.getCurrentId());
        lambdaQueryWrapper.orderByDesc(AddressBook::getUpdateTime);
        List<AddressBook> list = addressBookService.list(lambdaQueryWrapper);
        return R.success(list);


    }
}
