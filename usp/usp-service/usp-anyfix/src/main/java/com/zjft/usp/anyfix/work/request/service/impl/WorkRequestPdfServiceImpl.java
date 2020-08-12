package com.zjft.usp.anyfix.work.request.service.impl;

import com.zjft.usp.anyfix.work.request.composite.WorkRequestCompoService;
import com.zjft.usp.anyfix.work.request.dto.*;
import com.zjft.usp.anyfix.work.request.service.WorkRequestPdfService;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 工单pdf服务
 * @author cxd
 * @since 2020-05-8
 */
@Service
public class WorkRequestPdfServiceImpl implements WorkRequestPdfService {
    @Autowired
    private WorkRequestCompoService workRequestCompoService;

    /**
     * 方法实现说明getWorkRequestPdfDto
     * @date 2020/5/8
     * @param workId
     * @return com.zjft.usp.anyfix.work.request.dto.WorkRequestPdfDto
     */
    @Override
    public WorkRequestPdfDto getWorkRequestPdfDto(Long workId,UserInfo userInfo, ReqParam reqParam) {
        WorkDto workDto=  workRequestCompoService.findWorkDetail(workId, userInfo, reqParam);
        WorkRequestPdfDto workRequestPdfDto = new WorkRequestPdfDto();
        BeanUtils.copyProperties(workDto, workRequestPdfDto);
        return workRequestPdfDto;
    }
}
