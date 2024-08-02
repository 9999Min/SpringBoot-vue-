package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goods;
import com.wms.entity.Storage;
import com.wms.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/goods")
public class GoodsController {
    public static final int ADD = 0;
    public static final int DELETE = 1;
    public static final int IMPORT = 3;
    public static final int EXPORT = 4;

    @Autowired
    private GoodsService goodsService;

    //新增
    @PostMapping("/save")
    public Result save(@RequestBody Goods goods) {
        return goodsService.save(goods) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return goodsService.removeById(id) ? Result.suc() : Result.fail();
    }

    //按照仓库名查询，看是否有重复物品
    @GetMapping("/findByNameAndStorage")
    public Result findByNameAndStorage(@RequestParam("name") String name, @RequestParam("storage") Integer storage) {
        Goods goods = goodsService.lambdaQuery().eq(Goods::getName, name).eq(Goods::getStorage, storage).one();
        return goods != null ? Result.suc() : Result.fail();
    }

    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<Goods> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");
        String storage = (String) param.get("storage");
        String goodsType = (String) param.get("goodsType");

        LambdaQueryWrapper<Goods> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Goods::getName, name);
        }
        if (StringUtils.isNotBlank(storage)) {
            lambdaQueryWrapper.eq(Goods::getStorage, storage);
        }
        if (StringUtils.isNotBlank(goodsType)) {
            LambdaQueryWrapper<Goods> eq = lambdaQueryWrapper.eq(Goods::getGoodsType, goodsType);
        }

        IPage<Goods> result = goodsService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }
}
