package com.cc.ecassist.goodsTemplate.constant;

/**
 * 生产模式
 *
 * @author congcong.jiang
 * @date 2023-10-10
 */
public enum GenType {

    /**
     * 单型号模式 多型号模式
     */
    SINGLE(1, "单型号模式"),
    MULTI(2, "多型号模式"),
    ;

    GenType(Integer value, String label) {
        this.value = value;
        this.label = label;
    }

    private Integer value;
    private String label;

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
