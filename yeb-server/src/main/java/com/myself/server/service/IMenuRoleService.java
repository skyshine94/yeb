package com.myself.server.service;

import com.myself.server.pojo.MenuRole;
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
public interface IMenuRoleService extends IService<MenuRole> {

    RespBean updateMenusWithRole(Integer rid, Integer[] mids);
}
