package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.entity.Dish;
import com.rocky.bistro.mapper.DishMapper;
import com.rocky.bistro.service.DishService;
import org.springframework.stereotype.Service;

@Service
public class DishImpl extends ServiceImpl<DishMapper, Dish> implements DishService {
}
