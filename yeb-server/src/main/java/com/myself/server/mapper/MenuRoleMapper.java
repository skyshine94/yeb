package com.myself.server.mapper;

import com.myself.server.pojo.MenuRole;
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
public interface MenuRoleMapper extends BaseMapper<MenuRole> {

    Integer addMenusWithRole(@Param("rid") Integer rid, @Param("mids") Integer[] mids);
}
