package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.dto.DishDto;
import com.rocky.bistro.entity.Dish;
import com.rocky.bistro.entity.DishFlavor;
import com.rocky.bistro.mapper.DishMapper;
import com.rocky.bistro.service.DishFlavorService;
import com.rocky.bistro.service.DishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class DishImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId();

        List<DishFlavor> dishFlavorList = dishDto.getFlavors().stream().map(item -> {
            item.setDishId(id);
            return item;
        }).collect(Collectors.toList());
        dishFlavorService.saveBatch(dishFlavorList);
    }
}
