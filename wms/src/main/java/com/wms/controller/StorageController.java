package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Storage;
import com.wms.service.StorageService;
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
 * @since 2023-06-15
 */
@RestController
@RequestMapping("/storage")
public class StorageController {

    @Autowired
    private StorageService storageService;

    //新增
    @PostMapping("/saveOrUpdate")
    public Result save(@RequestBody Storage storage) {
        return storageService.saveOrUpdate(storage) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result del(@RequestParam String id) {
        return storageService.removeById(id) ? Result.suc() : Result.fail();
    }

    //按照仓库名查询，看是否有重复仓库名
    @GetMapping("/findByName")
    public Result findByNo(@RequestParam String name) {
        Storage storage = storageService.lambdaQuery().eq(Storage::getName, name).one();
        return storage != null ? Result.suc() : Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<Storage> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");

        LambdaQueryWrapper<Storage> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(Storage::getName, name);
        }

        IPage<Storage> result = storageService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //获取所有记录
    @GetMapping("/list")
    public Result list() {
        List<Storage> list = storageService.list();
        return Result.suc(list);
    }

}
