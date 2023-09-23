package com.cc.ecassist.controller;

import com.cc.ecassist.service.GoodsTemplateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public String genGoodsTemplateFiles() {
        try {
            goodsTemplateService.genGoodsTemplateFiles();
            return "success";
        } catch (Exception e) {
            log.error("生成失败", e);
            return "fail: " + e.getMessage();
        }
    }
}
