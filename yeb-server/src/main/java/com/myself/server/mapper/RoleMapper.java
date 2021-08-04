package com.myself.server.mapper;

import com.myself.server.pojo.Role;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<Role> getRolesByAdminId(Integer id);
}
