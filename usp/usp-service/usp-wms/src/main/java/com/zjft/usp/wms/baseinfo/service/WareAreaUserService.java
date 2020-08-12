package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.model.WareAreaUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库房人员表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
public interface WareAreaUserService extends IService<WareAreaUser> {

    /**
     * 根据区域ID找到用户列表
     * @datetime 2019/11/26 16:29
     * @version
     * @author dcyu
     * @param wareAreaId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareAreaUser>
     */
    List<WareAreaUser> listWareAreaUser(Long wareAreaId);

    /**
     * 根据区域ID找到用户列表（已映射中文名）
     * @datetime 2019/11/18 16:20
     * @version
     * @author dcyu
     * @param wareAreaId
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareAreaUser>
     */
    List<WareAreaUser> listWareAreaUser(Long wareAreaId, Long corpId);

    /**
     * 新增区域用户信息
     * @datetime 2019/11/18 16:31
     * @version
     * @author dcyu
     * @param wareAreaUsers
     * @return void
     */
    void insertWareAreaUser(List<WareAreaUser> wareAreaUsers);

    /**
     * 新增区域用户信息
     * @datetime 2019/11/25 14:30
     * @version
     * @author dcyu
     * @param areaId
     * @param wareAreaUsers
     * @param type
     * @return void
     */
    void insertWareAreaUser(Long areaId, Long[] wareAreaUsers, int type);

    /**
     * 根据区域ID删除用户信息
     * @datetime 2019/11/18 16:29
     * @version
     * @author dcyu
     * @param wareAreaId
     * @return void
     */
    void deleteWareAreaUserBy(Long wareAreaId);
}
