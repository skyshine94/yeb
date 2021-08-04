package com.myself.server.controller;

import com.myself.server.pojo.Admin;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.Salary;
import com.myself.server.service.IAdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 个人中心控制器
 *
 * @author Wei
 * @since 2021/6/5
 */
@RestController
@Api(tags = "AdminInfoController")
@RequestMapping("/admin")
public class AdminInfoController {

    @Autowired
    private IAdminService adminService;

    @ApiOperation(value = "更新用户信息")
    @PutMapping("/info")
    public RespBean updateAdminInfo(@RequestBody Admin admin, Authentication authentication) {
        if (adminService.updateById(admin)) {
            //重新设置Authentication
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(admin, null, authentication.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @ApiOperation(value = "更新用户密码")
    @PutMapping("/pass")
    public RespBean updateAdminPassword(@RequestBody Map<String, Object> info) {
        String oldPass = (String) info.get("oldPass");
        String pass = (String) info.get("pass");
        Integer adminId = (Integer) info.get("adminId");
        return adminService.updateAdminPassword(oldPass, pass, adminId);
    }
}
