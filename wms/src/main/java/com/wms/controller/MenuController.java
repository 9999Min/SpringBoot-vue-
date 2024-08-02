package com.wms.controller;


import com.wms.common.Result;
import com.wms.entity.Menu;
import com.wms.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/list")
    public Result list(@RequestParam String roleId) {
        List<Menu> list = menuService.lambdaQuery().like(Menu::getMenuRight, roleId).list();
        return Result.suc(list);
    }
}
