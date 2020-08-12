package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.model.WareDepotUser;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 库房人员表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareDepotUserService extends IService<WareDepotUser> {

    /**
     * 物料库房人员信息列表
     * @datetime 2019/11/19 10:35
     * @version 
     * @author dcyu 
     * @param wareDepotId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepotUser>
     */
    List<WareDepotUser> listWareDepotUser(Long wareDepotId);

    /**
     * 物料库房人员信息列表（已映射中文名）
     * @datetime 2019/11/26 16:37
     * @version
     * @author dcyu
     * @param wareDepotId
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepotUser>
     */
    List<WareDepotUser> listWareDepotUser(Long wareDepotId, Long corpId);
    
    /**
     * 新增物料库房人员信息
     * @datetime 2019/11/19 10:35
     * @version 
     * @author dcyu 
     * @param wareDepotUserList
     * @return void
     */
    void insertWareDepotUser(List<WareDepotUser> wareDepotUserList);

    /**
     * 新增物料库房人员信息
     * @datetime 2019/11/26 16:06
     * @version
     * @author dcyu
     * @param depotId
     * @param users
     * @param type
     * @return void
     */
    void insertWareDepotUser(Long depotId, Long[] users, int type);
    
    /**
     * 删除物料库房人员信息
     * @datetime 2019/11/19 10:35
     * @version 
     * @author dcyu 
     * @param wareDepotId
     * @return void
     */
    void deleteWareDepotUser(Long wareDepotId);
}
