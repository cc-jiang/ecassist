package com.cc.ecassist.goodsTemplate.constant;

/**
 * 生产模式
 *
 * @author congcong.jiang
 * @date 2023-10-10
 */
public enum GenType {

    /**
     * 单型号模式
     * 多型号模式 标题、货号一致
     */
    SINGLE(1, "单型号模式"),
    MULTI(2, "多型号模式"),
    ;

    GenType(Integer index, String name) {
        this.index = index;
        this.name = name;
    }

    private Integer index;
    private String name;

    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
