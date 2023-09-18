package com.cc.ecassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 颜色分类
 *
 * @author congcong.jiang
 * @date 2023-09-19
 */
@Setter
@Getter
public class ColorCategory {

    /**
     * 颜色分类
     */
    @ExcelProperty("颜色分类")
    private String category;

    /**
     * SKU图片
     */
    @ExcelProperty("SKU图片")
    private String skuImage;
}
