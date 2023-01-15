package com.rocky.bistro.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.rocky.bistro.common.R;
import com.rocky.bistro.dto.SetmealDto;
import com.rocky.bistro.entity.Category;
import com.rocky.bistro.entity.Setmeal;
import com.rocky.bistro.entity.SetmealDish;
import com.rocky.bistro.service.CategoryService;
import com.rocky.bistro.service.SetmealDishService;
import com.rocky.bistro.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@RequestMapping("/setmeal")
public class SetMealControler {

    @Autowired
    private SetmealService setmealService;

    @Autowired
    private SetmealDishService setmealDishService;

    @Autowired
    private CategoryService categoryService;

    @PostMapping
    public R<String> save(@RequestBody SetmealDto setmealDto){
        log.info("新增套餐: " + setmealDto.toString() );
        setmealService.saveWithDishes(setmealDto);
        return R.success("新增成功");
    }

    @GetMapping("/page")
    public R Page(int page,int pageSize, String name){
        log.info("进入到套餐分页查询方法");
        Page<Setmeal> stemealPage = new Page<>(page,pageSize);
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name!= null,Setmeal::getName,name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setmealService.page(stemealPage,queryWrapper);
        //创建setmealDto实体对象，用来封转返回给前端的信息
        List<Setmeal> records = stemealPage.getRecords().stream().map(item ->{
            LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(item,setmealDto);
            //获取分类id查询分类表
            Long categoryId = item.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null){
                setmealDto.setCategoryName(category.getName());
            }
            //根据套餐id查询关联的菜品信息
            Long id = item.getId();
            queryWrapper1.eq(SetmealDish::getSetmealId,id);
            List<SetmealDish> list = setmealDishService.list(queryWrapper1);
            if (list.size() > 0){
                setmealDto.setSetmealDishes(list);
            }
            return setmealDto;
        }).collect(Collectors.toList());
        stemealPage.setRecords(records);
        return R.success(stemealPage);
    }

    @DeleteMapping
    public R delete(@RequestParam List<Long> ids){
        log.info("删除套餐"+ids.toString());
        for(Long id : ids){
            setmealService.deleteWithDishes(id);
        }
        return R.success("删除成功");
    }
}
