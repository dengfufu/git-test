package com.zjft.usp.anyfix.settle.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.JsonObject;
import com.zjft.usp.anyfix.baseinfo.enums.EnabledEnum;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.AreaDto;
import com.zjft.usp.anyfix.common.service.RightCompoService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderContService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceManagerService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.anyfix.settle.dto.BankAccountDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDetailDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderDto;
import com.zjft.usp.anyfix.settle.enums.*;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderDetailFilter;
import com.zjft.usp.anyfix.settle.filter.SettleDemanderFilter;
import com.zjft.usp.anyfix.settle.mapper.SettleDemanderMapper;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import com.zjft.usp.anyfix.settle.service.SettleDemanderDetailService;
import com.zjft.usp.anyfix.settle.service.SettleDemanderService;
import com.zjft.usp.anyfix.utils.BusinessCodeGenerator;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeDto;
import com.zjft.usp.anyfix.work.fee.dto.WorkFeeVerifyDto;
import com.zjft.usp.anyfix.work.fee.enums.WorkFeeVerifySettleStatusEnum;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.dto.CorpBankAccountDto;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 委托商结算单 服务实现类
 * </p>
 *
 * @author canlei
 * @since 2020-01-13
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SettleDemanderServiceImpl extends ServiceImpl<SettleDemanderMapper, SettleDemander> implements SettleDemanderService {

    @Autowired
    private SettleDemanderDetailService settleDemanderDetailService;
    @Autowired
    private WorkDealService workDealService;
    @Resource
    private UasFeignService uasFeignService;
    @Autowired
    private BusinessCodeGenerator businessCodeGenerator;
    @Autowired
    private WorkFeeVerifyDetailService workFeeVerifyDetailService;
    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;
    @Autowired
    private DemanderServiceService demanderServiceService;
    @Autowired
    private RightCompoService rightCompoService;
    @Autowired
    private DemanderServiceManagerService demanderServiceManagerService;
    @Autowired
    private WorkFeeService workFeeService;
    @Autowired
    private DemanderContService demanderContService;
    // 合同号后四位编号起始值
    private static String START_CONT_NO = "00";

    /**
     * 分页查询
     *
     * @param settleDemanderFilter
     * @return
     * @author canlei
     */
    @Override
    public ListWrapper<SettleDemanderDto> query(SettleDemanderFilter settleDemanderFilter, Long userId, Long corpId) {
        ListWrapper<SettleDemanderDto> listWrapper = new ListWrapper<>();
        if (settleDemanderFilter == null || LongUtil.isZero(settleDemanderFilter.getCurrentCorp())) {
            return listWrapper;
        }
        // 客户经理只有自己负责委托商的范围权限
        if (rightCompoService.hasRight(corpId, userId, RightConstants.SETTLE_DEMANDER_MANAGER)
            && !rightCompoService.hasRight(corpId, userId, RightConstants.SETTLE_DEMANDER)) {
            List<Long> demanderCorpList = demanderServiceManagerService.listDemanderCorpByManager(corpId, userId);
            if (CollectionUtil.isEmpty(demanderCorpList)) {
                return listWrapper;
            }
            settleDemanderFilter.setDemanderCorpList(demanderCorpList);
        }
        Page page = new Page(settleDemanderFilter.getPageNum(), settleDemanderFilter.getPageSize());
        List<SettleDemanderDto> dtoList = this.baseMapper.pageByFilter(page, settleDemanderFilter);
        if (CollectionUtil.isNotEmpty(dtoList)) {
            HashSet<Long> corpIdSet = new HashSet<>();
            HashSet<Long> userIdSet = new HashSet<>();
            List<Long> settleIdList = new ArrayList<>();
            dtoList.forEach(settleDemanderDto -> {
                corpIdSet.add(settleDemanderDto.getDemanderCorp());
                corpIdSet.add(settleDemanderDto.getServiceCorp());
                userIdSet.add(settleDemanderDto.getOperator());
                userIdSet.add(settleDemanderDto.getCheckUser());
                settleIdList.add(settleDemanderDto.getSettleId());
            });
            List<Long> corpIdList = new ArrayList<>(corpIdSet);
            List<Long> userIdList = new ArrayList<>(userIdSet);
            Result<Map<Long, String>> corpMapResult = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            Map<Long, String> corpMap = corpMapResult == null ? new HashMap<>() : corpMapResult.getData();
            Result<Map<Long, String>> userMapResult = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdList));
            Map<Long, String> userMap = userMapResult == null ? new HashMap<>() : userMapResult.getData();
            // 设置附加属性
            dtoList.forEach(settleDemanderDto -> {
                settleDemanderDto.setDemanderCorpName(corpMap.get(settleDemanderDto.getDemanderCorp()));
                settleDemanderDto.setServiceCorpName(corpMap.get(settleDemanderDto.getServiceCorp()));
                settleDemanderDto.setStatusName(SettleStatusEnum.lookup(settleDemanderDto.getStatus()));
                settleDemanderDto.setOperatorName(userMap.get(settleDemanderDto.getOperator()));
                settleDemanderDto.setCheckUserName(userMap.get(settleDemanderDto.getCheckUser()));
                settleDemanderDto.setPayStatusName(SettlePayStatusEnum.lookup(settleDemanderDto.getPayStatus()));
                settleDemanderDto.setInvoiceStatusName(SettleInvoiceStatusEnum.lookup(settleDemanderDto.getInvoiceStatus()));
                // 结算方式名称
                settleDemanderDto.setSettleWayName(SettleWayEnum.lookup(settleDemanderDto.getSettleWay()));
            });
        }
        listWrapper.setList(dtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 添加
     *
     * @param settleDemanderDto
     * @param curUserId
     * @author canlei
     */
    @Override
    public void add(SettleDemanderDto settleDemanderDto, Long curUserId) {
        String note = checkData(settleDemanderDto);
        if (note.length() > 0) {
            throw new AppException(note);
        }
        List<SettleDemanderDetailDto> detailDtoList = settleDemanderDto.getDetailDtoList();
        Long settleId = KeyUtil.getId();
        // 结算单明细列表
        List<SettleDemanderDetail> detailList = new ArrayList<>();
        // 对账单列表
        List<WorkFeeVerify> workFeeVerifyList = new ArrayList<>();
        // 工单处理列表
        List<WorkDeal> workDealList = new ArrayList<>();
        List<Long> verifyIdList = new ArrayList<>();
        // 按周期结算
        for (SettleDemanderDetailDto detailDto : detailDtoList) {
            SettleDemanderDetail detail = new SettleDemanderDetail();
            detail.setDetailId(KeyUtil.getId());
            detail.setSettleId(settleId);
            // 按周期结算
            if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
                detail.setVerifyId(detailDto.getVerifyId());
                verifyIdList.add(detailDto.getVerifyId());
                WorkFeeVerify workFeeVerify = new WorkFeeVerify();
                workFeeVerify.setVerifyId(detailDto.getVerifyId());
                workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.IN_SETTLE.getCode());
                workFeeVerifyList.add(workFeeVerify);
            // 按单结算
            } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                detail.setWorkId(detailDto.getWorkId());
                WorkDeal workDeal = new WorkDeal();
                workDeal.setWorkId(detailDto.getWorkId());
                workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
                workDealList.add(workDeal);
            }
            detailList.add(detail);
        }
        SettleDemander settleDemander = new SettleDemander();
        BeanUtils.copyProperties(settleDemanderDto, settleDemander);
        settleDemander.setSettleId(settleId);
        // 生成结算单号
        if (StringUtils.isEmpty(settleDemanderDto.getSettleCode())) {
            settleDemander.setSettleCode(this.generateSettleCode(settleDemanderDto));
        }
        // 按单结算与按周期结算的工单数量
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
            List<WorkFeeVerifyDetail> verifyDetailList = this.workFeeVerifyDetailService.listByVerifyIdList(verifyIdList);
            if (CollectionUtil.isNotEmpty(verifyDetailList)) {
                for (WorkFeeVerifyDetail workFeeVerifyDetail : verifyDetailList) {
                    WorkDeal workDeal = new WorkDeal();
                    workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                    workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
                    workDealList.add(workDeal);
                }
            }
            settleDemander.setWorkQuantity(CollectionUtil.isNotEmpty(verifyDetailList) ? verifyDetailList.size() : 0);
        } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
            settleDemander.setWorkQuantity(CollectionUtil.isNotEmpty(detailDtoList) ? detailDtoList.size() : 0);
            settleDemander.setStartDate(null);
            settleDemander.setEndDate(null);
        }
        settleDemander.setOperator(curUserId);
        settleDemander.setOperateTime(DateTime.now());
        settleDemander.setStatus(SettleStatusEnum.UNCHECKED.getCode());
        settleDemander.setPayStatus(SettlePayStatusEnum.TO_PAY.getCode());
        settleDemander.setInvoiceStatus(SettleInvoiceStatusEnum.TO_INVOICE.getCode());
        // 保存结算单
        this.save(settleDemander);
        // 保存结算单明细
        this.settleDemanderDetailService.saveBatch(detailList);
        // 更新工单结算状态
        if (CollectionUtil.isNotEmpty(workDealList)) {
            this.workDealService.updateBatchById(workDealList);
        }
        // 更新对账单结算状态
        if (CollectionUtil.isNotEmpty(workFeeVerifyList)) {
            this.workFeeVerifyService.updateBatchById(workFeeVerifyList);
        }
        // 如果是按单结算，且添加了对账中的工单，则需要删除对账单中的该条明细
        if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
            // 需要删除的对账单明细
            List<Long> deleteDetailIdList = new ArrayList<>();
            // 需要更新的对账单
            List<WorkFeeVerify> updateVerifyList = new ArrayList<>();
            // 需要删除的对账单
            List<Long> deleteVerifyIdList = new ArrayList<>();
            List<Long> workIdList = detailList.stream().map(settleDemanderDetail -> settleDemanderDetail.getWorkId())
                    .collect(Collectors.toList());
            Map<Long, WorkFeeVerifyDetail> verifyDetailMap = this.workFeeVerifyDetailService.mapWorkIdAndDetail(workIdList);
            List<Long> workVerifyIdList = verifyDetailMap.values().stream().map(workFeeVerifyDetail -> workFeeVerifyDetail.getVerifyId())
                    .distinct().collect(Collectors.toList());
            Map<Long, WorkFeeVerify> workFeeVerifyMap = this.workFeeVerifyService.mapIdAndVerify(workVerifyIdList);
            for (SettleDemanderDetail detail : detailList) {
                WorkFeeVerifyDetail verifyDetail = verifyDetailMap.get(detail.getWorkId());
                if (verifyDetail != null) {
                    deleteDetailIdList.add(verifyDetail.getDetailId());
                    if (workFeeVerifyMap.containsKey(verifyDetail.getVerifyId())) {
                        WorkFeeVerify workFeeVerify = workFeeVerifyMap.get(verifyDetail.getVerifyId());
                        if (workFeeVerify != null) {
                            workFeeVerifyMap.get(verifyDetail.getVerifyId()).setWorkQuantity(workFeeVerify.getWorkQuantity() - 1);
                        }
                    }
                }
            }
            for (Map.Entry<Long, WorkFeeVerify> entry : workFeeVerifyMap.entrySet()) {
                WorkFeeVerify workFeeVerify = entry.getValue();
                if (workFeeVerify != null) {
                    if (workFeeVerify.getWorkQuantity().equals(0)) {
                        deleteVerifyIdList.add(workFeeVerify.getVerifyId());
                    } else {
                        updateVerifyList.add(workFeeVerify);
                    }
                }
            }
            if (CollectionUtil.isNotEmpty(deleteDetailIdList)) {
                this.workFeeVerifyDetailService.removeByIds(deleteDetailIdList);
            }
            if (CollectionUtil.isNotEmpty(deleteVerifyIdList)) {
                this.workFeeVerifyService.removeByIds(deleteVerifyIdList);
            }
            if (CollectionUtil.isNotEmpty(updateVerifyList)) {
                this.workFeeVerifyService.updateBatchById(updateVerifyList);
            }
        }
    }

    /**
     * 根据id查询
     *
     * @param settleId
     * @return
     * @author canlei
     */
    @Override
    public SettleDemanderDto findById(Long settleId) {
        if (LongUtil.isZero(settleId)) {
            return null;
        }
        SettleDemander settleDemander = this.getById(settleId);
        if (settleDemander == null) {
            throw new AppException("结算单不存在，请检查");
        }
        SettleDemanderDto settleDemanderDto = new SettleDemanderDto();
        BeanUtils.copyProperties(settleDemander, settleDemanderDto);
        // 远程调用获得地区映射
        AreaDto areaDto = null;
        if (StrUtil.isNotBlank(settleDemanderDto.getDistrict())) {
            Result areaResult = uasFeignService.findAreaByCode(settleDemanderDto.getDistrict());
            if (areaResult.getCode() == Result.SUCCESS) {
                areaDto = JsonUtil.parseObject(JsonUtil.toJson(areaResult.getData()), AreaDto.class);
            }
        }
        if (areaDto != null) {
            settleDemanderDto.setDistrictName(areaDto.getAreaName());
        }
        SettleDemanderDetailFilter filter = new SettleDemanderDetailFilter();
        filter.setSettleId(settleId);
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemander.getSettleWay())) {
            List<WorkFeeVerifyDto> detailList = this.settleDemanderDetailService.listWorkFeeVerifyByFilter(filter);
            settleDemanderDto.setWorkFeeVerifyDtoList(detailList);
        } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemander.getSettleWay())) {
            List<WorkFeeDto> detailList = this.settleDemanderDetailService.listWorkByFilter(filter);
            settleDemanderDto.setWorkFeeDtoList(detailList);
        }
        Set<Long> corpIdSet = new HashSet<>();
        Set<Long> userIdSet = new HashSet<>();
        corpIdSet.add(settleDemander.getDemanderCorp());
        corpIdSet.add(settleDemander.getServiceCorp());
        userIdSet.add(settleDemander.getOperator());
        userIdSet.add(settleDemander.getCheckUser());

        Map<Long, String> corpMap = this.uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet)).getData();
        Map<Long, String> userMap = this.uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet)).getData();
        settleDemanderDto.setStatusName(SettleStatusEnum.lookup(settleDemander.getStatus()));
        settleDemanderDto.setPayStatusName(SettlePayStatusEnum.lookup(settleDemander.getPayStatus()));
        settleDemanderDto.setInvoiceStatusName(SettleInvoiceStatusEnum.lookup(settleDemander.getInvoiceStatus()));
        settleDemanderDto.setReceiptStatusName(SettlePayStatusEnum.getReceiptName(settleDemander.getPayStatus()));
        settleDemanderDto.setServiceCorpName(corpMap.get(settleDemander.getServiceCorp()));
        settleDemanderDto.setDemanderCorpName(corpMap.get(settleDemander.getDemanderCorp()));
        settleDemanderDto.setOperatorName(userMap.get(settleDemander.getOperator()));
        settleDemanderDto.setCheckUserName(userMap.get(settleDemander.getCheckUser()));
        settleDemanderDto.setSettleWayName(SettleWayEnum.lookup(settleDemanderDto.getSettleWay()));
        // 获取委托协议号
        if (LongUtil.isNotZero(settleDemanderDto.getContId())) {
            DemanderCont demanderCont = this.demanderContService.getById(settleDemander.getContId());
            if (demanderCont != null) {
                settleDemanderDto.setContNo(demanderCont.getContNo());
            }
        }
        return settleDemanderDto;
    }

    /**
     * 确认结算单
     *
     * @param settleDemanderDto
     * @param curUserId
     * @author canlei
     */
    @Override
    public void check(SettleDemanderDto settleDemanderDto, Long curUserId) {
        if (settleDemanderDto == null || LongUtil.isZero(settleDemanderDto.getSettleId())) {
            throw new AppException("结算编号不能为空！");
        }
        SettleDemander settleDemander = this.getById(settleDemanderDto.getSettleId());
        if (settleDemander == null) {
            throw new AppException("结算单不存在，请检查！");
        }
        BeanUtils.copyProperties(settleDemanderDto, settleDemander);
        if (EnabledEnum.YES.getCode().equals(settleDemanderDto.getCheckResult())) {
            settleDemander.setStatus(SettleStatusEnum.CHECK_PASS.getCode());
        } else {
            settleDemander.setStatus(SettleStatusEnum.CHECK_REFUSE.getCode());
        }
        settleDemander.setCheckNote(settleDemanderDto.getCheckNote());
        settleDemander.setCheckUser(curUserId);
        settleDemander.setCheckTime(DateTime.now());
        this.updateById(settleDemander);
    }

    /**
     * 删除结算单
     *
     * @param settleId
     * @author canlei
     */
    @Override
    public void delete(Long settleId) {
        if (LongUtil.isZero(settleId)) {
            throw new AppException("结算单编号不能为空");
        }
        SettleDemander settleDemander = this.getById(settleId);
        if (settleDemander == null) {
            throw new AppException("结算单不存在，请检查！");
        }
        if (SettleStatusEnum.CHECK_PASS.getCode().equals(settleDemander.getStatus())) {
            throw new AppException("结算单已确认完成，不可删除！");
        }
        // 将关联对账单结算状态置为待结算
        List<SettleDemanderDetail> list = this.settleDemanderDetailService.listBySettleId(settleId);
        List<WorkFeeVerify> workFeeVerifyList = new ArrayList<>();
        List<Long> verifyIdList = new ArrayList<>();
        List<WorkDeal> workDealList = new ArrayList<>();
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemander.getSettleWay())) {
            if (CollectionUtil.isNotEmpty(list)) {
                for (SettleDemanderDetail settleDemanderDetail : list) {
                    WorkFeeVerify workFeeVerify = new WorkFeeVerify();
                    workFeeVerify.setVerifyId(settleDemanderDetail.getVerifyId());
                    workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.TO_SETTLE.getCode());
                    workFeeVerifyList.add(workFeeVerify);
                    verifyIdList.add(settleDemanderDetail.getVerifyId());
                }
            }
            // 将关联工单结算状态置为待结算
            if (CollectionUtil.isNotEmpty(verifyIdList)) {
                List<WorkFeeVerifyDetail> workFeeVerifyDetailList = this.workFeeVerifyDetailService.listByVerifyIdList(verifyIdList);
                if (CollectionUtil.isNotEmpty(workFeeVerifyDetailList)) {
                    for (WorkFeeVerifyDetail workFeeVerifyDetail : workFeeVerifyDetailList) {
                        WorkDeal workDeal = new WorkDeal();
                        workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                        workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_SETTLE.getCode());
                        workDealList.add(workDeal);
                    }
                }
            }
        } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemander.getSettleWay())) {
            if (CollectionUtil.isNotEmpty(list)) {
                for (SettleDemanderDetail detail : list) {
                    WorkDeal workDeal = new WorkDeal();
                    workDeal.setWorkId(detail.getWorkId());
                    workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_VERIFY.getCode());
                    workDealList.add(workDeal);
                }
            }
        }
        // 删除明细
        this.settleDemanderDetailService.deleteBySettleId(settleId);
        // 删除结算单
        this.removeById(settleId);
        // 更新对账单结算状态
        if (CollectionUtil.isNotEmpty(workFeeVerifyList)) {
            this.workFeeVerifyService.updateBatchById(workFeeVerifyList);
        }
        // 更新工单结算状态
        if (CollectionUtil.isNotEmpty(workDealList)) {
            this.workDealService.updateBatchById(workDealList);
        }
    }

    /**
     * 按单结算
     *
     * @param settleDemanderDto
     * @param userId
     * @param corpId
     * @return
     */
//    @Override
//    public Long addByWork(SettleDemanderDto settleDemanderDto, Long userId, Long corpId) {
//        if (settleDemanderDto == null || LongUtil.isZero(settleDemanderDto.getWorkId())) {
//            throw new AppException("工单编号不能为空！");
//        }
//        WorkDeal workDeal = this.workDealService.getById(settleDemanderDto.getWorkId());
//        WorkRequest workRequest = this.workRequestService.getById(settleDemanderDto.getWorkId());
//        if (workRequest == null || workDeal == null) {
//            throw new AppException("工单不存在，请检查");
//        }
//        if (!corpId.equals(workDeal.getServiceCorp())) {
//            throw new AppException("该工单的服务商不是当前企业，不能结算");
//        }
//        //TODO
////        if (!ServiceCheckStatusEnum.CHECK_PASS.getCode().equals(workDeal.getServiceCheckStatus())) {
////            throw new AppException("服务商未审核通过，不能结算");
////        }
//        WorkFee workFee = this.workFeeService.getById(settleDemanderDto.getWorkId());
//        if (workFee == null || workFee.getTotalFee() == null || workFee.getTotalFee().compareTo(BigDecimal.ZERO) <= 0) {
//            throw new AppException("工单无费用，无需结算");
//        }
//        SettleDemander settleDemander = new SettleDemander();
//        BeanUtils.copyProperties(settleDemanderDto, settleDemander);
//        settleDemander.setWorkQuantity(1);
//        settleDemander.setSettleCode(this.businessCodeGenerator
//                .getSettleDemanderCode(settleDemanderDto.getServiceCorp(), "", ""));
//        settleDemander.setSettleType(SettleDemanderTypeEnum.WORK_SETTLE.getCode());
//        settleDemander.setSettleId(KeyUtil.getId());
//        settleDemander.setDemanderCorp(workRequest.getDemanderCorp());
//        settleDemander.setServiceCorp(corpId);
//        settleDemander.setOperator(userId);
//        settleDemander.setOperateTime(DateTime.now());
//        settleDemander.setStatus(SettleStatusEnum.UNCHECKED.getCode());
//        settleDemander.setPayStatus(SettlePayStatusEnum.TO_PAY.getCode());
//        settleDemander.setInvoiceStatus(SettleInvoiceStatusEnum.TO_INVOICE.getCode());
//        // 添加时，将结算总金额和应付金额置为0，待确认通过后再置为实际费用，目的是避免每次修改工单费用都要同步到结算单费用
//        settleDemander.setSettleFee(BigDecimal.ZERO);
//        settleDemander.setPayable(BigDecimal.ZERO);
//        this.save(settleDemander);
//        // 添加明细
//        SettleDemanderDetail settleDemanderDetail = new SettleDemanderDetail();
//        settleDemanderDetail.setDetailId(KeyUtil.getId());
//        settleDemanderDetail.setSettleId(settleDemander.getSettleId());
//        settleDemanderDetail.setWorkId(settleDemanderDto.getWorkId());
//        this.settleDemanderDetailService.save(settleDemanderDetail);
//        // 更改工单结算状态
//        workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
//        this.workDealService.updateById(workDeal);
//        return settleDemander.getSettleId();
//    }

    /**
     * 查询收款账户信息
     *
     * @param settleDemanderFilter
     * @return
     */
    @Override
    public ListWrapper<BankAccountDto> listBankAccount(SettleDemanderFilter settleDemanderFilter) {
        ListWrapper<BankAccountDto> listWrapper = new ListWrapper<>();
        if (settleDemanderFilter == null || LongUtil.isZero(settleDemanderFilter.getServiceCorp())) {
            return null;
        }
        Page page = new Page(settleDemanderFilter.getPageNum(), settleDemanderFilter.getPageSize());
        List<BankAccountDto> bankAccountDtoList = this.baseMapper.pageBankAccount(page, settleDemanderFilter);
        listWrapper.setList(bankAccountDtoList);
        listWrapper.setTotal(page.getTotal());
        return listWrapper;
    }

    /**
     * 修改结算单
     *
     * @param settleDemanderDto
     * @param userId
     * @param serviceCorp
     */
    @Override
    public void update(SettleDemanderDto settleDemanderDto, Long userId, Long serviceCorp) {
        if (settleDemanderDto == null || LongUtil.isZero(settleDemanderDto.getSettleId())) {
            throw new AppException("结算单编号不能为空");
        }
        SettleDemander settleDemander = this.getById(settleDemanderDto.getSettleId());
        if (settleDemander == null) {
            throw new AppException("结算单不存在，请检查");
        }
        if (!serviceCorp.equals(settleDemanderDto.getServiceCorp())) {
            throw new AppException("当前企业非该结算单的服务商，不能修改");
        }
        if (SettleStatusEnum.CHECK_PASS.getCode().equals(settleDemander.getStatus())) {
            throw new AppException("该结算单已确认通过，不能修改");
        }
        String errMsg = this.checkData(settleDemanderDto);
        if (StrUtil.isNotEmpty(errMsg)) {
            throw new AppException(errMsg);
        }
        BeanUtils.copyProperties(settleDemanderDto, settleDemander);
        settleDemander.setNote(settleDemander.getNote() == null ? "" : settleDemander.getNote());
        List<SettleDemanderDetail> detailListOld = this.settleDemanderDetailService.listBySettleId(settleDemanderDto.getSettleId());
        if (CollectionUtil.isNotEmpty(detailListOld)) {
            List<WorkFeeVerify> verifyListOld = new ArrayList<>();
            List<WorkDeal> workDealListOld = new ArrayList<>();
            List<Long> verifyIdListOld = new ArrayList<>();
            // 按周期结算
            if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
                detailListOld.forEach(settleDemanderDetail -> {
                    WorkFeeVerify workFeeVerify = new WorkFeeVerify();
                    workFeeVerify.setVerifyId(settleDemanderDetail.getVerifyId());
                    workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.TO_SETTLE.getCode());
                    verifyListOld.add(workFeeVerify);
                    verifyIdListOld.add(settleDemanderDetail.getVerifyId());
                });
                List<WorkFeeVerifyDetail> verifyDetailListOld = this.workFeeVerifyDetailService.listByVerifyIdList(verifyIdListOld);
                if (CollectionUtil.isNotEmpty(verifyDetailListOld)) {
                    verifyDetailListOld.forEach(workFeeVerifyDetail -> {
                        WorkDeal workDeal = new WorkDeal();
                        workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                        workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_SETTLE.getCode());
                        workDealListOld.add(workDeal);
                    });
                }
            // 按单结算
            } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                detailListOld.forEach(settleDemanderDetail -> {
                    WorkDeal workDeal = new WorkDeal();
                    workDeal.setWorkId(settleDemanderDetail.getWorkId());
                    workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.TO_VERIFY.getCode());
                    workDealListOld.add(workDeal);
                });
            }
            // 更新对账单结算状态以及工单结算状态
            if (CollectionUtil.isNotEmpty(verifyListOld)) {
                this.workFeeVerifyService.updateBatchById(verifyListOld);
            }
            if (CollectionUtil.isNotEmpty(workDealListOld)) {
                this.workDealService.updateBatchById(workDealListOld);
            }
        }
        // 先删除明细，再新增
        this.settleDemanderDetailService.deleteBySettleId(settleDemanderDto.getSettleId());
        List<SettleDemanderDetailDto> detailDtoListNew =  settleDemanderDto.getDetailDtoList();
        if (CollectionUtil.isNotEmpty(detailDtoListNew)) {
            List<SettleDemanderDetail> settleDemanderDetailList = new ArrayList<>();
            List<WorkFeeVerify> verifyListNew = new ArrayList<>();
            List<WorkDeal> workDealListNew = new ArrayList<>();
            List<Long> verifyIdListNew = new ArrayList<>();
            for (SettleDemanderDetailDto settleDemanderDetailDto : settleDemanderDto.getDetailDtoList()) {
                SettleDemanderDetail settleDemanderDetail = new SettleDemanderDetail();
                settleDemanderDetail.setSettleId(settleDemanderDto.getSettleId());
                settleDemanderDetail.setDetailId(KeyUtil.getId());
                if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
                    settleDemanderDetail.setVerifyId(settleDemanderDetailDto.getVerifyId());WorkFeeVerify workFeeVerify = new WorkFeeVerify();
                    workFeeVerify.setVerifyId(settleDemanderDetailDto.getVerifyId());
                    workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.IN_SETTLE.getCode());
                    verifyListNew.add(workFeeVerify);
                    verifyIdListNew.add(settleDemanderDetailDto.getVerifyId());
                } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                    settleDemanderDetail.setWorkId(settleDemanderDetailDto.getWorkId());
                    WorkDeal workDeal = new WorkDeal();
                    workDeal.setWorkId(settleDemanderDetailDto.getWorkId());
                    workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
                    workDealListNew.add(workDeal);
                }
                settleDemanderDetailList.add(settleDemanderDetail);
            }
            if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
                List<WorkFeeVerifyDetail> verifyDetailListNew = this.workFeeVerifyDetailService.listByVerifyIdList(verifyIdListNew);
                if (CollectionUtil.isNotEmpty(verifyDetailListNew)) {
                    verifyDetailListNew.forEach(workFeeVerifyDetail -> {
                        WorkDeal workDeal = new WorkDeal();
                        workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                        workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
                        workDealListNew.add(workDeal);
                    });
                }
                settleDemander.setWorkQuantity(CollectionUtil.isNotEmpty(verifyDetailListNew) ? verifyDetailListNew.size() : 0);
            } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                settleDemander.setWorkQuantity(CollectionUtil.isNotEmpty(workDealListNew) ? workDealListNew.size() : 0);
                settleDemander.setStartDate(null);
                settleDemander.setEndDate(null);
            }
            // 保存新的结算单明细，且更新对账单以及工单结算状态
            this.settleDemanderDetailService.saveBatch(settleDemanderDetailList);
            if (CollectionUtil.isNotEmpty(verifyListNew)) {
                this.workFeeVerifyService.updateBatchById(verifyListNew);
            }
            if (CollectionUtil.isNotEmpty(workDealListNew)) {
                this.workDealService.updateBatchById(workDealListNew);
            }
            // 如果是按单结算，且添加了对账中的工单，则需要删除对账单中的该条明细
            if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                // 需要删除的对账单明细
                List<Long> deleteDetailIdList = new ArrayList<>();
                // 需要更新的对账单
                List<WorkFeeVerify> updateVerifyList = new ArrayList<>();
                // 需要删除的对账单
                List<Long> deleteVerifyIdList = new ArrayList<>();
                List<Long> workIdList = detailDtoListNew.stream().map(settleDemanderDetail -> settleDemanderDetail.getWorkId())
                        .collect(Collectors.toList());
                Map<Long, WorkFeeVerifyDetail> verifyDetailMap = this.workFeeVerifyDetailService.mapWorkIdAndDetail(workIdList);
                List<Long> workVerifyIdList = verifyDetailMap.values().stream().map(workFeeVerifyDetail -> workFeeVerifyDetail.getVerifyId())
                        .distinct().collect(Collectors.toList());
                Map<Long, WorkFeeVerify> workFeeVerifyMap = this.workFeeVerifyService.mapIdAndVerify(workVerifyIdList);
                for (SettleDemanderDetailDto detail : detailDtoListNew) {
                    WorkFeeVerifyDetail verifyDetail = verifyDetailMap.get(detail.getWorkId());
                    if (verifyDetail != null) {
                        deleteDetailIdList.add(verifyDetail.getDetailId());
                        if (workFeeVerifyMap.containsKey(verifyDetail.getVerifyId())) {
                            WorkFeeVerify workFeeVerify = workFeeVerifyMap.get(verifyDetail.getVerifyId());
                            if (workFeeVerify != null) {
                                workFeeVerifyMap.get(verifyDetail.getVerifyId()).setWorkQuantity(workFeeVerify.getWorkQuantity() - 1);
                            }
                        }
                    }
                }
                for (Map.Entry<Long, WorkFeeVerify> entry : workFeeVerifyMap.entrySet()) {
                    WorkFeeVerify workFeeVerify = entry.getValue();
                    if (workFeeVerify != null) {
                        if (workFeeVerify.getWorkQuantity().equals(0)) {
                            deleteVerifyIdList.add(workFeeVerify.getVerifyId());
                        } else {
                            updateVerifyList.add(workFeeVerify);
                        }
                    }
                }
                if (CollectionUtil.isNotEmpty(deleteDetailIdList)) {
                    this.workFeeVerifyDetailService.removeByIds(deleteDetailIdList);
                }
                if (CollectionUtil.isNotEmpty(deleteVerifyIdList)) {
                    this.workFeeVerifyService.removeByIds(deleteVerifyIdList);
                }
                if (CollectionUtil.isNotEmpty(updateVerifyList)) {
                    this.workFeeVerifyService.updateBatchById(updateVerifyList);
                }
            }
        }
        this.updateById(settleDemander);
    }

    /**
     * 生成结算单号
     *
     * @param settleDemanderDto
     * @return
     */
    @Override
    public String generateSettleCode(SettleDemanderDto settleDemanderDto) {
        if (settleDemanderDto == null ||
                LongUtil.isZero(settleDemanderDto.getServiceCorp()) ||
                LongUtil.isZero(settleDemanderDto.getDemanderCorp())) {
            throw new AppException("委托商编号不能为空");
        }
        if (LongUtil.isZero(settleDemanderDto.getContId()) || StringUtils.isEmpty(settleDemanderDto.getContNo())) {
            throw new AppException("委托协议号不能为空");
        }
        String formatDate = SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay()) ?
                "yyMM" : "yyMMdd";
        String prefix = settleDemanderDto.getContNo() + "-" + DateUtil.format(settleDemanderDto.getEndDate(), formatDate) + "-";
        return this.findMaxSettleCode(settleDemanderDto, prefix);
    }

    /**
     * 委托商确认费用根据对账单列表批量生成结算单(按单结算)
     *
     * @param workDealList
     * @param userId
     */
    @Override
    public void batchAddByWorkListAndDemander(List<WorkDeal> workDealList, Long userId, Long demanderCorp) {
        if (CollectionUtil.isEmpty(workDealList) || LongUtil.isZero(demanderCorp)) {
            return;
        }
        List<SettleDemanderDetail> detailList = new ArrayList<>();
        List<SettleDemander> settleDemanderList = new ArrayList<>();
        List<WorkDeal> workDealListNew = new ArrayList<>();
        List<Long> workIdList = workDealList.stream().map(workDeal -> workDeal.getWorkId()).collect(Collectors.toList());
        // 服务商与委托协议号映射
//        Map<Long, String> serviceAndContNoMap = this.demanderServiceService.mapServiceAndContNoByDemander(demanderCorp);
        Map<Long, List<DemanderContDto>> serviceAndContListMap = this.demanderContService.mapServiceAndContList(demanderCorp);
        // 结算单号前缀与最大编号映射
        Map<String, Integer> prefixAndMaxNoMap = this.mapPrefixAndMaxNoByDemander(workDealList, serviceAndContListMap);
        // 获取企业编号与银行账号映射
        List<Long> serviceCorpList = this.demanderServiceService.listServiceCorpIdsByDemander(demanderCorp);
        Map<Long, JSONObject> bankAccountMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(serviceCorpList)) {
            Result bankAccountResult = this.uasFeignService.mapCorpIdAndBankAccount(JsonUtil.toJson(serviceCorpList));
            if (Result.isSucceed(bankAccountResult)) {
                bankAccountMap = bankAccountResult.getData() == null ?
                        new HashMap<>() : JsonUtil.parseMap(JsonUtil.toJson(bankAccountResult.getData()));
            }
        }
        Map<Long, WorkFeeDto> workFeeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
        for (WorkDeal workDeal : workDealList) {
            // 委托协议号
            String contNo = null;
            List<DemanderContDto> contList = serviceAndContListMap.get(workDeal.getServiceCorp());
            DemanderContDto demanderContDto = null;
            if (CollectionUtil.isNotEmpty(contList)) {
                List<DemanderContDto> list = contList.stream().filter(dto ->
                        dto.getStartDate().compareTo(workDeal.getDispatchTime()) <= 0
                                && dto.getEndDate().compareTo(workDeal.getDispatchTime()) >= 0).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(list)) {
                    demanderContDto = list.get(0);
                    contNo = demanderContDto != null ? demanderContDto.getContNo() : "";
                }
            } else {
                continue;
            }
            if (demanderContDto == null || StringUtils.isEmpty(contNo)) {
                continue;
            }
            WorkFeeDto workFeeDto = workFeeMap.get(workDeal.getWorkId());
            if (workFeeDto == null || workFeeDto.getTotalFee() == null || BigDecimal.ZERO.equals(workFeeDto.getTotalFee())) {
                continue;
            }
            Long settleId = KeyUtil.getId();
            SettleDemander settleDemander = new SettleDemander();
            settleDemander.setSettleId(settleId);
            settleDemander.setServiceCorp(workDeal.getServiceCorp());
            settleDemander.setDemanderCorp(workDeal.getDemanderCorp());
            settleDemander.setSettleFee(workFeeDto.getTotalFee());
            settleDemander.setContId(demanderContDto.getId());
            settleDemander.setWorkQuantity(1);
            settleDemander.setStatus(SettleStatusEnum.UNCHECKED.getCode());
            settleDemander.setOperator(userId);
            settleDemander.setOperateTime(DateUtil.date());
            settleDemander.setNote("按单结算，自动生成");
            settleDemander.setStartDate(null);
            settleDemander.setEndDate(null);
            settleDemander.setSettleWay(SettleWayEnum.SETTLE_WORK.getCode());
            String prefix = contNo + "-" + DateUtil.format(workDeal.getEndTime(), "yyMMdd") + "-";
            // 生成结算单号
            int maxNo = prefixAndMaxNoMap.get(prefix) == null ? 0 : prefixAndMaxNoMap.get(prefix);
            DecimalFormat decimalFormat = new DecimalFormat(START_CONT_NO);
            settleDemander.setSettleCode(prefix + decimalFormat.format(maxNo + 1));
            prefixAndMaxNoMap.put(prefix, maxNo + 1);
            // 生成银行账号
            CorpBankAccountDto corpBankAccountDto = bankAccountMap.get(workDeal.getServiceCorp()) == null ?
                    null : JsonUtil.parseObject(JsonUtil.toJson(bankAccountMap.get(workDeal.getServiceCorp())), CorpBankAccountDto.class);
            if (corpBankAccountDto != null) {
                settleDemander.setAccountId(corpBankAccountDto.getAccountId());
                settleDemander.setAccountNumber(corpBankAccountDto.getAccountNumber());
                settleDemander.setAccountName(corpBankAccountDto.getAccountName());
                settleDemander.setAccountBank(corpBankAccountDto.getAccountBank());
            }
            settleDemanderList.add(settleDemander);
            // 生成结算单明细
            SettleDemanderDetail detail = new SettleDemanderDetail();
            detail.setDetailId(KeyUtil.getId());
            detail.setSettleId(settleId);
            detail.setWorkId(workDeal.getWorkId());
            detailList.add(detail);
            // 更新工单结算状态
            WorkDeal workDealNew = new WorkDeal();
            workDealNew.setWorkId(workDeal.getWorkId());
            workDealNew.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
            workDealListNew.add(workDealNew);
        }
        if (CollectionUtil.isNotEmpty(settleDemanderList)) {
            this.saveBatch(settleDemanderList);
        }
        if (CollectionUtil.isNotEmpty(detailList)) {
            this.settleDemanderDetailService.saveBatch(detailList);
        }
        if (CollectionUtil.isNotEmpty(workDealListNew)) {
            this.workDealService.updateBatchById(workDealListNew);
        }
    }

    /**
     * 自动确认费用根据对账单列表生成结算单(按单结算)
     *
     * @param workDealList
     * @param userId
     * @param serviceCorp
     */
    @Override
    public void batchAddByWorkListAndService(List<WorkDeal> workDealList, Long userId, Long serviceCorp) {
        if (CollectionUtil.isEmpty(workDealList)) {
            return;
        }
        List<SettleDemanderDetail> detailList = new ArrayList<>();
        List<SettleDemander> settleDemanderList = new ArrayList<>();
        List<WorkDeal> workDealListNew = new ArrayList<>();
        List<Long> workIdList = workDealList.stream().map(workDeal -> workDeal.getWorkId()).collect(Collectors.toList());
        // 委托商和委托协议号映射
        Map<Long, List<DemanderContDto>> demanderAndContListMap = this.demanderContService.mapDemanderAndContList(serviceCorp);
        // 结算单号前缀与最大编号映射
        Map<String, Integer> prefixAndMaxNoMap = this.mapPrefixAndMaxNoByService(workDealList, demanderAndContListMap);
        // 工单费用映射
        Map<Long, WorkFeeDto> workFeeMap = this.workFeeService.mapWorkIdAndFeeDto(workIdList);
        // 获取银行账号
        Result<CorpBankAccountDto> corpBankAccountDtoResult = this.uasFeignService.findCorpBankAccount(serviceCorp);
        CorpBankAccountDto corpBankAccountDto = null;
        if (Result.isSucceed(corpBankAccountDtoResult)) {
            corpBankAccountDto = corpBankAccountDtoResult.getData() == null ?
                    null : JsonUtil.parseObject(JsonUtil.toJson(corpBankAccountDtoResult.getData()), CorpBankAccountDto.class);
        }
        for (WorkDeal workDeal : workDealList) {
            // 委托协议号
            String contNo = null;
            DemanderContDto demanderContDto = null;
            List<DemanderContDto> contList = demanderAndContListMap.get(workDeal.getDemanderCorp());
            if (CollectionUtil.isNotEmpty(contList)) {
                List<DemanderContDto> list = contList.stream().filter(dto ->
                        dto.getStartDate().compareTo(workDeal.getDispatchTime()) <= 0
                                && dto.getEndDate().compareTo(workDeal.getDispatchTime()) >= 0).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(list)) {
                    demanderContDto = list.get(0);
                    contNo = demanderContDto != null ? list.get(0).getContNo() : "";
                }
            } else {
                continue;
            }
            if (demanderContDto == null || StringUtils.isEmpty(contNo)) {
                continue;
            }
            WorkFeeDto workFeeDto = workFeeMap.get(workDeal.getWorkId());
            if (workFeeDto == null || workFeeDto.getTotalFee() == null || BigDecimal.ZERO.equals(workFeeDto.getTotalFee())) {
                continue;
            }
            Long settleId = KeyUtil.getId();
            SettleDemander settleDemander = new SettleDemander();
            settleDemander.setSettleId(settleId);
            settleDemander.setServiceCorp(workDeal.getServiceCorp());
            settleDemander.setDemanderCorp(workDeal.getDemanderCorp());
            settleDemander.setContId(demanderContDto.getId());
            settleDemander.setSettleFee(workFeeDto.getTotalFee());
            settleDemander.setWorkQuantity(1);
            settleDemander.setStatus(SettleStatusEnum.UNCHECKED.getCode());
            settleDemander.setOperator(userId);
            settleDemander.setOperateTime(DateUtil.date());
            settleDemander.setNote("按单结算，自动生成");
            settleDemander.setStartDate(null);
            settleDemander.setEndDate(null);
            settleDemander.setSettleWay(SettleWayEnum.SETTLE_WORK.getCode());
            String prefix = contNo + "-" + DateUtil.format(workDeal.getEndTime(), "yyMMdd") + "-";
            // 生成结算单号
            int maxNo = prefixAndMaxNoMap.get(prefix) == null ? 0 : prefixAndMaxNoMap.get(prefix);
            DecimalFormat decimalFormat = new DecimalFormat(START_CONT_NO);
            settleDemander.setSettleCode(prefix + decimalFormat.format(maxNo + 1));
            prefixAndMaxNoMap.put(prefix, maxNo + 1);
            // 设置银行账号
            if (corpBankAccountDto != null) {
                settleDemander.setAccountId(corpBankAccountDto.getAccountId());
                settleDemander.setAccountNumber(corpBankAccountDto.getAccountNumber());
                settleDemander.setAccountName(corpBankAccountDto.getAccountName());
                settleDemander.setAccountBank(corpBankAccountDto.getAccountBank());
            }
            settleDemanderList.add(settleDemander);

            // 生成明细
            SettleDemanderDetail detail = new SettleDemanderDetail();
            detail.setDetailId(KeyUtil.getId());
            detail.setSettleId(settleId);
            detail.setWorkId(workDeal.getWorkId());
            detailList.add(detail);
            // 需要更新结算状态的工单
            WorkDeal workDealNew = new WorkDeal();
            workDealNew.setWorkId(workDeal.getWorkId());
            workDealNew.setSettleDemanderStatus(WorkSettleStatusEnum.IN_SETTLE.getCode());
            workDealListNew.add(workDealNew);

        }
        if (CollectionUtil.isNotEmpty(settleDemanderList)) {
            this.saveBatch(settleDemanderList);
        }
        if (CollectionUtil.isNotEmpty(detailList)) {
            this.settleDemanderDetailService.saveBatch(detailList);
        }
        if (CollectionUtil.isNotEmpty(workDealListNew)) {
            this.workDealService.updateBatchById(workDealListNew);
        }
    }

    /**
     * 查询可结算工单
     *
     * @param workFilter
     * @return
     */
    @Override
    public List<WorkFeeDto> listCanSettleWork(WorkFilter workFilter) {
        if (workFilter == null || LongUtil.isZero(workFilter.getServiceCorp()) || LongUtil.isZero(workFilter.getDemanderCorp())) {
            throw new AppException("服务商与委托商不能为空");
        }
        List<WorkFeeDto> workFeeDtoList = this.workFeeService.listByWorkFilter(workFilter);
        return workFeeDtoList;
    }

    /**
     * 根据前缀获取最大结算单号
     *
     * @param settleDemanderDto
     * @param prefix
     */
    private String findMaxSettleCode(SettleDemanderDto settleDemanderDto, String prefix) {
        String maxContNo = this.baseMapper.findMaxSettleCode(prefix, settleDemanderDto.getServiceCorp(), settleDemanderDto.getSettleId());
        if (StrUtil.isBlank(maxContNo)) {
            maxContNo = prefix + START_CONT_NO;
        }
        int code = Integer.parseInt(maxContNo.replace(prefix, "")) + 1;
        DecimalFormat decimalFormat = new DecimalFormat(START_CONT_NO);
        return prefix + decimalFormat.format(code);
    }

    /**
     * 根据服务商和工单列表获取结算单前缀与最大编号映射
     *
     * @param workDealList
     * @param demanderAndContNoMap
     * @return
     */
    private Map<String, Integer> mapPrefixAndMaxNoByService(List<WorkDeal> workDealList,
                                                            Map<Long, List<DemanderContDto>> demanderAndContNoMap) {
        Map<String, Integer> prefixAndMaxNoMap = new HashMap<>();
        List<String> prefixList = new ArrayList<>();
        for (WorkDeal workDeal : workDealList) {
            String contNo = null;
            List<DemanderContDto> contList = demanderAndContNoMap.get(workDeal.getDemanderCorp());
            if (CollectionUtil.isNotEmpty(contList)) {
                List<DemanderContDto> list = contList.stream().filter(dto ->
                        dto.getStartDate().compareTo(workDeal.getDispatchTime()) <= 0
                                && dto.getEndDate().compareTo(workDeal.getDispatchTime()) >= 0).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(list)) {
                    contNo = list.get(0) != null ? list.get(0).getContNo() : "";
                }
            } else {
                continue;
            }
            if (StringUtils.isEmpty(contNo)) {
                continue;
            }
            String prefix = contNo + "-" + DateUtil.format(workDeal.getEndTime(), "yyMMdd") + "-";
            prefixList.add(prefix);
        }
        List<SettleDemanderDto> maxNoList = this.baseMapper.listMaxSettleCode(prefixList);
        if (CollectionUtil.isNotEmpty(maxNoList)) {
            for (SettleDemanderDto settleDemanderDto : maxNoList) {
                String maxSettleCode = settleDemanderDto.getSettleCode();
                String maxNoStr = maxSettleCode.replace(settleDemanderDto.getPrefix(), "");
                Integer maxNo = Integer.parseInt(maxNoStr);
                prefixAndMaxNoMap.put(settleDemanderDto.getPrefix(), maxNo);
            }
        }
        return prefixAndMaxNoMap;
    }

    /**
     * 根据委托商和工单列表获取结算单前缀与最大编号映射
     *
     * @param workDealList
     * @return
     */
    private Map<String, Integer> mapPrefixAndMaxNoByDemander(List<WorkDeal> workDealList,
                                                             Map<Long, List<DemanderContDto>> serviceAndContNoMap) {
        Map<String, Integer> prefixAndMaxNoMap = new HashMap<>();
        List<String> prefixList = new ArrayList<>();
        for (WorkDeal workDeal : workDealList) {
            String contNo = null;
            List<DemanderContDto> contList = serviceAndContNoMap.get(workDeal.getServiceCorp());
            DemanderContDto demanderContDto = null;
            if (CollectionUtil.isNotEmpty(contList)) {
                List<DemanderContDto> list = contList.stream().filter(dto ->
                    dto.getStartDate().compareTo(workDeal.getDispatchTime()) <= 0
                            && dto.getEndDate().compareTo(workDeal.getDispatchTime()) >= 0).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(list)) {
                    demanderContDto = list.get(0);
                    contNo = demanderContDto != null ? demanderContDto.getContNo() : "";
                }
            } else {
                continue;
            }
//            String contNo = serviceAndContNoMap.get(workDeal.getServiceCorp());
            if (StringUtils.isEmpty(contNo)) {
                continue;
            }
            String prefix = contNo + "-" + DateUtil.format(workDeal.getEndTime(), "yyMMdd") + "-";
            prefixList.add(prefix);
        }
        List<SettleDemanderDto> maxNoList = this.baseMapper.listMaxSettleCode(prefixList);
        if (CollectionUtil.isNotEmpty(maxNoList)) {
            for (SettleDemanderDto settleDemanderDto : maxNoList) {
                String maxSettleCode = settleDemanderDto.getSettleCode();
                String maxNoStr = maxSettleCode.replace(settleDemanderDto.getPrefix(), "");
                Integer maxNo = Integer.parseInt(maxNoStr);
                prefixAndMaxNoMap.put(settleDemanderDto.getPrefix(), maxNo);
            }
        }
        return prefixAndMaxNoMap;
    }

    /**
     * 校验数据
     *
     * @param settleDemanderDto
     * @return
     * @author canlei
     */
    private String checkData(SettleDemanderDto settleDemanderDto) {
        StringBuilder note = new StringBuilder(8);
        if (settleDemanderDto == null) {
            note.append("参数不能为空");
        }
        if (LongUtil.isZero(settleDemanderDto.getServiceCorp())) {
            note.append("<br>服务商不能为空");
        }
        if (LongUtil.isZero(settleDemanderDto.getDemanderCorp())) {
            note.append("<br>委托商不能为空");
        }
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
            if (settleDemanderDto.getStartDate() == null) {
                note.append("<br>起始日期不能为空");
            }
            if (settleDemanderDto.getEndDate() == null) {
                note.append("<br>结束日期不能为空");
            }
        }
        if (settleDemanderDto.getSettleFee() == null || settleDemanderDto.getSettleFee().compareTo(BigDecimal.ZERO) <= 0) {
            note.append("<br>结算总金额应大于0");
        }
        if (IntUtil.isZero(settleDemanderDto.getSettleWay())) {
            note.append("<br>请选择结算方式");
        }
        if (CollectionUtil.isEmpty(settleDemanderDto.getDetailDtoList())) {
            if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(settleDemanderDto.getSettleWay())) {
                throw new AppException("按周期结算，请至少选中一条对账单记录");
            } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(settleDemanderDto.getSettleWay())) {
                throw new AppException("按单结算，请至少选中一条工单记录");
            }
        }
//        if (StringUtils.isNotEmpty(settleDemanderDto.getSettleCode())) {
//            QueryWrapper<SettleDemander> queryWrapper = new QueryWrapper<>();
//            queryWrapper.eq("cont_no", settleDemanderDto.getContNo());
//            if (LongUtil.isNotZero(settleDemanderDto.getSettleId())) {
//                queryWrapper.ne("settle_id", settleDemanderDto.getSettleId());
//            }
//            List<SettleDemander> settleDemanderList = this.list(queryWrapper);
//            if (CollectionUtil.isNotEmpty(settleDemanderList)) {
//                throw new AppException("合同号已存在，请重新生成");
//            }
//        }
        return note.toString();
    }

}
