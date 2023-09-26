package com.cc.ecassist.controller;

import com.cc.ecassist.common.domain.AjaxResult;
import com.cc.ecassist.domain.ProductVO;
import com.cc.ecassist.service.GoodsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Slf4j
@RestController
@RequestMapping("goodsTemplate")
public class GoodsTemplateController {

    @Autowired
    private GoodsTemplateService goodsTemplateService;

    @GetMapping("genGoodsTemplateFiles")
    public AjaxResult genGoodsTemplateFiles(List<ProductVO> productList) {
        goodsTemplateService.genGoodsTemplateFiles(productList);
        return AjaxResult.success();
    }

    @GetMapping("getModelList")
    public AjaxResult getModelList() {
        return AjaxResult.success(goodsTemplateService.getModelList());
    }

    @GetMapping("getKeywordList")
    public AjaxResult getKeywordList() {
        return AjaxResult.success(goodsTemplateService.getKeywordList());
    }

    @GetMapping("getModelData")
    public AjaxResult getModelData() {
        return AjaxResult.success(goodsTemplateService.getModelData());
    }

    @GetMapping("getKeywordData")
    public AjaxResult getKeywordData() {
        return AjaxResult.success(goodsTemplateService.getKeywordData());
    }
}
