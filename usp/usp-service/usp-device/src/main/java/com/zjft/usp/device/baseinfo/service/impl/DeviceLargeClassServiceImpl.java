package com.zjft.usp.device.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.baseinfo.common.service.CorpNameService;
import com.zjft.usp.device.baseinfo.dto.DeviceLargeClassDto;
import com.zjft.usp.device.baseinfo.filter.DeviceLargeClassFilter;
import com.zjft.usp.device.baseinfo.mapper.DeviceLargeClassMapper;
import com.zjft.usp.device.baseinfo.model.DeviceLargeClass;
import com.zjft.usp.device.baseinfo.model.DeviceSmallClass;
import com.zjft.usp.device.baseinfo.service.DeviceLargeClassService;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.baseinfo.service.DeviceSmallClassService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备大类表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceLargeClassServiceImpl extends ServiceImpl<DeviceLargeClassMapper, DeviceLargeClass> implements DeviceLargeClassService {

    @Resource
    private DeviceSmallClassService smallClassService;

    @Resource
    private AnyfixFeignService anyfixFeignService;

    @Resource
    private CorpNameService corpNameService;


    @Override
    public List<DeviceLargeClassDto> listDeviceLargeClass(DeviceLargeClassFilter deviceLargeClassFilter) {
        QueryWrapper<DeviceLargeClass> queryWrapper = new QueryWrapper<>();
        if(deviceLargeClassFilter != null){
            if(StrUtil.isNotBlank(deviceLargeClassFilter.getName())){
                queryWrapper.like("name",deviceLargeClassFilter.getName());
            }
            if(StrUtil.isNotBlank(deviceLargeClassFilter.getEnabled())){
                queryWrapper.eq("enabled", deviceLargeClassFilter.getEnabled().toUpperCase());
            }
            if(LongUtil.isNotZero(deviceLargeClassFilter.getCorp())){
                queryWrapper.eq("corp", deviceLargeClassFilter.getCorp());
            }
        }
        List<DeviceLargeClass> largeClassList = this.list(queryWrapper);
        List<DeviceLargeClassDto> largeClassDtoList = new ArrayList<>();
        if(largeClassList != null && largeClassList.size() > 0){
            /** 映射数据Map */
            for(DeviceLargeClass largeClass : largeClassList){
                /** Mapper转换 */
                DeviceLargeClassDto tmpDto = new DeviceLargeClassDto();
                BeanUtils.copyProperties(largeClass, tmpDto);
                largeClassDtoList.add(tmpDto);
            }
        }
        return largeClassDtoList;
    }

    @Override
    public Map<Long, String> mapClassIdAndNameByCorp(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if(corpId == null || corpId == 0L){
            return map;
        }
        List<DeviceLargeClass> list  = this.list(new QueryWrapper<DeviceLargeClass>().eq("corp", corpId));
        if(list != null && list.size() > 0){
            for (DeviceLargeClass deviceLargeClass: list){
                map.put(deviceLargeClass.getId(), deviceLargeClass.getName());
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapClassIdAndNameByCorpList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if(corpIdList == null || corpIdList.size() <= 0){
            return map;
        }
        List<DeviceLargeClass> list = this.list(new QueryWrapper<DeviceLargeClass>().in("corp", corpIdList));
        if(list != null && list.size() > 0){
            for (DeviceLargeClass largeClass: list){
                map.put(largeClass.getId(), largeClass.getName());
            }
        }
        return map;
    }

    @Override
    public ListWrapper<DeviceLargeClassDto> query(DeviceLargeClassFilter deviceLargeClassFilter) {
        ListWrapper<DeviceLargeClassDto> listWrapper = new ListWrapper<>();
        QueryWrapper<DeviceLargeClass> queryWrapper = new QueryWrapper<>();
        if(LongUtil.isZero(deviceLargeClassFilter.getCorp())) {
            Result<List<Long>> result = this.anyfixFeignService
                        .listDemanderCorpId(deviceLargeClassFilter.getCorpIdForDemander());
            List<Long> demanderCorpList = result == null ? null : result.getData();
            if(CollectionUtil.isNotEmpty(demanderCorpList)) {
                queryWrapper.or().in("corp",demanderCorpList);
                queryWrapper.orderByAsc("corp");
            } else{
                return listWrapper;
            }
        } else {
            queryWrapper.eq("corp",deviceLargeClassFilter.getCorp());
        }
        if(StrUtil.isNotBlank(deviceLargeClassFilter.getName())) {
            queryWrapper.like("name", deviceLargeClassFilter.getName());
        }
        if(StrUtil.isNotBlank(deviceLargeClassFilter.getEnabled())){
            queryWrapper.eq("enabled", deviceLargeClassFilter.getEnabled());
        }
        queryWrapper.orderByAsc("sort_no");
        Page page = new Page(deviceLargeClassFilter.getPageNum(), deviceLargeClassFilter.getPageSize());
        IPage<DeviceLargeClass> deviceLargeClassIpage = this.page(page, queryWrapper);
        List<DeviceLargeClass> deviceLargeClassList = deviceLargeClassIpage.getRecords();
        if(CollectionUtil.isNotEmpty(deviceLargeClassList)){
            List<DeviceLargeClassDto> dtoList = new ArrayList<>();
            // 增加委托商名称显示
            List<Long> corpIdList = deviceLargeClassList.stream().map(e -> e.getCorp()).distinct().collect(Collectors.toList());
            Map<Long, String> corpMap = corpNameService.corpIdNameMap(corpIdList);
            for(DeviceLargeClass deviceLargeClass: deviceLargeClassList){
                DeviceLargeClassDto deviceLargeClassDto = new DeviceLargeClassDto();
                BeanUtils.copyProperties(deviceLargeClass, deviceLargeClassDto);
                    deviceLargeClassDto.setCorpName(corpMap.get(deviceLargeClassDto.getCorp()));
                dtoList.add(deviceLargeClassDto);
            }
            listWrapper.setList(dtoList);
        }
        listWrapper.setTotal(deviceLargeClassIpage.getTotal());
        return listWrapper;
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
     * 模糊查询设备大类
     *
     * @param deviceLargeClassFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:19
     **/
    @Override
    public List<DeviceLargeClass> matchDeviceLargeClass(DeviceLargeClassFilter deviceLargeClassFilter) {
        return this.baseMapper.matchDeviceLargeClass(deviceLargeClassFilter);
    }

    @Override
    public void save(DeviceLargeClass deviceLargeClass, UserInfo userInfo, ReqParam reqParam) {
        if(StringUtils.isEmpty(deviceLargeClass.getName())) {
            throw new AppException("设备大类名称不能为空");
        }
        QueryWrapper<DeviceLargeClass> queryWrapper = new QueryWrapper<>();
        // 为空时设置当前公司
        if(LongUtil.isZero(deviceLargeClass.getCorp())){
            deviceLargeClass.setCorp(reqParam.getCorpId());
        }
        queryWrapper.eq("name",deviceLargeClass.getName());
        queryWrapper.eq("corp",deviceLargeClass.getCorp());
        List<DeviceLargeClass> deviceBrandList = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(deviceBrandList)) {
            throw new AppException("该设备大类名称已经存在");
        }
        deviceLargeClass.setId(KeyUtil.getId());
        deviceLargeClass.setOperator(userInfo.getUserId());
        deviceLargeClass.setOperateTime(DateUtil.date().toTimestamp());
        this.save(deviceLargeClass);
    }

    @Override
    public void update(DeviceLargeClass deviceLargeClass, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if(StringUtils.isEmpty(deviceLargeClass.getName())) {
            builder.append("设备大类名称不能为空");
        }
        if(LongUtil.isZero(deviceLargeClass.getCorp())) {
            builder.append("企业编号不能为空");
        }
        if( deviceLargeClass.getId() == 0){
            builder.append("远程处理方式编号不能为空");
        }
        if(builder.length() > 0 ){
            throw new AppException(builder.toString());
        }
        QueryWrapper<DeviceLargeClass> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name",deviceLargeClass.getName());
        queryWrapper.eq("corp",deviceLargeClass.getCorp());
        queryWrapper.ne("id", deviceLargeClass.getId());
        List<DeviceLargeClass> list = this.baseMapper.selectList(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备大类名称已经存在");
        }
        deviceLargeClass.setOperator(userInfo.getUserId());
        deviceLargeClass.setOperateTime(DateUtil.date().toTimestamp());
        this.updateById(deviceLargeClass);
    }

    @Override
    public void delete(Long id) {
        QueryWrapper<DeviceSmallClass> smallClassQueryWrapper = new QueryWrapper<>();
        smallClassQueryWrapper.eq("large_class_id",id);
        int count = smallClassService.count(smallClassQueryWrapper);
        if(count > 0) {
            throw new AppException("该设备大类有其它设备小类，无法删除");
        }
        this.removeById(id);
    }
}
