package com.cc.ecassist.controller;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.read.listener.ReadListener;
import com.cc.ecassist.constant.Constant;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.service.OnShelfService;
import com.cc.ecassist.utils.DateUtils;
import org.apache.commons.compress.utils.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Controller
@RequestMapping("onShelf")
public class OnShelfController {

    @Autowired
    private OnShelfService onShelfService;

    @PostMapping("exportExcel")
    public void exportExcel() {

        String templateName = Constant.PATH + String.format(Constant.ON_SHELF_EXCEL_NAME, "模板");
        List<OnShelfExportVO> template = Lists.newArrayList();
        EasyExcel.read(templateName, OnShelfExportVO.class, new ReadListener() {
            @Override
            public void invoke(Object data, AnalysisContext context) {
                template.add((OnShelfExportVO) data);
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                String exportName = Constant.PATH + String.format(Constant.ON_SHELF_EXCEL_NAME, DateUtils.dateTimeNow());
                EasyExcel.write(exportName)
                        .sheet("上架内容")
                        .doWrite(() -> onShelfService.getList(template.get(0)));
            }
        }).sheet().doRead();

    }
}
