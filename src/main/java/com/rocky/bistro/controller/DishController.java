package com.rocky.bistro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocky.bistro.common.R;
import com.rocky.bistro.dto.DishDto;
import com.rocky.bistro.entity.Category;
import com.rocky.bistro.entity.Dish;
import com.rocky.bistro.entity.DishFlavor;
import com.rocky.bistro.service.CategoryService;
import com.rocky.bistro.service.DishFlavorService;
import com.rocky.bistro.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @PostMapping
    public R<String> save(@RequestBody DishDto dishDto){
        log.info("新增菜品"+dishDto.toString());
        dishService.saveWithFlavor(dishDto);
        return R.success("新增菜品成功");
    }

    @GetMapping("/page")
    public R page(int page, int pageSize, String name) {
        log.info("开始查询......");
        //构造分页查询器
        Page<Dish> pageInfo = new Page<>(page, pageSize);
        Page<DishDto> dishPage = new Page<>();
        //条件构造器
        LambdaQueryWrapper<Dish> lambdaQueryWrapper = new LambdaQueryWrapper();
        //添加过滤条件
        lambdaQueryWrapper.like(name != null,Dish::getName,name);
        //添加排序条件
        lambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        //执行分页查询
        dishService.page(pageInfo,lambdaQueryWrapper);
        //将查询结果封装进新的page对象，作为返回数据
        BeanUtils.copyProperties(pageInfo,dishPage,"records");
        List<Dish> records = pageInfo.getRecords();
        //遍历records，把records里的dish对象转换成dishDto对象
        List<DishDto> dtoList = records.stream().map((item) -> {
            DishDto dishDto = new DishDto();
            //复制dish对象
            BeanUtils.copyProperties(item, dishDto);
            //根据dish中的categoryId查询数据库中的分类名字段
            Category category = categoryService.getById(item.getCategoryId());
            //是否为空
            if (category != null) {
                dishDto.setCategoryName(category.getName());
            }
            return dishDto;
        }).collect(Collectors.toList());
        //将新的records对象赋给dishPage
        dishPage.setRecords(dtoList);
        return R.success(dishPage);
    }

    @GetMapping("/{id}")
    public R<DishDto> queryById(@PathVariable Long id){
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    @PutMapping
    public R update(@RequestBody DishDto dishDto){
        dishService.updateByIdWithFlavor(dishDto);
        return R.success("修改成功");
    }

    @GetMapping("/list")
    public R getList(Dish dish){
        log.info("开始根据分类查询菜品信息...");
        //构造条件查询器，根据传递的参数是分类id或菜品名字查询
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(dish.getName()),Dish::getName,dish.getName());
        queryWrapper.eq(null != dish.getCategoryId(),Dish::getCategoryId,dish.getCategoryId());
        queryWrapper.eq(Dish::getStatus,1);
        List<Dish> list = dishService.list(queryWrapper);
        //将list中的dish对象转换成dishDto对象
        List<DishDto> dishDtos = list.stream().map(item ->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item, dishDto);
            //根据id查分类名
            Category category = categoryService.getById(item.getCategoryId());
            if (category != null){
                dishDto.setCategoryName(category.getName());
            }
            //查询口味信息
            LambdaQueryWrapper<DishFlavor> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(DishFlavor::getDishId,item.getId());
            dishDto.setFlavors(dishFlavorService.list(queryWrapper1));
            return dishDto;
        }).collect(Collectors.toList());
        return R.success(dishDtos);
    }
}
