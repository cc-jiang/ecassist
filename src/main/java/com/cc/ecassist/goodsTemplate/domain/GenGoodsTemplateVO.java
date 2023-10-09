package com.cc.ecassist.goodsTemplate.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 生成商品模板参数
 * @author congcong.jiang
 * @date 2023-10-03
 */
@Setter
@Getter
public class GenGoodsTemplateVO implements Serializable {

    private static final long serialVersionUID = -5500459721204096035L;

    /**
     * 生成路径
     */
    private String path;

    /**
     * 商品主图路径
     */
    private String mainImagePath;

    /**
     * 生成模式
     * 多型号模式
     * 单型号模式
     */
    private Integer genType;

    /**
     * 产品类目
     */
    private String productCategory;

    /**
     * 型号 多个用'|'分隔
     */
    private List<String> modelList;

    /**
     * 版本编号 多个用'|'分隔
     */
    private List<String> versionNoList;

    /**
     * 关键词 多个用'|'分隔
     */
    private List<String> keywordList;
}
