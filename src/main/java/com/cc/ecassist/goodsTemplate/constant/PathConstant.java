package com.cc.ecassist.goodsTemplate.constant;

import org.apache.logging.log4j.util.Strings;

/**
 * 常量
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
public class PathConstant {

    /**
     * 文件根目录
     */
    public static String PATH = "C:/ecassist/";

    /**
     * 上架文件名称
     */
    public static final String ON_SHELF_EXCEL_NAME = "优美达上架_%s.xlsx";
    /**
     * 上架文件模板
     */
    public static final String ON_SHELF_TEMPLATE_EXCEL_NAME = String.format(ON_SHELF_EXCEL_NAME, "模板");

    public static final String ATTRIBUTE_EXCEL_NAME = "属性.xlsx";
    public static final String COLOR_CATEGORY_EXCEL_NAME = "颜色分类.xlsx";

    /**
     * 商品模板路径
     */
    public static final String GOODS_TEMPLATE_PATH = "goodsTemplate/";

    /**
     * 添加新商品模板excel
     */
    public static final String GOODS_TEMPLATE_EXCEL_NAME = GOODS_TEMPLATE_PATH + "添加新商品模板.xlsx";
    /**
     * 批量商品属性导入excel
     */
    public static final String PRODUCT_PROPERTY_EXCEL_NAME = GOODS_TEMPLATE_PATH + "批量商品属性导入.xlsx";
    /**
     * 批量SKU属性导入excel
     */
    public static final String SKU_PROPERTY_EXCEL_NAME = GOODS_TEMPLATE_PATH + "批量SKU属性导入.xlsx";

    /**
     * 型号编码excel
     */
    public static final String MODEL_EXCEL_NAME = "型号编码.xlsx";

    /**
     * 型号词excel
     */
    public static final String MODEL_WORD_EXCEL_NAME = "型号词.xlsx";

    /**
     * 标题关键词excel
     */
    public static final String KEYWORD_EXCEL_NAME = "标题关键词.xlsx";

    public static final String ZIP_NAME = "添加新商品模板_%s.zip";

    public static String getFullPath(String path) {
        return PATH + path;
    }

    public static void setPATH(String path) {
        PATH = path + (path.endsWith("/") ? Strings.EMPTY : "/");
    }

}
