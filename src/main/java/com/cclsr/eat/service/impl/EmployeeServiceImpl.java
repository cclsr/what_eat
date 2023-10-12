package com.cclsr.eat.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cclsr.eat.entity.Employee;
import com.cclsr.eat.mapper.EmployeeMapper;
import com.cclsr.eat.service.EmployeeService;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
