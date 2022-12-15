package com.csy.imc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csy.imc.dto.DishDto;
import com.csy.imc.entity.Dish;

import java.util.List;

public interface DishService extends IService<Dish> {
    void saveWithFlavor(DishDto dishDto);

    DishDto getByIdWithFlavor(Long id);

    void updateWithFlavor(DishDto dishDto);

    void deleteByIdWithFlavor(List<Long> ids);
}
