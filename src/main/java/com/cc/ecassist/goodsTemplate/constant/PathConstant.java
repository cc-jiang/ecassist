package com.cc.ecassist.goodsTemplate.constant;

import com.cc.ecassist.utils.FileUtils;
import org.apache.logging.log4j.util.Strings;

/**
 * 常量
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
public class PathConstant {

    /**
     * 模板路径
     */
    public static String PATH = "C:/ecassist/";

    /**
     * 生成路径
     */
    public static String GEN_PATH = PATH;

    /**
     * 商品主图路径
     */
    public static String MAIN_IMAGE_PATH = PATH + "image/";

    /**
     * 上架文件名称
     */
    public static final String ON_SHELF_EXCEL_NAME = "优美达上架_%s.xlsx";
    /**
     * 上架文件模板
     */
    public static final String ON_SHELF_TEMPLATE_EXCEL_NAME = String.format(ON_SHELF_EXCEL_NAME, "模板_简化");

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

    /**
     * 获取完整模板路径
     * @param path
     * @return
     */
    public static String getFullPath(String path) {
        return PATH + path;
    }

    /**
     * 获取完整生成路径
     * @param path
     * @return
     */
    public static String getFullGenPath(String path) {
        return GEN_PATH + path;
    }

    public static void setPath(String path) {
        path = path.replace("\\", "/");
        PATH = path + (path.endsWith("/") ? Strings.EMPTY : "/");
        FileUtils.createDir(PathConstant.PATH);
    }

    public static void setGenPath(String genPath) {
        genPath = genPath.replace("\\", "/");
        GEN_PATH = genPath + (genPath.endsWith("/") ? Strings.EMPTY : "/");
    }

    public static void setMainImagePath(String mainImagePath) {
        mainImagePath = mainImagePath.replace("\\", "/");
        MAIN_IMAGE_PATH = mainImagePath + (mainImagePath.endsWith("/") ? Strings.EMPTY : "/");
    }

}
