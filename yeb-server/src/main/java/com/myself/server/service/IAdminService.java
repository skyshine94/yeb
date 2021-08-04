package com.myself.server.service;

import com.myself.server.pojo.Admin;
import com.baomidou.mybatisplus.extension.service.IService;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.Role;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface IAdminService extends IService<Admin> {

    RespBean login(String username, String password, String code, HttpServletRequest request);

    Admin getAdminByUsername(String username);

    List<Role> getRolesByAdminId(Integer id);

    List<Admin> getAdminsByKeywords(String keywords);

    RespBean updateAdminWithRole(Integer id, Integer[] rids);

    RespBean updateAdminPassword(String oldPass, String pass, Integer adminId);
}
