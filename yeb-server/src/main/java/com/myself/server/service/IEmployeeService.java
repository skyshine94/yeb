package com.myself.server.service;

import com.myself.server.pojo.Employee;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.RespPageBean;

import java.time.LocalDate;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface IEmployeeService extends IService<Employee> {

    RespPageBean getEmployeeByPage(Integer currentPage, Integer size, Employee employee, LocalDate[] beginDateScope);

    RespBean maxWorkID();

    RespBean addEmp(Employee employee);

    List<Employee> getAllEmployees();

    RespPageBean getAllEmployeeSalariesByPage(Integer currentPage, Integer size);
}
