package com.zjft.usp.anyfix.corp.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.corp.user.dto.StaffSkillDto;
import com.zjft.usp.anyfix.corp.user.filter.StaffSkillFilter;
import com.zjft.usp.anyfix.corp.user.mapper.StaffSkillMapper;
import com.zjft.usp.anyfix.corp.user.model.DeviceBranchUser;
import com.zjft.usp.anyfix.corp.user.model.StaffSkill;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.corp.user.service.DeviceBranchUserService;
import com.zjft.usp.anyfix.corp.user.service.StaffSkillService;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工程师技能表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-09-26
 */
@Service
public class StaffSkillServiceImpl extends ServiceImpl<StaffSkillMapper, StaffSkill> implements StaffSkillService {

    @Autowired
    private DeviceBranchUserService deviceBranchUserService;
    @Autowired
    private WorkTypeService workTypeService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private DeviceFeignService deviceFeignService;

    @Override
    public List<Long> skillMatchUserByBranch(StaffSkillDto staffSkillDto) {
        Assert.notNull(staffSkillDto, "staffSkillDto 不能为null");
        if (staffSkillDto.getBranchId() > 0) {
            List<DeviceBranchUser> deviceBranchUserList = deviceBranchUserService.list(new QueryWrapper<DeviceBranchUser>().eq("branch_id", staffSkillDto.getBranchId()));
            if (deviceBranchUserList != null) {
                List<Long> userIdList = new ArrayList<>();
                for (DeviceBranchUser deviceBranchUser : deviceBranchUserList) {
                    userIdList.add(deviceBranchUser.getUserId());
                }
                return this.skillMatchUser(staffSkillDto, userIdList);
            }
        }
        return null;
    }

    @Override
    public List<Long> skillMatchUserByBranchWithFuzzy(StaffSkillDto staffSkillDto) {
        Assert.notNull(staffSkillDto, "staffSkillDto 不能为null");
        if (staffSkillDto.getBranchId() > 0) {
            List<DeviceBranchUser> deviceBranchUserList = deviceBranchUserService.list(new QueryWrapper<DeviceBranchUser>().eq("branch_id", staffSkillDto.getBranchId()));
            if (deviceBranchUserList != null) {
                List<Long> userIdList = new ArrayList<>();
                for (DeviceBranchUser deviceBranchUser : deviceBranchUserList) {
                    userIdList.add(deviceBranchUser.getUserId());
                }
                return this.skillMatchUserWithFuzzy(staffSkillDto, userIdList);
            }
        }
        return null;
    }

    @Override
    public List<Long> skillMatchUser(StaffSkillDto staffSkillDto, List<Long> userIdList) {
        HashSet<Long> userIdSet = new HashSet<Long>();
        if (staffSkillDto != null && userIdList != null && userIdList.size() > 0) {
            List<StaffSkill> staffSkillList = this.list(new QueryWrapper<StaffSkill>().in("user_id", userIdList));
            for (StaffSkill staffSkill : staffSkillList) {
                //由于stafskill表属性都必填，所以无需判断null
                //所有技能属性都匹配才算技能匹配
                if (staffSkill.getWorkTypes().contains(staffSkillDto.getWorkTypes()) && staffSkill.getCorpId().equals(staffSkillDto.getCorpId())
                        && staffSkill.getSmallClassIds().contains(staffSkillDto.getSmallClassIds()) && staffSkill.getBrandIds().contains(staffSkillDto.getBrandIds())
                        && staffSkill.getModelIds().contains(staffSkillDto.getModelIds())) {
                    //技能完全匹配，添加用户,并利用hashset去重
                    userIdSet.add(staffSkill.getUserId());
                }
            }
        }
        return new ArrayList<>(userIdSet);
    }

    @Override
    public List<Long> skillMatchUserWithFuzzy(StaffSkillDto staffSkillDto, List<Long> userIdList) {
        Assert.notNull(staffSkillDto, "staffSkillDto 不能为null");
        Assert.notNull(userIdList, "userIdList 不能为null");

        HashSet<Long> userIdSet = new HashSet<Long>();
        List<StaffSkill> staffSkillList = this.list(new QueryWrapper<StaffSkill>().in("user_id", userIdList));
        for (StaffSkill staffSkill : staffSkillList) {
            //是否匹配标志
            boolean match = true;
            if (StrUtil.isNotEmpty(staffSkill.getWorkTypes()) && !staffSkill.getWorkTypes().contains(staffSkillDto.getWorkTypes())) {
                match = false;
            }
            if (staffSkill.getCorpId() > 0 && !staffSkill.getCorpId().equals(staffSkillDto.getCorpId())) {
                match = false;
            }
            if (StrUtil.isNotEmpty(staffSkill.getSmallClassIds()) && !staffSkill.getSmallClassIds().contains(staffSkillDto.getSmallClassIds())) {
                match = false;
            }
            if (StrUtil.isNotEmpty(staffSkill.getBrandIds()) && !staffSkill.getBrandIds().contains(staffSkillDto.getBrandIds())) {
                match = false;
            }
            if (StrUtil.isNotEmpty(staffSkill.getModelIds()) && !staffSkill.getModelIds().contains(staffSkillDto.getModelIds())) {
                match = false;
            }
            if (StrUtil.isNotEmpty(staffSkill.getWorkTypes()) && !staffSkill.getWorkTypes().contains(staffSkillDto.getWorkTypes())) {
                match = false;
            }
            if (staffSkill.getLargeClassId() > 0 && !staffSkill.getLargeClassId().equals(staffSkillDto.getLargeClassId())) {
                match = false;
            }
            if (match) {
                userIdSet.add(staffSkill.getUserId());
            }
        }
        return new ArrayList<>(userIdSet);
    }

    @Override
    public ListWrapper<StaffSkillDto> pageByFilter(StaffSkillFilter staffSkillFilter, ReqParam reqParam) {
        ListWrapper<StaffSkillDto> listWrapper = new ListWrapper<>();
        if (staffSkillFilter == null) {
            return listWrapper;
        }
        if (LongUtil.isZero(staffSkillFilter.getCorpId())) {
            staffSkillFilter.setCorpId(reqParam.getCorpId());
        }
        Page page = new Page(staffSkillFilter.getPageNum(), staffSkillFilter.getPageSize());
        QueryWrapper<StaffSkill> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", staffSkillFilter.getCorpId());
        if (staffSkillFilter.getUserId() != null && staffSkillFilter.getUserId() != 0L) {
            queryWrapper.eq("user_id", staffSkillFilter.getUserId());
        }
        if (staffSkillFilter.getWorkType() != null && staffSkillFilter.getWorkType() != 0L) {
            queryWrapper.like("work_types", "," + staffSkillFilter.getWorkType() + ",");
        }
        if (staffSkillFilter.getLargeClassId() != null && staffSkillFilter.getLargeClassId() != 0L) {
            queryWrapper.eq("large_class_id", staffSkillFilter.getLargeClassId());
        }
        if (staffSkillFilter.getSmallClassId() != null && staffSkillFilter.getSmallClassId() != 0L) {
            queryWrapper.like("small_class_ids", "," + staffSkillFilter.getSmallClassId() + ",");
        }
        if (staffSkillFilter.getBrandId() != null && staffSkillFilter.getBrandId() != 0L) {
            queryWrapper.like("brand_ids", "," + staffSkillFilter.getBrandId() + ",");
        }
        if (staffSkillFilter.getModelId() != null && staffSkillFilter.getModelId() != 0L) {
            queryWrapper.like("model_ids", "," + staffSkillFilter.getModelId() + ",");
        }
        IPage<StaffSkill> staffSkillIPage = this.page(page, queryWrapper);
        if (staffSkillIPage != null && staffSkillIPage.getRecords() != null && staffSkillIPage.getRecords().size() > 0) {
            List<Long> userIdList = new ArrayList<>();
            List<StaffSkillDto> dtoList = new ArrayList<>();
            List<Long> relatedCorpIdList = this.demanderServiceService.listRelatedCorpIdsByService(staffSkillFilter.getCorpId());
            for (StaffSkill staffSkill : staffSkillIPage.getRecords()) {
                userIdList.add(staffSkill.getUserId());
            }
            Map<Long, String> userIdAndNameMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList)).getData();
            Map<Integer, String> workTypeMap = this.workTypeService.mapWorkTypeByCorpIdList(relatedCorpIdList);
            Map<Long, String> largeClassMap = this.deviceFeignService.mapLargeClassByCorpIdList(relatedCorpIdList).getData();
            Map<Long, String> smallClassMap = this.deviceFeignService.mapSmallClassByCorpIdList(relatedCorpIdList).getData();
            Map<Long, String> deviceBrandMap = this.deviceFeignService.mapDeviceBrandByCorpIdList(relatedCorpIdList).getData();
            Result deviceModelMapResult = this.deviceFeignService.mapDeviceModel();
            Map<Long, String> deviceModelMap = JsonUtil.parseObject(JsonUtil.toJson(deviceModelMapResult.getData()), Map.class);

            for (StaffSkill staffSkill : staffSkillIPage.getRecords()) {
                StaffSkillDto staffSkillDto = new StaffSkillDto();
                BeanUtils.copyProperties(staffSkill, staffSkillDto);
                staffSkillDto.setListByString();
                staffSkillDto.setUserName(userIdAndNameMap.get(staffSkillDto.getUserId()));
                if (staffSkill.getLargeClassId() != null) {
                    if (largeClassMap.get(staffSkill.getLargeClassId()) != null) {
                        staffSkillDto.setLargeClassName(largeClassMap.get(staffSkill.getLargeClassId()));
                    }
                }
                if (staffSkillDto.getWorkTypeList() != null && staffSkillDto.getWorkTypeList().size() > 0) {
                    String workTypeNames = "";
                    for (Integer worktype : staffSkillDto.getWorkTypeList()) {
                        if (workTypeMap.get(worktype) != null) {
                            workTypeNames += workTypeMap.get(worktype) + ",";
                        }
                    }
                    if (workTypeNames.length() > 0) {
                        staffSkillDto.setWorkTypeNames(workTypeNames.substring(0, workTypeNames.length() - 1));
                    }
                }
                if (staffSkillDto.getBrandIdList() != null && staffSkillDto.getBrandIdList().size() > 0) {
                    String brandNames = "";
                    for (Long brandId : staffSkillDto.getBrandIdList()) {
                        if (deviceBrandMap.get(brandId) != null) {
                            brandNames += deviceBrandMap.get(brandId) + ",";
                        }
                    }
                    if (brandNames.length() > 0) {
                        staffSkillDto.setBrandNames(brandNames.substring(0, brandNames.length() - 1));
                    }
                }
                if (staffSkillDto.getSmallClassIdList() != null && staffSkillDto.getSmallClassIdList().size() > 0) {
                    String smallClassNames = "";
                    for (Long smallClassId : staffSkillDto.getSmallClassIdList()) {
                        if (smallClassMap.get(smallClassId) != null) {
                            smallClassNames += smallClassMap.get(smallClassId) + ",";
                        }
                    }
                    if (smallClassNames.length() > 0) {
                        staffSkillDto.setSmallClassNames(smallClassNames.substring(0, smallClassNames.length() - 1));
                    }
                }
                if (staffSkillDto.getModelIdList() != null && staffSkillDto.getModelIdList().size() > 0) {
                    String modelNames = "";
                    for (Long modelId : staffSkillDto.getModelIdList()) {
                        if (deviceModelMap.get(modelId) != null) {
                            modelNames += deviceModelMap.get(modelId) + ",";
                        }
                    }
                    if (modelNames.length() > 0) {
                        staffSkillDto.setModelNames(modelNames.substring(0, modelNames.length() - 1));
                    }
                }
                dtoList.add(staffSkillDto);
            }
            listWrapper.setList(dtoList);
        }
        listWrapper.setTotal(staffSkillIPage.getTotal());
        return listWrapper;
    }

    @Override
    public void addStaffSkill(StaffSkillDto staffSkillDto, ReqParam reqParam) {
        if (LongUtil.isZero(staffSkillDto.getCorpId())) {
            staffSkillDto.setCorpId(reqParam.getCorpId());
        }
        if (LongUtil.isZero(staffSkillDto.getUserId())) {
            throw new AppException("人员不能为空！");
        }
        staffSkillDto.setStringByList();
        StaffSkill staffSkill = new StaffSkill();
        staffSkill.setId(KeyUtil.getId());
        BeanUtils.copyProperties(staffSkillDto, staffSkill);
        this.save(staffSkill);
    }

    @Override
    public void updateStaffSkill(StaffSkillDto staffSkillDto) {
        StaffSkill staffSkill = this.getById(staffSkillDto.getId());
        if (staffSkill == null) {
            throw new AppException("记录已不存在！");
        }
        staffSkillDto.setStringByList();
        BeanUtils.copyProperties(staffSkillDto, staffSkill);
        this.updateById(staffSkill);
    }
}
