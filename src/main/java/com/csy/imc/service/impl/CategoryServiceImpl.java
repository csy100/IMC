package com.csy.imc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.imc.common.CustomException;
import com.csy.imc.entity.Category;
import com.csy.imc.entity.Dish;
import com.csy.imc.entity.Setmeal;
import com.csy.imc.mapper.CategoryMapper;
import com.csy.imc.service.CategoryService;
import com.csy.imc.service.DishService;
import com.csy.imc.service.SetMealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private DishService dishService;

    @Autowired
    private SetMealService setMealService;

    /**
     * 根据id删除分类
     * @param id
     */
    @Override
    public void remove(Long id) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(Dish::getCategoryId, id);
        int count1 = dishService.count(dishLambdaQueryWrapper);
        if (count1 > 0) {
            //已经关联菜品
            throw new CustomException("不能删除!");
        }

        LambdaQueryWrapper<Setmeal> setMealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setMealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        int count2 = setMealService.count(setMealLambdaQueryWrapper);
        if (count2 > 0) {
            //已经关联套餐
            throw new CustomException("不能删除!");
        }

        //未关联，删除该分类
        super.removeById(id);
    }
}
