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
     * 模板路径
     */
    private String path;

    /**
     * 生成路径
     */
    private String genPath;

    /**
     * 商品主图路径
     */
    private String mainImagePath;

    /**
     * 生成模式
     * @see com.cc.ecassist.goodsTemplate.constant.GenType
     */
    private Integer genType;

    /**
     * 尺码模式
     * @see com.cc.ecassist.goodsTemplate.constant.VersionType
     */
    private Integer versionType;

    /**
     * 自填尺码 英文逗号分割
     */
    private String versions;

    /**
     * 产品类目
     */
    private String productCategory;

    /**
     * 型号
     */
    private List<String> modelList;

    /**
     * 版本编号
     */
    private List<String> versionNoList;

    /**
     * 关键词
     */
    private List<String> keywordList;
}
