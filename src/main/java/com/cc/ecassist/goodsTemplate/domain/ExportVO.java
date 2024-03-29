package com.cc.ecassist.goodsTemplate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author congcong.jiang
 * @date 2023-09-17
 */
@Getter
@Setter
public class ExportVO implements Serializable {

    private static final long serialVersionUID = -5872296856289685461L;

    @ExcelProperty("名称")
    private String name;

    @ExcelProperty("值")
    private String value;

    @ExcelProperty("金额")
    private BigDecimal amount;

    @ExcelProperty("时间格式化")
    @DateTimeFormat
    private Date dateTime;

    @ExcelProperty("图片")
    private String image;

    @ExcelProperty("图片2")
    private byte[] image2;

    @ExcelProperty("日期格式化")
    @DateTimeFormat
    private Date date;

    @ExcelProperty("url")
    private String url;
}
