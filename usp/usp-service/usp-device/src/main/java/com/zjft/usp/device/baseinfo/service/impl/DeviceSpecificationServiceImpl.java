package com.zjft.usp.device.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.baseinfo.dto.DeviceSpecificationDto;
import com.zjft.usp.device.baseinfo.enums.EnabledEnum;
import com.zjft.usp.device.baseinfo.filter.DeviceSpecificationFilter;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.model.DeviceSpecification;
import com.zjft.usp.device.baseinfo.mapper.DeviceSpecificationMapper;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import com.zjft.usp.device.baseinfo.service.DeviceSpecificationService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 设备规格表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-01-20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceSpecificationServiceImpl extends ServiceImpl<DeviceSpecificationMapper, DeviceSpecification> implements DeviceSpecificationService {

    @Autowired
    private DeviceSmallClassService deviceSmallClassService;

    /**
     * 分页查询设备规格列表
     *
     * @param deviceSpecificationFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 14:43
     **/
    @Override
    public ListWrapper<DeviceSpecification> query(DeviceSpecificationFilter deviceSpecificationFilter) {
        ListWrapper<DeviceSpecification> listWrapper = new ListWrapper<>();
        if(LongUtil.isZero(deviceSpecificationFilter.getSmallClassId())){
            return listWrapper;
        }
        QueryWrapper<DeviceSpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("small_class_id", deviceSpecificationFilter.getSmallClassId());
        if(StrUtil.isNotBlank(deviceSpecificationFilter.getEnabled())) {
            queryWrapper.eq("enabled", deviceSpecificationFilter.getEnabled().toUpperCase());
        }
        if(StrUtil.isNotBlank(deviceSpecificationFilter.getMatchFilter())) {
            queryWrapper.like("name", deviceSpecificationFilter.getMatchFilter().trim());
        }
        queryWrapper.orderByAsc("name");
        Page page = new Page(deviceSpecificationFilter.getPageNum(), deviceSpecificationFilter.getPageSize());
        IPage<DeviceSpecification> deviceSpecificationIPage = this.page(page, queryWrapper);
        listWrapper.setList(deviceSpecificationIPage.getRecords());
        listWrapper.setTotal(deviceSpecificationIPage.getTotal());
        return listWrapper;
    }

    /**
     * 查询设备规格列表
     *
     * @param deviceSpecificationFilter
     * @return
     * @author zgpi
     * @date 2020/1/20 14:43
     **/
    @Override
    public List<DeviceSpecificationDto> list(DeviceSpecificationFilter deviceSpecificationFilter) {
        if(LongUtil.isZero(deviceSpecificationFilter.getCorp()) &&
                LongUtil.isZero(deviceSpecificationFilter.getSmallClassId())){
            return null;
        }
        QueryWrapper<DeviceSpecification> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(deviceSpecificationFilter.getCorp())) {
            queryWrapper.inSql("small_class_id", "select id from device_small_class where corp=" + deviceSpecificationFilter.getCorp());
        }
        if (LongUtil.isNotZero(deviceSpecificationFilter.getSmallClassId())) {
            queryWrapper.eq("small_class_id", deviceSpecificationFilter.getSmallClassId());
        }
        if(StrUtil.isNotBlank(deviceSpecificationFilter.getEnabled())) {
            queryWrapper.eq("enabled", deviceSpecificationFilter.getEnabled().toUpperCase());
        }
        queryWrapper.orderByAsc("small_class_id,name");
        List<DeviceSpecification> list = this.list(queryWrapper);
        List<DeviceSpecificationDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(list)) {
            DeviceSmallClass deviceSmallClass =  this.deviceSmallClassService.getById(list.get(0).getSmallClassId());
            Map<Long, String> smallClassMap = this.deviceSmallClassService.mapIdAndNameByCorp(deviceSmallClass.getCorp());
            list.forEach(deviceSpecification -> {
                DeviceSpecificationDto dto = new DeviceSpecificationDto();
                BeanUtils.copyProperties(deviceSpecification, dto);
                dto.setSmallClassName(smallClassMap.get(dto.getSmallClassId()));
                dtoList.add(dto);
            });
        }
        return dtoList;
    }

    /**
     * 设备小类编号与规格列表映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public Map<Long, List<DeviceSpecification>> mapSmallClassIdAndSpecificationList(Long corpId) {
        QueryWrapper<DeviceSpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp", corpId);
        List<DeviceSpecification> deviceSpecificationList = this.list(queryWrapper);
        Map<Long, List<DeviceSpecification>> map = new HashMap<>();
        List<DeviceSpecification> list = new ArrayList<>();
        for (DeviceSpecification deviceSpecification : deviceSpecificationList) {
            Long smallClassId = deviceSpecification.getSmallClassId();
            if (map.containsKey(smallClassId)) {
                list = map.get(smallClassId);
            }
            list.add(deviceSpecification);
            map.put(smallClassId, list);
        }
        return map;
    }

    /**
     * 删除设备规格
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/20 10:56
     **/
    @Override
    public void delDeviceSpecification(Long smallClassId) {
        UpdateWrapper<DeviceSpecification> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("small_class_id", smallClassId);
        this.remove(updateWrapper);
    }

    /**
     * 根据企业id获取设备规格map
     * @param corpId
     * @return
     */
    @Override
    public Map<Long, String> mapSpecificationByCorp(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if(corpId == null || corpId == 0L){
            return map;
        }
        List<DeviceSpecification> list  = this.list(new QueryWrapper<DeviceSpecification>().eq("corp", corpId));
        if(list != null && list.size() > 0){
            for (DeviceSpecification deviceSpecification: list){
                map.put(deviceSpecification.getId(), deviceSpecification.getName());
            }
        }
        return map;
    }

    /**
     * 根据设备小类编号获取编号与名称映射
     *
     * @param smallClassId
     * @return
     */
    @Override
    public Map<Long, String> mapBySmallClassId(Long smallClassId) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(smallClassId)) {
            return map;
        }
        QueryWrapper<DeviceSpecification> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("small_class_id", smallClassId);
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode());
        List<DeviceSpecification> list = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(deviceSpecification -> {
                map.put(deviceSpecification.getId(), deviceSpecification.getName());
            });
        }
        return map;
    }

    /**
     * 根据规格编号list获取 规格编号->[小类名称]规格名称的映射
     *
     * @param idList
     * @return
     */
    @Override
    public Map<Long, String> mapIdAndSmallClassSpecName(List<Long> idList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(idList)) {
            return map;
        }
        DeviceSpecificationFilter filter = new DeviceSpecificationFilter();
        List<DeviceSpecification> list = this.baseMapper.selectBatchIds(idList);
        if (CollectionUtil.isNotEmpty(list)) {
            DeviceSmallClass deviceSmallClass =  this.deviceSmallClassService.getById(list.get(0).getSmallClassId());
            Map<Long, String> smallClassMap = this.deviceSmallClassService.mapIdAndNameByCorp(deviceSmallClass.getCorp());
            list.forEach(deviceSpecification -> {
                String smallClassName = smallClassMap.get(deviceSpecification.getSmallClassId());
                String smallClassSpecName = "";
                if (StrUtil.isNotEmpty(smallClassName)) {
                    smallClassSpecName += "[" + smallClassName + "]";
                }
                smallClassSpecName += deviceSpecification.getName();
                map.put(deviceSpecification.getId(), smallClassSpecName);
            });
        }
        return map;
    }

    /**
     * 设备规格ID与名称映射
     * @date 2020/3/15
     * @param IdList
     * @return java.util.Map<java.lang.Long,java.lang.String>
     */
    @Override
    public Map<Long, String> mapIdAndName(List<Long> IdList) {
        Map<Long, String> mapIdAndName = new HashMap<>();
        if (IdList != null && IdList.size() > 0) {
            List<DeviceSpecification> userRealList = this.list(new QueryWrapper<DeviceSpecification>().in("id",IdList));
            if(userRealList != null){
                for(DeviceSpecification deviceSpecification : userRealList){
                    mapIdAndName.put(deviceSpecification.getId(),deviceSpecification.getName());
                }
            }
        }
        return mapIdAndName;
    }
}
