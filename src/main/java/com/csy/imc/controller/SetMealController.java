package com.csy.imc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csy.imc.common.Result;
import com.csy.imc.dto.DishDto;
import com.csy.imc.dto.SetmealDto;
import com.csy.imc.entity.Category;
import com.csy.imc.entity.Setmeal;
import com.csy.imc.service.CategoryService;
import com.csy.imc.service.SetMealDishService;
import com.csy.imc.service.SetMealService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/setmeal")
public class SetMealController {

    @Autowired
    private SetMealService setMealService;

    @Autowired
    private SetMealDishService setMealDishService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增药包功能
     * @param setMealDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody SetmealDto setMealDto) {
        setMealService.saveWithDish(setMealDto);
        return Result.success("新增药品成功!");
    }


    /**
     * 药包分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name){
        Page<Setmeal> pageInfo = new Page<>(page, pageSize);
        Page<SetmealDto> dtoPageInfo = new Page<>();

        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(name != null, Setmeal::getName, name);
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);
        setMealService.page(pageInfo, queryWrapper);

        BeanUtils.copyProperties(pageInfo,dtoPageInfo,"records");

        List<SetmealDto> setmealDtoList = new ArrayList<>();
        for (Setmeal record : pageInfo.getRecords()) {
            SetmealDto setmealDto = new SetmealDto();
            BeanUtils.copyProperties(record, setmealDto);

            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                setmealDto.setCategoryName(categoryName);

                setmealDtoList.add(setmealDto);
            }
        }

        dtoPageInfo.setRecords(setmealDtoList);

        return Result.success(dtoPageInfo);
    }

    /**
     * 药包删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> delete(@RequestParam(value = "ids") List<Long> ids){

        setMealService.removeWithDish(ids);

        return Result.success("药包数据删除成功");
    }


    /**
     * 根据id查询药品及其口味
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<SetmealDto> getById(@PathVariable Long id) {
        SetmealDto setmealDto = setMealService.getByIdWithDish(id);
        return Result.success(setmealDto);
    }

    @PutMapping
    public Result<String> update(@RequestBody SetmealDto setmealDto) {
        setMealService.updateWithDish(setmealDto);
        return Result.success("修改成功!");
    }

    /**
     * 修改状态信息
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> alter(@PathVariable int status, @RequestParam(value = "ids") List<Long> ids){

        for (Long id:ids) {
            Setmeal setmeal = setMealService.getById(id);
            setmeal.setStatus(status);
            setMealService.updateById(setmeal);
        }

        return Result.success("修改成功!");
    }

    /**
     * 根据条件查询药包数据
     * @param setmeal
     * @return
     */
    @GetMapping("/list")
    public Result<List<Setmeal>> list(Setmeal setmeal){
        LambdaQueryWrapper<Setmeal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(setmeal.getCategoryId() != null, Setmeal::getCategoryId, setmeal.getCategoryId());
        queryWrapper.eq(setmeal.getStatus() != null, Setmeal::getStatus, setmeal.getStatus());
        queryWrapper.orderByDesc(Setmeal::getUpdateTime);

        List<Setmeal> list = setMealService.list(queryWrapper);

        return Result.success(list);
    }
}
