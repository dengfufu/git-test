package com.zjft.usp.uas.corp.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.utils.RsaUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import com.zjft.usp.uas.baseinfo.service.CfgIndustryService;
import com.zjft.usp.uas.corp.dto.*;
import com.zjft.usp.uas.corp.enums.CorpOperationEnum;
import com.zjft.usp.uas.corp.enums.StaffScopeEnum;
import com.zjft.usp.uas.corp.filter.CorpRegistryFilter;
import com.zjft.usp.uas.corp.mapper.CorpRegistryMapper;
import com.zjft.usp.uas.corp.model.*;
import com.zjft.usp.uas.corp.service.*;
import com.zjft.usp.uas.right.composite.SysRoleCompoService;
import com.zjft.usp.uas.right.enums.TenantLevelEnum;
import com.zjft.usp.uas.right.dto.SysRoleDto;
import com.zjft.usp.uas.right.model.SysRole;
import com.zjft.usp.uas.right.model.SysTenant;
import com.zjft.usp.uas.right.service.SysTenantService;
import com.zjft.usp.uas.user.model.UserReal;
import com.zjft.usp.uas.user.service.UserRealService;
import com.zjft.usp.uas.user.service.UserRegisterService;
import com.zjft.usp.uas.user.service.UserSafeService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 企业注册实现类
 *
 * @author canlei
 * @date 2019-08-04
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class CorpRegistryServiceImpl extends ServiceImpl<CorpRegistryMapper, CorpRegistry> implements CorpRegistryService {

    @Autowired
    private UserSafeService userSafeService;
    @Autowired
    private UserRealService userRealService;
    @Autowired
    private CorpOperationService corpOperationService;
    @Autowired
    private CorpUserService corpUserService;
//    @Autowired
//    private CorpAdminService corpAdminService;
    @Autowired
    private CfgIndustryService cfgIndustryService;
    @Autowired
    private CfgAreaService cfgAreaService;
    @Autowired
    private CorpVerifyAppService corpVerifyAppService;
    @Autowired
    private CorpVerifyService corpVerifyService;
    @Autowired
    private SysRoleCompoService sysRoleCompoService;
    @Autowired
    private RedisRepository redisRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Resource
    private FileFeignService fileFeignService;

    @Autowired
    private UserRegisterService userRegisterService;

    @Autowired
    private SysTenantService sysTenantService;
    @Autowired
    private CorpRegistryService corpRegistryService;

    /**
     * 分页查询企业
     *
     * @param corpRegistryFilter
     * @return
     * @author zgpi
     * @date 2019/12/18 16:48
     **/
    @Override
    public ListWrapper<CorpDto> query(CorpRegistryFilter corpRegistryFilter,
                                      Long curUserId) {
        Page page = new Page(corpRegistryFilter.getPageNum(), corpRegistryFilter.getPageSize());
        List<CorpRegistry> corpRegistryList = this.baseMapper.query(page, corpRegistryFilter);
        Map<String, String> regionMap = cfgAreaService.selectAreaMap();
        Map<Long, CorpUser> corpUserMap = corpUserService.mapCorpIdByUser(curUserId);
        List<CorpDto> dtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(corpRegistryList)) {
            CorpDto dto;
            for (CorpRegistry corpRegistry : corpRegistryList) {
                dto = new CorpDto();
                BeanUtils.copyProperties(corpRegistry, dto);
                dto.areaName(regionMap.get(dto.getProvince()), regionMap.get(dto.getCity()), regionMap.get(dto.getDistrict()));
                if (corpUserMap.containsKey(corpRegistry.getCorpId())) {
                    dto.setInCorp(true);
                }
                dtoList.add(dto);
            }
        }
        return ListWrapper.<CorpDto>builder().list(dtoList).total(page.getTotal()).build();
    }

    @Override
    public Long corpRegistry(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId) {
        if ("usp-web".equals(corpRegistryDto.getClientId())) {
            if (StrUtil.isBlank(corpRegistryDto.getMobile())) {
                throw new AppException("注册人手机号不能为空！");
            }
            if (StrUtil.isBlank(corpRegistryDto.getRegUserName())) {
                throw new AppException("注册人姓名不能为空！");
            }
            CorpUserInfoDto userInfo = corpUserService.getUserInfoByMobile(corpRegistryDto.getMobile());
            if (userInfo != null) {
                if (!corpRegistryDto.getRegUserName().equals(userInfo.getUserName())) {
                    throw new AppException("手机号已被其它用户注册或手机号、姓名不正确！");
                } else {
                    corpRegistryDto.setRegUserId(userInfo.getUserId());
                }
            } else {
                //注册企业
                Long userId = userRegisterService.registryByCorpAdmin(corpRegistryDto.getMobile(), corpRegistryDto.getRegUserName());
                corpRegistryDto.setRegUserId(userId);
            }
        } else {
            corpRegistryDto.setRegUserId(curUserId);
        }
        if (this.ifNameExists(corpRegistryDto.getCorpName())) {
            throw new AppException("该企业名称已存在！");
        }
        if (StrUtil.isNotBlank(corpRegistryDto.getDistrict())) {
            if (corpRegistryDto.getDistrict().length() >= 2) {
                corpRegistryDto.setProvince(corpRegistryDto.getDistrict().substring(0, 2));
            }
            if (corpRegistryDto.getDistrict().length() >= 4) {
                corpRegistryDto.setCity(corpRegistryDto.getDistrict().substring(0, 4));
            }
            if (corpRegistryDto.getDistrict().length() <= 4) {
                corpRegistryDto.setDistrict(null);
            }
        }
        CorpRegistry corpRegistry = new CorpRegistry();
        BeanUtils.copyProperties(corpRegistryDto, corpRegistry);
        if (LongUtil.isNotZero(corpRegistryDto.getOwnerUserId())) {
            corpRegistry.setOwnerUserId(corpRegistryDto.getOwnerUserId());
        }

        corpRegistry.setRegTime(DateUtil.date());
        //支付宝账号默认为空
        corpRegistry.setAliPayLogonId("");
        //生成公司编号
        Long corpId = KeyUtil.getId();
        corpRegistry.setCorpId(corpId);
       /* String safePasswd = userSafeService.getSavePasswd(corpRegistry.getPasswd(), corpRegistryDto.getPublicKey(), corpRegistry.getCorpId());*/
        /*corpRegistry.setPasswd(safePasswd);*/
        this.save(corpRegistry);
        //生成操作记录
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_REGISTRY, corpRegistry.getCorpId(), reqParam, curUserId, corpRegistryDto.getClientId());
        //注册用户加入到企业用户表和企业管理员表
        corpUserService.addCorpUser(corpRegistry.getRegUserId(), corpRegistry.getCorpId(), corpRegistryDto.getRegUserName());
//        corpAdminService.addCorpAdmin(corpRegistry.getRegUserId(), corpRegistry.getCorpId());
        //所有者用户加入到企业用户表和企业管理员表
        if (corpRegistry.getOwnerUserId() != null) {
            //暂时没有企业所有者，姓名暂时置为空
            corpUserService.addCorpUser(corpRegistry.getOwnerUserId(), corpRegistry.getCorpId(), "");
//            corpAdminService.addCorpAdmin(corpRegistry.getOwnerUserId(), corpRegistry.getCorpId());
        }
        // 修改用户认证的姓名
        if ("usp-mobile".equals(corpRegistryDto.getClientId())) {
            UserReal userReal = new UserReal();
            userReal.setUserId(curUserId);
            userReal.setUserName(corpRegistryDto.getRegUserName());
            userRealService.saveOrUpdate(userReal);
        }
        // 添加系统角色
        sysRoleCompoService.addSysRole(corpRegistry.getCorpId(), corpRegistry.getRegUserId());
        // 删除临时文件表数据
        if (LongUtil.isNotZero(corpRegistry.getLogoImg())) {
            this.fileFeignService.deleteFileTemporaryByID(corpRegistry.getLogoImg());
        }
        return corpId;
    }

    /**
     * 添加企业
     *
     * @param corpRegistry
     * @return
     * @author zgpi
     * @date 2020/1/16 10:54
     **/
    @Override
    public Long addCorp(CorpRegistry corpRegistry) {
        if (this.ifNameExists(corpRegistry.getCorpName())) {
            throw new AppException("该企业名称已存在！");
        }
        corpRegistry.setRegTime(DateUtil.date());
        //生成公司编号
        corpRegistry.setCorpId(KeyUtil.getId());
        this.save(corpRegistry);
        return corpRegistry.getCorpId();
    }

    @Override
    public CorpDto queryCorpInfo(Long corpId, Long curUserId) {
        Map<String, String> regionMap = cfgAreaService.selectAreaMap();
        CorpRegistry corpRegistry = this.getById(corpId);
        CorpVerify corpVerify = corpVerifyService.getById(corpId);
        CorpVerifyApp corpVerifyApp = corpVerifyAppService.selectApplying(corpId);
        SysTenant sysTenant =sysTenantService.getById(corpId);
        CorpDto dto = new CorpDto();
        if (corpRegistry != null) {
            BeanUtils.copyProperties(corpRegistry, dto);
            String region = regionMap.get(dto.getProvince()) + regionMap.get(dto.getCity()) + regionMap.get(dto.getDistrict());
            dto.setRegion(region);
        }
        if (corpVerify != null) {
            BeanUtils.copyProperties(corpVerify, dto, "corpId", "txId", "province", "city", "address");
            dto.setVerifyStatus(true);
        }
        if (corpVerifyApp != null) {
            dto.setVerifyAppStatus(true);
        }
        List<Long> userIdList = new ArrayList<>();
        if(LongUtil.isNotZero(dto.getRegUserId())) {
            userIdList.add(dto.getRegUserId());
            Map<Long, String> userMap = userRealService.mapIdAndName(userIdList);
            if (CollectionUtil.isNotEmpty(userMap)) {
                dto.setRegUserName(userMap.get(dto.getRegUserId()));
            }
        }
        if(sysTenant != null){
            if(StringUtils.isNotEmpty(sysTenant.getServiceDemander())){
                dto.setServiceDemander(sysTenant.getServiceDemander());
            }
            if(StringUtils.isNotEmpty(sysTenant.getServiceProvider())){
                dto.setServiceProvider(sysTenant.getServiceProvider());
            }
            if(StringUtils.isNotEmpty(sysTenant.getDeviceUser())){
                dto.setDeviceUser(sysTenant.getDeviceUser());
            }
        }
        dto.areaName(regionMap.get(dto.getProvince()), regionMap.get(dto.getCity()), regionMap.get(dto.getDistrict()));
        return dto;
    }

    @Override
    public void changePassword(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId, String clientId) throws Exception {
        if (corpRegistryDto == null || corpRegistryDto.getCorpId() == null || StringUtil.isNullOrEmpty(corpRegistryDto.getPasswd())) {
            throw new AppException("参数解析错误！");
        }
        CorpRegistry corpRegistryOld = this.getById(corpRegistryDto.getCorpId());
        String passwd = decipherPasswd(corpRegistryDto.getPasswd(), corpRegistryDto.getPublicKey());
        if (corpRegistryOld == null) {
            throw new AppException("出现异常！");
        }
        if (!StringUtil.isNullOrEmpty(passwd) && this.passwordEncoder.matches(passwd, corpRegistryOld.getPasswd())) {
            String safeNewPasswd = userSafeService.getSavePasswd(corpRegistryDto.getNewPasswd(), corpRegistryDto.getPublicKey(), corpRegistryDto.getCorpId());
            corpRegistryOld.setPasswd(safeNewPasswd);
            this.updateById(corpRegistryOld);
        } else {
            throw new AppException("旧密码输入错误！");
        }
        corpRegistryOld.updateById();
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_PASSWORD, corpRegistryOld.getCorpId(), reqParam, curUserId, clientId);
    }

    @Override
    public void updateCorpReg(CorpRegistryDto corpRegistryDto, ReqParam reqParam, Long curUserId, String clientId) {
        if (corpRegistryDto == null || corpRegistryDto.getCorpId() == null) {
            throw new AppException("参数解析错误！");
        }
        CorpRegistry corpRegistryOld = this.getById(corpRegistryDto.getCorpId());
        if (corpRegistryOld == null) {
            throw new AppException("企业不存在！");
        }
        if (corpRegistryDto.isMyCorp()) {
            if (!curUserId.equals(corpRegistryOld.getRegUserId())) {
                throw new AppException("只允许修改自己注册的企业！");
            }
        }
        Long oldLogo = corpRegistryOld.getLogoImg();
        if (StrUtil.isBlank(corpRegistryOld.getDistrict())) {
            corpRegistryOld.setDistrict("");
        }
        if (StrUtil.isBlank(corpRegistryOld.getCity())) {
            corpRegistryOld.setCity("");
        }
        if (StrUtil.isBlank(corpRegistryOld.getProvince())) {
            corpRegistryOld.setProvince("");
        }
        BeanUtils.copyProperties(corpRegistryDto, corpRegistryOld,
                "corpId", "txId", "passwd", "aliPayLogonId", "appId");
        /*Date time = corpRegistryOld.getRegTime();*/
        this.updateById(corpRegistryOld);
        corpOperationService.addCorpOperation(CorpOperationEnum.CO_UPDATE_REGISTRY, corpRegistryOld.getCorpId(),
                reqParam, curUserId, clientId);
        // 删除旧文件
        if (LongUtil.isNotZero(oldLogo) && (LongUtil.isZero(corpRegistryOld.getLogoImg()) ||
                !oldLogo.equals(corpRegistryOld.getLogoImg()))) {
            fileFeignService.delFile(oldLogo);
        }
        // 删除临时文件表数据
        if (LongUtil.isNotZero(corpRegistryDto.getLogoImg())) {
            fileFeignService.deleteFileTemporaryByID(corpRegistryDto.getLogoImg());
        }
    }

    @Override
    public List<CorpDto> listCorpByFilter(CorpRegistryFilter corpRegistryFilter, Long curUserId) {
        List<CorpRegistry> corpRegistries = this.listCorpRegByFilter(corpRegistryFilter);
        Map<String, String> regionMap = cfgAreaService.selectAreaMap();
        Map<Long, CorpUser> corpUserMap = corpUserService.mapCorpIdByUser(curUserId);
        List<CorpDto> dtoList = new ArrayList<>();
        if (corpRegistries != null && corpRegistries.size() > 0) {
            for (CorpRegistry corpRegistry : corpRegistries) {
                CorpDto dto = new CorpDto();
                BeanUtils.copyProperties(corpRegistry, dto);
                dto.areaName(regionMap.get(dto.getProvince()), regionMap.get(dto.getCity()), regionMap.get(dto.getDistrict()));
                if (corpUserMap.get(corpRegistry.getCorpId()) != null) {
                    dto.setInCorp(true);
                }
                dtoList.add(dto);
            }
        }
        return dtoList;
    }

    @Override
    public List<StaffScopeDto> listStaffScope() {
        Map<Integer, String> map = StaffScopeEnum.map;
        List<StaffScopeDto> list = new ArrayList<>();
        map.forEach((key, value) -> {
            StaffScopeDto staffScopeDto = new StaffScopeDto();
            staffScopeDto.setCode(key);
            staffScopeDto.setName(value);
            list.add(staffScopeDto);
        });
        return list;
    }

    @Override
    public boolean ifNameExists(String corpName) {
        Map<String, Object> colMap = new HashMap<>();
        colMap.put("corpName", corpName);
        List<CorpRegistry> corpRegistryList = (List<CorpRegistry>) listByMap(colMap);
        return corpRegistryList != null && corpRegistryList.size() > 0;
    }

    @Override
    public Map<Long, CorpRegistry> mapCorpIdAndRegistry(List<Long> corpIdList) {
        List<CorpRegistry> list = (List<CorpRegistry>) listByIds(corpIdList);
        Map<Long, CorpRegistry> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (CorpRegistry registry : list) {
                map.put(registry.getCorpId(), registry);
            }
        }
        return map;
    }

    /**
     * 获得企业ID与简称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2020/6/3 20:08
     **/
    @Override
    public Map<Long, String> mapCorpIdAndShortName(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (corpIdList == null || corpIdList.size() == 0) {
            return map;
        }
        List<CorpRegistry> list = (List<CorpRegistry>) listByIds(corpIdList);
        if (list != null && list.size() > 0) {
            for (CorpRegistry registry : list) {
                map.put(registry.getCorpId(), registry.getShortName());
            }
        }
        return map;
    }

    @Override
    public String decipherPasswd(String encryptedPw, String publicKey) {
        com.baomidou.mybatisplus.core.toolkit.Assert.notEmpty(encryptedPw, "encryptedPw 不能为空");
        Assert.notEmpty(publicKey, "publicKey 不能为空");
        Object privateKey = redisRepository.get(publicKey);
        if (privateKey == null) {
            throw new AppException("响应超时，公钥失效，请重试！");
        }
        String passwd = RsaUtil.decipher(encryptedPw, (String) privateKey);
        return passwd;
    }

    @Override
    public Map<Long, String> getCorpNameList(List<Long> corpIdList) {
        List<CorpRegistry> list = (List<CorpRegistry>) listByIds(corpIdList);
        Map<Long, String> map = new HashMap<>();
        if (list != null && list.size() > 0) {
            for (CorpRegistry registry : list) {
                map.put(registry.getCorpId(), registry.getShortName());
            }
        }
        return map;
    }


    @Override
    public List<Long> listCorpIdByFilter(CorpRegistryFilter corpRegistryFilter) {
        List<CorpRegistry> corpRegistries = this.listCorpRegByFilter(corpRegistryFilter);
        List<Long> corpIds = new ArrayList<>();
        if (corpRegistries != null && corpRegistries.size() > 0) {
            for (CorpRegistry corpRegistry : corpRegistries) {
                corpIds.add(corpRegistry.getCorpId());
            }
        }
        return corpIds;
    }

    private List<CorpRegistry> listCorpRegByFilter(CorpRegistryFilter corpRegistryFilter) {
        QueryWrapper<CorpRegistry> queryWrapper = new QueryWrapper<>();
        if (corpRegistryFilter != null) {
            if (StrUtil.isNotBlank(corpRegistryFilter.getCorpName())) {
                queryWrapper.like("corpname", corpRegistryFilter.getCorpName());
            }
            if (StrUtil.isNotBlank(corpRegistryFilter.getProvince())) {
                queryWrapper.eq("province", corpRegistryFilter.getProvince());
            }
            if (StrUtil.isNotBlank(corpRegistryFilter.getCity())) {
                queryWrapper.eq("city", corpRegistryFilter.getCity());
            }
            if (StrUtil.isNotBlank(corpRegistryFilter.getDistrict())) {
                queryWrapper.eq("district", corpRegistryFilter.getDistrict());
            }
            if (StrUtil.isNotBlank(corpRegistryFilter.getMobileFilter())) {
                queryWrapper.and(wrapper -> wrapper.like("address", corpRegistryFilter.getMobileFilter().trim())
                        .or().like("telephone", corpRegistryFilter.getMobileFilter().trim())
                        .or().like("corpname", corpRegistryFilter.getMobileFilter().trim()));
            }
            if (LongUtil.isNotZero(corpRegistryFilter.getUserId())) {
                queryWrapper.inSql("corpid", "select corpid from uas_corp_user where userid=" + corpRegistryFilter.getUserId());
            }
        }
        return this.list(queryWrapper);
    }


    @Override
    public List<CorpDto> listCorpByIdList(List<Long> corpIdList) {
        List<CorpDto> dtoList = new ArrayList<>();
        if (corpIdList == null || corpIdList.size() <= 0) {
            return dtoList;
        }
        List<CorpDto> corpDtoList = this.baseMapper.selectCorpWithRegUserName(corpIdList);
        Map<String, String> areaMap = this.cfgAreaService.selectAreaMap();
        if (corpDtoList != null && corpDtoList.size() >= 0) {
            for (CorpDto corpDto : corpDtoList) {
                String region = "";
                if (!StringUtil.isNullOrEmpty(corpDto.getProvince())) {
                    region += areaMap.get(corpDto.getProvince()) == null ? "" : areaMap.get(corpDto.getProvince());
                }
                if (!StringUtil.isNullOrEmpty(corpDto.getCity())) {
                    region += areaMap.get(corpDto.getCity()) == null ? "" : areaMap.get(corpDto.getCity());
                }
                if (!StringUtil.isNullOrEmpty(corpDto.getDistrict())) {
                    region += areaMap.get(corpDto.getDistrict()) == null ? "" : areaMap.get(corpDto.getDistrict());
                }
                corpDto.setRegion(region);
                dtoList.add(corpDto);
            }
        }
        return dtoList;
    }

    @Override
    public List<CorpNameDto> listCorpNameMapByUserId(Long userId) {
        if (userId == null || userId == 0L) {
            return new ArrayList<>();
        }
        return this.baseMapper.listCorpNameMapByUserId(userId);
    }

    /**
     * 模糊查询企业
     *
     * @param corpRegistryFilter
     * @return
     * @author zgpi
     * @date 2019/11/19 11:02
     **/
    @Override
    public List<CorpRegistry> matchCorp(CorpRegistryFilter corpRegistryFilter) {
        return this.baseMapper.matchCorp(corpRegistryFilter);
    }

    @Override
    public CorpDto getCorpInfoWithAddress(Long corpId) {
        CorpRegistry corpRegistry = this.getById(corpId);
        CorpDto dto = new CorpDto();
        if (corpRegistry != null) {
            Map<String, String> regionMap = cfgAreaService.selectAreaMap();
            if (corpRegistry != null) {
                BeanUtils.copyProperties(corpRegistry, dto);
                String region = regionMap.get(dto.getProvince()) + regionMap.get(dto.getCity()) + regionMap.get(dto.getDistrict());
                region = region.replace("null", "");
                dto.setRegion(region);
            }
        }
        return dto;
    }

    @Override
    public List<CorpRegistry> getCorpByName(String corpName) {
        QueryWrapper<CorpRegistry> wrapper = new QueryWrapper<>();
        wrapper.eq("corpname", corpName);
        return this.baseMapper.selectList(wrapper);
    }

    @Override
    public Long feignCreateDemander(String jsonString) {
        JSONObject jsonObj = JSONObject.parseObject(jsonString);
        CorpRegistryDto corpRegistryDto = JSONObject
                .toJavaObject((JSON) jsonObj.get("registry"), CorpRegistryDto.class);
        Long curUserId = (Long) jsonObj.get("curUserId");
        ReqParam reqParam = JSONObject
                .toJavaObject((JSON) jsonObj.get("reqParams"), ReqParam.class);
        Long corpId = this.corpRegistry(corpRegistryDto, reqParam, curUserId);
        SysTenant sysTenant = new SysTenant();
        sysTenant.setCorpId(corpId);
        sysTenant.setServiceDemander("Y");
        sysTenant.setDemanderLevel(TenantLevelEnum.ORDINARY.getCode());
        sysTenant.setServiceProvider("N");
        sysTenant.setDeviceUser("N");
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(curUserId);
        sysTenantService.addSysTenant(sysTenant, userInfo);
        return corpId;
    }

    @Override
    public CorpDto getCorpDetailWithRegUserNameById(Long corpId) {
        if(LongUtil.isZero(corpId)) {
            throw new AppException("企业编号不能为空");
        }
        CorpDto corpDto = this.baseMapper.selectCorpWithRegUserNameById(corpId);
        if(corpDto != null) {

            Map<String, String> regionMap = cfgAreaService.selectAreaMap();
            if (corpDto != null) {
                String region = regionMap.get(corpDto.getProvince()) + regionMap.get(corpDto.getCity()) + regionMap.get(corpDto.getDistrict());
                region = region.replace("null", "");
                corpDto.setRegion(region);
            }
        }
        return  corpDto;
    }

    /**
     * 根据corpId查询企业
     * @date 2020/6/11
     * @param corpId
     * @return com.zjft.usp.uas.corp.model.CorpRegistry
     */
    @Override
    public CorpRegistry getCorpInfoById(Long corpId) {
        return this.baseMapper.selectById(corpId);
    }

    /**
     * 自动认证
     * @date 2020/6/14
     * @param corpId
     * @return void
     */
    @Override
    public void addVerifyByCorpId(Long corpId) {
        //添加租户，自动认证
        CorpRegistry corpRegistry = corpRegistryService.getCorpInfoById(corpId);
        corpRegistry.setVerify(1);
        corpRegistryService.saveOrUpdate(corpRegistry);
        //添加自动认证信息
        corpVerifyService.createVerifyByCorpId(corpId);
    }
    @Override
    public String getMaxCorpCode() {
        return this.baseMapper.selectMaxCorpCode();
    }

    @Override
    public void setMaxCorpCode(Long corpId) {
        QueryWrapper<CorpRegistry> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("corpcode").eq("corpid", corpId);
        CorpRegistry corpRegistry = this.baseMapper.selectOne(queryWrapper);
        // 查到有corpCode不进行更新
        if(StrUtil.isNotEmpty(corpRegistry.getCorpCode())) {
            return;
        }
        String maxCorpCode = this.getMaxCorpCode();
        Integer integer = Integer.parseInt(maxCorpCode);
        integer = integer + 1;
        UpdateWrapper<CorpRegistry> wrapper = new UpdateWrapper<>();
        wrapper.set("corpcode", String.valueOf(integer))
                .eq("corpid", corpId);
        this.update(wrapper);
    }

    /**
     * 根据用户id查询企业信息和用户权限
     * @param userId
     * @author xpwu
     * @return
     */
    @Override
    public List<CorpRoleDto> listCorpRoleByUserId(Long userId){
        List<CorpRoleDto> corpRoleDtoList = this.baseMapper.listCorpRoleByUserId(userId);
        List<SysRole> sysRoleList = sysRoleCompoService.listUserRole(userId);
        Map<String, String> areaMap = cfgAreaService.selectAreaMap();
        String provinceName,cityName,districtName;
        if(CollectionUtil.isNotEmpty(corpRoleDtoList)) {
            for (CorpRoleDto corpRoleDto : corpRoleDtoList) {
                provinceName = StrUtil.trimToEmpty(areaMap.get(StrUtil.trimToEmpty(corpRoleDto.getProvince())));
                cityName = StrUtil.trimToEmpty(areaMap.get(StrUtil.trimToEmpty(corpRoleDto.getCity())));
                districtName = StrUtil.trimToEmpty(areaMap.get(StrUtil.trimToEmpty(corpRoleDto.getDistrict())));
                Long corpId = corpRoleDto.getCorpId();
                ArrayList<SysRole> roles = new ArrayList<>();
                for (SysRole sysRole : sysRoleList) {
                    if(corpId.equals(sysRole.getCorpId())){
                        roles.add(sysRole);
                    }
                }
                provinceName = provinceName.replace("省", "").replace("自治区", "").replace("特别行政区", "")
                        .replace("回族", "").replace("壮族", "").replace("维吾尔族", "");
                if(districtName.equals(cityName)){
                   districtName = "";
                }
                if (!cityName.equals(provinceName + "市")) {
                    cityName = provinceName + cityName;
                }
                districtName = cityName + districtName;
                corpRoleDto.setRegion(districtName);
                corpRoleDto.setSysRoleList(roles);
            }
            return corpRoleDtoList;
        }
        return null;
    }

    /**
     * 获取企业系统编号与企业编号的映射
     *
     * @param corpIdList
     * @return
     */
    @Override
    public Map<Long, String> mapCorpIdAndCode(List<Long> corpIdList) {
        Map<Long, String> map = new HashMap<>();
        if (CollectionUtil.isEmpty(corpIdList)) {
            return map;
        }
        List<CorpRegistry> list = this.list(new QueryWrapper<CorpRegistry>().in("corpid", corpIdList));
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.toMap(CorpRegistry::getCorpId, CorpRegistry::getCorpCode));
        }
        return map;
    }

}
