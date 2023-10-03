package com.cc.ecassist.goodsTemplate.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 型号
 *
 * @author congcong.jiang
 * @date 2023-09-26
 */
@Setter
@Getter
public class ModelVO implements Serializable {

    private static final long serialVersionUID = 3419599534612584330L;

    /**
     * 版本编号
     */
    private String versionNo;

    /**
     * 版本
     */
    private String version;

    /**
     * 编码
     */
    private String code;
}
