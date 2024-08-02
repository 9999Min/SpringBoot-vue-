package com.wms.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.extension.service.IService;
import com.wms.entity.RecordRes;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */
public interface RecordService extends IService<Record> {

    IPage<RecordRes> customPage(Page<RecordRes> page, QueryWrapper<RecordRes> wrapper);
}
