package com.myself.server.controller;

import com.myself.server.pojo.Admin;
import com.myself.server.pojo.AdminLogin;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.Role;
import com.myself.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.List;

/**
 * 登录控制器
 *
 * @author Wei
 * @since 2021/5/28
 */
@RestController
@Api(tags = "LoginController")
public class LoginController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "登录并返回Token")
    @PostMapping("/login")
    public RespBean login(@RequestBody AdminLogin adminLogin, HttpServletRequest request) {
        return adminService.login(adminLogin.getUsername(), adminLogin.getPassword(), adminLogin.getCode(), request);
    }

    @ApiOperation(value = "获取登录用户信息")
    @GetMapping("/admin/info")
    public Admin getAdminInfo(Principal principal) {
        if (null == principal) {
            return null;
        }
        String username = principal.getName();
        Admin admin = adminService.getAdminByUsername(username);
        //不返回密码
        admin.setPassword(null);
        List<Role> roles = adminService.getRolesByAdminId(admin.getId());
        admin.setRoles(roles);
        return admin;
    }

    @ApiOperation(value = "退出登录")
    @GetMapping("/logout")
    public RespBean loginout() {
        return RespBean.success("退出成功！");
    }
}
