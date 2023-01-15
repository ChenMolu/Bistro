package com.rocky.bistro.dto;

import com.rocky.bistro.entity.Setmeal;
import com.rocky.bistro.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
