package com.myself.server.service.impl;

import com.myself.server.pojo.Menu;
import com.myself.server.mapper.MenuMapper;
import com.myself.server.service.IMenuService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;


/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements IMenuService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;
    @Autowired
    private MenuMapper menuMapper;

    @Override
    //根据用户id获取菜单列表
    public List<Menu> getMenusByAdminId(Integer id) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        //从redis获取菜单数据
        List<Menu> menus = (List<Menu>) valueOperations.get("menu_" + id);
        //如果为空，从数据库获取
        if (CollectionUtils.isEmpty(menus)) {
            menus = menuMapper.getMenusByAdminId(id);
            //将数据设置到redis中
            valueOperations.set("menu_" + id, menus);
        }
        return menus;
    }

    @Override
    //获取角色和对应的菜单
    public List<Menu> getMenusWithRole() {
        return menuMapper.getMenusWithRole();
    }

    @Override
    //获取所有菜单
    public List<Menu> getAllMenus() {
        return menuMapper.getAllMenus();
    }
}
