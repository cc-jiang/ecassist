package com.cc.ecassist.service;

import com.cc.ecassist.domain.OnShelfExportVO;

import java.util.List;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
public interface OnShelfService {

    /**
     * 获取导出数据
     * @return
     */
    List<OnShelfExportVO> getList(OnShelfExportVO template);
}
