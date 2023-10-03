package com.cc.ecassist.goodsTemplate.service.impl;

import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson2.JSONObject;
import com.cc.ecassist.common.exception.ServiceException;
import com.cc.ecassist.goodsTemplate.constant.Constant;
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

        List<OnShelfExportVO> templateList = genOnShelfExcel(genGoodsTemplateVO);
        Map<String, List<OnShelfExportVO>> mapByProductName =
                templateList.stream().collect(Collectors.groupingBy(OnShelfExportVO::getProductName));

        LinkedList<GoodsTemplateVO> goodsTemplateList = Lists.newLinkedList();

        FileUtils.deleteDir(Constant.GOODS_TEMPLATE_PATH);
        mapByProductName.forEach((productName, skuList) -> {
            // 生成路径
            String productPath = Constant.GOODS_TEMPLATE_PATH + productName + "/";
            FileUtils.deleteDir(productPath);

            // 商品 即型号
            OnShelfExportVO product = skuList.get(0);

            File[] imageList ;
            try {
                imageList = Objects.requireNonNull(new File(product.getMainImagePath()).listFiles());
            } catch (NullPointerException e) {
                throw new RuntimeException("图片路径不存在");
            }
            Map<String, File> imageByName = Arrays.stream(imageList)
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
            skuList.forEach(sku -> {
                // sku路径 名称为 颜色分类
                String skuPath = productPath + sku.getColorCategory();
                genNewImages(skuPath, imageByName, sku.getSkuImage(), sku.getSkuTransparentImage());

                // excel：添加新商品模板的数据
                GoodsTemplateVO goodsTemplate = new GoodsTemplateVO();
                BeanUtils.copyProperties(sku, goodsTemplate);
                goodsTemplate.setShippingPlace(goodsTemplate.getShippingPlace().replace('-', '>'));
                goodsTemplateList.add(goodsTemplate);

//                List<String> versionNoList = genGoodsTemplateVO.getVersionNoList();
//                for (String versionNo : versionNoList) {
//                    List<ModelVO> modelList = modelByVersionNo.get(versionNo);
//                    modelList.forEach(model -> {
//                        GoodsTemplateVO goodsTemplate = new GoodsTemplateVO();
//                        BeanUtils.copyProperties(sku, goodsTemplate);
//                        goodsTemplate.setShippingPlace(goodsTemplate.getShippingPlace().replace('-', '>'));
//                        // 尺码（版本）
//                        goodsTemplate.setVersion(model.getVersion());
//                        goodsTemplateList.add(goodsTemplate);
//                    });
//                }
            });

        });
        // 生成excel
        genGoodsTemplateExcel(goodsTemplateList);
        // 压缩文件
        String zipFileName = Constant.PATH + String.format(Constant.ZIP_NAME,
                DateUtils.dateTimeNow());
        FileUtils.zipDirectory(Constant.GOODS_TEMPLATE_PATH, zipFileName);
        return zipFileName;
    }

    /**
     * 生成excel：添加新商品模板.xlsx
     * @param goodsTemplateList
     */
    private void genGoodsTemplateExcel(LinkedList<GoodsTemplateVO> goodsTemplateList) {
        // 按excel格式 先填充一行空值
        goodsTemplateList.addFirst(new GoodsTemplateVO());
        EasyExcel.write(Constant.GOODS_TEMPLATE_PATH + "添加新商品模板.xlsx")
                .sheet()
                .relativeHeadRowIndex(1)
                .head(GoodsTemplateVO.class)
                .doWrite(goodsTemplateList);
    }

    private List<OnShelfExportVO> genOnShelfExcel(GenGoodsTemplateVO genGoodsTemplateVO) {
        List<OnShelfExportVO> templateList =
                EasyExcel.read(Constant.ON_SHELF_TEMPLATE_EXCEL_NAME).head(OnShelfExportVO.class).sheet(0).doReadSync();
        List<ColorCategory> colorCategoryList =
                EasyExcel.read(Constant.COLOR_CATEGORY_EXCEL_NAME).head(ColorCategory.class).sheet(0).doReadSync();


        List<ModelVO> modelList = getModelList();
        Map<String, List<ModelVO>> modelByVersionNo =
                modelList.stream().collect(Collectors.groupingBy(ModelVO::getVersionNo));
        Map<String, ModelVO> modelByCode =
                modelList.stream().collect(Collectors.toMap(ModelVO::getCode, e -> e));

        OnShelfExportVO template = templateList.get(0);
        if (!template.getMainImagePath().endsWith("\\")) {
            template.setMainImagePath(template.getMainImagePath() + "\\");
        }


        List<OnShelfExportVO> result = Lists.newArrayList();
        // 默认单型号模式
        List<String> selectModelList = Lists.newArrayList(genGoodsTemplateVO.getModelList());
        // 多型号模式 把版本编码一样的型号都生成
        if (Constant.GenType.MULTI.getValue().equals(genGoodsTemplateVO.getGenType())) {
            List<String> finalSelectModelList = Lists.newArrayList();
            genGoodsTemplateVO.getVersionNoList().forEach(versionNo -> {
                finalSelectModelList.addAll(modelByVersionNo.get(versionNo).stream().map(ModelVO::getCode).collect(Collectors.toList()));
            });
            selectModelList = Lists.newArrayList(finalSelectModelList);
        }
        selectModelList.forEach(model -> {
            // TODO: 2023/10/3 标题新规则
//            template.setProductTitle(buildTitle(product));
            colorCategoryList.forEach(colorCategory -> {

                OnShelfExportVO export = new OnShelfExportVO();
                BeanUtils.copyProperties(template, export);
                export.setModel(model);
                export.setProductName(model + genGoodsTemplateVO.getProductCategory());
//                export.setMainImagePath(template.getMainImagePath() + model);
                export.setColorCategory(colorCategory.getCategory());
                export.setSkuImage(colorCategory.getSkuImage());
                export.setVersion(modelByCode.get(model).getVersion());
                result.add(export);
            });
        });


        String exportName = String.format(Constant.ON_SHELF_EXCEL_NAME, DateUtils.dateTimeNow());
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
        keyword.removeIf(StringUtils::isBlank);
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
            if (title.length() + keyword.get(i - 1).length() > maxTitleLength) {
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
        result.sort(Comparator.comparing(ModelVO::getVersionNo));
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

    @Override
    public List<OnShelfExportVO> getTemplateList(String excelName) {
        excelName = StringUtils.isBlank(excelName) ? Constant.ON_SHELF_TEMPLATE_EXCEL_NAME : excelName;
        return EasyExcel.read(excelName).head(OnShelfExportVO.class).sheet().doReadSync();
    }
}
