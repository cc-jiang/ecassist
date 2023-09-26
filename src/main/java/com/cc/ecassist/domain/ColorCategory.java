package com.cc.ecassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 颜色分类
 *
 * @author congcong.jiang
 * @date 2023-09-19
 */
@Setter
@Getter
public class ColorCategory implements Serializable {

    private static final long serialVersionUID = -473429242481990791L;

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
