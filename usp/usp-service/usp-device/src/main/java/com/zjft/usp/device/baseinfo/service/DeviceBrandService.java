package com.zjft.usp.device.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.filter.DeviceBrandFilter;
import com.zjft.usp.device.baseinfo.model.DeviceBrand;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备品牌表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
public interface DeviceBrandService extends IService<DeviceBrand> {

    List<DeviceBrandDto> listDeviceBrand(DeviceBrandFilter deviceBrandFilter);

    /**
     * 根据客户编号获取id和名称的映射
     *
     * @param customCorp
     * @return
     * @author zgpi
     * @date 2019/10/16 7:48 下午
     **/
    Map<Long, String> mapIdAndNameByCorpId(Long customCorp);

    /**
     * 根据客户编号列表获取id和名称的映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/16 7:48 下午
     **/
    Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList);

    /**
     * 获得编号与名称映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/16 7:48 下午
     **/
    Map<Long, String> mapIdAndName();

    /**
     * 分页查询品牌
     * @param deviceBrandFilter
     * @return
     */
    ListWrapper<DeviceBrandDto> query(DeviceBrandFilter deviceBrandFilter);

    /**
     * 模糊查询设备品牌
     *
     * @param deviceBrandFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:42
     **/
    List<DeviceBrand> matchDeviceBrand(DeviceBrandFilter deviceBrandFilter);

    /**
     * 设备型号列表
     *
     * @param demanderCorp
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2019/9/25 2:44 下午
     **/
    List<DeviceBrandDto> listBrandModel(Long demanderCorp, Long smallClassId);

    /**
     * 保存设备品牌
     * @param deviceBrand
     * @param userInfo
     * @param reqParam
     * @return
     */
    void save(DeviceBrand deviceBrand , UserInfo userInfo, ReqParam reqParam);

    /**
     * 更新设备品牌
     * @param deviceBrand
     * @param userInfo
     * @return
     */
    void update(DeviceBrand deviceBrand,UserInfo userInfo);

    /**
     * 删除该设备品牌
     * @param id
     */
    void delete(Long id);
}
