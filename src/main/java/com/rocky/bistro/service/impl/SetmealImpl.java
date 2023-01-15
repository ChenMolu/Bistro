package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.dto.SetmealDto;
import com.rocky.bistro.entity.Setmeal;
import com.rocky.bistro.entity.SetmealDish;
import com.rocky.bistro.mapper.SetmealMapper;
import com.rocky.bistro.service.SetmealDishService;
import com.rocky.bistro.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SetmealImpl extends ServiceImpl<SetmealMapper, Setmeal> implements SetmealService {

    @Autowired
    private SetmealDishService setmealDishService;

    @Override
    @Transactional
    public void saveWithDishes(SetmealDto setmealDto) {
        this.save(setmealDto);
        //获取菜品信息
        List<SetmealDish> dishes = setmealDto.getSetmealDishes();
        dishes.stream().map(item -> {
            item.setSetmealId(setmealDto.getId());
            return item;
        }).collect(Collectors.toList());
        setmealDishService.saveBatch(dishes);
    }

    @Override
    @Transactional
    public void deleteWithDishes(Long id) {
        this.removeById(id);
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SetmealDish::getSetmealId,id);
        setmealDishService.remove(queryWrapper);
    }


}
