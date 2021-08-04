package com.myself.server.service.impl;

import com.myself.server.pojo.Department;
import com.myself.server.mapper.DepartmentMapper;
import com.myself.server.pojo.RespBean;
import com.myself.server.service.IDepartmentService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@Service
public class DepartmentServiceImpl extends ServiceImpl<DepartmentMapper, Department> implements IDepartmentService {

    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    //获取所有部门
    public List<Department> getAllDepartments() {
        return departmentMapper.getAllDepartments(-1);
    }

    @Override
    //添加部门
    public RespBean addDep(Department department) {
        department.setEnabled(true);
        departmentMapper.addDep(department);
        Integer result = department.getResult();
        if (1 == result) {
            return RespBean.success("添加成功", department);
        }
        return RespBean.error("添加失败");
    }

    @Override
    //删除部门
    public RespBean deleteDep(Integer id) {
        Department department = new Department();
        department.setId(id);
        departmentMapper.deleteDep(department);
        Integer result = department.getResult();
        if (-2 == result) {
            return RespBean.error("该部门下还有子部门，删除失败！");
        }
        if (-1 == result) {
            return RespBean.error("该部门下还有员工，删除失败！");
        }
        if (1 == result) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败");
    }
}
