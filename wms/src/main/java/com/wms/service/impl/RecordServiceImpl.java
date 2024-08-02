package com.wms.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Record;
import com.wms.entity.RecordRes;
import com.wms.mapper.RecordMapper;
import com.wms.service.RecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */
@Service
public class RecordServiceImpl extends ServiceImpl<RecordMapper, Record> implements RecordService {

    @Resource
    private RecordMapper recordMapper;
    
    @Override
    public IPage<RecordRes> customPage(Page<RecordRes> page, QueryWrapper<RecordRes> wrapper) {
        return recordMapper.customPage(page, wrapper);
    }
}
