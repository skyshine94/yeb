package com.myself.server.service;

import com.myself.server.pojo.Department;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myself.server.pojo.RespBean;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface IDepartmentService extends IService<Department> {

    List<Department> getAllDepartments();

    RespBean addDep(Department department);

    RespBean deleteDep(Integer id);
}
