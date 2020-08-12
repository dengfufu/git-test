package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.filter.RemoteWayFilter;
import com.zjft.usp.anyfix.baseinfo.model.RemoteWay;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;

import java.util.List;

/**
 * <p>
 * 远程处理方式表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface RemoteWayService extends IService<RemoteWay> {

    List<RemoteWay> selectByServiceCrop(Long serviceCorp);

    /**
     * 分页查询远程处理方式
     *
     * @param remoteWayFilter
     * @return
     * @author zgpi
     * @date 2019/11/16 15:10
     **/
    ListWrapper<RemoteWay> query(RemoteWayFilter remoteWayFilter);


    /**
     * 保存远程处理方式
     * @param remoteWay
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(RemoteWay remoteWay, UserInfo userInfo, ReqParam reqParam);

    /**
     * 修改远程处理方式
     * @param remoteWay
     * @param userInfo
     * @return
     */
    void update(RemoteWay remoteWay, UserInfo userInfo);
}
