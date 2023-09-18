package com.cc.ecassist.service.impl;

import com.cc.ecassist.domain.OnShelfExportVO;
import com.cc.ecassist.service.OnShelfService;
import com.google.common.collect.Lists;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 上架
 *
 * @author congcong.jiang
 * @date 2023-09-18
 */
@Service
public class OnShelfServiceImpl implements OnShelfService {

    @Override
    public List<OnShelfExportVO> getList(OnShelfExportVO template) {
        List<OnShelfExportVO> result = Lists.newArrayList();
        result.add(template);


        return result;
    }
}
