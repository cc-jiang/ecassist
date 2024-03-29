package com.cc.ecassist.goodsTemplate.controller;

import com.cc.ecassist.goodsTemplate.service.OnShelfService;
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
@RestController
@RequestMapping("onShelf")
public class OnShelfController {

    @Autowired
    private OnShelfService onShelfService;

    @GetMapping("exportExcel")
    public String exportExcel() {
        return onShelfService.exportExcel();
    }
}
