package com.rocky.bistro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocky.bistro.common.R;
import com.rocky.bistro.entity.Category;
import com.rocky.bistro.entity.Employee;
import com.rocky.bistro.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R Save(@RequestBody Category category){
        log.info("增加分类"+category.toString());
        categoryService.save(category);
        return R.success("添加成功");
    }

    @GetMapping("/page")
    public R<Page<Category>> page(int page, int pageSize){
        log.info("page = {},pageSize = {}" ,page,pageSize);
        //构造分页查询器
        Page pageInfo = new Page(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Category> lambdaQueryWrapper = new LambdaQueryWrapper();

        lambdaQueryWrapper.orderByAsc(Category::getSort);

        categoryService.page(pageInfo,lambdaQueryWrapper);
        return R.success(pageInfo);
    }

    @DeleteMapping
    public R<String> delete(Long ids){
        log.info("删除该分类: "+ids.toString());
        categoryService.removeById(ids);
        return R.success("删除成功");
    }

    @PutMapping
    public R<String> change(@RequestBody Category category){
        log.info("修改分类");
        categoryService.updateById(category);
        return R.success("修改成功");
    }

    /**
     * 根据条件查询菜品列表
     * @param type
     * @return
     */
    @GetMapping("/list")
    public R list(Integer type){
        LambdaQueryWrapper<Category> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(type != null,Category::getType,type);
        //添加排序条件
        queryWrapper.orderByAsc(Category::getSort).orderByDesc(Category::getUpdateTime);
        List<Category> list = categoryService.list(queryWrapper);
        return R.success(list);
    }

}
