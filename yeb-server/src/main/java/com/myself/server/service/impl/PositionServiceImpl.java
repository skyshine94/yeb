package com.myself.server.service.impl;

import com.myself.server.pojo.Position;
import com.myself.server.mapper.PositionMapper;
import com.myself.server.service.IPositionService;
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
public class PositionServiceImpl extends ServiceImpl<PositionMapper, Position> implements IPositionService {

}
