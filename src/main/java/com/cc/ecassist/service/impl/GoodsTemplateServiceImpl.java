package com.cc.ecassist.service.impl;

import com.alibaba.excel.EasyExcel;
import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.domain.GoodsTemplateVO;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.service.GoodsTemplateService;
import com.cc.ecassist.utils.FileUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 商品模板
 *
 * @author congcong.jiang
 * @date 2023-09-23
 */
@Slf4j
@Service
public class GoodsTemplateServiceImpl implements GoodsTemplateService {

    @Override
    public void genGoodsTemplateFiles() {
        List<OnShelfExportVO> templateList =
                EasyExcel.read(Constant.ON_SHELF_TEMPLATE_EXCEL_NAME).head(OnShelfExportVO.class).sheet(0).doReadSync();
        Map<String, List<OnShelfExportVO>> mapByProductName =
                templateList.stream().collect(Collectors.groupingBy(OnShelfExportVO::getProductName));
        mapByProductName.forEach((productName, skuList) -> {
            // 生成路径
            String productPath = Constant.GOODS_TEMPLATE_NAME + productName + "/";
            FileUtils.deleteDir(productPath);

            // 商品 即型号
            OnShelfExportVO product = skuList.get(0);

            Map<String, File> imageByName = Arrays.stream(Objects.requireNonNull(new File(product.getMainImagePath()).listFiles()))
                    .collect(Collectors.toMap(image -> FilenameUtils.getBaseName(image.getName()), image -> image));
            // 商品图片
            String imagePath= productPath + "商品图片";
            genNewImages(imagePath, imageByName, product.getMainImage(), product.getTransparentImage());

            // 电脑版商品详情图
            String pcPath= productPath + "电脑版商品详情图";
            genNewImages(pcPath, imageByName, product.getPcProductDetail(), null);

            // 手机版商品详情图
            String mobilePath = productPath + "手机版商品详情图";
            genNewImages(mobilePath, imageByName, product.getPcProductDetail(), null);

            // sku图片
            skuList.forEach(e -> {
                // sku路径 名称为 颜色分类
                String skuPath = productPath + e.getColorCategory();
                genNewImages(skuPath, imageByName, e.getSkuImage(), e.getSkuTransparentImage());

            });

            // excel：添加新商品模板
            EasyExcel.write(productPath + "添加新商品模板.xlsx")
                    .sheet()
            .relativeHeadRowIndex(1)
            .head(GoodsTemplateVO.class)
            .doWrite(()-> {
                List<GoodsTemplateVO> list = Lists.newArrayList();
                // 按excel格式 先填充一行空值
                list.add(new GoodsTemplateVO());
                GoodsTemplateVO e = new GoodsTemplateVO();
                BeanUtils.copyProperties(product, e);
                e.setShippingPlace(e.getShippingPlace().replace('-', '>'));
                list.add(e);
                return list;
            });

        });
    }

    /**
     * 生成文件夹和指定的图片
     * @param newPath 路径
     * @param imageByName 所有图片
     * @param images 源图片文件名 不带图片格式 多个用'|'分隔
     * @param transparentImage 透明图 null不生成
     */
    private void genNewImages(String newPath, Map<String, File> imageByName, String images, String transparentImage) {
        if (!newPath.endsWith("/")) {
            newPath += "/";
        }
        FileUtils.createDir(newPath);

        File origin;
        String[] imageNames = images.split("\\|");
        for (int i = 0; i < imageNames.length; i++) {
            String imageName = imageNames[i];
            origin = imageByName.get(imageName);
            String newImagePath = newPath + (i + 1) + "." + FilenameUtils.getExtension(origin.getName());
            copyImage(origin, newImagePath);
        }
        // 透明图
        if (transparentImage != null) {
            origin = imageByName.get(transparentImage);
            copyImage(origin, newPath + "透明图" + "." + FilenameUtils.getExtension(origin.getName()));
        }
    }

    /**
     * 复制图片
     * @param origin 源图片
     * @param destPathPrefix 目标路径 包括文件名
     */
    private void copyImage(File origin, String destPathPrefix) {
        int retry = 3;
        while (retry-- > 0) {
            File newSkuTransparentImage = new File(destPathPrefix);
            try {
                FileSystemUtils.copyRecursively(origin, newSkuTransparentImage);
                return;
            } catch (Exception e) {
                if (retry == 0) {
                    throw new RuntimeException(e);
                }
                log.error("复制图片失败，源路径：{}；新路径：{}", origin.getPath(), newSkuTransparentImage.getPath(), e);
            }
        }
    }
}
