package com.csy.imc.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.csy.imc.entity.Employee;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
