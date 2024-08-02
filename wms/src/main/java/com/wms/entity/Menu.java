package com.wms.entity;

import java.io.Serializable;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author 朱雅杨
 * @since 2023-06-15
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Menu对象", description="")
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    private Integer id;

    @ApiModelProperty(value = "菜单编码")
    private String menuCode;

    @ApiModelProperty(value = "菜单名字")
    private String menuName;

    @ApiModelProperty(value = "菜单级别")
    private String menuLevel;

    @ApiModelProperty(value = "菜单的父code")
    private String menuParentCode;

    @ApiModelProperty(value = "点击触发的函数")
    private String menuClick;

    @ApiModelProperty(value = "权限 0超级管理员，1表示管理员，2表示普通用户，可以用逗号组合使用")
    private String menuRight;

    private String menuComponent;

    private String menuIcon;


}
