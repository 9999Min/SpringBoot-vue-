package com.wms.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 *
 * </p>
 *
 * @author wms
 * @since 2023-02-13
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class RecordRes extends Record {
    private String goodsName;
    private String storageName;
    private String goodsTypeName;
    private String applicant;
    private String operator;
}
