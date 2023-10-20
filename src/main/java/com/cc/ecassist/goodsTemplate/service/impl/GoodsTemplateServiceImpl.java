package com.cc.ecassist.goodsTemplate.service.impl;

import com.alibaba.excel.EasyExcel;
import com.cc.ecassist.common.exception.ServiceException;
import com.cc.ecassist.goodsTemplate.constant.GenType;
import com.cc.ecassist.goodsTemplate.constant.PathConstant;
import com.cc.ecassist.goodsTemplate.domain.ColorCategory;
import com.cc.ecassist.goodsTemplate.domain.GenGoodsTemplateVO;
import com.cc.ecassist.goodsTemplate.domain.ModelVO;
import com.cc.ecassist.goodsTemplate.domain.OnShelfExportVO;
import com.cc.ecassist.goodsTemplate.service.GoodsTemplateService;
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
import java.util.Comparator;
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
    public String genGoodsTemplateFiles(GenGoodsTemplateVO genGoodsTemplateVO) {

        updatePath(genGoodsTemplateVO);

        List<OnShelfExportVO> templateList = genOnShelfExcel(genGoodsTemplateVO);
        Map<String, List<OnShelfExportVO>> mapByProductName =
                templateList.stream().collect(Collectors.groupingBy(OnShelfExportVO::getProductName));

        FileUtils.deleteDir(PathConstant.getFullGenPath(PathConstant.GOODS_TEMPLATE_PATH));
        mapByProductName.forEach((productName, skuList) -> {
            // 生成路径
            String productPath = PathConstant.getFullGenPath(PathConstant.GOODS_TEMPLATE_PATH) + productName + "/";
            FileUtils.deleteDir(productPath);

            // 商品 即型号
            OnShelfExportVO product = skuList.get(0);

            Map<String, File> imageByName = getImageMap(product.getMainImagePath());
            // 商品图片
            String imagePath = productPath + "商品图片";
            genNewImages(imagePath, imageByName, product.getMainImage(), product.getTransparentImage());

            // 电脑版商品详情图
            String pcPath = productPath + "电脑版商品详情图";
            genNewImages(pcPath, imageByName, product.getPcProductDetail(), null);

            // 手机版商品详情图
            String mobilePath = productPath + "手机版商品详情图";
            genNewImages(mobilePath, imageByName, product.getPcProductDetail(), null);

            skuList.stream()
                    .collect(Collectors.toMap(OnShelfExportVO::getColorCategory, e -> e, (e1, e2) -> e1))
                    .forEach((colorCategory, sku) -> {
                        // sku图片 路径名称为 颜色分类
                        String skuPath = productPath + colorCategory;
                        genNewImages(skuPath, getImageMap(sku.getMainImagePath()), sku.getSkuImage(),
                                sku.getSkuTransparentImage());
                    });

        });
        return "";
    }

    /**
     * 获取文件夹下的图片
     *
     * @param imagePath
     * @return
     */
    private Map<String, File> getImageMap(String imagePath) {
        File[] imageList;
        try {
            imageList = Objects.requireNonNull(new File(imagePath).listFiles());
        } catch (NullPointerException e) {
            throw new RuntimeException("商品主图路径不存在：" + imagePath);
        }
        return Arrays.stream(imageList)
                .collect(Collectors.toMap(image -> FilenameUtils.getBaseName(image.getName()), image -> image));
    }

    @Override
    public void updatePath(GenGoodsTemplateVO genGoodsTemplateVO) {
        PathConstant.setPath(genGoodsTemplateVO.getPath());
        PathConstant.setGenPath(genGoodsTemplateVO.getGenPath());
        PathConstant.setMainImagePath(genGoodsTemplateVO.getMainImagePath());
    }

    /**
     * 按模板匹配型号和颜色分类生成数据
     *
     * @param genGoodsTemplateVO
     * @return
     */
    private List<OnShelfExportVO> genOnShelfExcel(GenGoodsTemplateVO genGoodsTemplateVO) {

        // 模板excel
        List<OnShelfExportVO> templateList = getTemplateList(null);
        OnShelfExportVO originTemplate = templateList.get(0);
        originTemplate.setMainImagePath(genGoodsTemplateVO.getMainImagePath());
        if (!originTemplate.getMainImagePath().endsWith("\\")) {
            originTemplate.setMainImagePath(originTemplate.getMainImagePath() + "\\");
        }
        // 颜色分类excel
        List<ColorCategory> colorCategoryList =
                EasyExcel.read(PathConstant.getFullPath(PathConstant.COLOR_CATEGORY_EXCEL_NAME)).head(ColorCategory.class).sheet(0).doReadSync();
        // 型号编码excel
        List<ModelVO> modelList = getModelList();
        Map<String, List<ModelVO>> modelByVersionNo =
                modelList.stream().collect(Collectors.groupingBy(ModelVO::getVersionNo));
        Map<String, ModelVO> modelByCode =
                modelList.stream().collect(Collectors.toMap(ModelVO::getCode, e -> e));

        List<OnShelfExportVO> result = Lists.newArrayList();

        genGoodsTemplateVO.getModelList().stream().map(modelByCode::get).forEach(modelVO -> {
            OnShelfExportVO template = new OnShelfExportVO();
            BeanUtils.copyProperties(originTemplate, template);

            String modelCode = modelVO.getCode();
            // 默认单型号模式
            List<String> selectModelList = Lists.newArrayList(modelVO.getCode());
            // 多型号模式 把版本编码一样的型号都生成 商品标题、货号相同
            if (GenType.MULTI.getIndex().equals(genGoodsTemplateVO.getGenType())) {
                selectModelList.addAll(modelByVersionNo.get(modelVO.getVersionNo()).stream()
                        .map(ModelVO::getCode)
                        .collect(Collectors.toList()));
                selectModelList = selectModelList.stream().distinct().collect(Collectors.toList());
            }
            // 单型号模式selectModelList只有一个，所以也不需要重新构建标题和货号
            template.setProductTitle(modelCode + genGoodsTemplateVO.getProductCategory());
            template.setProductName(modelCode + genGoodsTemplateVO.getProductCategory());

            selectModelList.forEach(model -> {
                template.setModel(model);

                colorCategoryList.forEach(colorCategory -> {

                    template.setSkuImage(colorCategory.getSkuImage());
                    template.setColorCategory(colorCategory.getCategory());

                    String versions = modelByCode.get(model).getVersion();
                    for (String version : versions.split(",")) {
                        OnShelfExportVO export = new OnShelfExportVO();
                        BeanUtils.copyProperties(template, export);
                        export.setMainImagePath(template.getMainImagePath() + model);
                        export.setVersion(version);
                        result.add(export);
                    }
                });
            });

        });
        return result;
    }

    /**
     * 生成文件夹和指定的图片
     *
     * @param newPath          路径
     * @param imageByName      所有图片
     * @param images           源图片文件名 不带图片格式 多个用'|'分隔
     * @param transparentImage 透明图 null不生成
     */
    private void genNewImages(String newPath, Map<String, File> imageByName, String images, String transparentImage) {
        if (!newPath.endsWith("/")) {
            newPath += "/";
        }
        FileUtils.createDir(newPath);

        String originPath = FilenameUtils.getFullPath(imageByName.values().stream().findFirst().get().getPath())
                .replace('\\', '/');

        File origin;
        String[] imageNames = images.split("\\|");
        for (int i = 0; i < imageNames.length; i++) {
            String imageName = imageNames[i];
            origin = imageByName.get(imageName);
            if (origin == null) {
                throw new ServiceException("图片不存在：" + originPath + imageName);
            }
            String newImagePath = newPath + (i + 1) + "." + FilenameUtils.getExtension(origin.getName());
            copyImage(origin, newImagePath);
        }
        // 透明图
        if (transparentImage != null) {
            origin = imageByName.get(transparentImage);
            if (origin == null) {
                throw new ServiceException("透明图不存在：" + originPath + transparentImage);
            }
            copyImage(origin, newPath + "透明图" + "." + FilenameUtils.getExtension(origin.getName()));
        }
    }

    /**
     * 复制图片
     *
     * @param origin         源图片
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

        List<Map<Integer, String>> data =
                EasyExcel.read(PathConstant.getFullPath(PathConstant.MODEL_EXCEL_NAME)).sheet().doReadSync();
        data.forEach(map -> {
            for (int i = 0; i < map.size(); i += 3) {
                String versionNo = map.get(i);
                String version = map.get(i + 1);
                String code = map.get(i + 2);
                if (StringUtils.isBlank(versionNo) || StringUtils.isBlank(version) || StringUtils.isBlank(code) ||
                        "版本".equals(version) || "编码".equals(code)) {
                    continue;
                }
                ModelVO model = new ModelVO();
                model.setVersionNo(versionNo);
                model.setVersion(version.replace('/', ' '));
                model.setCode(code);
                result.add(model);
            }
        });
        result.sort(Comparator.comparing(ModelVO::getVersionNo));
        return result;
    }

    public List<OnShelfExportVO> getTemplateList(String excelName) {
        excelName = PathConstant.getFullPath(StringUtils.isBlank(excelName) ?
                PathConstant.ON_SHELF_TEMPLATE_EXCEL_NAME : excelName);
        return EasyExcel.read(excelName).head(OnShelfExportVO.class).sheet().doReadSync();
    }
}
