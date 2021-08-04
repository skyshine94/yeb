package com.myself.server.config.mybatisPlus;

import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * mybatisPlus配置类
 *
 * @author Wei
 * @since 2021/6/3
 */
@Configuration
@MapperScan("com.myself.server.mapper")
public class mybatisPlusConfig {

    @Bean
    //配置分页插件
    public PaginationInterceptor paginationInterceptor(){
        return new PaginationInterceptor();
    }

}
