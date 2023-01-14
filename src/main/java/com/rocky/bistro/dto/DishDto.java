package com.rocky.bistro.dto;

import com.rocky.bistro.entity.Dish;
import com.rocky.bistro.entity.DishFlavor;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class DishDto extends Dish implements Serializable {

    //菜品口味
    private List<DishFlavor> Flavors;
    //所属分类名
    private String categoryName;
    //
    private Integer copies;

}