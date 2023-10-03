package com.cc.ecassist.goodsTemplate.domain;

import com.alibaba.excel.annotation.ExcelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 上架模板文件 新版
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Setter
@Getter
public class GoodsTemplateVO implements Serializable {

    private static final long serialVersionUID = 7567208514497669551L;

    /**
     * 占位 第一列为空
     */
    @ExcelProperty
    private String placeholder;

    /**
     * 产品类目
     */
    @ExcelProperty("类目")
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
     * 标语加链接的文字
     */
    @ExcelProperty("标语加链接的文字")
    private String productSloganWithUrl;
    /**
     * 标语链接地址
     */
    @ExcelProperty("标语链接地址")
    private String productSloganUrl;
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
     * 京东价（元）
     */
    @ExcelProperty("京东价")
    private String jdPrice;
    /**
     * 市场价（元）
     */
    @ExcelProperty("市场价")
    private String marketPrice;
    /**
     * 成本价（元）
     */
    @ExcelProperty("成本价")
    private String costPrice;
    /**
     * 颜色分类
     */
    @ExcelProperty("颜色分类")
    private String colorCategory;
    /**
     * 版本
     */
    @ExcelProperty("尺码")
    private String version;
    /**
     * SKU商品条形码
     */
    @ExcelProperty("SKU条形码")
    private String skuBarcode;
    /**
     * SKU京东价
     */
    @ExcelProperty("SKU京东价")
    private String skuJdPrice;
    /**
     * SKU库存
     */
    @ExcelProperty("库存")
    private String skuInventory;
    /**
     * 商家sku
     */
    @ExcelProperty("商家sku")
    private String sku;
    /**
     * 主图视频
     */
    @ExcelProperty("主图视频链接")
    private String mainImageVideo;
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
     * 发票限制
     */
    @ExcelProperty("下单验证码")
    private String verifyCode;
    /**
     * 24小时最大购买数
     */
    @ExcelProperty("24小时最大购买数")
    private String maxBuy;
    /**
     * 是否指定快递配送
     */
    @ExcelProperty("是否指定快递配送")
    private String isSpecifyExpress;

}
