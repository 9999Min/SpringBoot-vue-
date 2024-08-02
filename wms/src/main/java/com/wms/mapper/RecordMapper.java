package com.wms.mapper;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Constants;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.entity.Record;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.wms.entity.RecordRes;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */
public interface RecordMapper extends BaseMapper<Record> {

    IPage<RecordRes> customPage(Page<RecordRes> page, @Param(Constants.WRAPPER) QueryWrapper<RecordRes> wrapper);
}
