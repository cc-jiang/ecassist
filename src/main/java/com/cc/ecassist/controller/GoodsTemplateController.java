package com.cc.ecassist.controller;

import com.cc.ecassist.common.domain.AjaxResult;
import com.cc.ecassist.domain.ProductVO;
import com.cc.ecassist.service.GoodsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

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

    @RequestMapping("index")
    public String index(Model model) {
        model.addAttribute("modelData", goodsTemplateService.getModelData());
        model.addAttribute("modelList", goodsTemplateService.getModelList());
        model.addAttribute("keywordData", goodsTemplateService.getKeywordData());
        return "index";
    }

    @GetMapping("genGoodsTemplateFiles")
    @ResponseBody
    public AjaxResult genGoodsTemplateFiles(List<ProductVO> productList) {
        goodsTemplateService.genGoodsTemplateFiles(productList);
        return AjaxResult.success();
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
