package com.cc.ecassist.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.domain.ColorCategory;
import com.cc.ecassist.domain.GoodsTemplateVO;
import com.cc.ecassist.domain.KeywordVO;
import com.cc.ecassist.domain.ModelVO;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.domain.ProductVO;
import com.cc.ecassist.service.GoodsTemplateService;
import com.cc.ecassist.utils.DateUtils;
import com.cc.ecassist.utils.FileUtils;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
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
    public void genGoodsTemplateFiles(List<ProductVO> productList) {

        Map<String, ProductVO> productByName =
                productList.stream().collect(Collectors.toMap(ProductVO::getProductName, e -> e));

        List<OnShelfExportVO> templateList = genOnShelfExcel(productByName);
        Map<String, List<OnShelfExportVO>> mapByProductName =
                templateList.stream().collect(Collectors.groupingBy(OnShelfExportVO::getProductName));

        mapByProductName.forEach((productName, skuList) -> {
            // 生成路径
            String productPath = Constant.GOODS_TEMPLATE_PATH + productName + "/";
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

    private List<OnShelfExportVO> genOnShelfExcel(Map<String, ProductVO> productByName) {
        List<OnShelfExportVO> templateList =
                EasyExcel.read(Constant.ON_SHELF_TEMPLATE_EXCEL_NAME).head(OnShelfExportVO.class).sheet(0).doReadSync();
        List<ColorCategory> colorCategoryList =
                EasyExcel.read(Constant.COLOR_CATEGORY_EXCEL_NAME).head(ColorCategory.class).sheet(0).doReadSync();

        String exportName = String.format(Constant.ON_SHELF_EXCEL_NAME, DateUtils.dateTimeNow());
        List<OnShelfExportVO> result = Lists.newArrayList();
        templateList.forEach(template -> {
            template.setProductTitle(buildTitle(productByName.get(template.getProductName())));
            colorCategoryList.forEach(colorCategory -> {
                template.setColorCategory(colorCategory.getCategory());
                template.setSkuImage(colorCategory.getSkuImage());
                OnShelfExportVO export = new OnShelfExportVO();
                BeanUtils.copyProperties(template, export);
                result.add(export);
            });
        });
        EasyExcel.write(exportName)
                .sheet("上架内容")
                .head(OnShelfExportVO.class)
                .doWrite(result);
        return result;
    }

    /**
     * 构建商品标题 每个货号使用同一个标题
     * 选择多个型号 多个关键词(打乱顺序)
     * 型号1 + 类目 + {型号n + 关键词n}*n + 多余的关键词
     *
     * @param productVO
     * @return
     */
    private String buildTitle(ProductVO productVO) {

        StringBuilder title = new StringBuilder();
        String[] model = StringUtils.defaultString(productVO.getModel()).split("\\|");
        String[] keywordArr = StringUtils.defaultString(productVO.getKeyword()).split("\\|");
        List<String> keyword = Lists.newArrayList(keywordArr);
        // 打乱顺序
        Collections.shuffle(keyword);
        // 型号1 + 类目
        title.append(model[0]).append(productVO.getProductCategory());
        int maxTitleLength = 100;
        int i = 1;
        // 型号 + 关键词 例 x50PRO诸事顺利
        while (i <= keyword.size()) {
            if (i < model.length) {
                if (title.length() + model[i].length() > maxTitleLength) {
                    break;
                }
                title.append(model[i]);
            }
            if (title.length() + keyword.get(i).length() > maxTitleLength) {
                break;
            }
            // 如果关键词更多 会把多的拼接完
            title.append(keyword.get(i++ - 1));
        }
        return title.toString();
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

    @Override
    public List<ModelVO> getModelList() {
        List<ModelVO> result = Lists.newArrayList();

        List<Map<Integer, String>> data = EasyExcel.read(Constant.MODEL_EXCEL_NAME).sheet().doReadSync();
        data.forEach(map -> {
            for (int i = 0; i < map.size(); i += 3) {
                String versionNo = map.get(i);
                String version = map.get(i + 1);
                String code = map.get(i + 2);
                if (StringUtils.isBlank(versionNo) || StringUtils.isBlank(version) || StringUtils.isBlank(code)) {
                    continue;
                }
                ModelVO model = new ModelVO();
                model.setVersionNo(versionNo);
                model.setVersion(version);
                model.setCode(code);
                result.add(model);
            }
        });
        return result;
    }

    @Override
    public List<KeywordVO> getKeywordList() {
        return EasyExcel.read(Constant.KEYWORD_EXCEL_NAME).head(KeywordVO.class).sheet().doReadSync();
    }

    @Override
    public JSONObject getModelData() {
        List<ModelVO> modelList = getModelList();
        return new JSONObject(modelList.stream().collect(Collectors.groupingBy(ModelVO::getVersionNo)));
    }

    @Override
    public JSONObject getKeywordData() {
        List<KeywordVO> keywordList = getKeywordList();
        return new JSONObject(keywordList.stream()
                .collect(Collectors.groupingBy(KeywordVO::getProductCategory,
                        Collectors.groupingBy(KeywordVO::getType))));
    }
}
