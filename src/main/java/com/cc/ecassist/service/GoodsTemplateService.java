package com.cc.ecassist.service;

import com.alibaba.fastjson2.JSONObject;
import com.cc.ecassist.domain.KeywordVO;
import com.cc.ecassist.domain.ModelVO;
import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.domain.ProductVO;

import java.util.List;

/**
 * 商品模板
 *
 * @author congcong.jiang
 * @date 2023-09-23
 */
public interface GoodsTemplateService {

    String genGoodsTemplateFiles(List<ProductVO> productList);

    List<ModelVO> getModelList();

    List<KeywordVO> getKeywordList();

    JSONObject getModelData();

    JSONObject getKeywordData();

    List<OnShelfExportVO> getTemplateList(String excelName);
}
