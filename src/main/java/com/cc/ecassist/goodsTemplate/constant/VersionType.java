package com.cc.ecassist.goodsTemplate.constant;

/**
 * 生产模式
 *
 * @author congcong.jiang
 * @date 2023-10-10
 */
public enum VersionType {

    /**
     * 单型号模式 多型号模式
     */
    DEFAULT(1, "使用型号版本"),
    MANUAL(2, "自填尺码"),
    ;

    VersionType(Integer index, String name) {
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
