package com.zjft.usp.anyfix.work.request.service;

import com.zjft.usp.anyfix.work.request.dto.WorkRequestPdfDto;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

/**
 * 工单pdf服务
 * @author cxd
 * @since 2020-05-07
 */
public interface WorkRequestPdfService {

    WorkRequestPdfDto getWorkRequestPdfDto(Long workId, UserInfo userInfo, ReqParam reqParam);
}
