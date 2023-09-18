package com.cc.ecassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * 属性
 *
 * @author congcong.jiang
 * @date 2023-09-19
 */
@Setter
@Getter
public class Attribute {

    /**
     * 型号
     */
    @ExcelProperty("型号")
    private String model;
    /**
     * 关键词
     */
    @ExcelProperty("关键词")
    private String keyword;
    /**
     * 品牌
     */
    @ExcelProperty("品牌")
    private String brand;
    /**
     * 热门属性
     */
    @ExcelProperty("热门属性")
    private String hotAttribute;
    /**
     * 版本
     */
    @ExcelProperty("版本")
    private String version;
}
