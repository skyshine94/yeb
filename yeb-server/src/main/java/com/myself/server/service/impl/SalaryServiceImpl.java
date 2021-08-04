package com.myself.server.service.impl;

import com.myself.server.pojo.Salary;
import com.myself.server.mapper.SalaryMapper;
import com.myself.server.service.ISalaryService;
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
public class SalaryServiceImpl extends ServiceImpl<SalaryMapper, Salary> implements ISalaryService {

}
