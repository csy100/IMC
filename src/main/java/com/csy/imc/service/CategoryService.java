package com.csy.imc.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.csy.imc.entity.Category;

public interface CategoryService extends IService<Category> {
    void remove(Long id);
}
