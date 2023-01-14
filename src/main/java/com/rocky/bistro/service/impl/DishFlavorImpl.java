package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.entity.DishFlavor;
import com.rocky.bistro.mapper.DishFlavorMapper;
import com.rocky.bistro.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
