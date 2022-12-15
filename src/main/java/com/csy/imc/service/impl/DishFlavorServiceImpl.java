package com.csy.imc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.imc.entity.DishFlavor;
import com.csy.imc.mapper.DishFlavorMapper;
import com.csy.imc.service.DishFlavorService;
import org.springframework.stereotype.Service;

@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor> implements DishFlavorService {
}
