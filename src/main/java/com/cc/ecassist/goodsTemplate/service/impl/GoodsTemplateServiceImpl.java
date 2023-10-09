package com.cc.ecassist.goodsTemplate.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.cc.ecassist.common.exception.ServiceException;
import com.cc.ecassist.goodsTemplate.constant.GenType;
import com.cc.ecassist.goodsTemplate.constant.PathConstant;
import com.cc.ecassist.goodsTemplate.domain.*;
import com.cc.ecassist.goodsTemplate.service.GoodsTemplateService;
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
import java.util.*;
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

        if (StringUtils.isNotBlank(genGoodsTemplateVO.getPath())) {
            PathConstant.setPATH(genGoodsTemplateVO.getPath().replace("\\", "/"));
            FileUtils.createDir(PathConstant.PATH);
        }

        List<OnShelfExportVO> templateList = genOnShelfExcel(genGoodsTemplateVO);
        Map<String, List<OnShelfExportVO>> mapByProductName =
                templateList.stream().collect(Collectors.groupingBy(OnShelfExportVO::getProductName));

        // 需要生成三个excel文件数据
        LinkedList<GoodsTemplateVO> goodsTemplateList = Lists.newLinkedList();
        LinkedList<ProductPropertyVO> productPropertyList = Lists.newLinkedList();
        LinkedList<SkuPropertyVO> skuPropertyList = Lists.newLinkedList();

        FileUtils.deleteDir(PathConstant.getFullPath(PathConstant.GOODS_TEMPLATE_PATH));
        mapByProductName.forEach((productName, skuList) -> {
            // 生成路径
            String productPath = PathConstant.getFullPath(PathConstant.GOODS_TEMPLATE_PATH) + productName + "/";
            FileUtils.deleteDir(productPath);

            // 商品 即型号
            OnShelfExportVO product = skuList.get(0);

            // excel：批量商品属性导入
            productPropertyList.add(buildProductProperty(product));

            File[] imageList;
            try {
                imageList = Objects.requireNonNull(new File(product.getMainImagePath()).listFiles());
            } catch (NullPointerException e) {
                throw new RuntimeException("商品主图路径不存在：" + product.getMainImagePath());
            }
            Map<String, File> imageByName = Arrays.stream(imageList)
                    .collect(Collectors.toMap(image -> FilenameUtils.getBaseName(image.getName()), image -> image));
            // 商品图片
            String imagePath = productPath + "商品图片";
            genNewImages(imagePath, imageByName, product.getMainImage(), product.getTransparentImage());

            // 电脑版商品详情图
            String pcPath = productPath + "电脑版商品详情图";
            genNewImages(pcPath, imageByName, product.getPcProductDetail(), null);

            // 手机版商品详情图
            String mobilePath = productPath + "手机版商品详情图";
            genNewImages(mobilePath, imageByName, product.getPcProductDetail(), null);

            // sku图片
            skuList.forEach(sku -> {
                // sku路径 名称为 颜色分类
                String skuPath = productPath + sku.getColorCategory();
                genNewImages(skuPath, imageByName, sku.getSkuImage(), sku.getSkuTransparentImage());

                // excel：批量SKU属性导入
                skuPropertyList.add(buildSkuProperty(sku));
                // excel：添加新商品模板的数据
                GoodsTemplateVO goodsTemplate = new GoodsTemplateVO();
                BeanUtils.copyProperties(sku, goodsTemplate);
                goodsTemplate.setShippingPlace(goodsTemplate.getShippingPlace().replace('-', '>'));
                goodsTemplateList.add(goodsTemplate);
            });

        });
        // 生成excel
        genGoodsTemplateExcel(goodsTemplateList, productPropertyList, skuPropertyList);
        // 压缩文件
        String zipFileName = String.format(PathConstant.getFullPath(PathConstant.ZIP_NAME),
                DateUtils.dateTimeNow());
        FileUtils.zipDirectory(PathConstant.getFullPath(PathConstant.GOODS_TEMPLATE_PATH), zipFileName);
        return zipFileName;
    }

    /**
     * 构建商品属性对象
     *
     * @param product
     * @return
     */
    private ProductPropertyVO buildProductProperty(OnShelfExportVO product) {
        ProductPropertyVO result = new ProductPropertyVO();
        result.setCode(product.getProductTitle());

        StringBuilder property = new StringBuilder();
        property.append("适用品牌:").append(product.getApplicableBrand());

        result.setProperty(property.toString());
        return result;
    }

    /**
     * 构建sku属性对象
     *
     * @param sku
     * @return
     */
    private SkuPropertyVO buildSkuProperty(OnShelfExportVO sku) {
        SkuPropertyVO result = new SkuPropertyVO();
        result.setCode(sku.getProductTitle());
        result.setSku(sku.getColorCategory() + sku.getVersion());

        StringBuilder property = new StringBuilder();
        property.append("图案:").append(sku.getSkuPattern()).append(";");
        property.append("功能:").append(sku.getSkuFunction()).append(";");
        property.append("工艺:").append(sku.getSkuProcess()).append(";");
        property.append("材质:").append(sku.getSkuMaterial()).append(";");
        property.append("热门机型:").append(sku.getSkuHotModel()).append(";");
        property.append("风格:").append(sku.getSkuStyle()).append(";");
        property.append("款式:").append(sku.getSkuDesign());

        result.setProperty(property.toString().replace('|', ' '));
        return result;
    }

    /**
     * 生成excel：添加新商品模板.xlsx
     *
     * @param goodsTemplateList
     */
    private void genGoodsTemplateExcel(LinkedList<GoodsTemplateVO> goodsTemplateList,
                                       LinkedList<ProductPropertyVO> productPropertyList,
                                       LinkedList<SkuPropertyVO> skuPropertyList) {
        // 按excel格式 先填充一行空值
        goodsTemplateList.addFirst(new GoodsTemplateVO());
        EasyExcel.write(PathConstant.getFullPath(PathConstant.GOODS_TEMPLATE_EXCEL_NAME))
                .sheet()
                .relativeHeadRowIndex(1)
                .head(GoodsTemplateVO.class)
                .doWrite(goodsTemplateList);

        EasyExcel.write(PathConstant.getFullPath(PathConstant.PRODUCT_PROPERTY_EXCEL_NAME))
                .sheet()
                .head(ProductPropertyVO.class)
                .doWrite(productPropertyList);

        EasyExcel.write(PathConstant.getFullPath(PathConstant.SKU_PROPERTY_EXCEL_NAME))
                .sheet()
                .head(SkuPropertyVO.class)
                .doWrite(skuPropertyList);
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
        OnShelfExportVO template = templateList.get(0);
        template.setMainImagePath(genGoodsTemplateVO.getMainImagePath());
        if (!template.getMainImagePath().endsWith("\\")) {
            template.setMainImagePath(template.getMainImagePath() + "\\");
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
        // 型号词excel
        List<ModelWordVO> modelWordList = getModelWordList();
        Map<String, ModelWordVO> modelWordByCode =
                modelWordList.stream().collect(Collectors.toMap(ModelWordVO::getCode, e -> e));

        // 属性excel
        List<Attribute> attributeList =
                EasyExcel.read(PathConstant.getFullPath(PathConstant.ATTRIBUTE_EXCEL_NAME)).head(Attribute.class).sheet(0).doReadSync();
        Map<String, Attribute> attributeByModel = attributeList.stream().collect(Collectors.toMap(Attribute::getModel
                , e -> e));


        List<OnShelfExportVO> result = Lists.newArrayList();
        // 默认单型号模式
        List<String> selectModelList = Lists.newArrayList(genGoodsTemplateVO.getModelList());
        // 多型号模式 把版本编码一样的型号都生成
        if (GenType.MULTI.getValue().equals(genGoodsTemplateVO.getGenType())) {
            selectModelList = genGoodsTemplateVO.getVersionNoList().stream()
                    .map(modelByVersionNo::get)
                    .flatMap(List::stream)
                    .map(ModelVO::getCode)
                    .collect(Collectors.toList());
        }
        selectModelList.forEach(model -> {
            template.setProductTitle(buildTitle(model, genGoodsTemplateVO, modelByCode, modelByVersionNo,
                    modelWordByCode));
            Attribute attribute = attributeByModel.get(model);
            if (attribute == null) {
                throw new ServiceException("属性.xlsx不存在该型号：" + model);
            }
            template.setApplicableBrand(attribute.getBrand());
            template.setSkuHotModel(attribute.getHotAttribute());

            colorCategoryList.forEach(colorCategory -> {
                OnShelfExportVO export = new OnShelfExportVO();
                BeanUtils.copyProperties(template, export);
                export.setModel(model);
                export.setProductName(model + genGoodsTemplateVO.getProductCategory());
                export.setMainImagePath(template.getMainImagePath() + model);
                export.setColorCategory(colorCategory.getCategory());
                export.setSkuImage(colorCategory.getSkuImage());
                export.setVersion(modelByCode.get(model).getVersion());
                result.add(export);
            });
        });


        String exportName = String.format(PathConstant.getFullPath(PathConstant.ON_SHELF_EXCEL_NAME),
                DateUtils.dateTimeNow());
        EasyExcel.write(exportName)
                .sheet("上架内容")
                .head(OnShelfExportVO.class)
                .doWrite(result);
        return result;
    }

    /**
     * 构建商品标题 每个型号使用同一个标题
     * 型号词中的关键词 和 页面选择的关键词 都打乱顺序
     * 型号关键词1 + 类目 + {型号关键词n + 页面关键词n}*n + 多余的页面关键词
     *
     * @param model
     * @param genGoodsTemplateVO
     * @param modelByCode
     * @param modelByVersionNo
     * @param modelWordByCode
     * @return
     */
    private String buildTitle(String model, GenGoodsTemplateVO genGoodsTemplateVO, Map<String, ModelVO> modelByCode,
                              Map<String, List<ModelVO>> modelByVersionNo, Map<String, ModelWordVO> modelWordByCode) {

        StringBuilder title = new StringBuilder();

        List<String> modelList = Collections.singletonList(model);
        if (GenType.MULTI.getValue().equals(genGoodsTemplateVO.getGenType())) {
            ModelVO modelVO = modelByCode.get(model);
            modelList =
                    modelByVersionNo.get(modelVO.getVersionNo()).stream().map(ModelVO::getCode).collect(Collectors.toList());
        }
        // 型号关键词
        List<String> modelKeyword = modelList.stream()
                .map(modelWordByCode::get)
                .map(ModelWordVO::getOtherKeyword)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());

        // 选择的关键词
        List<String> keyword = genGoodsTemplateVO.getKeywordList();
        keyword.removeIf(StringUtils::isBlank);
        // 打乱顺序
        Collections.shuffle(modelKeyword);
        Collections.shuffle(keyword);

        // 型号关键词1 + 类目
        title.append(modelWordByCode.get(model).getFirstKeyword()).append(genGoodsTemplateVO.getProductCategory());
        int maxTitleLength = 50;
        int i = 0;
        // 型号 + 关键词 例 x50PRO诸事顺利
        while (i < keyword.size()) {
            if (i < modelKeyword.size()) {
                if (title.length() + modelKeyword.get(i).length() > maxTitleLength) {
                    break;
                }
                title.append(modelKeyword.get(i));
            }
            if (title.length() + keyword.get(i).length() > maxTitleLength) {
                break;
            }
            // 如果关键词更多 会把多的拼接完
            title.append(keyword.get(i++));
        }
        return title.toString();
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

        File origin;
        String[] imageNames = images.split("\\|");
        for (int i = 0; i < imageNames.length; i++) {
            String imageName = imageNames[i];
            origin = imageByName.get(imageName);
            if (origin == null) {
                throw new ServiceException("图片不存在：" + imageName);
            }
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
        result.sort(Comparator.comparing(ModelVO::getVersionNo));
        return result;
    }

    @Override
    public List<KeywordVO> getKeywordList() {
        return EasyExcel.read(PathConstant.getFullPath(PathConstant.KEYWORD_EXCEL_NAME)).head(KeywordVO.class).sheet().doReadSync();
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

    @Override
    public List<OnShelfExportVO> getTemplateList(String excelName) {
        excelName = PathConstant.getFullPath(StringUtils.isBlank(excelName) ?
                PathConstant.ON_SHELF_TEMPLATE_EXCEL_NAME : excelName);
        return EasyExcel.read(excelName).head(OnShelfExportVO.class).sheet().doReadSync();
    }

    /**
     * 获取型号词excel数据
     *
     * @return
     */
    private List<ModelWordVO> getModelWordList() {
        List<ModelWordVO> result = Lists.newArrayList();

        List<Map<Integer, String>> data =
                EasyExcel.read(PathConstant.getFullPath(PathConstant.MODEL_WORD_EXCEL_NAME)).sheet().doReadSync();
        data.forEach(map -> {
            String code = map.get(0);
            String firstKeyword = map.get(1);
            if (StringUtils.isBlank(code) || StringUtils.isBlank(firstKeyword)) {
                return;
            }
            ModelWordVO modelWord = new ModelWordVO();
            modelWord.setCode(code);
            modelWord.setFirstKeyword(firstKeyword.equals("无") ? code : firstKeyword);
            List<String> otherKeyword = Lists.newArrayList();
            for (int i = 2; i < map.size(); i++) {
                if (StringUtils.isNotBlank(map.get(i))) {
                    otherKeyword.add(map.get(i));
                }
            }
            modelWord.setOtherKeyword(otherKeyword);
            result.add(modelWord);

        });
        return result;
    }
}
