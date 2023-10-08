package com.cc.ecassist.goodsTemplate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 商品属性
 *
 * @author congcong.jiang
 * @date 2023-10-08
 */
@Setter
@Getter
public class ProductPropertyVO implements Serializable {

    private static final long serialVersionUID = -5422687672069756579L;

    /**
     * 商品编码 标题
     */
    @ExcelProperty("商品编码")
    private String code;

    /**
     * 商品属性(属性名:属性值;属性名:属性值,属性值)
     */
    @ExcelProperty("商品属性")
    private String property;
}
