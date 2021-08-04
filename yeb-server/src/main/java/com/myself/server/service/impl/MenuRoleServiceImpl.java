package com.myself.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myself.server.pojo.MenuRole;
import com.myself.server.mapper.MenuRoleMapper;
import com.myself.server.pojo.RespBean;
import com.myself.server.service.IMenuRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@Service
public class MenuRoleServiceImpl extends ServiceImpl<MenuRoleMapper, MenuRole> implements IMenuRoleService {

    @Autowired
    private MenuRoleMapper menuRoleMapper;

    //更新角色菜单
    @Transactional
    @Override
    public RespBean updateMenusWithRole(Integer rid, Integer[] mids) {
        //删除角色菜单
        menuRoleMapper.delete(new QueryWrapper<MenuRole>().eq("rid", rid));
        //更新角色菜单时如果清空了角色菜单也需要返回提示信息
        if (null == mids || 0 == mids.length) {
            return RespBean.success("更新成功！");
        }
        //批量添加角色菜单
        Integer result = menuRoleMapper.addMenusWithRole(rid, mids);
        if (result == mids.length) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }
}
