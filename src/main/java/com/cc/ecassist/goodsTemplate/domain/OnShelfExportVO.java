package com.cc.ecassist.goodsTemplate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上架文件
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Setter
@Getter
public class OnShelfExportVO implements Serializable {

    private static final long serialVersionUID = -4089905339577710489L;

    /**
     * 序号
     */
    @ExcelProperty("序号")
    private String productNo;
    /**
     * 商品ID
     */
    @ExcelProperty("商品ID")
    private String productId;
    /**
     * 操作方式
     */
    @ExcelProperty("操作方式")
    private String operateMethod;
    /**
     * 产品类目
     */
    @ExcelProperty("产品类目")
    private String productCategory;
    /**
     * 商品标题
     */
    @ExcelProperty("商品标题")
    private String productTitle;
    /**
     * 商品标语
     */
    @ExcelProperty("商品标语")
    private String productSlogan;
    /**
     * 品牌
     */
    @ExcelProperty("品牌")
    private String brand;
    /**
     * 无理由退货
     */
    @ExcelProperty("无理由退货")
    private String noReasonReturn;
    /**
     * 型号
     */
    @ExcelProperty("型号")
    private String model;
    /**
     * 商品条形码
     */
    @ExcelProperty("商品条形码")
    private String productBarcode;
    /**
     * 商品毛重(公斤)
     */
    @ExcelProperty("商品毛重(公斤)")
    private String grossWeight;
    /**
     * [包装]长(mm)
     */
    @ExcelProperty("[包装]长(mm)")
    private String packageLength;
    /**
     * [包装]宽(mm)
     */
    @ExcelProperty("[包装]宽(mm)")
    private String packageWidth;
    /**
     * [包装]高(mm)
     */
    @ExcelProperty("[包装]高(mm)")
    private String packageHeight;
    /**
     * 货号
     */
    @ExcelProperty("货号")
    private String productName;
    /**
     * 产地
     */
    @ExcelProperty("产地")
    private String origin;
    /**
     * 适用品牌
     */
    @ExcelProperty("适用品牌")
    private String applicableBrand;
    /**
     * 颜色
     */
    @ExcelProperty("颜色")
    private String color;
    /**
     * 适用位置
     */
    @ExcelProperty("适用位置")
    private String applicableLocation;
    /**
     * 安装方式
     */
    @ExcelProperty("安装方式")
    private String installMethod;
    /**
     * 图案
     */
    @ExcelProperty("图案")
    private String pattern;
    /**
     * 京东价（元）
     */
    @ExcelProperty("京东价（元）")
    private String jdPrice;
    /**
     * 市场价（元）
     */
    @ExcelProperty("市场价（元）")
    private String marketPrice;
    /**
     * 折扣
     */
    @ExcelProperty("折扣")
    private String discount;
    /**
     * 成本价（元）
     */
    @ExcelProperty("成本价（元）")
    private String costPrice;
    /**
     * 颜色分类
     */
    @ExcelProperty("颜色分类")
    private String colorCategory;
    /**
     * 版本
     */
    @ExcelProperty("版本")
    private String version;
    /**
     * SKU京东价
     */
    @ExcelProperty("SKU京东价")
    private String skuJdPrice;
    /**
     * SKU库存
     */
    @ExcelProperty("SKU库存")
    private String skuInventory;
    /**
     * SKU商品条形码
     */
    @ExcelProperty("SKU商品条形码")
    private String skuBarcode;
    /**
     * SKU工艺
     */
    @ExcelProperty("SKU工艺")
    private String skuProcess;
    /**
     * SKU功能
     */
    @ExcelProperty("SKU功能")
    private String skuFunction;
    /**
     * SKU款式
     */
    @ExcelProperty("SKU款式")
    private String skuDesign;
    /**
     * SKU图案
     */
    @ExcelProperty("SKU图案")
    private String skuPattern;
    /**
     * SKU热门机型
     */
    @ExcelProperty("SKU热门机型")
    private String skuHotModel;
    /**
     * SKU风格
     */
    @ExcelProperty("SKU风格")
    private String skuStyle;
    /**
     * SKU材质
     */
    @ExcelProperty("SKU材质")
    private String skuMaterial;
    /**
     * SKU颜色
     */
    @ExcelProperty("SKU颜色")
    private String skuColor;
    /**
     * SKU适用品牌
     */
    @ExcelProperty("SKU适用品牌")
    private String skuBrand;
    /**
     * 商家sku
     */
    @ExcelProperty("商家sku")
    private String sku;
    /**
     * SKU图片
     */
    @ExcelProperty("SKU图片")
    private String skuImage;
    /**
     * SKU透明图
     */
    @ExcelProperty("SKU透明图")
    private String skuTransparentImage;
    /**
     * 商品主图路径
     */
    @ExcelProperty("商品主图路径")
    private String mainImagePath;
    /**
     * 主图图片
     */
    @ExcelProperty("主图图片")
    private String mainImage;
    /**
     * 主图透明图
     */
    @ExcelProperty("主图透明图")
    private String transparentImage;
    /**
     * 主图视频
     */
    @ExcelProperty("主图视频")
    private String mainImageVideo;
    /**
     * 电脑版商品详情
     */
    @ExcelProperty("电脑版商品详情")
    private String pcProductDetail;
    /**
     * 店内分类
     */
    @ExcelProperty("店内分类")
    private String inStoreCategory;
    /**
     * 发货地
     */
    @ExcelProperty("发货地")
    private String shippingPlace;
    /**
     * 运费
     */
    @ExcelProperty("运费")
    private String freight;
    /**
     * 配送时效
     */
    @ExcelProperty("配送时效")
    private String deliveryTime;
    /**
     * 包装清单
     */
    @ExcelProperty("包装清单")
    private String packageList;
    /**
     * 售后服务
     */
    @ExcelProperty("售后服务")
    private String afterSaleService;
    /**
     * 销售单位
     */
    @ExcelProperty("销售单位")
    private String saleUnit;
    /**
     * 支付方式限制
     */
    @ExcelProperty("支付方式限制")
    private String payMethodLimit;
    /**
     * 发票限制
     */
    @ExcelProperty("发票限制")
    private String invoiceLimit;
    /**
     * 特色服务标名称
     */
    @ExcelProperty("特色服务标名称")
    private String specialServiceLabel;
    /**
     * 操作状态
     */
    @ExcelProperty("操作状态")
    private String operationStatus;
    /**
     * 操作时间
     */
    @ExcelProperty("操作时间")
    private String operationTime;
    /**
     * 京东账号
     */
    @ExcelProperty("京东账号")
    private String jdAccount;
    /**
     * 京东密码
     */
    @ExcelProperty("京东密码")
    private String jdPassword;

}
