package com.myself.server.service.impl;

import com.myself.server.pojo.Oplog;
import com.myself.server.mapper.OplogMapper;
import com.myself.server.service.IOplogService;
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
public class OplogServiceImpl extends ServiceImpl<OplogMapper, Oplog> implements IOplogService {

}
