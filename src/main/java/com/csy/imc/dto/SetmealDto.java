package com.csy.imc.dto;

import com.csy.imc.entity.Setmeal;
import com.csy.imc.entity.SetmealDish;
import lombok.Data;

import java.util.List;

@Data
public class SetmealDto extends Setmeal {

    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
