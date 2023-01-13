package com.rocky.bistro.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rocky.bistro.entity.Category;
import com.rocky.bistro.mapper.CategoryMapper;
import com.rocky.bistro.service.CategoryService;
import org.springframework.stereotype.Service;

@Service
public class CategoryImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService{
    @Override
    public void remove(Long id) {

    }
}
