package com.cc.ecassist.controller;

import com.cc.ecassist.service.OnShelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("exportExcel")
    public String exportExcel() {
        onShelfService.exportExcel();
        return "success";
    }
}
