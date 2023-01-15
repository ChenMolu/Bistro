package com.rocky.bistro.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.rocky.bistro.dto.SetmealDto;
import com.rocky.bistro.entity.Setmeal;


public interface SetmealService extends IService<Setmeal> {

    public void saveWithDishes(SetmealDto setmealDto);
    public void deleteWithDishes(Long id);
}
