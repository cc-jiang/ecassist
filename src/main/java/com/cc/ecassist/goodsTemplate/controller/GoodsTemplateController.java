package com.cc.ecassist.goodsTemplate.controller;

import com.cc.ecassist.common.domain.AjaxResult;
import com.cc.ecassist.goodsTemplate.domain.GenGoodsTemplateVO;
import com.cc.ecassist.goodsTemplate.service.GoodsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Slf4j
@Controller
@RequestMapping("goodsTemplate")
public class GoodsTemplateController {

    @Autowired
    private GoodsTemplateService goodsTemplateService;

    @PostMapping("genGoodsTemplateFiles")
    @ResponseBody
    public AjaxResult genGoodsTemplateFiles(@RequestBody GenGoodsTemplateVO genGoodsTemplateVO) {
        return AjaxResult.success(goodsTemplateService.genGoodsTemplateFiles(genGoodsTemplateVO));
    }

    @GetMapping("getTemplateList")
    @ResponseBody
    public AjaxResult getTemplateList(String excelName) {
        return AjaxResult.success(goodsTemplateService.getTemplateList(excelName));
    }

    @GetMapping("getModelList")
    @ResponseBody
    public AjaxResult getModelList() {
        return AjaxResult.success(goodsTemplateService.getModelList());
    }

    @GetMapping("getKeywordList")
    @ResponseBody
    public AjaxResult getKeywordList() {
        return AjaxResult.success(goodsTemplateService.getKeywordList());
    }

    @GetMapping("getModelData")
    @ResponseBody
    public AjaxResult getModelData() {
        return AjaxResult.success(goodsTemplateService.getModelData());
    }

    @GetMapping("getKeywordData")
    @ResponseBody
    public AjaxResult getKeywordData() {
        return AjaxResult.success(goodsTemplateService.getKeywordData());
    }
}
