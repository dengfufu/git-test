package com.zjft.usp.device.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.dto.DeviceClassCompoDto;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.dto.DeviceSmallClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceSmallClassFilter;
import com.zjft.usp.device.baseinfo.mapper.DeviceSmallClassMapper;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.service.DeviceLargeClassService;
import com.zjft.usp.device.baseinfo.service.DeviceModelService;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 * 设备小类表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceSmallClassServiceImpl extends ServiceImpl<DeviceSmallClassMapper, DeviceSmallClass> implements DeviceSmallClassService {

    @Autowired
    private DeviceLargeClassService deviceLargeClassService;

    @Resource
    private DeviceSmallClassMapper deviceSmallClassMapper;

    @Resource
    private DeviceModelService deviceModelService;

    @Override
    public List<DeviceSmallClassDto> listDeviceSmallClass(DeviceSmallClassDto smallClassDto) {
        QueryWrapper<DeviceSmallClass> queryWrapper = new QueryWrapper<>();
        if (smallClassDto != null) {
            if (StrUtil.isNotBlank(smallClassDto.getName())) {
                queryWrapper.like("name", smallClassDto.getName());
            }
            if (StrUtil.isNotBlank(smallClassDto.getEnabled())) {
                queryWrapper.eq("enabled", smallClassDto.getEnabled().toUpperCase());
            }
            if (LongUtil.isNotZero(smallClassDto.getCorp())) {
                queryWrapper.eq("corp", smallClassDto.getCorp());
            }
            if (LongUtil.isNotZero(smallClassDto.getLargeClassId())) {
                queryWrapper.eq("large_class_id", smallClassDto.getLargeClassId());
            }
        }
        List<DeviceSmallClass> smallClassList = this.list(queryWrapper);
        List<DeviceSmallClassDto> smallClassDtoList = new ArrayList<>();
        if (smallClassList != null && smallClassList.size() > 0) {
            Map<Long, String> largeClassMap = this.deviceLargeClassService.mapClassIdAndNameByCorp(smallClassDto.getCorp());
            /** 映射数据Map */
            for (DeviceSmallClass smallClass : smallClassList) {
                /** Mapper转换 */
                DeviceSmallClassDto tmpDto = new DeviceSmallClassDto();
                BeanUtils.copyProperties(smallClass, tmpDto);
                tmpDto.setLargeClassName(largeClassMap.get(tmpDto.getLargeClassId()));
                smallClassDtoList.add(tmpDto);
            }
        }
        return smallClassDtoList;
    }

    /**
     * 获得设备小类详情
     *
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2020/1/16 16:43
     **/
    @Override
    public DeviceSmallClassDto findDtoById(Long smallClassId) {
        DeviceSmallClassDto deviceSmallClassDto = new DeviceSmallClassDto();
        DeviceSmallClass deviceSmallClass = this.getById(smallClassId);
        if (deviceSmallClass != null) {
            BeanUtils.copyProperties(deviceSmallClass, deviceSmallClassDto);
            DeviceLargeClass deviceLargeClass = deviceLargeClassService.getById(deviceSmallClass.getLargeClassId());
            if (deviceLargeClass != null) {
                deviceSmallClassDto.setLargeClassName(deviceLargeClass.getName());
            }
        }
        return deviceSmallClassDto;
    }

    /**
     * 分页查询设备类型
     *
     * @param page
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2020/6/10 15:05
     **/
    @Override
    public List<DeviceSmallClassDto> queryDeviceSmallClass(Page page, DeviceSmallClassFilter deviceSmallClassFilter) {
        return this.baseMapper.queryDeviceSmallClass(page, deviceSmallClassFilter);
    }

    @Override
    public List<DeviceLargeClassDto> listDeviceClass(Long corpId) {
        List<DeviceLargeClassDto> largeClassDtoList = new ArrayList<>();
        List<DeviceSmallClassDto> list = deviceSmallClassMapper.listDeviceClass(corpId);
        Map<DeviceLargeClassDto, List<DeviceSmallClassDto>> map = new LinkedHashMap<>();
        for (DeviceSmallClassDto deviceClassDto : list) {
            DeviceLargeClassDto largeDto = new DeviceLargeClassDto();
            largeDto.setId(deviceClassDto.getLargeClassId());
            largeDto.setName(deviceClassDto.getLargeClassName());
            List<DeviceSmallClassDto> smallClassList = new ArrayList<>();
            if (map.containsKey(largeDto)) {
                smallClassList = map.get(largeDto);
            }
            smallClassList.add(deviceClassDto);
            map.put(largeDto, smallClassList);
        }
        for (DeviceLargeClassDto deviceLargeClassDto : map.keySet()) {
            deviceLargeClassDto.setDeviceSmallClassDtoList(map.get(deviceLargeClassDto));
            largeClassDtoList.add(deviceLargeClassDto);
        }
        return largeClassDtoList;
    }

    @Override
    public Map<Long, String> mapIdAndNameByCorp(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if (corpId == null || corpId == 0L) {
            return map;
        }
        List<DeviceSmallClass> list = this.list(new QueryWrapper<DeviceSmallClass>().eq("corp", corpId));
        if (list != null && list.size() > 0) {
            for (DeviceSmallClass deviceSmallClass : list) {
                map.put(deviceSmallClass.getId(), deviceSmallClass.getName());
            }
        }
        return map;
    }

    /**
     * 根据客户编号列表获取设备小类的编号和名称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/17 12:13 下午
     **/
    @Override
    public Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<DeviceSmallClass> list = this.list(new QueryWrapper<DeviceSmallClass>().in("corp", corpIdList));
        if (list != null && list.size() > 0) {
            for (DeviceSmallClass deviceSmallClass : list) {
                map.put(deviceSmallClass.getId(), deviceSmallClass.getName());
            }
        }
        return map;
    }

    @Override
    public Map<Long, DeviceClassCompoDto> mapDeviceClassCompoByCorpIds(List<Long> corpIdList) {
        Map<Long, DeviceClassCompoDto> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }

        DeviceSmallClassFilter deviceSmallClassFilter = new DeviceSmallClassFilter();
        deviceSmallClassFilter.setCorpList(corpIdList);

        List<DeviceClassCompoDto> list = this.deviceSmallClassMapper.listDeviceClassCompoBy(deviceSmallClassFilter);
        if (list != null && list.size() > 0) {
            for (DeviceClassCompoDto deviceClassCompoDto : list) {
                map.put(deviceClassCompoDto.getSmallClassId(), deviceClassCompoDto);
            }
        }
        return map;
    }

    @Override
    public Map<Long, DeviceSmallClass> mapSmallClassByCorpId(Long corpId) {
        Map<Long, DeviceSmallClass> map = new HashMap<>();
        if (LongUtil.isZero(corpId)) {
            return map;
        }
        List<DeviceSmallClass> list = this.list(new QueryWrapper<DeviceSmallClass>().eq("corp", corpId));
        if (list != null && list.size() > 0) {
            for (DeviceSmallClass deviceSmallClass : list) {
                map.put(deviceSmallClass.getId(), deviceSmallClass);
            }
        }
        return map;
    }

    /**
     * 获得最大顺序号
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/11/15 16:47
     **/
    @Override
    public Integer findMaxSortNo(Long corpId) {
        return this.baseMapper.findMaxSortNo(corpId);
    }

    /**
     * 模糊查询设备小类
     *
     * @param deviceSmallClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:20
     **/
    @Override
    public List<DeviceSmallClass> matchDeviceSmallClass(DeviceSmallClassFilter deviceSmallClassFilter) {
        return this.baseMapper.matchDeviceSmallClass(deviceSmallClassFilter);
    }

    @Override
    public Long save(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo, ReqParam reqParam) {
        StringBuilder builder = new StringBuilder(16);
        if (StringUtils.isEmpty(deviceSmallClassDto.getName())) {
            builder.append("设备小类名称不能为空！");
        }
        if (LongUtil.isZero(deviceSmallClassDto.getLargeClassId())) {
            builder.append("设备大类编号不能为空！");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        DeviceSmallClass deviceSmallClass = new DeviceSmallClass();
        BeanUtils.copyProperties(deviceSmallClassDto, deviceSmallClass);
        QueryWrapper<DeviceSmallClass> queryWrapper = new QueryWrapper<>();
        // 为空时设置当前公司
        if (LongUtil.isZero(deviceSmallClassDto.getCorp())) {
            deviceSmallClassDto.setCorp(reqParam.getCorpId());
        }
        queryWrapper.eq("large_class_id", deviceSmallClass.getLargeClassId());
        queryWrapper.eq("name", deviceSmallClass.getName());
        queryWrapper.eq("corp", deviceSmallClass.getCorp());
        List<DeviceSmallClass> deviceBrandList = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(deviceBrandList)) {
            throw new AppException("该设备小类名称已经存在！");
        }
        deviceSmallClass.setId(KeyUtil.getId());
        deviceSmallClass.setOperator(userInfo.getUserId());
        deviceSmallClass.setOperateTime(DateUtil.date());
        this.save(deviceSmallClass);
        return deviceSmallClass.getId();
    }

    @Override
    public void update(DeviceSmallClassDto deviceSmallClassDto, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder(16);
        if (StringUtils.isEmpty(deviceSmallClassDto.getName())) {
            builder.append("设备小类名称不能为空！");
        }
        if (LongUtil.isZero(deviceSmallClassDto.getLargeClassId())) {
            builder.append("设备大类编号不能为空！");
        }
        if (LongUtil.isZero(deviceSmallClassDto.getCorp())) {
            builder.append("企业编号不能为空！");
        }
        if (LongUtil.isZero(deviceSmallClassDto.getId())) {
            builder.append("设备小类编号不能为空！");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        DeviceSmallClass deviceSmallClass = new DeviceSmallClass();
        BeanUtils.copyProperties(deviceSmallClassDto, deviceSmallClass);
        QueryWrapper<DeviceSmallClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", deviceSmallClass.getName());
        queryWrapper.eq("corp", deviceSmallClass.getCorp());
        queryWrapper.eq("large_class_id", deviceSmallClass.getLargeClassId());
        queryWrapper.ne("id", deviceSmallClass.getId());
        List<DeviceSmallClass> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备小类名称已经存在！");
        }
        deviceSmallClass.setOperator(userInfo.getUserId());
        deviceSmallClass.setOperateTime(DateUtil.date());
        this.updateById(deviceSmallClass);
    }

    @Override
    public void delete(Long id) {
        QueryWrapper<DeviceModel> deviceModelQueryWrapper = new QueryWrapper<>();
        deviceModelQueryWrapper.eq("small_class_id", id);
        int count = deviceModelService.count(deviceModelQueryWrapper);
        if (count > 0) {
            throw new AppException("该设备小类有其它设备型号，无法删除");
        }
        this.removeById(id);
    }


    /**
     * 获得设备分类小类id和大类名称-小类名称map
     *
     * @param customCorp
     * @return
     * @author ljzhu
     */
    @Override
    public Map<Long, String> getDeviceClassMap(Long customCorp) {
        Map<Long, String> map = new HashMap<>();
        List<DeviceLargeClassDto> list = this.listDeviceClass(customCorp);

        if (list != null && list.size() > 0) {
            for (DeviceLargeClassDto largeClassDto : list) {
                List<DeviceSmallClassDto> slist = largeClassDto.getDeviceSmallClassDtoList();

                if (slist != null && slist.size() > 0) {
                    for (DeviceSmallClassDto smallClassDto : slist) {
                        map.put(smallClassDto.getId(), largeClassDto.getName() + "-" + smallClassDto.getName());
                    }
                }
            }

        }
        return map;
    }

    /**
     * 根据设备大类编号查询设备小类编号list
     *
     * @param largeClassId
     * @return
     */
    @Override
    public List<Long> listSmallClassIdByLargeClassId(Long largeClassId) {
        if (LongUtil.isZero(largeClassId)) {
            return null;
        }
        List<DeviceSmallClass> smallClassList = this.list(new QueryWrapper<DeviceSmallClass>().eq("large_class_id", largeClassId));
        List<Long> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(smallClassList)) {
            smallClassList.forEach(deviceSmallClass -> list.add(deviceSmallClass.getId()));
        }
        return list;
    }

}
