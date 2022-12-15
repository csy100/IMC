package com.csy.imc.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.imc.common.CustomException;
import com.csy.imc.dto.SetmealDto;
import com.csy.imc.entity.Setmeal;
import com.csy.imc.entity.SetmealDish;
import com.csy.imc.mapper.SetMealMapper;
import com.csy.imc.service.SetMealDishService;
import com.csy.imc.service.SetMealService;
import com.sun.org.apache.bcel.internal.generic.LMUL;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SetMealServiceImpl extends ServiceImpl<SetMealMapper, Setmeal> implements SetMealService {

    @Autowired
    private SetMealDishService setMealDishService;

    /**
     * 新增套餐及其关联的菜品
     *
     * @param setMealDto
     */
    @Transactional
    @Override
    public void saveWithDish(SetmealDto setMealDto) {
        this.save(setMealDto);
        for (SetmealDish setmealDish : setMealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setMealDto.getId());
        }

        setMealDishService.saveBatch(setMealDto.getSetmealDishes());

    }

    @Transactional
    @Override
    public void removeWithDish(List<Long> ids) {
        //查询套餐状态，确定是否可以删除
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Setmeal::getId,ids);
        queryWrapper.eq(Setmeal::getStatus,1);

        //如果不能删除，抛出一个业务异常
        int count = this.count(queryWrapper);
        if(count>0){
            throw new CustomException("套餐正在售卖中，不能删除");
        }
        //如果可以删除，先删除套餐表中的数据--setmeal
        this.removeByIds(ids);

        //删除关系表中的数据--setmeal_dish
        LambdaQueryWrapper<SetmealDish> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(SetmealDish::getSetmealId,ids);

        setMealDishService.remove(queryWrapper1);
    }

    @Override
    @Transactional
    public SetmealDto getByIdWithDish(Long id) {
        Setmeal setmeal = this.getById(id);
        SetmealDto setmealDto = new SetmealDto();
        BeanUtils.copyProperties(setmeal, setmealDto);

        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, id);
        List<SetmealDish> setmealDishList = setMealDishService.list(setmealDishLambdaQueryWrapper);
        setmealDto.setSetmealDishes(setmealDishList);
        return setmealDto;
    }

    @Override
    public void updateWithDish(SetmealDto setmealDto) {
        this.updateById(setmealDto);
        LambdaQueryWrapper<SetmealDish> setmealDishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealDishLambdaQueryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        setMealDishService.remove(setmealDishLambdaQueryWrapper);
        for (SetmealDish setmealDish : setmealDto.getSetmealDishes()) {
            setmealDish.setSetmealId(setmealDto.getId());
        }
        setMealDishService.saveBatch(setmealDto.getSetmealDishes());
    }
}


















