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
import com.zjft.usp.device.baseinfo.dto.DeviceBrandDto;
import com.zjft.usp.device.baseinfo.dto.DeviceModelDto;
import com.zjft.usp.device.baseinfo.filter.DeviceBrandFilter;
import com.zjft.usp.device.baseinfo.mapper.DeviceBrandMapper;
import com.zjft.usp.device.baseinfo.mapper.DeviceModelMapper;
import com.zjft.usp.device.baseinfo.model.DeviceBrand;
import com.zjft.usp.device.baseinfo.model.DeviceModel;
import com.zjft.usp.device.baseinfo.service.DeviceBrandService;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备品牌表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-22
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceBrandServiceImpl extends ServiceImpl<DeviceBrandMapper, DeviceBrand> implements DeviceBrandService {

    @Resource
    private DeviceModelMapper deviceModelMapper;
    @Resource
    private FileFeignService fileFeignService;

    @Resource
    private AnyfixFeignService anyfixFeignService;

    @Resource
    private CorpNameService corpNameService;

    @Autowired
    private UasFeignService uasFeignService;

    @Override
    public List<DeviceBrandDto> listDeviceBrand(DeviceBrandFilter deviceBrandFilter) {
        if (deviceBrandFilter != null && LongUtil.isNotZero(deviceBrandFilter.getSmallClassId())) {
            // 根据设备类型
            List<DeviceBrandDto> deviceBrandDtoList = new ArrayList<>();
            List<DeviceBrand> deviceBrandList = this.baseMapper.listDeviceBrand(deviceBrandFilter);
            if (deviceBrandList != null && deviceBrandList.size() > 0) {
                /** 映射数据Map */
                for (DeviceBrand deviceBrand : deviceBrandList) {
                    /** Mapper转换 */
                    DeviceBrandDto tmpDto = new DeviceBrandDto();
                    BeanUtils.copyProperties(deviceBrand, tmpDto);
                    deviceBrandDtoList.add(tmpDto);
                }
            }
            return deviceBrandDtoList;
        }
        QueryWrapper<DeviceBrand> queryWrapper = new QueryWrapper<>();
        if (deviceBrandFilter != null) {
            if (StrUtil.isNotBlank(deviceBrandFilter.getName())) {
                queryWrapper.like("name", deviceBrandFilter.getName());
            }
            if (StrUtil.isNotBlank(deviceBrandFilter.getEnabled())) {
                queryWrapper.eq("enabled", deviceBrandFilter.getEnabled().toUpperCase());
            }
            if (LongUtil.isNotZero(deviceBrandFilter.getCorp())) {
                queryWrapper.eq("corp", deviceBrandFilter.getCorp());
            }
        }
        List<DeviceBrand> deviceBrandList = this.list(queryWrapper);
        List<DeviceBrandDto> deviceBrandDtoList = new ArrayList<>();
        if (deviceBrandList != null && deviceBrandList.size() > 0) {
            /** 映射数据Map */
            for (DeviceBrand deviceBrand : deviceBrandList) {
                /** Mapper转换 */
                DeviceBrandDto tmpDto = new DeviceBrandDto();
                BeanUtils.copyProperties(deviceBrand, tmpDto);
                deviceBrandDtoList.add(tmpDto);
            }
        }
        return deviceBrandDtoList;
    }

    @Override
    public Map<Long, String> mapIdAndNameByCorpId(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if (LongUtil.isZero(corpId)) {
            return map;
        }
        List<DeviceBrand> list = this.list(new QueryWrapper<DeviceBrand>().eq("corp", corpId));
        if (list != null && list.size() > 0) {
            for (DeviceBrand deviceBrand : list) {
                map.put(deviceBrand.getId(), deviceBrand.getName());
            }
        }
        return map;
    }

    /**
     * 根据客户编号列表获取id和名称的映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/17 12:14 下午
     **/
    @Override
    public Map<Long, String> mapIdAndNameByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<DeviceBrand> list = this.list(new QueryWrapper<DeviceBrand>().in("corp", corpIdList));
        if (list != null && list.size() > 0) {
            for (DeviceBrand deviceBrand : list) {
                map.put(deviceBrand.getId(), deviceBrand.getName());
            }
        }
        return map;
    }

    /**
     * 获得编号与名称映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/16 7:48 下午
     **/
    @Override
    public Map<Long, String> mapIdAndName() {
        Map<Long, String> map = new HashMap<>();
        List<DeviceBrand> list = this.list();
        if (list != null && list.size() > 0) {
            for (DeviceBrand deviceBrand : list) {
                map.put(deviceBrand.getId(), deviceBrand.getName());
            }
        }
        return map;
    }

    @Override
    public ListWrapper<DeviceBrandDto> query(DeviceBrandFilter deviceBrandFilter) {
        ListWrapper<DeviceBrandDto> listWrapper = new ListWrapper<>();
        QueryWrapper<DeviceBrand> queryWrapper = new QueryWrapper<>();
        if(LongUtil.isZero(deviceBrandFilter.getCorp())) {
            Result<List<Long>> result = this.
                    anyfixFeignService.listDemanderCorpId(deviceBrandFilter.getCorpIdForDemander());
            List<Long> demanderCorpList = result == null ? null : result.getData();
            if(CollectionUtil.isNotEmpty(demanderCorpList)) {
                queryWrapper.in("corp",demanderCorpList);
                queryWrapper.orderByAsc("corp");
            } else{
                return listWrapper;
            }
        } else {
            queryWrapper.eq("corp",deviceBrandFilter.getCorp());
        }
        if (StrUtil.isNotBlank(deviceBrandFilter.getName())) {
            queryWrapper.like("name", deviceBrandFilter.getName());
        }
        if (StrUtil.isNotBlank(deviceBrandFilter.getEnabled())) {
            queryWrapper.eq("enabled", deviceBrandFilter.getEnabled());
        }
        Page page = new Page(deviceBrandFilter.getPageNum(), deviceBrandFilter.getPageSize());
        IPage<DeviceBrand> deviceBrandIPage = this.page(page, queryWrapper);
        List<DeviceBrand> deviceBrandList = deviceBrandIPage.getRecords();
        if (CollectionUtil.isNotEmpty(deviceBrandList)) {
            List<Long> corpIdList = deviceBrandList.stream().map(e -> e.getCorp()).distinct().collect(Collectors.toList());
            List<DeviceBrandDto> deviceBrandDtoList = new ArrayList<>();
            // 增加委托商名称显示
            Map<Long, String> corpMap = corpNameService.corpIdNameMap(corpIdList);
            for (DeviceBrand deviceBrand : deviceBrandIPage.getRecords()) {
                /** Mapper转换 */
                DeviceBrandDto tmpDto = new DeviceBrandDto();
                BeanUtils.copyProperties(deviceBrand, tmpDto);
                tmpDto.setCorpName(corpMap.get(tmpDto.getCorp()));
                deviceBrandDtoList.add(tmpDto);
            }
            listWrapper.setList(deviceBrandDtoList);
        }
        listWrapper.setTotal(deviceBrandIPage.getTotal());
        return listWrapper;
    }

    /**
     * 模糊查询设备品牌
     *
     * @param deviceBrandFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 10:42
     **/
    @Override
    public List<DeviceBrand> matchDeviceBrand(DeviceBrandFilter deviceBrandFilter) {
        return this.baseMapper.matchDeviceBrand(deviceBrandFilter);
    }

    /**
     * 设备型号列表
     *
     * @param demanderCorp
     * @param smallClassId
     * @return
     * @author zgpi
     * @date 2019/9/25 2:44 下午
     **/
    @Override
    public List<DeviceBrandDto> listBrandModel(Long demanderCorp, Long smallClassId) {
        List<DeviceBrandDto> deviceBrandDtoList = new ArrayList<>();
        List<DeviceModelDto> list = deviceModelMapper.listDeviceModelDto(demanderCorp, smallClassId);
        Map<DeviceBrandDto, List<DeviceModelDto>> map = new LinkedHashMap<>();
        DeviceBrandDto deviceBrandDto;
        for (DeviceModelDto deviceModelDto : list) {
            deviceBrandDto = new DeviceBrandDto();
            deviceBrandDto.setId(deviceModelDto.getBrandId());
            deviceBrandDto.setName(deviceModelDto.getBrandName());
            List<DeviceModelDto> modelDtoList = new ArrayList<>();
            if (map.containsKey(deviceBrandDto)) {
                modelDtoList = map.get(deviceBrandDto);
            }
            if (LongUtil.isNotZero(deviceModelDto.getId())) {
                modelDtoList.add(deviceModelDto);
            }
            map.put(deviceBrandDto, modelDtoList);
        }
        for (DeviceBrandDto dto : map.keySet()) {
            dto.setDeviceModelDtoList(map.get(dto));
            deviceBrandDtoList.add(dto);
        }
        return deviceBrandDtoList;
    }

    @Override
    public void save(DeviceBrand deviceBrand, UserInfo userInfo, ReqParam reqParam) {
        if (StringUtils.isEmpty(deviceBrand.getName())) {
            throw new AppException("设备品牌名称不能为空");
        }
        QueryWrapper<DeviceBrand> queryWrapper = new QueryWrapper<>();
        // 为空时设置当前公司
        if(LongUtil.isZero(deviceBrand.getCorp())){
            deviceBrand.setCorp(reqParam.getCorpId());
        }
        queryWrapper.eq("name", deviceBrand.getName());
        queryWrapper.eq("corp", deviceBrand.getCorp());
        List<DeviceBrand> deviceBrandList = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(deviceBrandList)) {
            throw new AppException("该设备品牌名称已经存在");
        }
        deviceBrand.setId(KeyUtil.getId());
        deviceBrand.setOperator(userInfo.getUserId());
        deviceBrand.setOperateTime(DateUtil.date().toTimestamp());
        this.save(deviceBrand);
        // 保存成功后删除临时文件表数据
        if (LongUtil.isNotZero(deviceBrand.getLogo())) {
            this.fileFeignService.deleteFileTemporaryByID(deviceBrand.getLogo());
        }
    }

    @Override
    public void update(DeviceBrand deviceBrand, UserInfo userInfo) {
        StringBuilder builder = new StringBuilder();
        if (StringUtils.isEmpty(deviceBrand.getName())) {
            builder.append("设备品牌名称不能为空");
        }
        if (LongUtil.isZero(deviceBrand.getCorp())) {
            builder.append("企业编号不能为空");
        }
        if (deviceBrand.getId() == 0) {
            builder.append("设备品牌编号不能为空");
        }
        if (builder.length() > 0) {
            throw new AppException(builder.toString());
        }
        QueryWrapper<DeviceBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", deviceBrand.getName());
        queryWrapper.eq("corp", deviceBrand.getCorp());
        queryWrapper.ne("id", deviceBrand.getId());
        List<DeviceBrand> list = this.baseMapper.selectList(queryWrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备品牌名称已经存在");
        }
        DeviceBrand deviceBrandOld = this.getById(deviceBrand.getId());
        deviceBrand.setOperator(userInfo.getUserId());
        deviceBrand.setOperateTime(DateUtil.date().toTimestamp());
        this.updateById(deviceBrand);
        // 删除旧文件
        if (LongUtil.isNotZero(deviceBrandOld.getLogo()) && (LongUtil.isZero(deviceBrand.getLogo()) ||
                !deviceBrandOld.getLogo().equals(deviceBrand.getLogo()))) {
            fileFeignService.delFile(deviceBrandOld.getLogo());
        }
        // 删除临时文件表数据
        if (LongUtil.isNotZero(deviceBrand.getLogo())) {
            fileFeignService.deleteFileTemporaryByID(deviceBrand.getLogo());
        }
    }

    @Override
    public void delete(Long id) {
        QueryWrapper<DeviceModel> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", id);
        int count = deviceModelMapper.selectCount(wrapper);
        if (count > 0) {
            throw new AppException("该设备品牌有其它设备型号，无法删除");
        }
        this.removeById(id);
    }
}
