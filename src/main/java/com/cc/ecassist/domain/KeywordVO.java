package com.cc.ecassist.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 关键词
 *
 * @author congcong.jiang
 * @date 2023-09-26
 */
@Setter
@Getter
public class KeywordVO implements Serializable {

    private static final long serialVersionUID = -3720906067826622138L;

    /**
     * 产品类目
     */
    @ExcelProperty("类目")
    private String productCategory;

    /**
     * 关键词类型
     */
    @ExcelProperty("类型")
    private String type;

    /**
     * 关键词名称
     */
    @ExcelProperty("名称")
    private String name;
}
