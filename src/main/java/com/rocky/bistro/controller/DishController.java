package com.rocky.bistro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocky.bistro.common.R;
import com.rocky.bistro.entity.Category;
import com.rocky.bistro.entity.Dish;
import com.rocky.bistro.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @GetMapping("/page")
    public R<Page<Dish>> page(int page, int pageSize){
        log.info("page = {},pageSize = {}" ,page,pageSize);
        //构造分页查询器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器

        return R.success(pageInfo);
    }
}
