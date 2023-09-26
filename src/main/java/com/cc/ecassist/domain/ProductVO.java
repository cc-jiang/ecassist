package com.cc.ecassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 货物
 *
 * @author congcong.jiang
 * @date 2023-09-26
 */
@Setter
@Getter
public class ProductVO implements Serializable {

    private static final long serialVersionUID = 2373802969726358586L;

    /**
     * 货号
     */
    @ExcelProperty("货号")
    private String productName;

    /**
     * 产品类目
     */
    @ExcelProperty("类目")
    private String productCategory;


    /**
     * 型号 多个用'|'分隔
     */
    @ExcelProperty("型号")
    private String model;


    /**
     * 关键词 多个用'|'分隔
     */
    @ExcelProperty("关键词")
    private String keyword;
}
