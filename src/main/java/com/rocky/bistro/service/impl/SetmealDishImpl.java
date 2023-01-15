package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.entity.SetmealDish;
import com.rocky.bistro.mapper.SetmealDishMapper;
import com.rocky.bistro.service.SetmealDishService;
import org.springframework.stereotype.Service;

@Service
public class SetmealDishImpl extends ServiceImpl<SetmealDishMapper, SetmealDish> implements SetmealDishService {
}
