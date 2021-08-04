package com.myself.server.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.myself.server.pojo.Employee;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.RespPageBean;
import com.myself.server.pojo.Salary;
import com.myself.server.service.IEmployeeService;
import com.myself.server.service.ISalaryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 员工账套控制器
 *
 * @author Wei
 * @since 2021/6/5
 */
@RestController
@Api(tags = "SalarySobCfgController")
@RequestMapping("/salary/sobcfg")
public class SalarySobCfgController {

    @Autowired
    private ISalaryService salaryService;
    @Autowired
    private IEmployeeService employeeService;

    @ApiOperation(value = "分页获取所有员工账套")
    @GetMapping("/")
    public RespPageBean getAllEmployeeSalariesByPage(@RequestParam(defaultValue = "1") Integer currentPage, @RequestParam(defaultValue = "10") Integer size) {
        return employeeService.getAllEmployeeSalariesByPage(currentPage, size);
    }

    @ApiOperation(value = "获取所有工资账套")
    @GetMapping("/salaries")
    public List<Salary> getAllSalaries() {
        return salaryService.list();
    }

    @ApiOperation(value = "更新员工账套")
    @PutMapping("/")
    public RespBean updateEmployeeSalary(Integer id, Integer sid) {
        if (employeeService.update(new UpdateWrapper<Employee>().set("salaryId", sid).eq("id", id))) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}

