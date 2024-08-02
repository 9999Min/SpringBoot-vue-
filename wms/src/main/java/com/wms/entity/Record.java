package com.wms.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.time.LocalDateTime;
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
 * @since 2023-06-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="Record对象", description="")
public class Record implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "货品id")
    private Integer goods;

    @ApiModelProperty(value = "取货人/补货人")
    private Integer userId;

    @ApiModelProperty(value = "操作人id")
    private Integer adminId;

    @ApiModelProperty(value = "数量")
    private Integer count;

    @ApiModelProperty(value = "操作时间")
    private LocalDateTime createTime;

    @ApiModelProperty(value = "备注")
    private String remark;

    //不是表里的字段，只是用于判断是出库还是入库
    @TableField(exist = false)
    private int action;

    //不是表里的字段，只是用于判断是出库还是入库
    @TableField(exist = false)
    private int storageId;

}
