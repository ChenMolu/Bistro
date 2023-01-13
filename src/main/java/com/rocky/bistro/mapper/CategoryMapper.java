package com.rocky.bistro.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.rocky.bistro.entity.Category;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CategoryMapper extends BaseMapper<Category> {
}
