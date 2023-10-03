package com.cc.ecassist.goodsTemplate.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 模板引擎controller
 *
 * @author congcong.jiang
 * @date 2023-09-27
 */
@Controller
public class ThymeleafController {

    @RequestMapping
    public String index() {
        return "index";
    }
}
