package com.csy.imc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csy.imc.dto.DishDto;
import com.csy.imc.dto.SetmealDto;
import com.csy.imc.entity.Setmeal;

import java.util.List;

public interface SetMealService extends IService<Setmeal> {
    void saveWithDish(SetmealDto setMealDto);

    void removeWithDish(List<Long> ids);

    SetmealDto getByIdWithDish(Long id);

    void updateWithDish(SetmealDto setmealDto);

}
