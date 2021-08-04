package com.myself.server.service.impl;

import com.myself.server.pojo.Nation;
import com.myself.server.mapper.NationMapper;
import com.myself.server.service.INationService;
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
public class NationServiceImpl extends ServiceImpl<NationMapper, Nation> implements INationService {

}
