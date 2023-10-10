package com.cc.ecassist.goodsTemplate.service;

import com.alibaba.fastjson2.JSONObject;
import com.cc.ecassist.goodsTemplate.domain.*;

import java.util.List;

/**
 * 商品模板
 *
 * @author congcong.jiang
 * @date 2023-09-23
 */
public interface GoodsTemplateService {

    String genGoodsTemplateFiles(GenGoodsTemplateVO genGoodsTemplateVO);

    void updatePath(String path);

    List<ModelVO> getModelList();

    List<KeywordVO> getKeywordList();

    JSONObject getModelData();

    JSONObject getKeywordData();

    List<OnShelfExportVO> getTemplateList(String excelName);
}
