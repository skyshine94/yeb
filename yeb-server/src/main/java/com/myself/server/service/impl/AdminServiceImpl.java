package com.myself.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.myself.server.mapper.AdminRoleMapper;
import com.myself.server.mapper.RoleMapper;
import com.myself.server.pojo.Admin;
import com.myself.server.mapper.AdminMapper;
import com.myself.server.pojo.AdminRole;
import com.myself.server.pojo.RespBean;
import com.myself.server.pojo.Role;
import com.myself.server.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.myself.server.utils.AdminUtil;
import com.myself.server.utils.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author myself
 * @since 2021-05-28
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AdminRoleMapper adminRoleMapper;

    @Override
    //登录并返回Token
    public RespBean login(String username, String password, String code, HttpServletRequest request) {
        //获取session中的验证码
        String captcha = (String) request.getSession().getAttribute("captcha");
        if (StringUtils.isEmpty(code) || !captcha.equalsIgnoreCase(code)) {
            return RespBean.error("验证码输入错误！");
        }
        //登录
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (null == userDetails || !passwordEncoder.matches(password, userDetails.getPassword())) {
            return RespBean.error("用户名或密码不正确！");
        }
        if (!userDetails.isEnabled()) {
            return RespBean.error("账号已被禁用");
        }
        //将用户信息存入安全上下文
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        //根据用户信息创建Token
        String token = jwtTokenUtil.createTokenByUser(userDetails);
        //存入Bearer头
        Map<String, String> tokenMap = new HashMap<>();
        tokenMap.put("tokenHead", tokenHead);
        tokenMap.put("token", token);
        return RespBean.success("登录成功", tokenMap);
    }

    @Override
    //获取登录用户信息
    public Admin getAdminByUsername(String username) {
        return adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username).eq("enabled", true));
    }

    @Override
    //根据用户id获取角色
    public List<Role> getRolesByAdminId(Integer id) {
        return roleMapper.getRolesByAdminId(id);
    }

    @Override
    //根据关键字获取操作员列表（去除当前登录操作员）
    public List<Admin> getAdminsByKeywords(String keywords) {
        return adminMapper.getAdminsByKeywords(AdminUtil.getAdmin().getId(), keywords);
    }

    @Override
    //更新操作员角色
    public RespBean updateAdminWithRole(Integer id, Integer[] rids) {
        //删除操作员角色
        adminRoleMapper.delete(new QueryWrapper<AdminRole>().eq("adminId", id));
        //批量添加操作员角色
        Integer result = adminRoleMapper.addAdminWithRole(id, rids);
        if (result == rids.length) {
            return RespBean.success("更新成功！");
        }
        return RespBean.error("更新失败！");
    }

    @Override
    //更新用户密码
    public RespBean updateAdminPassword(String oldPass, String pass, Integer adminId) {
        Admin admin = adminMapper.selectById(adminId);
        if (passwordEncoder.matches(oldPass, admin.getPassword())) {
            admin.setPassword(passwordEncoder.encode(pass));
            int result = adminMapper.updateById(admin);
            if (1 == result) {
                return RespBean.success("更新成功！");
            }
        }
        return RespBean.error("更新失败！");
    }

}
