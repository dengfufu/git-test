package com.zjft.usp.uas.corp.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.corp.enums.CorpOperationEnum;
import com.zjft.usp.uas.corp.model.CorpOperation;
import com.zjft.usp.uas.corp.model.CorpRegistry;
import com.zjft.usp.uas.corp.model.CorpUserApp;
import com.zjft.usp.uas.corp.model.CorpVerifyApp;

/**
 * @author canlei
 * 2019-08-04
 */
public interface CorpOperationService extends IService<CorpOperation> {

    /**
     * 注册时添加操作记录
     * @param corpOperationEnum 操作类型
     * @param corpId 公司编号
     * @param reqParam 公共参数
     * @return
     */
    int addCorpOperation(CorpOperationEnum corpOperationEnum, long corpId, ReqParam reqParam, long curUserId, String clientId);

}
