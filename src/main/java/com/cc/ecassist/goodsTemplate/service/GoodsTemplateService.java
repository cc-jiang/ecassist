package com.cc.ecassist.goodsTemplate.service;

import com.cc.ecassist.goodsTemplate.domain.GenGoodsTemplateVO;
import com.cc.ecassist.goodsTemplate.domain.ModelVO;

import java.util.List;

/**
 * 商品模板
 *
 * @author congcong.jiang
 * @date 2023-09-23
 */
public interface GoodsTemplateService {

    String genGoodsTemplateFiles(GenGoodsTemplateVO genGoodsTemplateVO);

    void updatePath(GenGoodsTemplateVO genGoodsTemplateVO);

    List<ModelVO> getModelList();
}
