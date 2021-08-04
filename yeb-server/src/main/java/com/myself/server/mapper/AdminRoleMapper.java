package com.myself.server.mapper;

import com.myself.server.pojo.AdminRole;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface AdminRoleMapper extends BaseMapper<AdminRole> {

    Integer addAdminWithRole(@Param("id") Integer id, @Param("rids") Integer[] rids);
}
