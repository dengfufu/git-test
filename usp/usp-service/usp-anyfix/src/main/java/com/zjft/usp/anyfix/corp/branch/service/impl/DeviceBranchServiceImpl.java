package com.zjft.usp.anyfix.corp.branch.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.druid.support.json.JSONUtils;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.common.feign.dto.UserRealDto;
import com.zjft.usp.anyfix.corp.branch.dto.DeviceBranchDto;
import com.zjft.usp.anyfix.corp.branch.filter.DeviceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.mapper.DeviceBranchMapper;
import com.zjft.usp.anyfix.corp.branch.model.DeviceBranch;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUser;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 设备网点表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-24
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class DeviceBranchServiceImpl extends ServiceImpl<DeviceBranchMapper, DeviceBranch> implements DeviceBranchService {

    @Autowired
    private DemanderCustomService demanderCustomService;
    @Autowired
    private DeviceBranchUserService deviceBranchUserService;
    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DemanderServiceService demanderServiceService;

    @Override
    public ListWrapper<DeviceBranchDto> queryDeviceBranch(DeviceBranchFilter deviceBranchFilter) {
        Page page = new Page(deviceBranchFilter.getPageNum(), deviceBranchFilter.getPageSize());
        List<Long> demanderCorpList = new ArrayList<>();
        // 如果是为服务商，查询所有的委托商
        List<DeviceBranchDto> list = this.baseMapper.queryDeviceBranchForService(page, deviceBranchFilter);
        Map<String, String> areaMap = null;
        Result<Map<String, String>> areaMapResult = uasFeignService.mapAreaCodeAndName();
        if (areaMapResult != null && areaMapResult.getCode() == Result.SUCCESS) {
            areaMap = areaMapResult.getData();
        }
        areaMap = areaMap == null ? new HashMap<>() : areaMap;

        List<DeviceBranchDto> deviceBranchDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(list)) {
            List<Long> userIdList = new ArrayList<>();
            List<Long> customIdList = new ArrayList<>();
            List<Long> customCorpList = new ArrayList<>();
            for (DeviceBranchDto deviceBranchDto : list) {
                userIdList.add(deviceBranchDto.getContactId());
                customIdList.add(deviceBranchDto.getCustomId());
                customCorpList.add(deviceBranchDto.getCustomCorp());
                if (LongUtil.isNotZero(deviceBranchDto.getDemanderCorp())) {
                    demanderCorpList.add(deviceBranchDto.getDemanderCorp());
                }
            }
            // 人员映射
            Map<Long, String> userMap = null;
            Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
                userMap = userMapResult.getData();
            }
            userMap = userMap == null ? new HashMap<>() : userMap;
            // 客户关系编号与企业名称映射
            Map<Long, String> customIdAndNameMap = demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            // 客户企业编号与企业名称映射
            Map<Long, String> corpIdAndNameMap = null;
            demanderCorpList.addAll(customCorpList);
            Result<Map<Long, String>> corpIdAndNameMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JSONUtils.toJSONString(demanderCorpList));
            if (corpIdAndNameMapResult != null && corpIdAndNameMapResult.getCode() == Result.SUCCESS) {
                corpIdAndNameMap = corpIdAndNameMapResult.getData();
            }
            corpIdAndNameMap = corpIdAndNameMap == null ? new HashMap<>() : corpIdAndNameMap;

            for (DeviceBranchDto deviceBranchDto : list) {
                if (LongUtil.isNotZero(deviceBranchDto.getCustomCorp())) {
                    deviceBranchDto.setCustomCorpName(StrUtil.trimToEmpty(corpIdAndNameMap.get(deviceBranchDto.getCustomCorp())));
                } else {
                    deviceBranchDto.setCustomCorpName(StrUtil.trimToEmpty(customIdAndNameMap.get(deviceBranchDto.getCustomId())));
                }
                if (LongUtil.isNotZero(deviceBranchDto.getContactId()) &&
                        StrUtil.isBlank(deviceBranchDto.getContactName())) {
                    deviceBranchDto.setContactName(StrUtil.trimToEmpty(userMap.get(deviceBranchDto.getContactId())));
                }
                if(LongUtil.isNotZero(deviceBranchDto.getDemanderCorp())) {
                    deviceBranchDto.setDemanderCorpName(StrUtil.trimToEmpty(corpIdAndNameMap.get(deviceBranchDto.getDemanderCorp())));
                }
                if (LongUtil.isNotZero(deviceBranchDto.getUpperBranchId())) {
                    DeviceBranch entity = this.getById(deviceBranchDto.getUpperBranchId());
                    deviceBranchDto.setUpperFullName(this.buildFullBranchName(entity));
                }
                String provinceName = "";
                String cityName = "";
                String districtName = "";
                if (!StringUtils.isEmpty(deviceBranchDto.getProvince())) {
                    provinceName = StrUtil.trimToEmpty(areaMap.get(deviceBranchDto.getProvince()));
                }
                if (!StringUtils.isEmpty(deviceBranchDto.getCity())) {
                    cityName = StrUtil.trimToEmpty(areaMap.get(deviceBranchDto.getCity()));
                }
                if (!StringUtils.isEmpty(deviceBranchDto.getDistrict())) {
                    districtName = StrUtil.trimToEmpty(areaMap.get(deviceBranchDto.getDistrict()));
                }
                deviceBranchDto.setAreaName(provinceName + cityName + districtName);
                deviceBranchDtoList.add(deviceBranchDto);
            }
        }
        return ListWrapper.<DeviceBranchDto>builder()
                .list(deviceBranchDtoList)
                .total(page.getTotal())
                .build();
    }

    @Override
    public DeviceBranchDto findDtoById(Long branchId) {
        DeviceBranchDto deviceBranchDto = new DeviceBranchDto();
        DeviceBranch deviceBranch = this.getById(branchId);
        if (deviceBranch != null) {
            BeanUtils.copyProperties(deviceBranch, deviceBranchDto);
            Set<String> codeSet = new HashSet<>();
            codeSet.add(deviceBranch.getProvince());
            codeSet.add(deviceBranch.getCity());
            codeSet.add(deviceBranch.getDistrict());
            List<String> list = new ArrayList<>(codeSet);
            if (LongUtil.isNotZero(deviceBranch.getContactId()) &&
                    StringUtils.isBlank(deviceBranch.getContactName())) {
                Result userResult = uasFeignService.findUserRealDtoById(deviceBranch.getContactId());
                UserRealDto userRealDto = null;
                if (userResult != null && userResult.getCode() == Result.SUCCESS) {
                    userRealDto = JsonUtil.parseObject(JsonUtil.toJson(userResult.getData()), UserRealDto.class);
                }
                if (userRealDto != null) {
                    deviceBranchDto.setContactName(userRealDto.getUserName());
                }
            } else {
                deviceBranchDto.setContactName(deviceBranch.getContactName());
            }
            Map<String, String> nameMap = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(list)).getData();
            String cityName = nameMap.get(deviceBranch.getCity());
            String provinceName = nameMap.get(deviceBranch.getProvince());
            String districtName = nameMap.get(deviceBranch.getDistrict());
            deviceBranchDto.setAreaName(provinceName + cityName + districtName);
            if(LongUtil.isNotZero(deviceBranchDto.getCustomId())) {
                DemanderCustom demanderCustom = demanderCustomService.getById(deviceBranchDto.getCustomId());
                deviceBranchDto.setCustomCorpName(demanderCustom != null ? demanderCustom.getCustomCorpName() : "");
                Result corpResult = uasFeignService.findCorpById(demanderCustom.getDemanderCorp());
                if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
                    CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
                    deviceBranchDto.setDemanderCorpName(corpDto != null ? corpDto.getShortName() : "");
                }
            } else if(LongUtil.isNotZero(deviceBranchDto.getCustomCorp())) {
                Result corpResult = uasFeignService.findCorpById(deviceBranchDto.getCustomCorp());
                if (corpResult != null && corpResult.getCode() == Result.SUCCESS) {
                    CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpResult.getData()), CorpDto.class);
                    deviceBranchDto.setCustomCorpName(corpDto != null ? corpDto.getShortName() : "");
                }
            }
            return deviceBranchDto;
        }
        return null;
    }

    /**
     * 添加设备网点
     *
     * @param deviceBranchDto
     * @param userInfo
     * @param reqParam
     * @return
     * @author zgpi
     * @date 2019/12/18 10:23
     **/
    @Override
    public Long addDeviceBranch(DeviceBranchDto deviceBranchDto, UserInfo userInfo, ReqParam reqParam) {
        QueryWrapper<DeviceBranch> wrapper = new QueryWrapper<>();
        DeviceBranch deviceBranch = new DeviceBranch();
        Long customCorp = deviceBranchDto.getCustomCorp();
        Long customId = deviceBranchDto.getCustomId();
        if (LongUtil.isZero(customCorp) && LongUtil.isZero(customId)) {
            customCorp = deviceBranchDto.getDemanderCustom().getCustomCorp();
            customId = deviceBranchDto.getDemanderCustom().getCustomId();
        }
        wrapper.eq("custom_id", customId);
        wrapper.eq("custom_corp", customCorp);
        wrapper.eq("branch_name", deviceBranchDto.getBranchName());
        List<DeviceBranch> list = this.baseMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备网点名称已经存在！");
        }
        BeanUtils.copyProperties(deviceBranchDto, deviceBranch);
        deviceBranch.setCustomId(customId);
        deviceBranch.setCustomCorp(customCorp);
        deviceBranch.setBranchId(KeyUtil.getId());
        deviceBranch.setOperator(userInfo.getUserId());
        deviceBranch.setOperateTime(DateUtil.date());
        this.save(deviceBranch);
        return deviceBranch.getBranchId();
    }

    /**
     * 修改设备网点
     *
     * @param deviceBranchDto
     * @return
     * @author zgpi
     * @date 2019/12/18 10:24
     **/
    @Override
    public void updateDeviceBranch(DeviceBranchDto deviceBranchDto) {
        Long customCorp = deviceBranchDto.getCustomCorp();
        Long customId = deviceBranchDto.getCustomId();
        if (LongUtil.isZero(customCorp) && LongUtil.isZero(customId)) {
            customCorp = deviceBranchDto.getDemanderCustom().getCustomCorp();
            customId = deviceBranchDto.getDemanderCustom().getCustomId();
        }
        QueryWrapper<DeviceBranch> wrapper = new QueryWrapper<>();
        wrapper.eq("custom_id", customId);
        wrapper.eq("custom_corp", customCorp);
        wrapper.eq("branch_name", deviceBranchDto.getBranchName());
        wrapper.ne("branch_id", deviceBranchDto.getBranchId());
        List<DeviceBranch> list = this.baseMapper.selectList(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            throw new AppException("该设备网点名称已经存在");
        }
        DeviceBranch deviceBranch = new DeviceBranch();
        BeanUtils.copyProperties(deviceBranchDto, deviceBranch);
        deviceBranch.setCustomId(customId);
        deviceBranch.setCustomCorp(customCorp);

        /**不允许自己引用自己，也不允许直接交叉引用，间接交叉引用*/
        if (LongUtil.isNotZero(deviceBranch.getBranchId()) && LongUtil.isNotZero(deviceBranch.getUpperBranchId())) {

            if (deviceBranch.getBranchId().compareTo(deviceBranch.getUpperBranchId()) == 0) {
                throw new AppException("该设备上级网点不能选择自己，请重新选择");
            }

            QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper();
            queryWrapper.eq("custom_corp", deviceBranch.getCustomCorp());
            queryWrapper.eq("branch_id", deviceBranch.getUpperBranchId());
            queryWrapper.eq("upper_branch_id", deviceBranch.getBranchId());
            list = this.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                throw new AppException("该设备上级网点名称不允许相互直接引用");
            }

            /**设置A.upper = B时，  找出 B的所有上级，里面不能包含A*/
            /**查找upperBranchId的所有上级，里面不能包含branchId*/

            listParentBranchId = new ArrayList<>();
            searchParentBranchId(deviceBranch.getUpperBranchId());

            if (CollectionUtil.isNotEmpty(listParentBranchId) && listParentBranchId.contains(deviceBranch.getBranchId())) {
                throw new AppException("该设备上级网点名称不允许相互间接引用");
            }
        }

        if (deviceBranch.getUpperBranchId() == null) {
            deviceBranch.setUpperBranchId(0L);
        }

        deviceBranch.setOperateTime(DateUtil.date());
        this.updateById(deviceBranch);
    }


    private List<Long> listParentBranchId = new ArrayList<>();

    private void searchParentBranchId(Long branchId) {
        DeviceBranch deviceBranch = this.getById(branchId);
        if (deviceBranch != null) {
            if (LongUtil.isNotZero(deviceBranch.getUpperBranchId())) {
                QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper();
                queryWrapper.eq("branch_id", deviceBranch.getUpperBranchId());
                deviceBranch = this.getOne(queryWrapper);
                if (deviceBranch != null) {
                    listParentBranchId.add(deviceBranch.getBranchId());
                    searchParentBranchId(deviceBranch.getBranchId());
                }
            }
        }
    }


    /**
     * 删除设备网点
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2019/12/18 10:24
     **/
    @Override
    public void delDeviceBranch(Long branchId) {
        this.removeById(branchId);
        deviceBranchUserService.remove(new UpdateWrapper<DeviceBranchUser>().eq("branch_id", branchId));
    }

    /**
     * 下级网点数
     *
     * @param upperBranchId
     * @return
     * @author zgpi
     * @date 2019/11/5 18:24
     **/
    @Override
    public Integer countByUpperId(Long upperBranchId) {
        return this.count(new QueryWrapper<DeviceBranch>().eq("upper_branch_id", upperBranchId));
    }

    @Override
    public Map<Long, DeviceBranch> mapDeviceBranchByCorp(Long customCorp) {
        Map<Long, DeviceBranch> map = new HashMap<>();
        if (customCorp == null || customCorp == 0L) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().eq("custom_corp", customCorp));
        if (list != null && list.size() > 0) {
            for (DeviceBranch deviceBranch : list) {
                map.put(deviceBranch.getBranchId(), deviceBranch);
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapDeviceBranchNameByCorp(Long customCorp) {
        Map<Long, String> map = new HashMap<>();
        if (customCorp == null || customCorp == 0L) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().eq("custom_corp", customCorp));
        if (list != null && list.size() > 0) {
            for (DeviceBranch deviceBranch : list) {
                map.put(deviceBranch.getBranchId(), deviceBranch.getBranchName());
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapDeviceBranchByCorpIdList(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(corpIdList)) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().in("custom_corp", corpIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            for (DeviceBranch deviceBranch : list) {
                map.put(deviceBranch.getBranchId(), deviceBranch.getBranchName());
            }
        }
        return map;
    }

    /**
     * 根据客户编号列表获得设备网点ID与名称映射
     *
     * @param customIdList
     * @return
     * @author zgpi
     * @date 2020/1/16 14:53
     **/
    @Override
    public Map<Long, String> mapCustomDeviceBranchByCustomIdList(List<Long> customIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(customIdList)) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().in("custom_id", customIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            for (DeviceBranch deviceBranch : list) {
                map.put(deviceBranch.getBranchId(), deviceBranch.getBranchName());
            }
        }
        return map;
    }

    /**
     * 根据网点编号列表获得编号与名称映射
     *
     * @param branchIdList 网点编号列表
     * @return
     * @author zgpi
     * @date 2020/2/14 14:09
     */
    @Override
    public Map<Long, String> mapIdAndNameByIdList(List<Long> branchIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(branchIdList)) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().in("branch_id", branchIdList));
        map = list.stream().collect(Collectors.toMap(e -> e.getBranchId(), e -> e.getBranchName()));
        return map;
    }

    /**
     * 模糊查询设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 10:22
     **/
    @Override
    public List<DeviceBranchDto> matchDeviceBranch(DeviceBranchFilter deviceBranchFilter) {
        if (LongUtil.isNotZero(deviceBranchFilter.getCustomId()) &&
                LongUtil.isZero(deviceBranchFilter.getCustomCorp())) {
            DemanderCustom demanderCustom = demanderCustomService.getById(deviceBranchFilter.getCustomId());
            if (demanderCustom != null) {
                deviceBranchFilter.setCustomCorp(demanderCustom.getCustomCorp());
            }
        }
        List<DeviceBranchDto> list = this.baseMapper.matchDeviceBranch(deviceBranchFilter);
        // 查询上级网点名称
        Map<Long, DeviceBranch> map = new HashMap<>();
        List<Long> pidList = new ArrayList<>();
        List<DeviceBranch> pList = new ArrayList<>();
        pList.addAll(list);
        while (pList != null && pList.size() > 0) {
            for (DeviceBranch deviceBranch : pList) {
                if (!map.containsKey(deviceBranch.getBranchId())) {
                    map.put(deviceBranch.getBranchId(), deviceBranch);
                }
                if (LongUtil.isNotZero(deviceBranch.getUpperBranchId()) &&
                        !map.containsKey(deviceBranch.getUpperBranchId())) {
                    pidList.add(deviceBranch.getUpperBranchId());
                }
            }
            if (pidList.size() > 0) {
                QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper();
                queryWrapper.in("branch_id", pidList);
                pList = this.list(queryWrapper);
                pidList.clear();
            } else {
                pList = null;
            }
        }
        for (DeviceBranchDto deviceBranch : list) {
            deviceBranch.setFullName(this.buildFullBranchName(deviceBranch, map));
        }
        for (DeviceBranchDto deviceBranch : list) {
            deviceBranch.setBranchName(deviceBranch.getFullName());
        }
        return list;
    }

    /**
     * 模糊查询相关企业的设备网点
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/2/14 16:19
     */
    @Override
    public List<DeviceBranchDto> matchRelateDeviceBranch(DeviceBranchFilter deviceBranchFilter) {
        return this.baseMapper.matchRelateDeviceBranch(deviceBranchFilter);
    }

    private String buildFullBranchName(DeviceBranch deviceBranch, Map<Long, DeviceBranch> map) {
        if (deviceBranch == null) {
            return "";
        }
        if (LongUtil.isNotZero(deviceBranch.getUpperBranchId())) {
            return this.buildFullBranchName(map.get(deviceBranch.getUpperBranchId()), map) +
                    deviceBranch.getBranchName();
        }
        return deviceBranch.getBranchName();
    }

    @Override
    public String buildFullBranchName(DeviceBranch deviceBranch) {
        if (deviceBranch == null) {
            return "";
        }
        if (LongUtil.isNotZero(deviceBranch.getUpperBranchId())) {
            DeviceBranch deviceBranchUpper = this.getById(deviceBranch.getUpperBranchId());
            return this.buildFullBranchName(deviceBranchUpper) + deviceBranch.getBranchName();
        }
        return deviceBranch.getBranchName();
    }

    /**
     * 根据网点ID列表获得设备网点ID与名称映射
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/4/21 10:23
     **/
    @Override
    public Map<Long, String> mapDeviceBranchByBranchIdList(Collection<Long> branchIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(branchIdList)) {
            return map;
        }
        List<DeviceBranch> list = this.list(new QueryWrapper<DeviceBranch>().in("branch_id", branchIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            for (DeviceBranch deviceBranch : list) {
                map.put(deviceBranch.getBranchId(), deviceBranch.getBranchName());
            }
        }
        return map;
    }

    @Override
    public List<DeviceBranch> selectUpperBranchList(DeviceBranchFilter deviceFilter) {
        return this.baseMapper.selectUpperBranchList(deviceFilter);
    }

    @Override
    public Map<String, Long> batchAddDeviceBranch(List<DeviceBranch> deviceBranches) {
        if(CollectionUtil.isEmpty(deviceBranches)) {
            throw new AppException("设备网点不能为空");
        }
        for(DeviceBranch deviceBranch : deviceBranches) {
            if(StringUtils.isBlank(deviceBranch.getBranchName())) {
                throw new AppException("设备网点名称不能为空");
            }
        }
        QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper<>();
        for(DeviceBranch deviceBranch : deviceBranches) {
            queryWrapper.or( wrapper ->wrapper.eq("branch_name",deviceBranch.getBranchName())
                    .eq("custom_corp",deviceBranch.getCustomCorp()));
        }
        queryWrapper.select("branch_id");
        List<DeviceBranch> queryList = this.list(queryWrapper);
        Map<String,DeviceBranch> nameDeviceMap = new HashMap();
        if(CollectionUtil.isNotEmpty(queryList)) {
            for (DeviceBranch deviceBranch : queryList) {
                nameDeviceMap.put(deviceBranch.getBranchName(), deviceBranch);
            }
        }
        //删除多个
        deviceBranches.removeIf(s -> nameDeviceMap.get(s) != null);
        Map<String,Long> returnMap = new HashMap<>();
        for(DeviceBranch deviceBranch : deviceBranches) {
            Long branchId = KeyUtil.getId();
            deviceBranch.setBranchId(branchId);
            returnMap.put(deviceBranch.getBranchName(),branchId);
        }
        this.saveBatch(deviceBranches);
        return returnMap;
    }

    @Override
    public List<DeviceBranch> listDeviceBranch(DeviceBranchFilter deviceBranchFilter) {
        QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper();
        queryWrapper.eq("custom_corp", deviceBranchFilter.getCustomCorp());
        if (StrUtil.isNotBlank(deviceBranchFilter.getEnabled())) {
            queryWrapper.eq("enabled", deviceBranchFilter.getEnabled().toUpperCase());
        }
        queryWrapper.orderByAsc("branch_name");
        return this.list(queryWrapper);
    }

    /**
     * 分页查询客户设备网点列表
     *
     * @param deviceBranchFilter
     * @return
     * @author zgpi
     * @date 2019/11/5 16:48
     **/
    @Override
    public ListWrapper<DeviceBranchDto> queryDeviceBranchByCustom(DeviceBranchFilter deviceBranchFilter) {
        if (LongUtil.isZero(deviceBranchFilter.getCustomId())
                && LongUtil.isZero(deviceBranchFilter.getCustomCorp())
                && CollectionUtil.isEmpty(deviceBranchFilter.getCustomOrCorpIdList())) {
            return ListWrapper.<DeviceBranchDto>builder()
                    .list(null)
                    .total(0L)
                    .build();
        }
        IPage<DeviceBranch> page = new Page(deviceBranchFilter.getPageNum(), deviceBranchFilter.getPageSize());
        QueryWrapper<DeviceBranch> queryWrapper = new QueryWrapper();
        queryWrapper.eq("enabled", deviceBranchFilter.getEnabled().toUpperCase());
        if (LongUtil.isNotZero(deviceBranchFilter.getCustomCorp())) {
            queryWrapper.eq("custom_corp", deviceBranchFilter.getCustomCorp());
        } else if (LongUtil.isNotZero(deviceBranchFilter.getCustomId())) {
            queryWrapper.eq("custom_id", deviceBranchFilter.getCustomId());
        }
        if (CollectionUtil.isNotEmpty(deviceBranchFilter.getCustomOrCorpIdList())) {
            queryWrapper.in("custom_id", deviceBranchFilter.getCustomOrCorpIdList())
                    .or().in("custom_corp", deviceBranchFilter.getCustomOrCorpIdList());
        }
        if (LongUtil.isNotZero(deviceBranchFilter.getUpperBranchId())) {
            queryWrapper.eq("upper_branch_id", deviceBranchFilter.getUpperBranchId());
        }
        if (StrUtil.isNotBlank(deviceBranchFilter.getMatchFilter())) {
            queryWrapper.and(wrapper -> wrapper.like("address", StrUtil.trimToEmpty(deviceBranchFilter.getMatchFilter()))
                    .or().like("branch_phone", StrUtil.trimToEmpty(deviceBranchFilter.getMatchFilter()))
                    .or().like("branch_name", StrUtil.trimToEmpty(deviceBranchFilter.getMatchFilter())));
        }
        queryWrapper.orderByAsc("branch_name");
        IPage<DeviceBranch> iPage = this.page(page, queryWrapper);
        Result<Map<String, String>> areaMapResult = uasFeignService.mapAreaCodeAndName();
        Map<String, String> areaMap = areaMapResult.getData();
        areaMap = areaMap == null ? new HashMap<>() : areaMap;
        List<Long> userIdList = iPage.getRecords().stream().map(e -> e.getContactId()).collect(Collectors.toList());
        Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
        Map<Long, String> userMap = new HashMap<>();
        if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
            userMap = userMapResult.getData();
        }
        List<DeviceBranchDto> deviceBranchDtoList = new ArrayList<>();
        if (CollectionUtils.isNotEmpty(iPage.getRecords())) {
            DeviceBranchDto deviceBranchDto;
            for (DeviceBranch deviceBranch : iPage.getRecords()) {
                deviceBranchDto = new DeviceBranchDto();
                BeanUtils.copyProperties(deviceBranch, deviceBranchDto);
                if (LongUtil.isNotZero(deviceBranch.getContactId()) &&
                        StringUtils.isBlank(deviceBranch.getContactName())) {
                    deviceBranchDto.setContactName(StrUtil.trimToEmpty(userMap.get(deviceBranch.getContactId())));
                }
                String provinceName = "";
                String cityName = "";
                String districtName = "";
                if (deviceBranch.getDistrict().length() >= 2) {
                    String provinceCode = deviceBranch.getDistrict().substring(0, 2);
                    provinceName = StrUtil.trimToEmpty(areaMap.get(provinceCode));
                }
                if (deviceBranch.getDistrict().length() >= 4) {
                    String cityCode = deviceBranch.getDistrict().substring(0, 4);
                    cityName = StrUtil.trimToEmpty(areaMap.get(cityCode));
                }
                if (deviceBranch.getDistrict().length() >= 4) {
                    String districtCode = deviceBranch.getDistrict().substring(0, 6);
                    districtName = StrUtil.trimToEmpty(areaMap.get(districtCode));
                }
                deviceBranchDto.setAreaName(provinceName + cityName + districtName);
                deviceBranchDtoList.add(deviceBranchDto);
            }
        }
        return ListWrapper.<DeviceBranchDto>builder()
                .list(deviceBranchDtoList)
                .total(iPage.getTotal())
                .build();
    }
}
