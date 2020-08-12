package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.filter.WarePropertyRightFilter;
import com.zjft.usp.wms.baseinfo.model.WarePropertyRight;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 产权表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WarePropertyRightService extends IService<WarePropertyRight> {

    /***
     * 根据corpId获取产权编号与名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     */
    Map<Long, String> mapIdAndName(Long corpId);

    /**
     * 物料产权信息列表
     * @datetime 2019/11/26 10:09
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WarePropertyRight>
     */
    List<WarePropertyRight> listWarePropertyRight();

    /**
     * 根据条件查找物料产权信息
     * @datetime 2019/11/26 10:09
     * @version
     * @author dcyu
     * @param warePropertyRightFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WarePropertyRight>
     */
    ListWrapper<WarePropertyRight> queryWarePropertyRight(WarePropertyRightFilter warePropertyRightFilter);

    /**
     * 新增物料产权信息
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     * @param propertyRight
     * @return void
     */
    void insertWarePropertyRight(WarePropertyRight propertyRight);

    /**
     * 更新物料产权信息
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     * @param propertyRight
     * @return void
     */
    void updateWarePropertyRight(WarePropertyRight propertyRight);

    /**
     * 删除物料产权信息
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     * @param rightId
     * @return void
     */
    void deleteWarePropertyRight(Long rightId);
}
