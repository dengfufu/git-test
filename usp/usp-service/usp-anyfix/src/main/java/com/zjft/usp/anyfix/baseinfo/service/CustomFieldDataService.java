package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.filter.CustomFieldDataFilter;
import com.zjft.usp.anyfix.baseinfo.model.CustomFieldData;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义字段数据表 服务类
 * </p>
 *
 * @author cxd
 * @since 2020-01-08
 */
public interface CustomFieldDataService extends IService<CustomFieldData> {

    /**
     * 自定义字段数据
     *
     * @param customFieldDataList 自定义字段数据
     */
    void addCustomFieldDataList(List<CustomFieldData> customFieldDataList);

    /**
     * 批量修改自定义字段数据
     *
     * @param formId
     * @param customFieldDataList
     * @return
     * @author zgpi
     * @date 2020/2/26 20:06
     */
    void modCustomFieldDataList(Long formId, List<CustomFieldData> customFieldDataList);

    /**
     * 根据条件查询自定义字段数据
     *
     * @param customFieldDataFilter 自定义字段数据条件
     * @return
     */
    List<CustomFieldData> queryCustomFieldData(CustomFieldDataFilter customFieldDataFilter);

    /**
     * 根据设备档案id删除自定义字段数据
     *
     * @param deviceId 设备档案id
     */
    void deleteCustomFieldData(Long deviceId);
}
