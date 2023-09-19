package com.cc.ecassist.service.impl;

import com.alibaba.excel.EasyExcel;
import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.domain.Attribute;
import com.cc.ecassist.domain.ColorCategory;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.service.OnShelfService;
import com.cc.ecassist.utils.DateUtils;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Service
public class OnShelfServiceImpl implements OnShelfService {

    @Override
    public String exportExcel() {

        List<Attribute> attributeList =
                EasyExcel.read(Constant.ATTRIBUTE_EXCEL_NAME).head(Attribute.class).sheet(0).doReadSync();
        List<ColorCategory> colorCategoryList =
                EasyExcel.read(Constant.COLOR_CATEGORY_EXCEL_NAME).head(ColorCategory.class).sheet(0).doReadSync();
        List<OnShelfExportVO> templateList =
                EasyExcel.read(Constant.ON_SHELF_TEMPLATE_EXCEL_NAME).head(OnShelfExportVO.class).sheet(0).doReadSync();

        OnShelfExportVO template = templateList.get(0);

        String exportName = String.format(Constant.ON_SHELF_EXCEL_NAME, DateUtils.dateTimeNow());
        EasyExcel.write(exportName)
                .sheet("上架内容")
                .head(OnShelfExportVO.class)
                .doWrite(() -> buildExportList(template, attributeList, colorCategoryList));
        return exportName;
    }

    private List<OnShelfExportVO> buildExportList(OnShelfExportVO template, List<Attribute> attributeList,
                                                  List<ColorCategory> colorCategoryList) {
        List<OnShelfExportVO> result = Lists.newArrayList();
        attributeList.forEach(attribute -> {
            template.setProductTitle(buildTitle(template, attribute));
            template.setModel(attribute.getModel());
            template.setProductNumber(template.getProductNumber() + attribute.getModel());
            template.setVersion(attribute.getVersion());
            template.setSkuHotModel(attribute.getHotAttribute());
            template.setSkuBrand(attribute.getBrand());
            template.setMainImagePath(template.getMainImagePath() + attribute.getModel());
            colorCategoryList.forEach(colorCategory -> {
                template.setColorCategory(colorCategory.getCategory());
                template.setSkuImage(colorCategory.getSkuImage());
                OnShelfExportVO export = new OnShelfExportVO();
                BeanUtils.copyProperties(template, export);
                result.add(export);
            });
        });
        return result;
    }

    /**
     * 构建商品标题
     * 关键字 + {版本+sku风格}*n + 版本+sku材质 + 版本+sku款式
     *
     * @param template
     * @param attribute
     * @return
     */
    private String buildTitle(OnShelfExportVO template, Attribute attribute) {
        StringBuilder titleBuilder = new StringBuilder();
        titleBuilder.append(attribute.getKeyword());
//            String[] category = template.getProductCategory().split(">");
//            titleBuilder.append(category[category.length - 1].replace("/", ""));
        String[] version = StringUtils.defaultString(attribute.getVersion()).split("\\|");
        String[] skuStyle = StringUtils.defaultString(template.getSkuStyle()).split("\\|");
        int i = 0;
        // 版本+sku风格 例 x50PRO诸事顺利
        for (; i < Math.min(skuStyle.length, version.length); i++) {
            titleBuilder.append(version[i]).append(skuStyle[i]);
        }
        String versionRegex = "[version]";
        // 版本+sku材质
        titleBuilder.append(versionRegex).append(template.getSkuMaterial());
        // 版本+sku款式
        titleBuilder.append(versionRegex).append(template.getSkuDesign());
        String title = titleBuilder.toString();
        for (; i < version.length; i++) {
            title = StringUtils.replaceOnce(title, versionRegex, version[i]);
        }
        // 版本数量不够则不拼接版本 直接拼接sku材质和sku款式
        title = StringUtils.remove(title, versionRegex);
        return title;
    }
}
