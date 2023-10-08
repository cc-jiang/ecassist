package com.cc.ecassist.goodsTemplate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * SKU属性
 *
 * @author congcong.jiang
 * @date 2023-10-08
 */
@Setter
@Getter
public class SkuPropertyVO implements Serializable {

    private static final long serialVersionUID = 7460314832998469635L;

    /**
     * 商品编码 标题
     */
    @ExcelProperty("商品编码")
    private String code;

    /**
     * 京东SKU 颜色+版本
     */
    @ExcelProperty("京东SKU")
    private String sku;

    /**
     * SKU属性(属性名:属性值;属性名:属性值,属性值)
     */
    @ExcelProperty("SKU属性")
    private String property;
}
