package com.leo.reggie.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.leo.reggie.entity.OrderDetail;
import com.leo.reggie.mapper.OrderDetailMapper;
import com.leo.reggie.service.OrderDetailService;
import org.springframework.stereotype.Service;


@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail> implements OrderDetailService {
}
