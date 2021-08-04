package com.myself.server.service.impl;

import com.myself.server.pojo.Role;
import com.myself.server.mapper.RoleMapper;
import com.myself.server.service.IRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements IRoleService {

}
