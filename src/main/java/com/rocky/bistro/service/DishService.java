package com.rocky.bistro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rocky.bistro.dto.DishDto;
import com.rocky.bistro.entity.Dish;
import org.springframework.stereotype.Service;


public interface DishService extends IService<Dish> {
    public void saveWithFlavor(DishDto dishDto);
}
