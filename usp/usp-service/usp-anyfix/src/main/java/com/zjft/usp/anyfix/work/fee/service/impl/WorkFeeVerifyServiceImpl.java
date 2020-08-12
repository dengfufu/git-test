package com.zjft.usp.anyfix.work.fee.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.AreaDto;
import com.zjft.usp.anyfix.common.service.RightCompoService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderAutoConfigDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderAutoConfigService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderContService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.settle.enums.SettleTypeEnum;
import com.zjft.usp.anyfix.settle.enums.WorkSettleStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeCheckStatusEnum;
import com.zjft.usp.anyfix.work.check.enums.FeeConfirmStatusEnum;
import com.zjft.usp.anyfix.work.check.model.WorkCheck;
import com.zjft.usp.anyfix.work.check.service.WorkCheckService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDetailExcelDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.enums.WorkFeeVerifySettleStatusEnum;
import com.zjft.usp.anyfix.work.fee.enums.WorkFeeVerifyStatusEnum;
import com.zjft.usp.anyfix.work.fee.filter.WorkFeeVerifyFilter;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeMapper;
import com.zjft.usp.anyfix.work.fee.mapper.WorkFeeVerifyMapper;
import com.zjft.usp.anyfix.work.fee.model.WorkFee;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeDetail;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ExcelCodeMsg;
import com.zjft.usp.anyfix.work.request.enums.WarrantyModeEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.security.Key;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 委托商对账单表 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-05-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WorkFeeVerifyServiceImpl extends ServiceImpl<WorkFeeVerifyMapper, WorkFeeVerify> implements WorkFeeVerifyService {

    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private WorkFeeVerifyDetailService workFeeVerifyDetailService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkCheckService workCheckService;
    @Autowired
    private RightCompoService rightCompoService;
    @Autowired
    private DemanderServiceManagerService demanderServiceManagerService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private DemanderAutoConfigService demanderAutoConfigService;
    @Autowired
    private DemanderContService demanderContService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private WorkFeeMapper workFeeMapper;
    // 对账单号序号
    private static final String VERIFY_NAME_NO = "00";

    /**
     * 分页查询
     *
     * @param workFeeVerifyFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeVerifyDto> query(WorkFeeVerifyFilter workFeeVerifyFilter, Long userId, Long corpId) {
        ListWrapper<WorkFeeVerifyDto> listWrapper = new ListWrapper<>();
        if (workFeeVerifyFilter == null || LongUtil.isZero(workFeeVerifyFilter.getCurrentCorp())) {
            return listWrapper;
        }
        // 如果是客户经理，则只能查看客户经理范围内委托商的对账单
        if (rightCompoService.hasRight(corpId, userId, RightConstants.WORK_FEE_VERIFY_MANAGER)
            && !rightCompoService.hasRight(corpId, userId, RightConstants.WORK_FEE_VERIFY_QUERY)) {
            List<Long> demanderCorpList = this.demanderServiceManagerService.listDemanderCorpByManager(corpId, userId);
            if (CollectionUtil.isEmpty(demanderCorpList)) {
                return listWrapper;
            }
            workFeeVerifyFilter.setDemanderCorpList(demanderCorpList);
        }
        Page page = new Page(workFeeVerifyFilter.getPageNum(), workFeeVerifyFilter.getPageSize());
        List<WorkFeeVerifyDto> list = this.baseMapper.pageByFilter(page, workFeeVerifyFilter);
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> corpSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();
            List<Long> verifyIdList = new ArrayList<>();
            list.forEach(workFeeVerifyDto -> {
                corpSet.add(workFeeVerifyDto.getServiceCorp());
                corpSet.add(workFeeVerifyDto.getDemanderCorp());
                userIdSet.add(workFeeVerifyDto.getAddUser());
                verifyIdList.add(workFeeVerifyDto.getVerifyId());
            });
            List<Long> corpIdList = new ArrayList<>(corpSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            Map<Long, BigDecimal> verifyIdAndFeeMap = this.mapVerifyIdAndWorkTotalFee(verifyIdList);
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && corpMapResult.getCode() == 0) {
                corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
            }
            Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userMap = new HashMap<>();
            if (userMapResult != null && userMapResult.getCode() == 0) {
                userMap = userMapResult.getData() == null ? new HashMap<>() : userMapResult.getData();
            }
            for (WorkFeeVerifyDto workFeeVerifyDto : list) {
                workFeeVerifyDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getDemanderCorp())));
                workFeeVerifyDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getServiceCorp())));
                workFeeVerifyDto.setAddUserName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDto.getAddUser())));
                workFeeVerifyDto.setStatusName(WorkFeeVerifyStatusEnum.lookup(workFeeVerifyDto.getStatus()));
                workFeeVerifyDto.setSettleStatusName(WorkFeeVerifySettleStatusEnum.lookup(workFeeVerifyDto.getSettleStatus()));
                if (WorkFeeVerifyStatusEnum.TO_VERIFY.getCode().equals(workFeeVerifyDto.getStatus()) ||
                        WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode().equals(workFeeVerifyDto.getStatus())) {
                    workFeeVerifyDto.setTotalAmount(verifyIdAndFeeMap.get(workFeeVerifyDto.getVerifyId()));
                }
            }
        }
        listWrapper.setList(list);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 查询可对账工单
     *
     * @param workFilter
     * @return
     */
    @Override
    public WorkFeeVerifyDto listCanVerifyWork(WorkFilter workFilter) {
        if (workFilter == null) {
            throw new AppException("参数不能为空");
        }
        if (LongUtil.isZero(workFilter.getServiceCorp())) {
            throw new AppException("服务商不能为空");
        }
        if (LongUtil.isZero(workFilter.getDemanderCorp())) {
            throw new AppException("委托商不能为空");
        }
        if (workFilter.getStartDate() == null || workFilter.getEndDate() == null) {
            throw new AppException("开始、截止日期不能为空");
        }
        WorkFeeVerifyDto workFeeVerifyDto = new WorkFeeVerifyDto();
        List<WorkFeeDto> list = this.workFeeService.listByWorkFilter(workFilter);
        int confirmNum = 0;
        int checkNum = 0;
        if (CollectionUtil.isNotEmpty(list)) {
            for (WorkFeeDto workFeeDto : list) {
                if (workFeeDto.getFeeConfirmStatus().equals(FeeConfirmStatusEnum.CONFIRM_PASS.getCode())) {
                    confirmNum++;
                }
                if (FeeCheckStatusEnum.CHECK_PASS.getCode().equals(workFeeDto.getFeeCheckStatus())) {
                    checkNum++;
                }
            }
        }
        workFeeVerifyDto.setWorkFeeDtoList(list);
        workFeeVerifyDto.setConfirmNum(confirmNum);
        workFeeVerifyDto.setCheckNum(checkNum);
        workFeeVerifyDto.setWorkQuantity(CollectionUtil.isEmpty(list) ? 0 : list.size());
        return workFeeVerifyDto;
    }

    /**
     * 添加对账单
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    @Override
    public void add(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId) {
        String errorMsg = this.checkData(workFeeVerifyDto);
        if (errorMsg.length() > 0) {
            throw new AppException(errorMsg);
        }
        List<WorkFeeDto> workFeeDtoList = workFeeVerifyDto.getWorkFeeDtoList();
        if (CollectionUtil.isEmpty(workFeeDtoList)) {
            throw new AppException("请选择对账工单");
        }
        Long verifyId = KeyUtil.getId();
        List<WorkFeeVerifyDetail> detailList = new ArrayList<>();
        List<WorkDeal> workDealList = new ArrayList<>();
        for (WorkFeeDto workFeeDto : workFeeDtoList) {
            WorkFeeVerifyDetail detail = new WorkFeeVerifyDetail();
            detail.setDetailId(KeyUtil.getId());
            detail.setVerifyId(verifyId);
            detail.setWorkId(workFeeDto.getWorkId());
            detail.setVerifyAmount(workFeeDto.getTotalFee());
            detailList.add(detail);
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workFeeDto.getWorkId());
            workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_VERIFY.getCode());
            workDealList.add(workDeal);
        }
        WorkFeeVerify workFeeVerify = new WorkFeeVerify();
        BeanUtils.copyProperties(workFeeVerifyDto, workFeeVerify);
        // 生成对账单号
        Result corpDtoResult = this.uasFeignService.findCorpById(workFeeVerifyDto.getDemanderCorp());
        String prefix = "";
        if (corpDtoResult != null && Result.SUCCESS == corpDtoResult.getCode().intValue()) {
            CorpDto corpDto = JsonUtil.parseObject(JsonUtil.toJson(corpDtoResult.getData()), CorpDto.class);
            if (corpDto == null) {
                throw new AppException("获取委托商信息失败，请重试");
            } else {
                prefix = corpDto.getCorpCode();
            }
        } else {
            throw new AppException("获取委托商信息失败, 请重试");
        }
        prefix += "-" + DateUtil.format(workFeeVerifyDto.getEndDate(), "yyMM") + "-";
        workFeeVerify.setVerifyName(this.createVerifyName(prefix));
        // 先将对账单总费用以及对账后总费用置为0，目的是避免工单费用变更后需同步更新对账单费用
        workFeeVerify.setTotalAmount(BigDecimal.ZERO);
        workFeeVerify.setVerifyAmount(BigDecimal.ZERO);
        workFeeVerify.setVerifyId(verifyId);
        workFeeVerify.setAddUser(userId);
        workFeeVerify.setAddTime(DateTime.now());
        workFeeVerify.setWorkQuantity(detailList.size());
        workFeeVerify.setStatus(WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode());
        this.save(workFeeVerify);
        this.workFeeVerifyDetailService.saveBatch(detailList);
        // 更新工单结算状态
        this.workDealService.updateBatchById(workDealList);
    }

    /**
     * 更新对账单
     *
     * @param workFeeVerifyDto
     * @param userId
     */
    @Override
    public void update(WorkFeeVerifyDto workFeeVerifyDto, Long userId) {
        if (workFeeVerifyDto == null || LongUtil.isZero(workFeeVerifyDto.getVerifyId())) {
            throw new AppException("对账单不存在，请检查！");
        }
        List<WorkFeeDto> workFeeDtoListNew = workFeeVerifyDto.getWorkFeeDtoList();
        if (CollectionUtil.isEmpty(workFeeDtoListNew)) {
            throw new AppException("请选择对账工单");
        }
        WorkFeeVerify workFeeVerifyOld = this.getById(workFeeVerifyDto.getVerifyId());
        if (workFeeVerifyOld == null) {
            throw new AppException("对账单不存在，请检查！");
        }
        if (!WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode().equals(workFeeVerifyOld.getStatus())) {
            throw new AppException("对账单状态为【" + WorkFeeVerifyStatusEnum.lookup(workFeeVerifyOld.getStatus()) + "】,不能修改");
        }
        List<WorkFeeVerifyDetail> detailList = this.workFeeVerifyDetailService.listByVerifyId(workFeeVerifyDto.getVerifyId());
        // 先删除明细，再新增
        this.workFeeVerifyDetailService.deleteByVerifyId(workFeeVerifyDto.getVerifyId());
        if (CollectionUtil.isNotEmpty(detailList)) {
            List<WorkDeal> workDealListOld = new ArrayList<>();
            detailList.forEach(workFeeVerifyDetail -> {
                WorkDeal workDeal = new WorkDeal();
                workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_VERIFY.getCode());
                workDealListOld.add(workDeal);
            });
            // 更新已删除的工单结算状态
            this.workDealService.updateBatchById(workDealListOld);
        }
        List<WorkFeeVerifyDetail> detailListNew = new ArrayList<>();
        List<WorkDeal> workDealListNew = new ArrayList<>();
        for (WorkFeeDto workFeeDto : workFeeDtoListNew) {
            WorkDeal workDeal = new WorkDeal();
            workDeal.setWorkId(workFeeDto.getWorkId());
            workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_VERIFY.getCode());
            workDealListNew.add(workDeal);
            WorkFeeVerifyDetail workFeeVerifyDetail = new WorkFeeVerifyDetail();
            workFeeVerifyDetail.setDetailId(KeyUtil.getId());
            workFeeVerifyDetail.setWorkId(workFeeDto.getWorkId());
            workFeeVerifyDetail.setVerifyId(workFeeVerifyDto.getVerifyId());
            workFeeVerifyDetail.setVerifyAmount(workFeeDto.getTotalFee());
            detailListNew.add(workFeeVerifyDetail);
        }
        // 添加新明细
        this.workFeeVerifyDetailService.saveBatch(detailListNew);
        // 更新新添加的工单结算状态
        this.workDealService.updateBatchById(workDealListNew);
        BeanUtils.copyProperties(workFeeVerifyDto, workFeeVerifyOld);
        workFeeVerifyOld.setWorkQuantity(workFeeDtoListNew.size());
        workFeeVerifyOld.setTotalAmount(BigDecimal.ZERO);
        workFeeVerifyOld.setVerifyAmount(BigDecimal.ZERO);
        workFeeVerifyOld.setAddUser(userId);
        workFeeVerifyOld.setAddTime(DateUtil.date());
        this.updateById(workFeeVerifyOld);
    }

    /**
     * 提交对账
     *
     * @param workFeeVerifyDto
     * @param userId
     */
    @Override
    public void submit(WorkFeeVerifyDto workFeeVerifyDto, Long userId) {
        if (workFeeVerifyDto == null || LongUtil.isZero(workFeeVerifyDto.getVerifyId())) {
            throw new AppException("对账单编号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.getById(workFeeVerifyDto.getVerifyId());
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        if (!WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode().equals(workFeeVerify.getStatus())) {
            throw new AppException("对账单状态不是【待提交】，不能提交对账");
        }
        List<WorkFeeVerifyDetailDto> detailDtoList = this.workFeeVerifyDetailService.listDtoByVerifyId(workFeeVerifyDto.getVerifyId());
        List<WorkFeeVerifyDetail> updateDetailList = new ArrayList<>();
        // 未审核工单数量
        int uncheckNum = 0;
        for (WorkFeeVerifyDetailDto workFeeVerifyDetailDto : detailDtoList) {
            if (!FeeCheckStatusEnum.CHECK_PASS.getCode().equals(workFeeVerifyDetailDto.getFeeCheckStatus())) {
                uncheckNum++;
            }
            // 将对账后费用更新为和工单费用一致。
            if (!workFeeVerifyDetailDto.getTotalFee().equals(workFeeVerifyDetailDto.getVerifyAmount())) {
                WorkFeeVerifyDetail workFeeVerifyDetail = new WorkFeeVerifyDetail();
                workFeeVerifyDetail.setDetailId(workFeeVerifyDetailDto.getDetailId());
                workFeeVerifyDetail.setVerifyAmount(workFeeVerifyDetailDto.getTotalFee());
                updateDetailList.add(workFeeVerifyDetail);
            }
        }
        if (uncheckNum > 0) {
            throw new AppException("对账单还有 " + uncheckNum + " 工单 费用未审核，不能提交对账，请检查");
        }
        workFeeVerify.setStatus(WorkFeeVerifyStatusEnum.TO_VERIFY.getCode());
        workFeeVerify.setAddUser(userId);
        workFeeVerify.setAddTime(DateUtil.date());
        this.updateById(workFeeVerify);
        if (CollectionUtil.isNotEmpty(updateDetailList)) {
            this.workFeeVerifyDetailService.updateBatchById(updateDetailList);
        }
    }

    /**
     * 删除对账单
     *
     * @param verifyId
     * @param userId
     * @param corpId
     */
    @Override
    public void delete(Long verifyId, Long userId, Long corpId) {
        if (LongUtil.isZero(verifyId)) {
            throw new AppException("对账单不存在，请检查");
        }
        WorkFeeVerify workFeeVerify = this.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        if (!corpId.equals(workFeeVerify.getServiceCorp())) {
            throw new AppException("当前企业不是对账单的服务商，不能删除");
        }
        if (!WorkFeeVerifyStatusEnum.TO_VERIFY.getCode().equals(workFeeVerify.getStatus())
            && !WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode().equals(workFeeVerify.getStatus())) {
            throw new AppException("对账单状态不是待提交或待对账，不能删除");
        }
        // 更新工单处理表工单结算状态
        List<WorkDeal> workDealList = this.workFeeVerifyDetailService.listWorkDealByVerifyId(verifyId);
        if (CollectionUtil.isNotEmpty(workDealList)) {
            workDealList.forEach(workDeal -> {
                workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_VERIFY.getCode());
            });
            this.workDealService.updateBatchById(workDealList);
        }
        // 删除对账明细
        this.workFeeVerifyDetailService.deleteByVerifyId(verifyId);
        this.removeById(verifyId);
    }

    /**
     * 获取对账单详情
     *
     * @param verifyId
     * @return
     */
    @Override
    public WorkFeeVerifyDto findDetail(Long verifyId) {
        if (LongUtil.isZero(verifyId)) {
            return null;
        }
        WorkFeeVerify workFeeVerify = this.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        WorkFeeVerifyDto workFeeVerifyDto = new WorkFeeVerifyDto();
        BeanUtils.copyProperties(workFeeVerify, workFeeVerifyDto);
        List<Long> corpIdList = new ArrayList<>();
        List<Long> userIdList = new ArrayList<>();
        corpIdList.add(workFeeVerify.getServiceCorp());
        corpIdList.add(workFeeVerify.getDemanderCorp());
        userIdList.add(workFeeVerify.getAddUser());
        userIdList.add(workFeeVerify.getCheckUser());
        userIdList.add(workFeeVerify.getConfirmUser());
        Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpMap = new HashMap<>();
        if (corpMapResult != null && corpMapResult.getCode() == 0) {
            corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
        }
        Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
        Map<Long, String> userMap = new HashMap<>();
        if (userMapResult != null && userMapResult.getCode() == 0) {
            userMap = userMapResult.getData() == null ? new HashMap<>() : userMapResult.getData();
        }
        // 远程调用获得地区映射
        AreaDto areaDto = null;
        if (StrUtil.isNotBlank(workFeeVerifyDto.getDistrict())) {
            Result areaResult = uasFeignService.findAreaByCode(workFeeVerifyDto.getDistrict());
            if (areaResult.getCode() == Result.SUCCESS) {
                areaDto = JsonUtil.parseObject(JsonUtil.toJson(areaResult.getData()), AreaDto.class);
            }
        }
        if (areaDto != null) {
            workFeeVerifyDto.setDistrictName(areaDto.getAreaName());
        }
        // 获取委托协议号
        DemanderContFilter demanderContFilter = new DemanderContFilter();
        BeanUtils.copyProperties(workFeeVerify, demanderContFilter);
        DemanderContDto demanderContDto = demanderContService.queryCont(demanderContFilter);
        if (demanderContDto != null) {
            workFeeVerifyDto.setContNo(demanderContDto.getContNo());
            workFeeVerifyDto.setContId(demanderContDto.getId());
        }
        workFeeVerifyDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getServiceCorp())));
        workFeeVerifyDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getDemanderCorp())));
        workFeeVerifyDto.setAddUserName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDto.getAddUser())));
        workFeeVerifyDto.setCheckUserName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDto.getCheckUser())));
        workFeeVerifyDto.setConfirmUserName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDto.getConfirmUser())));
        workFeeVerifyDto.setStatusName(WorkFeeVerifyStatusEnum.lookup(workFeeVerify.getStatus()));
        workFeeVerifyDto.setSettleStatusName(WorkFeeVerifySettleStatusEnum.lookup(workFeeVerifyDto.getSettleStatus()));
        List<WorkFeeVerifyDetailDto> detailDtoList = this.workFeeVerifyDetailService.listDtoByVerifyId(verifyId);
        workFeeVerifyDto.setDetailDtoList(detailDtoList);
        int confirmNum = 0;
        int checkedNum = 0;
        List<Long> verifyIdList = new ArrayList<>();
        verifyIdList.add(workFeeVerify.getVerifyId());
        Map<Long, BigDecimal> verifyIdAndTotalFeeMap = this.mapVerifyIdAndWorkTotalFee(verifyIdList);
        if (CollectionUtil.isNotEmpty(detailDtoList)) {
            for (WorkFeeVerifyDetailDto workFeeVerifyDetailDto : detailDtoList) {
                if (FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workFeeVerifyDetailDto.getFeeConfirmStatus())) {
                    confirmNum++;
                }
                if (FeeCheckStatusEnum.CHECK_PASS.getCode().equals(workFeeVerifyDetailDto.getFeeCheckStatus())) {
                    checkedNum++;
                }
            }
        }
        workFeeVerifyDto.setConfirmNum(confirmNum);
        workFeeVerifyDto.setCheckNum(checkedNum);
        if (WorkFeeVerifyStatusEnum.TO_VERIFY.getCode().equals(workFeeVerifyDto.getStatus()) ||
                WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode().equals(workFeeVerifyDto.getStatus())) {
            workFeeVerifyDto.setTotalAmount(verifyIdAndTotalFeeMap.get(workFeeVerifyDto.getVerifyId()));
        }
        return workFeeVerifyDto;
    }

    /**
     * 对账
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    @Override
    public void verify(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId) {
        if (workFeeVerifyDto == null || LongUtil.isZero(workFeeVerifyDto.getVerifyId())) {
            throw new AppException("对账单编号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.getById(workFeeVerifyDto.getVerifyId());
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        if (!WorkFeeVerifyStatusEnum.TO_VERIFY.getCode().equals(workFeeVerify.getStatus()) &&
                !WorkFeeVerifyStatusEnum.REFUSE.getCode().equals(workFeeVerify.getStatus())) {
            throw new AppException("对账单状态为【" + WorkFeeVerifyStatusEnum.lookup(workFeeVerify.getStatus()) + "】，不能对账");
        }
        List<WorkFeeVerifyDetailDto> detailList = this.workFeeVerifyDetailService.listDtoByVerifyId(workFeeVerifyDto.getVerifyId());
        List<WorkFeeVerifyDetail> updateList = new ArrayList<>();
        BigDecimal verifyAmount = BigDecimal.ZERO;
        BigDecimal totalAmount = BigDecimal.ZERO;
        Map<Long, WorkFeeVerifyDetailDto> verifiedMap = workFeeVerifyDto.getVerifiedMap() == null ? new HashMap<>() : workFeeVerifyDto.getVerifiedMap();
        for (WorkFeeVerifyDetailDto workFeeVerifyDetailDto : detailList) {
            WorkFeeVerifyDetailDto verifyDetailDto = verifiedMap.get(workFeeVerifyDetailDto.getDetailId());
            if (verifyDetailDto != null) {
                WorkFeeVerifyDetail workFeeVerifyDetail = new WorkFeeVerifyDetail();
                workFeeVerifyDetail.setDetailId(workFeeVerifyDetailDto.getDetailId());
                workFeeVerifyDetail.setVerifyAmount(verifyDetailDto.getVerifyAmount());
                workFeeVerifyDetail.setNote(verifyDetailDto.getNote());
                updateList.add(workFeeVerifyDetail);
                verifyAmount = verifyAmount.add(workFeeVerifyDetail.getVerifyAmount());
            } else {
                verifyAmount = verifyAmount.add(workFeeVerifyDetailDto.getVerifyAmount());
            }
            totalAmount = totalAmount.add(workFeeVerifyDetailDto.getTotalFee());
        }
        WorkFeeVerify workFeeVerifyNew = new WorkFeeVerify();
        workFeeVerifyNew.setVerifyId(workFeeVerifyDto.getVerifyId());
        workFeeVerifyNew.setCheckUser(userId);
        workFeeVerifyNew.setCheckTime(DateTime.now());
        workFeeVerifyNew.setVerifyAmount(verifyAmount);
        workFeeVerifyNew.setTotalAmount(totalAmount);
        workFeeVerifyNew.setStatus(WorkFeeVerifyStatusEnum.TO_CONFIRM.getCode());
        this.updateById(workFeeVerifyNew);
        // 更新对账过的明细
        if (CollectionUtil.isNotEmpty(updateList)) {
            this.workFeeVerifyDetailService.updateBatchById(updateList);
        }
        // 对账后，将工单的费用确认状态置为通过
        List<WorkDeal> workDealListNew = new ArrayList<>();
        List<WorkCheck> workCheckList = new ArrayList<>();
        List<WorkDeal> workDealList = this.workFeeVerifyDetailService.listWorkDealByVerifyId(workFeeVerifyDto.getVerifyId());
        if (CollectionUtil.isNotEmpty(workDealList)) {
            workDealList.forEach(workDeal -> {
                if (!FeeConfirmStatusEnum.CONFIRM_PASS.getCode().equals(workDeal.getFeeConfirmStatus())) {
                    WorkDeal workDealNew = new WorkDeal();
                    workDealNew.setWorkId(workDeal.getWorkId());
                    workDealNew.setFeeConfirmStatus(FeeConfirmStatusEnum.CONFIRM_PASS.getCode());
                    workDealNew.setFeeConfirmTime(DateTime.now());
                    workDealListNew.add(workDealNew);
                    WorkCheck workCheck = new WorkCheck();
                    workCheck.setWorkId(workDeal.getWorkId());
                    workCheck.setFeeConfirmStatus(FeeConfirmStatusEnum.CONFIRM_PASS.getCode());
                    workCheck.setFeeConfirmUser(userId);
                    workCheck.setFeeConfirmTime(DateTime.now());
                    workCheck.setFeeConfirmNote("对账后，自动确认通过");
                    workCheckList.add(workCheck);
                }
            });
            if (CollectionUtil.isNotEmpty(workDealListNew)) {
                this.workDealService.updateBatchById(workDealListNew);
            }
            if (CollectionUtil.isNotEmpty(workCheckList)) {
                this.workCheckService.updateBatchById(workCheckList);
            }
        }
    }

    /**
     * 确认
     *
     * @param workFeeVerifyDto
     * @param userId
     * @param corpId
     */
    @Override
    public void confirm(WorkFeeVerifyDto workFeeVerifyDto, Long userId, Long corpId) {
        if (workFeeVerifyDto == null || LongUtil.isZero(workFeeVerifyDto.getVerifyId())) {
            throw new AppException("对账单编号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.getById(workFeeVerifyDto.getVerifyId());
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        if (!WorkFeeVerifyStatusEnum.TO_CONFIRM.getCode().equals(workFeeVerify.getStatus()) &&
                !WorkFeeVerifyStatusEnum.REFUSE.getCode().equals(workFeeVerify.getStatus())) {
            throw new AppException("对账单状态为【" + WorkFeeVerifyStatusEnum.lookup(workFeeVerify.getStatus()) + "】，不能确认");
        }
        WorkFeeVerify workFeeVerifyNew = new WorkFeeVerify();
        workFeeVerifyNew.setVerifyId(workFeeVerifyDto.getVerifyId());
        if (EnabledEnum.YES.getCode().equals(workFeeVerifyDto.getConfirmResult())) {
            workFeeVerifyNew.setStatus(WorkFeeVerifyStatusEnum.PASS.getCode());
        } else if (EnabledEnum.NO.getCode().equals(workFeeVerifyDto.getConfirmResult())) {
            workFeeVerifyNew.setStatus(WorkFeeVerifyStatusEnum.REFUSE.getCode());
        } else {
            throw new AppException("确认结果格式不正确，请重新选择");
        }
        workFeeVerifyNew.setConfirmUser(userId);
        workFeeVerifyNew.setConfirmTime(DateTime.now());
        workFeeVerifyNew.setConfirmNote(workFeeVerifyDto.getConfirmNote());
        this.updateById(workFeeVerifyNew);
        List<WorkFeeVerifyDetail> detailList = this.workFeeVerifyDetailService.listByVerifyId(workFeeVerifyDto.getVerifyId());
        List<WorkDeal> workDealList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(detailList)) {
            detailList.forEach(workFeeVerifyDetail -> {
                WorkDeal workDeal = new WorkDeal();
                workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_SETTLE.getCode());
                workDealList.add(workDeal);
            });
            // 更新工单的结算状态
            this.workDealService.updateBatchById(workDealList);
        }
    }

    /**
     * 分页查询可结算对账单
     *
     * @param workFeeVerifyFilter
     * @return
     */
    @Override
    public ListWrapper<WorkFeeVerifyDto> queryCanSettleVerify(WorkFeeVerifyFilter workFeeVerifyFilter) {
        ListWrapper<WorkFeeVerifyDto> listWrapper = new ListWrapper<>();
        if (workFeeVerifyFilter == null ||
                LongUtil.isZero(workFeeVerifyFilter.getServiceCorp()) ||
                LongUtil.isZero(workFeeVerifyFilter.getDemanderCorp())) {
            return listWrapper;
        }
        Page page = new Page(workFeeVerifyFilter.getPageNum(), workFeeVerifyFilter.getPageSize());
        List<WorkFeeVerifyDto> list = this.baseMapper.queryCanSettleVerify(page, workFeeVerifyFilter);
        if (CollectionUtil.isNotEmpty(list)) {
            Set<Long> corpSet = new HashSet<>();
            Set<Long> userIdSet = new HashSet<>();
            list.forEach(workFeeVerifyDto -> {
                corpSet.add(workFeeVerifyDto.getServiceCorp());
                corpSet.add(workFeeVerifyDto.getDemanderCorp());
                userIdSet.add(workFeeVerifyDto.getAddUser());
            });
            List<Long> corpIdList = new ArrayList<>(corpSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = new HashMap<>();
            if (corpMapResult != null && corpMapResult.getCode() == 0) {
                corpMap = corpMapResult.getData() == null ? new HashMap<>() : corpMapResult.getData();
            }
            Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userMap = new HashMap<>();
            if (userMapResult != null && userMapResult.getCode() == 0) {
                userMap = userMapResult.getData() == null ? new HashMap<>() : userMapResult.getData();
            }
            for (WorkFeeVerifyDto workFeeVerifyDto : list) {
                workFeeVerifyDto.setDemanderCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getDemanderCorp())));
                workFeeVerifyDto.setServiceCorpName(StrUtil.trimToEmpty(corpMap.get(workFeeVerifyDto.getServiceCorp())));
                workFeeVerifyDto.setAddUserName(StrUtil.trimToEmpty(userMap.get(workFeeVerifyDto.getAddUser())));
                workFeeVerifyDto.setStatusName(WorkFeeVerifyStatusEnum.lookup(workFeeVerifyDto.getStatus()));
                workFeeVerifyDto.setSettleStatusName(WorkFeeVerifySettleStatusEnum.lookup(workFeeVerifyDto.getSettleStatus()));
            }
        }
        listWrapper.setList(list);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 根据对账单号list获取对账单号与工单总费用的map
     *
     * @param verifyIdList
     * @return
     */
    @Override
    public Map<Long, BigDecimal> mapVerifyIdAndWorkTotalFee(List<Long> verifyIdList) {
        Map<Long, BigDecimal> map = new HashMap<>();
        if (CollectionUtil.isEmpty(verifyIdList)) {
            return map;
        }
        List<WorkFeeDto> list = this.workFeeService.listByVerifyIdList(verifyIdList);
        if (CollectionUtil.isNotEmpty(list)) {
            list.forEach(workFeeDto -> {
                BigDecimal totalFee = map.get(workFeeDto.getVerifyId());
                if (totalFee == null) {
                    totalFee = BigDecimal.ZERO;
                }
                totalFee = totalFee.add(workFeeDto.getTotalFee());
                map.put(workFeeDto.getVerifyId(), totalFee);
            });
        }
        return map;
    }

    /**
     * 导入对账
     *
     * @param excelDtoList
     * @param verifyId
     * @param userId
     * @param corpId
     * @return
     */
    @Override
    public String importVerify(List<WorkFeeVerifyDetailExcelDto> excelDtoList, Long verifyId, Long userId, Long corpId) {
        if (CollectionUtil.isEmpty(excelDtoList)) {
            throw new AppException("未读取到文件内容");
        }
        if (LongUtil.isZero(verifyId)) {
            throw new AppException("对账单号不能为空");
        }
        WorkFeeVerify workFeeVerify = this.getById(verifyId);
        if (workFeeVerify == null) {
            throw new AppException("对账单不存在，请检查");
        }
        if (!corpId.equals(workFeeVerify.getDemanderCorp())) {
            throw new AppException("当前企业非对账单委托商，请检查");
        }
        if (!WorkFeeVerifyStatusEnum.TO_VERIFY.getCode().equals(workFeeVerify.getStatus()) &&
            !WorkFeeVerifyStatusEnum.REFUSE.getCode().equals(workFeeVerify.getStatus())) {
            throw new AppException("对账单状态为【" + WorkFeeVerifyStatusEnum.lookup(workFeeVerify.getStatus()) + "】，不能对账，请检查");
        }
        StringBuilder sb = new StringBuilder(64);

        Map<Long, WorkFeeVerifyDetail> workIdAndDetailMap = this.workFeeVerifyDetailService.mapWorkIdAndDetailByVerifyId(verifyId);
        // 校验对账工单数量
        if (workIdAndDetailMap.size() != excelDtoList.size()) {
            throw new AppException("对账工单数量有误，应对账工单数量为【" + workIdAndDetailMap.size() + "】，导入对账工单数量为【" + excelDtoList.size() + "】");
        }

        // 更新的对账明细列表
        Map<Long, WorkFeeVerifyDetailDto> verifiedMap = new HashMap<>();

        // 校验重复性,key为工单编号，value为行号集合
        Map<Long, List<Integer>> repeatCheckMap = new HashMap<>();

        // 行数
        int rowNum = 1;
        // 获取文件中数据
        for (WorkFeeVerifyDetailExcelDto excelDto : excelDtoList) {
            WorkFeeVerifyDetailDto workFeeVerifyDetailDto = new WorkFeeVerifyDetailDto();
            WorkFeeVerifyDetail workFeeVerifyDetailOld = null;
            rowNum++;
            // 工单号
            String workIdStr = excelDto.getWorkId();
            Long workId = strToLong(workIdStr);
            if (LongUtil.isZero(workId)) {
                sb.append("第" +rowNum+ "行，对账工单不存在,").append("<br>");
            } else {
                if (!workIdAndDetailMap.containsKey(workId)) {
                    sb.append("第" +rowNum+ "行，对账工单不存在于对账单中,").append("<br>");
                }
                workFeeVerifyDetailOld = workIdAndDetailMap.get(workId);
                workFeeVerifyDetailDto.setWorkId(workId);

                List<Integer> rowNumList = repeatCheckMap.get(workId) == null ? new ArrayList<>() : repeatCheckMap.get(workId);
                rowNumList.add(rowNum);
                repeatCheckMap.put(workId, rowNumList);
            }

            // 其余属性忽略不计，直接校验对账后费用和对账说明
            String verifyAmountStr = excelDto.getVerifyAmount();
            BigDecimal verifyAmount = strToBigDecimal(verifyAmountStr);
            if (verifyAmount == null || verifyAmount.compareTo(BigDecimal.ZERO) <= 0) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_IS_POSITIVE_NUMBER.fillArgs(rowNum, "对账后费用"));
            } else {
                workFeeVerifyDetailDto.setVerifyAmount(verifyAmount);
            }

            String note = StrUtil.trimToEmpty(excelDto.getNote()).trim();
            if (StrUtil.isNotEmpty(note) && note.length() > 100) {
                sb.append(ExcelCodeMsg.IMPORT_FIELD_LENGTH_LIMIT.fillArgs(rowNum, "对账说明", 100));
            } else {
                workFeeVerifyDetailDto.setNote(note);
            }

            // 存在则放入待更新
            if (workFeeVerifyDetailOld != null) {
                verifiedMap.put(workFeeVerifyDetailOld.getDetailId(), workFeeVerifyDetailDto);
            }

        }

        // 校验重复数据
        repeatCheckMap.entrySet().forEach(entry -> {
            if (entry.getValue() != null && entry.getValue().size() > 1) {
                String rowNums = entry.getValue().toString();
                sb.append("第" + rowNums + "行对账工单重复<br>");
            }
        });

        // 校验通过
        if (sb.length() <= 0) {
            WorkFeeVerifyDto workFeeVerifyDto = new WorkFeeVerifyDto();
            workFeeVerifyDto.setVerifyId(verifyId);
            workFeeVerifyDto.setVerifiedMap(verifiedMap);
            // 对账
            this.verify(workFeeVerifyDto, userId, corpId);
            sb.append("导入对账成功");
        }
        return sb.toString();
    }

    /**
     * 根据前缀生成对账单号
     *
     * @param prefix
     * @return
     */
    @Override
    public String createVerifyName(String prefix) {
        if (StrUtil.isEmpty(prefix)) {
            return null;
        }
        String maxVerifyName = this.baseMapper.findMaxVerifyName(prefix);
        if (StrUtil.isEmpty(maxVerifyName)) {
            maxVerifyName = prefix + VERIFY_NAME_NO;
        }
        int code = Integer.parseInt(maxVerifyName.replace(prefix, "")) + 1;
        DecimalFormat decimalFormat = new DecimalFormat(VERIFY_NAME_NO);
        return prefix + decimalFormat.format(code);
    }

    /**
     * 自动生成对账单
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public String autoCreateVerify(Long serviceCorp) {
        StringBuilder sb = new StringBuilder();
        if (LongUtil.isZero(serviceCorp)) {
            sb.append("未获取到服务商参数, 任务执行中止");
            return sb.toString();
        }
        List<DemanderService> demanderServiceList = this.demanderServiceService.list(new QueryWrapper<DemanderService>()
                .eq("service_corp", serviceCorp));
        if (CollectionUtil.isEmpty(demanderServiceList)) {
            sb.append("未获取到委托商列表，任务执行完成");
            return sb.toString();
        }
        // 委托商自动化配置设置
        Map<Long, DemanderAutoConfigDto> demanderAutoConfigDtoMap = this.demanderAutoConfigService.mapDemanderAndAutoConfig(serviceCorp);
        // 生成对账单列表
        List<WorkFeeVerify> verifyList = new ArrayList<>();
        // 生成对账单明细列表
        List<WorkFeeVerifyDetail> detailList = new ArrayList<>();
        // 更新工单处理列表
        List<WorkDeal> workDealList = new ArrayList<>();
        // 获取委托商系统编号与企业编号的映射
        List<Long> corpIdList = demanderServiceList.stream().map(demanderService -> demanderService.getDemanderCorp()).collect(Collectors.toList());
        Result<Map<Long, String>> corpIdAndCodeMapResult = uasFeignService.mapCorpIdAndCode(JsonUtil.toJson(corpIdList));
        Map<Long, String> corpIdAndCodeMap = new HashMap<>();
        if (corpIdAndCodeMapResult != null && Result.SUCCESS == corpIdAndCodeMapResult.getCode().intValue()) {
            corpIdAndCodeMap = corpIdAndCodeMapResult.getData() == null ? new HashMap<>() : corpIdAndCodeMapResult.getData();
        }
        // 获取对账截止日期
        Date startDate = null;
        Calendar calEnd = Calendar.getInstance();
        calEnd.set(Calendar.DAY_OF_MONTH, 0);
        Date endDate = DateUtil.date(calEnd);
        List<String> prefixList = new ArrayList<>();
        Integer prefixLength = 0;
        for (DemanderService demanderService : demanderServiceList) {
            String corpCode = StrUtil.trimToEmpty(corpIdAndCodeMap.get(demanderService.getDemanderCorp()));
            String yearMonth = DateUtil.format(endDate, "yyMM");
            String prefix = corpCode + "-" + yearMonth + "-";
            prefixList.add(prefix);
            prefixLength = prefix.length();
        }
        List<WorkFeeVerifyDto> dtoList = this.baseMapper.listMaxVerifyName(prefixList, prefixLength);
        Map<String, Integer> prefixAndMaxNoMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(dtoList)) {
            dtoList.forEach(workFeeVerifyDto -> {
                String maxNoStr = workFeeVerifyDto.getVerifyName().replace(workFeeVerifyDto.getPrefix(), "");
                Integer maxNo = Integer.parseInt(maxNoStr);
                prefixAndMaxNoMap.put(workFeeVerifyDto.getPrefix(), maxNo);
            });
        }
        for (DemanderService demanderService : demanderServiceList) {
            // 如果自动对账为关闭，则不自动对账
            DemanderAutoConfigDto demanderAutoConfigDto = demanderAutoConfigDtoMap.get(demanderService.getDemanderCorp());
            if (demanderAutoConfigDto == null || !EnabledEnum.YES.getCode().equals(demanderAutoConfigDto.getAutoVerify())) {
                continue;
            }
            // 获取对账起始日期
            Integer day = DateUtil.dayOfMonth(DateUtil.date());
            Calendar calStart = Calendar.getInstance();
            // 按月结算
            if (SettleTypeEnum.PER_MONTH.getCode().equals(demanderAutoConfigDto.getSettleType())) {
                if (day.equals(demanderAutoConfigDto.getSettleDay())) {
                    calStart.add(Calendar.MONTH, -1);
                    calStart.set(Calendar.DAY_OF_MONTH, 1);
                    startDate = DateUtil.date(calStart);
                } else {
                    continue;
                }
                // 按季度结算
            } else if (SettleTypeEnum.PER_SEASON.getCode().equals(demanderAutoConfigDto.getSettleType())) {
                if (day.equals(demanderAutoConfigDto.getSettleDay())) {
                    Integer month = DateUtil.month(DateUtil.date()) + 1;
                    if (month == 4 || month == 7 || month == 10 || month == 1) {
                        calStart.add(Calendar.MONTH, -3);
                        calStart.set(Calendar.DAY_OF_MONTH, 1);
                        startDate = DateUtil.date(calStart);
                    } else {
                        continue;
                    }
                } else {
                    continue;
                }
            } else {
                continue;
            }
            // 查询工单的过滤器
            WorkFilter workFilter = new WorkFilter();
            workFilter.setServiceCorp(serviceCorp);
            workFilter.setDemanderCorp(demanderService.getDemanderCorp());
            workFilter.setWorkStatuses(WorkStatusEnum.getSettleStatusList().stream()
                    .map(intValue -> String.valueOf(intValue)).collect(Collectors.joining(",")));
            workFilter.setWarrantyModes(WarrantyModeEnum.listSettleWarrantyStatus().stream()
                    .map(intValue -> String.valueOf(intValue)).collect(Collectors.joining(",")));
            workFilter.setSettleDemanderStatus(WorkSettleStatusEnum.TO_VERIFY.getCode());
            workFilter.setStartDate(startDate);
            workFilter.setEndDate(endDate);
            // 获取应对账工单
            List<WorkFeeDto> workFeeDtoList = this.workFeeMapper.listByWorkFilter(workFilter);
            if (CollectionUtil.isEmpty(workFeeDtoList)) {
                continue;
            }
            // 对账单
            WorkFeeVerify workFeeVerify = new WorkFeeVerify();
            workFeeVerify.setVerifyId(KeyUtil.getId());
            workFeeVerify.setAddTime(DateUtil.date());
            workFeeVerify.setNote("自动生成");
            workFeeVerify.setStatus(WorkFeeVerifyStatusEnum.TO_SUBMIT.getCode());
            workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.TO_SETTLE.getCode());
            workFeeVerify.setWorkQuantity(workFeeDtoList.size());
            workFeeVerify.setServiceCorp(serviceCorp);
            workFeeVerify.setDemanderCorp(demanderService.getDemanderCorp());
            workFeeVerify.setStartDate(startDate);
            workFeeVerify.setEndDate(endDate);
            if (!corpIdAndCodeMap.containsKey(demanderService.getDemanderCorp()) ||
                    StringUtils.isEmpty(corpIdAndCodeMap.containsKey(demanderService.getDemanderCorp()))) {
                continue;
            }
            // 设置对账单号
            String prefix = corpIdAndCodeMap.get(demanderService.getDemanderCorp()) + "-" + DateUtil.format(endDate, "yyMM")
                    + "-";
            Integer maxVerifyNo = prefixAndMaxNoMap.get(prefix) == null ? 0 : prefixAndMaxNoMap.get(prefix);
            DecimalFormat decimalFormat = new DecimalFormat(VERIFY_NAME_NO);
            workFeeVerify.setVerifyName(prefix + decimalFormat.format(maxVerifyNo + 1));
            verifyList.add(workFeeVerify);
            // 更新最大编号
            prefixAndMaxNoMap.put(prefix, maxVerifyNo + 1);
            // 对账单明细
            for (WorkFeeDto workFeeDto : workFeeDtoList) {
                WorkFeeVerifyDetail workFeeVerifyDetail = new WorkFeeVerifyDetail();
                workFeeVerifyDetail.setDetailId(KeyUtil.getId());
                workFeeVerifyDetail.setVerifyId(workFeeVerify.getVerifyId());
                workFeeVerifyDetail.setWorkId(workFeeDto.getWorkId());
                workFeeVerifyDetail.setVerifyAmount(workFeeDto.getTotalFee() == null ? BigDecimal.ZERO : workFeeDto.getTotalFee());
                detailList.add(workFeeVerifyDetail);
                // 工单结算状态设置为对账中
                WorkDeal workDeal = new WorkDeal();
                workDeal.setWorkId(workFeeDto.getWorkId());
                workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_VERIFY.getCode());
                workDealList.add(workDeal);
            }
        }
        // 保存对账单和明细
        if (CollectionUtil.isNotEmpty(verifyList)) {
            this.saveBatch(verifyList);
            sb.append("任务执行完成, 共生成【" + verifyList.size() + "】条对账单" );
        } else {
            sb.append("任务执行完成，未生成对账单");
        }
        if (CollectionUtil.isNotEmpty(detailList)) {
            this.workFeeVerifyDetailService.saveBatch(detailList);
        }
        // 更新对应工单的结算状态
        if (CollectionUtil.isNotEmpty(workDealList)) {
            this.workDealService.updateBatchById(workDealList);
        }
        return sb.toString();
    }

    /**
     * 根据对账单编号列表获取编号和对账单的映射
     *
     * @param verifyIdList
     * @return
     */
    @Override
    public Map<Long, WorkFeeVerify> mapIdAndVerify(List<Long> verifyIdList) {
        Map<Long, WorkFeeVerify> map = new HashMap<>();
        if (CollectionUtil.isEmpty(verifyIdList)) {
            return map;
        }
        List<WorkFeeVerify> workFeeVerifyList = this.list(new QueryWrapper<WorkFeeVerify>().in("verify_id", verifyIdList));
        if (CollectionUtil.isNotEmpty(workFeeVerifyList)) {
            map = workFeeVerifyList.stream().collect(Collectors.toMap(item -> item.getVerifyId(), item -> item));
        }
        return map;
    }


    /**
     * 字符串转Long，不抛异常
     *
     * @param str
     * @return
     */
    private Long strToLong(String str) {
        Long strLong = null;
        try {
            strLong = Long.parseLong(str);
            return strLong;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 字符串转BigDecimal
     *
     * @param str
     * @return
     */
    private BigDecimal strToBigDecimal(String str) {
        BigDecimal strBigDecimal = null;
        try {
            strBigDecimal = new BigDecimal(str);
            return strBigDecimal;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 校验提交数据
     *
     * @param workFeeVerifyDto
     * @return
     */
    private String checkData(WorkFeeVerifyDto workFeeVerifyDto) {
        StringBuilder error = new StringBuilder(8);
        if (workFeeVerifyDto == null) {
            error.append("参数不能为空");
        }
        if (LongUtil.isZero(workFeeVerifyDto.getServiceCorp())) {
            error.append("<br>服务商不能为空");
        }
        if (LongUtil.isZero(workFeeVerifyDto.getDemanderCorp())) {
            error.append("<br>委托商不能为空");
        }
        if (workFeeVerifyDto.getStartDate() == null) {
            error.append("<br>起始日期不能为空");
        }
        if (workFeeVerifyDto.getEndDate() == null) {
            error.append("<br>结束日期不能为空");
        }
        return error.toString();
    }

}
