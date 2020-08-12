package com.zjft.usp.anyfix.work.request.service;

import com.zjft.usp.anyfix.work.request.dto.WorkRequestExcelDto;
import com.zjft.usp.anyfix.work.request.dto.WorkRequestExportDto;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;
import java.util.Map;

/**
 * 工单excel服务
 * @author ljzhu
 * @since 2020-02-25
 */
public interface WorkRequestExcelService {

    List<Long> checkImportData(List<WorkRequestExcelDto> workRequestExcelDtoList,UserInfo userInfo, ReqParam reqParam);

    Map<Integer,String []> getMapDropDown(long demanderCorp);

    List<WorkRequestExcelDto> getTemplateWorkRequestExcelDtoList(Long demanderCorp, String demanderCorpName);

    /**
     * 获取导出工单数据
     * @param workFilter
     * @param userInfo
     * @param reqParam
     * @return
     */
    List<WorkRequestExportDto> getWorkRequestExportDtoList( WorkFilter workFilter,
                                                            UserInfo userInfo,
                                                            ReqParam reqParam);

    /**
     * 批量导入发送消息
     * @param workIdList
     * @param autoHandel
     * @author xpwu
     */
    public void sendImportWorkMessage(List<Long> workIdList, String autoHandel);
}
