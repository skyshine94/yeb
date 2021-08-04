package com.myself.server.controller;


import com.myself.server.pojo.Admin;
import com.myself.server.pojo.Menu;
import com.myself.server.service.IMenuService;
import com.myself.server.utils.AdminUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
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
@Api(tags = "MenuController")
@RequestMapping("/system/cfg/")
public class MenuController {

    @Autowired
    private IMenuService menuService;

    @ApiOperation(value = "根据用户id查询菜单列表")
    @GetMapping("/menu")
    public List<Menu> getMenusByAdminId() {
        Integer id = AdminUtil.getAdmin().getId();
        List<Menu> menus = menuService.getMenusByAdminId(id);
        return menus;
    }

}
