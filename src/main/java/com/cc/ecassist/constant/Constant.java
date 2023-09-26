package com.cc.ecassist.constant;

/**
 * 常量
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
public class Constant {

    /**
     * 文件根目录
     */
    public static final String PATH = "C:/ecassist/";

    /**
     * 上架文件名称
     */
    public static final String ON_SHELF_EXCEL_NAME = PATH + "优美达上架_%s.xlsx";
    /**
     * 上架文件模板
     */
    public static final String ON_SHELF_TEMPLATE_EXCEL_NAME = String.format(ON_SHELF_EXCEL_NAME, "模板");

    public static final String ATTRIBUTE_EXCEL_NAME = PATH + "属性.xlsx";
    public static final String COLOR_CATEGORY_EXCEL_NAME = PATH + "颜色分类.xlsx";

    /**
     * 商品模板路径
     */
    public static final String GOODS_TEMPLATE_PATH = PATH + "goodsTemplate/";

    /**
     * 型号编码excel
     */
    public static final String MODEL_EXCEL_NAME = PATH + "型号编码.xlsx";

    /**
     * 标题关键词excel
     */
    public static final String KEYWORD_EXCEL_NAME = PATH + "标题关键词.xlsx";
}
