package com.wms.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.wms.common.QueryPageParam;
import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.entity.User;
import com.wms.service.MenuService;
import com.wms.service.UserService;
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
 * @since 2023-06-07
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private MenuService menuService;

    //新增或修改
    @PostMapping("/saveOrUpdate")
    public Result saveOrMod(@RequestBody User user) {
        return userService.saveOrUpdate(user) ? Result.suc() : Result.fail();
    }

    //删除
    @GetMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return userService.removeById(id) ? Result.suc() : Result.fail();
    }

    //按照账号查询，看是否有重复账号
    @GetMapping("/findByNo")
    public Result findByNo(@RequestParam String no) {
        User user = userService.lambdaQuery().eq(User::getNo, no).one();
        return user != null ? Result.suc() : Result.fail();
    }

    //分页查询
    @PostMapping("/listPage")
    public Result listPage(@RequestBody QueryPageParam query) {
        Page<User> page = new Page<>();
        page.setCurrent(query.getPageNum());
        page.setSize(query.getPageSize());

        HashMap<String, Object> param = query.getParam();
        String name = (String) param.get("name");
        String sex = (String) param.get("sex");
        String roleId = (String) param.get("roleId");

        LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper<>();

        if (StringUtils.isNotBlank(name)) {
            lambdaQueryWrapper.like(User::getName, name);
        }
        if (StringUtils.isNotBlank(sex)) {
            lambdaQueryWrapper.eq(User::getSex, sex);
        }
        if (StringUtils.isNotBlank(roleId)) {
            lambdaQueryWrapper.eq(User::getRoleId, roleId);
        }

        IPage<User> result = userService.page(page, lambdaQueryWrapper);
        return Result.suc(result.getRecords(), result.getTotal());
    }

    //登录
    @PostMapping("/login")
    public Result login(@RequestBody User loginUser) {
        User user = userService.lambdaQuery()
                .eq(User::getNo, loginUser.getNo())
                .eq(User::getPassword, loginUser.getPassword()).one();
        if (user != null) {
            user.setPassword("******");
            List<Menu> menu = menuService.lambdaQuery().like(Menu::getMenuRight, user.getRoleId()).list();
            HashMap<String, Object> res = new HashMap<>();
            res.put("user", user);
            res.put("menu", menu);
            return Result.suc(res);
        }
        return Result.fail();
    }

}
