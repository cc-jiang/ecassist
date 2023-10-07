package com.cc.ecassist.goodsTemplate.domain;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

/**
 * 型号词
 *
 * @author congcong.jiang
 * @date 2023-10-07
 */
@Setter
@Getter
public class ModelWordVO implements Serializable {

    private static final long serialVersionUID = 6978401294514227121L;

    /**
     * 编码
     */
    private String code;

    /**
     * 关键词1
     */
    private String firstKeyword;

    /**
     * 其他关键词 关键词2...关键词n
     */
    private List<String> otherKeyword;
}
