package com.wms.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Record;
import com.wms.entity.RecordRes;
import com.wms.service.GoodsService;
import com.wms.service.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */

@RestController
@RequestMapping("/record")
public class RecordController {

    @Autowired
    private RecordService recordService;

    @Autowired
    private GoodsService goodsService;

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<RecordRes> page = new Page<>();
        page.setSize(query.getPageSize());
        page.setCurrent(query.getPageNum());

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");
        String storage = (String) param.get("storage");
        String goodsType = (String) param.get("goodsType");
        Integer roleId = (Integer) param.get("roleId");
        Integer userId = (Integer) param.get("userId");

        QueryWrapper<RecordRes> queryWrapper = new QueryWrapper<>();
        //queryWrapper.apply(" a.goods=b.id and b.storage=c.id and b.goodsType=d.id ");

        if (roleId.equals(2)) {
            queryWrapper.apply("r.user_id = " + userId);
        }
        if (StringUtils.isNotBlank(name)) {
            queryWrapper.like("g.name", name);
        }
        if (StringUtils.isNotBlank(storage)) {
            queryWrapper.eq("s.id", storage);
        }
        if (StringUtils.isNotBlank(goodsType)) {
            queryWrapper.eq("t.id", goodsType);
        }
        queryWrapper.orderByDesc("r.create_time");
        IPage<RecordRes> result = recordService.customPage(page, queryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Record record) {
        if (record.getAction() == GoodsController.ADD) {//新增
            List<Goods> list = goodsService.list();
            Integer goodsId = list.get(list.size() - 1).getId();
            record.setGoods(goodsId);
//        } else if (record.getAction() == GoodsController.DELETE) {//删除
//            record.setCount(- record.getCount());
        } else {//出入库
            int change = record.getCount();
            if (record.getAction() == GoodsController.EXPORT) {//出库
                change = - change;
            }
            record.setCount(change);
            saveChange(record.getGoods(), change);
        }
        return recordService.save(record) ? Result.suc() : Result.fail();
    }

    /**
     * 修改物品数量
     * @param goodsId
     * @param change
     */
    private void saveChange(int goodsId, int change) {
        Goods goods = goodsService.getById(goodsId);
        goods.setCount(change + goods.getCount());
        goodsService.updateById(goods);
    }
}
