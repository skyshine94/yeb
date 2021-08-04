package com.myself.server.utils;

import com.myself.server.pojo.Admin;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 操作员工具类
 *
 * @author Wei
 * @since 2021/5/30
 */

public class AdminUtil {

    public static Admin getAdmin(){
        return (Admin) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
