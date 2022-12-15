package com.csy.imc.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.csy.imc.common.Result;
import com.csy.imc.dto.DishDto;
import com.csy.imc.entity.Category;
import com.csy.imc.entity.Dish;
import com.csy.imc.entity.DishFlavor;
import com.csy.imc.service.CategoryService;
import com.csy.imc.service.DishFlavorService;
import com.csy.imc.service.DishService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@RestController
@RequestMapping("/dish")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 新增药品
     *
     * @param dishDto
     * @return
     */
    @PostMapping
    public Result<String> save(@RequestBody DishDto dishDto) {
        dishService.saveWithFlavor(dishDto);
        return Result.success("新增药品成功!");
    }

    /**
     * 分页展示药品功能
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @GetMapping("/page")
    public Result<Page> page(int page, int pageSize, String name) {
        Page<Dish> dishPage = new Page<>(page, pageSize);
        Page<DishDto> dishDtoPage = new Page<>();

        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.like(Strings.isNotEmpty(name), Dish::getName, name);
        dishLambdaQueryWrapper.orderByDesc(Dish::getUpdateTime);
        dishService.page(dishPage, dishLambdaQueryWrapper);

        BeanUtils.copyProperties(dishPage, dishDtoPage, "records");

        List<DishDto> dishDtoList = new ArrayList<>();
        for (Dish record : dishPage.getRecords()) {
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(record, dishDto);

            Long categoryId = record.getCategoryId();
            Category category = categoryService.getById(categoryId);
            if (category != null) {
                String categoryName = category.getName();
                dishDto.setCategoryName(categoryName);
                dishDtoList.add(dishDto);
            }

        }

        dishDtoPage.setRecords(dishDtoList);
        return Result.success(dishDtoPage);
    }


    /**
     * 根据id查询药品及其口味
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result<DishDto> getById(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return Result.success(dishDto);
    }

    /**
     * 修改药品
     *
     * @param dishDto
     * @return
     */
    @PutMapping
    public Result<String> update(@RequestBody DishDto dishDto) {
        dishService.updateWithFlavor(dishDto);

        return Result.success("修改成功!");
    }

    /**
     * 删除
     * @param ids
     * @return
     */
    @DeleteMapping
    public Result<String> remove(@RequestParam(value = "ids") List<Long> ids) {
        dishService.deleteByIdWithFlavor(ids);
        return Result.success("删除成功!");
    }

    /**
     *
     * @param dish
     * @return
     */
    @GetMapping("/list")
    public Result<List<Dish>> list(Dish dish) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapper.eq(dish.getCategoryId() != null, Dish::getCategoryId, dish.getCategoryId());
        dishLambdaQueryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        dishLambdaQueryWrapper.eq(Dish::getStatus, 1);

        List<Dish> dishList = dishService.list(dishLambdaQueryWrapper);
        return Result.success(dishList);
    }

    /**
     * 更改停售起售状态
     * @param status
     * @param ids
     * @return
     */
    @PostMapping("/status/{status}")
    public Result<String> change(@PathVariable int status, Long[] ids) {
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapper = new LambdaQueryWrapper<>();

        for (Long id : ids) {
            Dish dish = dishService.getById(id);
            dish.setStatus(status);
            dishService.updateById(dish);
        }

        return Result.success("修改成功!");
    }

}
