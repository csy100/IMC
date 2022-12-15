package com.csy.imc.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.csy.imc.entity.Employee;
import com.csy.imc.mapper.EmployeeMapper;
import com.csy.imc.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
