package com.zjft.usp.anyfix.settle.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.settle.composite.SettleDemanderPaymentCompoService;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderOnlinePaymentDto;
import com.zjft.usp.anyfix.settle.dto.SettleDemanderPaymentDto;
import com.zjft.usp.anyfix.settle.enums.*;
import com.zjft.usp.anyfix.settle.model.SettleDemander;
import com.zjft.usp.anyfix.settle.model.SettleDemanderDetail;
import com.zjft.usp.anyfix.settle.model.SettleDemanderPayment;
import com.zjft.usp.anyfix.settle.service.SettleDemanderDetailService;
import com.zjft.usp.anyfix.settle.service.SettleDemanderPaymentService;
import com.zjft.usp.anyfix.settle.service.SettleDemanderService;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.deal.service.WorkDealService;
import com.zjft.usp.anyfix.work.fee.enums.WorkFeeVerifySettleStatusEnum;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerify;
import com.zjft.usp.anyfix.work.fee.model.WorkFeeVerifyDetail;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyDetailService;
import com.zjft.usp.anyfix.work.fee.service.WorkFeeVerifyService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 委托商结算付款聚合服务实现类
 *
 * @author zgpi
 * @date 2020/4/30 11:11
 */
@Slf4j
@Service
@Transactional(rollbackFor = Exception.class)
public class SettleDemanderPaymentCompoServiceImpl implements SettleDemanderPaymentCompoService {

    @Autowired
    private SettleDemanderPaymentService settleDemanderPaymentService;
    @Autowired
    private SettleDemanderService settleDemanderService;
    @Autowired
    private SettleDemanderDetailService settleDemanderDetailService;
    @Autowired
    private WorkDealService workDealService;
    @Autowired
    private WorkFeeVerifyDetailService workFeeVerifyDetailService;
    @Autowired
    private WorkFeeVerifyService workFeeVerifyService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private FileFeignService fileFeignService;

    /**
     * 付款信息详情
     *
     * @param settleId
     * @return
     * @author zgpi
     * @date 2020/5/5 11:50
     **/
    @Override
    public SettleDemanderPaymentDto viewSettleDemanderPayment(Long settleId) {
        SettleDemanderPayment settleDemanderPayment = settleDemanderPaymentService
                .getOne(new QueryWrapper<SettleDemanderPayment>()
                        .eq("settle_id", settleId));
        if (settleDemanderPayment == null) {
            return null;
        }
        SettleDemanderPaymentDto settleDemanderPaymentDto = new SettleDemanderPaymentDto();
        BeanUtils.copyProperties(settleDemanderPayment, settleDemanderPaymentDto);
        Set<Long> userIdSet = new HashSet<>();
        userIdSet.add(settleDemanderPayment.getInvoiceUser());
        userIdSet.add(settleDemanderPayment.getPayer());
        userIdSet.add(settleDemanderPayment.getPayOperator());
        userIdSet.add(settleDemanderPayment.getReceiptUser());
        Result<Map<Long, String>> userMapResult = uasFeignService
                .mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet));
        Map<Long, String> userMap = new HashMap<>();
        if (userMapResult != null && userMapResult.getCode() == Result.SUCCESS) {
            userMap = userMapResult.getData();
        }
        settleDemanderPaymentDto.setInvoiceUserName(StrUtil.trimToEmpty(userMap.get(settleDemanderPayment.getInvoiceUser())));
        settleDemanderPaymentDto.setPayOperatorName(StrUtil.trimToEmpty(userMap.get(settleDemanderPayment.getPayOperator())));
        settleDemanderPaymentDto.setReceiptUserName(StrUtil.trimToEmpty(userMap.get(settleDemanderPayment.getReceiptUser())));
        this.findFileList(settleDemanderPaymentDto, settleDemanderPayment);
        return settleDemanderPaymentDto;
    }

    /**
     * 开票
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/30 10:46
     **/
    @Override
    public void invoice(SettleDemanderPaymentDto settleDemanderPaymentDto, Long curUserId) {
        if (settleDemanderPaymentDto.getInvoiceTime() == null) {
            throw new AppException("开票日期不能为空！");
        }
        Date today = DateUtil.parse(DateUtil.today()).toJdkDate();
        String date = DateUtil.format(settleDemanderPaymentDto.getInvoiceTime(), "yyyy-MM-dd");
        Date invoiceTime = DateUtil.parse(date).toJdkDate();
        if (invoiceTime.after(today)) {
            throw new AppException("开票日期不能晚于今天！");
        }
        settleDemanderPaymentDto.setInvoiceTime(invoiceTime);
        Long settleId = settleDemanderPaymentDto.getSettleId();
        SettleDemander dbSettleDemander = settleDemanderService.getById(settleId);
        if (dbSettleDemander == null) {
            throw new AppException("结算单不存在！");
        }
        SettleDemanderPayment settleDemanderPayment = settleDemanderPaymentService
                .getOne(new QueryWrapper<SettleDemanderPayment>()
                        .eq("settle_id", settleId));
        if (settleDemanderPayment == null) {
            settleDemanderPayment = new SettleDemanderPayment();
            BeanUtils.copyProperties(settleDemanderPaymentDto, settleDemanderPayment);
            settleDemanderPayment.setSettleId(settleId);
            settleDemanderPayment.setPayId(KeyUtil.getId());
        }
        settleDemanderPayment.setInvoiceUser(curUserId);
        settleDemanderPayment.setInvoiceTime(settleDemanderPaymentDto.getInvoiceTime());
        settleDemanderPayment.setInvoiceOperateTime(DateUtil.date());
        List<Long> fileIdList = settleDemanderPaymentDto.getInvoiceFileIdList();
        if (CollectionUtil.isNotEmpty(fileIdList)) {
            settleDemanderPayment.setInvoiceFiles(StrUtil.join(",", fileIdList));
            // 删除临时文件表数据
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }
        settleDemanderPaymentService.saveOrUpdate(settleDemanderPayment);
        // 修改结算单开票状态
        SettleDemander settleDemander = new SettleDemander();
        settleDemander.setSettleId(settleId);
        settleDemander.setInvoiceStatus(SettleInvoiceStatusEnum.INVOICED.getCode());
        settleDemanderService.updateById(settleDemander);
    }

    /**
     * 在线付款
     *
     * @param settleDemanderOnlinePaymentDto
     * @author CK
     */
    @Override
    public void payOnline(SettleDemanderOnlinePaymentDto settleDemanderOnlinePaymentDto) {
        log.info("在线付款,{}", settleDemanderOnlinePaymentDto);
        Long curUserId = settleDemanderOnlinePaymentDto.getPayOperator();
        SettleDemanderPaymentDto paydto = new SettleDemanderPaymentDto();
        paydto.setPayMethod(SettlePayMethodEnum.PAY_ONLINE.getCode());
        paydto.setSettleId(settleDemanderOnlinePaymentDto.getSettleId());
        paydto.setPayOperator(settleDemanderOnlinePaymentDto.getPayOperator());
        paydto.setPayTime(settleDemanderOnlinePaymentDto.getPayOperateTime());
        this.pay(paydto, curUserId); // 付款

        paydto.setReceiptTime(DateUtil.date());
        this.receipt(paydto, curUserId); // 收款
    }

    /**
     * 线下支付
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @author CK
     */
    @Override
    public void payOffline(SettleDemanderPaymentDto settleDemanderPaymentDto, Long curUserId) {
        if (settleDemanderPaymentDto.getPayTime() == null) {
            throw new AppException("付款日期不能为空！");
        }
        Date today = DateUtil.parse(DateUtil.today()).toJdkDate();
        String date = DateUtil.format(settleDemanderPaymentDto.getPayTime(), "yyyy-MM-dd");
        Date payTime = DateUtil.parse(date).toJdkDate();
        if (payTime.after(today)) {
            throw new AppException("付款日期不能晚于今天！");
        }
        settleDemanderPaymentDto.setPayTime(payTime);
        Long settleId = settleDemanderPaymentDto.getSettleId();
        SettleDemander dbSettleDemander = settleDemanderService.getById(settleId);
        if (dbSettleDemander == null) {
            throw new AppException("结算单不存在！");
        }
//        if (!SettleStatusEnum.CHECK_PASS.getCode().equals(dbSettleDemander.getStatus())) {
//            throw new AppException("结算单未确认通过，不能付款！");
//        }

        settleDemanderPaymentDto.setPayMethod(SettlePayMethodEnum.PAY_ONLINE.getCode());
        this.pay(settleDemanderPaymentDto, curUserId);
    }

    /**
     * 付款
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/30 17:03
     **/
    public void pay(SettleDemanderPaymentDto settleDemanderPaymentDto, Long curUserId) {
        Long settleId = settleDemanderPaymentDto.getSettleId();
        SettleDemanderPayment settleDemanderPayment = settleDemanderPaymentService
                .getOne(new QueryWrapper<SettleDemanderPayment>()
                        .eq("settle_id", settleId));
        if (settleDemanderPayment == null) {
            settleDemanderPayment = new SettleDemanderPayment();
            BeanUtils.copyProperties(settleDemanderPaymentDto, settleDemanderPayment);
            settleDemanderPayment.setSettleId(settleId);
            settleDemanderPayment.setPayId(KeyUtil.getId());
        }
        settleDemanderPayment.setPayer(settleDemanderPaymentDto.getPayer());
        settleDemanderPayment.setPayerName(settleDemanderPaymentDto.getPayerName());
        settleDemanderPayment.setPayTime(settleDemanderPaymentDto.getPayTime());
        // 默认为线下支付
        settleDemanderPayment.setPayMethod(settleDemanderPaymentDto.getPayMethod());
        settleDemanderPayment.setPayOperator(curUserId);
        settleDemanderPayment.setPayOperateTime(DateUtil.date());
        List<Long> fileIdList = settleDemanderPaymentDto.getPayFileIdList();
        if (CollectionUtil.isNotEmpty(fileIdList)) {
            settleDemanderPayment.setPayFiles(StrUtil.join(",", fileIdList));
            // 删除临时文件表数据
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }
        settleDemanderPaymentService.saveOrUpdate(settleDemanderPayment);
        // 修改结算单付款状态
        SettleDemander settleDemander = this.settleDemanderService.getById(settleId);
        if (settleDemander != null) {
            // 未确认通过的结算单置为确认通过
            if (!SettleStatusEnum.CHECK_PASS.getCode().equals(settleDemander.getStatus())) {
                settleDemander.setStatus(SettleStatusEnum.CHECK_PASS.getCode());
                settleDemander.setCheckTime(DateTime.now());
                settleDemander.setCheckUser(curUserId);
            }
            settleDemander.setPayStatus(SettlePayStatusEnum.TO_RECEIPT.getCode());
            settleDemanderService.updateById(settleDemander);
        }
    }

    /**
     * 收款
     *
     * @param settleDemanderPaymentDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/5/3 15:26
     **/
    @Override
    public void receipt(SettleDemanderPaymentDto settleDemanderPaymentDto, Long curUserId) {
        if (settleDemanderPaymentDto.getReceiptTime() == null) {
            throw new AppException("收款日期不能为空！");
        }
        Date today = DateUtil.parse(DateUtil.today()).toJdkDate();
        String date = DateUtil.format(settleDemanderPaymentDto.getReceiptTime(), "yyyy-MM-dd");
        Date receiptTime = DateUtil.parse(date).toJdkDate();
        if (receiptTime.after(today)) {
            throw new AppException("收款日期不能晚于当前今天！");
        }
        settleDemanderPaymentDto.setReceiptTime(receiptTime);
        Long settleId = settleDemanderPaymentDto.getSettleId();
        SettleDemander dbSettleDemander = settleDemanderService.getById(settleId);
        if (dbSettleDemander == null) {
            throw new AppException("结算单不存在！");
        }
        if (SettlePayStatusEnum.RECEIPTED.getCode().equals(dbSettleDemander.getPayStatus())) {
            throw new AppException("结算单已收款，不能重复收款！");
        }

        SettleDemanderPayment settleDemanderPayment = settleDemanderPaymentService
                .getOne(new QueryWrapper<SettleDemanderPayment>()
                        .eq("settle_id", settleId));
        if (settleDemanderPayment == null) {
            settleDemanderPayment = new SettleDemanderPayment();
            BeanUtils.copyProperties(settleDemanderPaymentDto, settleDemanderPayment);
            settleDemanderPayment.setSettleId(settleId);
            settleDemanderPayment.setPayId(KeyUtil.getId());
        }
        settleDemanderPayment.setReceiptUser(curUserId);
        settleDemanderPayment.setReceiptTime(settleDemanderPaymentDto.getReceiptTime());
        settleDemanderPayment.setReceiptOperateTime(DateUtil.date());
        List<Long> fileIdList = settleDemanderPaymentDto.getReceiptFileIdList();
        if (CollectionUtil.isNotEmpty(fileIdList)) {
            settleDemanderPayment.setReceiptFiles(StrUtil.join(",", fileIdList));
            // 删除临时文件表数据
            this.fileFeignService.deleteFileTemporaryByFileIdList(fileIdList);
        }
        settleDemanderPaymentService.saveOrUpdate(settleDemanderPayment);

        // 修改结算单付款状态
        SettleDemander settleDemander = new SettleDemander();
        settleDemander.setSettleId(settleId);
        settleDemander.setStatus(SettleStatusEnum.CHECK_PASS.getCode());
        settleDemander.setPayStatus(SettlePayStatusEnum.RECEIPTED.getCode());
        settleDemanderService.updateById(settleDemander);
        List<WorkFeeVerify> workFeeVerifyList = new ArrayList<>();
        List<WorkDeal> workDealList = new ArrayList<>();
        List<Long> verifyIdList = new ArrayList<>();
        List<SettleDemanderDetail> list = this.settleDemanderDetailService.listBySettleId(settleId);
        if (SettleWayEnum.SETTLE_PERIOD.getCode().equals(dbSettleDemander.getSettleWay())) {
            if (CollectionUtil.isNotEmpty(list)) {
                for (SettleDemanderDetail settleDemanderDetail : list) {
                    WorkFeeVerify workFeeVerify = new WorkFeeVerify();
                    workFeeVerify.setVerifyId(settleDemanderDetail.getVerifyId());
                    workFeeVerify.setSettleStatus(WorkFeeVerifySettleStatusEnum.SETTLED.getCode());
                    workFeeVerifyList.add(workFeeVerify);
                    verifyIdList.add(settleDemanderDetail.getVerifyId());
                }
                if (CollectionUtil.isNotEmpty(verifyIdList)) {
                    List<WorkFeeVerifyDetail> workFeeVerifyDetailList = this.workFeeVerifyDetailService.listByVerifyIdList(verifyIdList);
                    if (CollectionUtil.isNotEmpty(workFeeVerifyDetailList)) {
                        for (WorkFeeVerifyDetail workFeeVerifyDetail : workFeeVerifyDetailList) {
                            WorkDeal workDeal = new WorkDeal();
                            workDeal.setWorkId(workFeeVerifyDetail.getWorkId());
                            workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.SETTLED.getCode());
                            workDealList.add(workDeal);
                        }
                    }
                }
            }
        } else if (SettleWayEnum.SETTLE_WORK.getCode().equals(dbSettleDemander.getSettleWay())) {
            if (CollectionUtil.isNotEmpty(list)) {
                for (SettleDemanderDetail settleDemanderDetail : list) {
                    WorkDeal workDeal = new WorkDeal();
                    workDeal.setWorkId(settleDemanderDetail.getWorkId());
                    workDeal.setSettleDemanderStatus(WorkSettleStatusEnum.SETTLED.getCode());
                    workDealList.add(workDeal);
                }
            }
        }
        // 将关联对账单结算状态置为已结算
        if (CollectionUtil.isNotEmpty(workFeeVerifyList)) {
            this.workFeeVerifyService.updateBatchById(workFeeVerifyList);
        }
        // 将关联工单结算状态置为已结算
        if (CollectionUtil.isNotEmpty(workDealList)) {
            this.workDealService.updateBatchById(workDealList);
        }
    }

    /**
     * 获得付款附件信息
     *
     * @param settleDemanderPaymentDto
     * @param settleDemanderPayment
     * @return
     * @author zgpi
     * @date 2020/5/5 13:55
     **/
    private void findFileList(SettleDemanderPaymentDto settleDemanderPaymentDto,
                              SettleDemanderPayment settleDemanderPayment) {
        List<Long> payFileIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(settleDemanderPayment.getPayFiles())) {
            long[] fileIdList = StrUtil.splitToLong(settleDemanderPayment.getPayFiles(), ",");
            payFileIdList = Arrays.stream(fileIdList).boxed().collect(Collectors.toList());
            settleDemanderPaymentDto.setPayFileIdList(payFileIdList);
        }
        List<Long> invoiceFileIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(settleDemanderPayment.getInvoiceFiles())) {
            long[] fileIdList = StrUtil.splitToLong(settleDemanderPayment.getInvoiceFiles(), ",");
            invoiceFileIdList = Arrays.stream(fileIdList).boxed().collect(Collectors.toList());
            settleDemanderPaymentDto.setInvoiceFileIdList(invoiceFileIdList);
        }
        List<Long> receiptFileIdList = new ArrayList<>();
        if (StrUtil.isNotBlank(settleDemanderPayment.getReceiptFiles())) {
            long[] fileIdList = StrUtil.splitToLong(settleDemanderPayment.getReceiptFiles(), ",");
            receiptFileIdList = Arrays.stream(fileIdList).boxed().collect(Collectors.toList());
            settleDemanderPaymentDto.setReceiptFileIdList(receiptFileIdList);
        }
        List<Long> fileIdList = new ArrayList<>();
        fileIdList.addAll(payFileIdList);
        fileIdList.addAll(invoiceFileIdList);
        fileIdList.addAll(receiptFileIdList);
        Result fileResult = fileFeignService.listFileDtoByIdList(JsonUtil.toJson(fileIdList));
        List<FileInfoDto> fileInfoDtoList = null;
        if (fileResult.getCode() == Result.SUCCESS) {
            fileInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(fileResult.getData()), FileInfoDto.class);
        }

        List<FileInfoDto> invoiceFileList = new ArrayList<>();
        List<FileInfoDto> payFileList = new ArrayList<>();
        List<FileInfoDto> receiptFileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(fileInfoDtoList)) {
            for (FileInfoDto fileInfoDto : fileInfoDtoList) {
                if (CollectionUtil.isNotEmpty(invoiceFileIdList)
                        && invoiceFileIdList.contains(fileInfoDto.getFileId())) {
                    invoiceFileList.add(fileInfoDto);
                }
                if (CollectionUtil.isNotEmpty(payFileIdList)
                        && payFileIdList.contains(fileInfoDto.getFileId())) {
                    payFileList.add(fileInfoDto);
                }
                if (CollectionUtil.isNotEmpty(receiptFileIdList)
                        && receiptFileIdList.contains(fileInfoDto.getFileId())) {
                    receiptFileList.add(fileInfoDto);
                }
            }
        }
        settleDemanderPaymentDto.setInvoiceFileList(invoiceFileList);
        settleDemanderPaymentDto.setPayFileList(payFileList);
        settleDemanderPaymentDto.setReceiptFileList(receiptFileList);
    }
}
