package com.cc.ecassist.service.impl;

import com.alibaba.excel.EasyExcel;
import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.domain.Attribute;
import com.cc.ecassist.domain.ColorCategory;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.service.OnShelfService;
import com.cc.ecassist.utils.DateUtils;
import com.google.common.collect.Lists;
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
    public void exportExcel() {

        List<Attribute> attributeList = EasyExcel.read(Constant.ATTRIBUTE_EXCEL_NAME).sheet(0).doReadSync();
        List<ColorCategory> colorCategoryList =
                EasyExcel.read(Constant.COLOR_CATEGORY_EXCEL_NAME).sheet(0).doReadSync();
        List<OnShelfExportVO> templateList = EasyExcel.read(Constant.ON_SHELF_TEMPLATE_EXCEL_NAME).sheet(0).doReadSync();

        OnShelfExportVO template = templateList.get(0);

        String exportName = String.format(Constant.ON_SHELF_EXCEL_NAME, DateUtils.dateTimeNow());
        EasyExcel.write(exportName)
                .sheet("上架内容")
                .doWrite(() -> buildExportList(template, attributeList, colorCategoryList));
    }

    private List<OnShelfExportVO> buildExportList(OnShelfExportVO template, List<Attribute> attributeList,
                                                  List<ColorCategory> colorCategoryList) {
        List<OnShelfExportVO> result = Lists.newArrayList();
        attributeList.forEach(attribute -> {
            // TODO: 2023/9/19 title规则待定
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
}
