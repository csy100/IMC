package com.csy.imc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.imc.dto.DishDto;
import com.csy.imc.entity.Dish;
import com.csy.imc.entity.DishFlavor;
import com.csy.imc.mapper.DishMapper;
import com.csy.imc.service.DishFlavorService;
import com.csy.imc.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish> implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    /**
     * 保存
     * @param dishDto
     */
    @Override
    @Transactional
    public void saveWithFlavor(DishDto dishDto) {
        this.save(dishDto);

        Long id = dishDto.getId();

        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(id);
        }

        dishFlavorService.saveBatch(dishDto.getFlavors());
    }

    /**
     * 根据id查询菜品信息和对应的口味信息
     *
     * @param id
     */
    @Override
    @Transactional
    public DishDto getByIdWithFlavor(Long id) {
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, id);
        List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorLambdaQueryWrapper);
        dishDto.setFlavors(dishFlavorList);

        return dishDto;
    }

    /**
     * 更新
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        this.updateById(dishDto);

        LambdaQueryWrapper<DishFlavor> dishFlavorLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishFlavorLambdaQueryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(dishFlavorLambdaQueryWrapper);

        for (DishFlavor flavor : dishDto.getFlavors()) {
            flavor.setDishId(dishDto.getId());
        }
        dishFlavorService.saveBatch(dishDto.getFlavors());

    }

    /**
     * 删除
     * @param ids
     */
    @Override
    @Transactional
    public void deleteByIdWithFlavor(List<Long> ids) {
        this.removeByIds(ids);

        for (Long id : ids) {
            DishFlavor dishFlavor = dishFlavorService.getById(id);
            dishFlavorService.removeById(dishFlavor);
        }

    }
}
