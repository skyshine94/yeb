package com.myself.server.mapper;

import com.myself.server.pojo.Admin;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
public interface AdminMapper extends BaseMapper<Admin> {

    List<Admin> getAdminsByKeywords(@Param("id") Integer id, @Param("keywords") String keywords);
}
