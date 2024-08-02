package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Goodstype;
import com.wms.service.GoodstypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-16
 */
@RestController
@RequestMapping("/goodsType")
public class GoodstypeController {

    @Autowired
    private GoodstypeService goodstypeService;

    //新增
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody Goodstype goodstype) {
        return goodstypeService.saveOrUpdate(goodstype) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return goodstypeService.removeById(id) ? Result.suc() : Result.fail();
    }

    //按照仓库名查询，看是否有重复仓库名
    @GetMapping("/findByName")
    public Result findByNo(@RequestParam String name) {
        Goodstype goodstype = goodstypeService.lambdaQuery().eq(Goodstype::getName, name).one();
        return goodstype != null ? Result.suc() : Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<Goodstype> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");

        LambdaQueryWrapper<Goodstype> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Goodstype::getName, name);
        }

        IPage<Goodstype> result = goodstypeService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //获取所有记录
    @GetMapping("/list")
    public Result list() {
        List<Goodstype> list = goodstypeService.list();
        return Result.suc(list);
    }
}
