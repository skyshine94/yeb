package com.myself.server.controller;


import com.myself.server.pojo.Admin;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.Role;
import com.myself.server.service.IAdminService;
import com.myself.server.service.IRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@RestController
@Api(tags = "AdminController")
@RequestMapping("/system/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;
    @Autowired
    private IRoleService roleService;

    @ApiOperation("根据关键字获取操作员列表")
    @GetMapping("/")
    public List<Admin> getAdminsByKeywords(String keywords) {
        return adminService.getAdminsByKeywords(keywords);
    }

    @ApiOperation("删除操作员")
    @DeleteMapping("/{id}")
    public RespBean deleteAdminById(@PathVariable Integer id) {
        if (adminService.removeById(id)) {
            return RespBean.success("删除成功！");
        }
        return RespBean.error("删除失败！");
    }

    @ApiOperation("更新操作员")
    @PutMapping("/")
    public RespBean updateAdmin(@RequestBody Admin admin) {
        if (adminService.updateById(admin)) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "获取所有角色")
    @GetMapping("/role")
    public List<Role> getAllRoles() {
        return roleService.list();
    }

    @ApiOperation("更新操作员角色")
    @PutMapping("/role")
    public RespBean updateAdminWithRole(Integer id, Integer[] rids){
        return adminService.updateAdminWithRole(id, rids);
    }
}
