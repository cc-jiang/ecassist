package com.cc.ecassist.goodsTemplate.controller;

import com.cc.ecassist.common.domain.AjaxResult;
import com.cc.ecassist.goodsTemplate.constant.PathConstant;
import com.cc.ecassist.goodsTemplate.domain.GenGoodsTemplateVO;
import com.cc.ecassist.goodsTemplate.service.GoodsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("genGoodsTemplateFiles")
    public AjaxResult genGoodsTemplateFiles(@RequestBody GenGoodsTemplateVO genGoodsTemplateVO) {
        return AjaxResult.success(goodsTemplateService.genGoodsTemplateFiles(genGoodsTemplateVO));
    }

    @PostMapping("updatePath")
    public AjaxResult updatePath(@RequestBody GenGoodsTemplateVO genGoodsTemplateVO) {
        goodsTemplateService.updatePath(genGoodsTemplateVO);
        return AjaxResult.success();
    }

    @GetMapping("getDefaultPath")
    public AjaxResult getDefaultPath() {
        GenGoodsTemplateVO result = new GenGoodsTemplateVO();
        result.setPath(PathConstant.PATH.replace("/", "\\"));
        result.setGenPath(PathConstant.GEN_PATH.replace("/", "\\"));
        result.setMainImagePath(PathConstant.MAIN_IMAGE_PATH.replace("/", "\\"));
        return AjaxResult.success(result);
    }

    @GetMapping("getTemplateList")
    public AjaxResult getTemplateList(String excelName) {
        return AjaxResult.success(goodsTemplateService.getTemplateList(excelName));
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
