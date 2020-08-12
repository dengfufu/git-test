package com.zjft.usp.anyfix.goods.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.anyfix.baseinfo.service.ExpressCompanyService;
import com.zjft.usp.anyfix.common.constant.RightConstants;
import com.zjft.usp.anyfix.common.feign.dto.FileInfoDto;
import com.zjft.usp.anyfix.common.service.DataScopeCompoService;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.goods.composite.GoodsPostCompoService;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDetailDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostSignDto;
import com.zjft.usp.anyfix.goods.dto.GoodsPostWorkDto;
import com.zjft.usp.anyfix.goods.enums.*;
import com.zjft.usp.anyfix.goods.filter.GoodsPostFilter;
import com.zjft.usp.anyfix.goods.model.*;
import com.zjft.usp.anyfix.goods.service.*;
import com.zjft.usp.anyfix.utils.BusinessCodeGenerator;
import com.zjft.usp.common.dto.RightScopeDto;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.service.FileFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 * 物品寄送聚合服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class GoodsPostCompoServiceImpl implements GoodsPostCompoService {

    @Autowired
    private BusinessCodeGenerator businessCodeGenerator;
    @Autowired
    private GoodsPostService goodsPostService;
    @Autowired
    private GoodsPostWorkService goodsPostWorkService;
    @Autowired
    private GoodsPostFileService goodsPostFileService;
    @Autowired
    private GoodsPostDetailService goodsPostDetailService;
    @Autowired
    private GoodsPostDetailFileService goodsPostDetailFileService;
    @Autowired
    private ExpressCompanyService expressCompanyService;

    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private DataScopeCompoService dataScopeCompoService;

    @Resource
    private UasFeignService uasFeignService;
    @Resource
    private FileFeignService fileFeignService;

    /**
     * 分页查询物品寄送
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/16 20:37
     */
    @Override
    public ListWrapper<GoodsPostDto> queryGoodsPost(GoodsPostFilter goodsPostFilter,
                                                    Long curUserId,
                                                    Long curCorpId) {
        Page<GoodsPostDto> page = new Page(goodsPostFilter.getPageNum(), goodsPostFilter.getPageSize());

        if (LongUtil.isZero(goodsPostFilter.getCorpId())) {
            return ListWrapper.<GoodsPostDto>builder()
                    .list(new ArrayList<>())
                    .total(0L)
                    .build();
        }

        // 获得范围权限
        this.findUserRight(goodsPostFilter, curUserId, curCorpId);

        List<GoodsPostDto> goodsPostDtoList = goodsPostService.queryGoodsPost(page, goodsPostFilter);
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> corpIdSet = new HashSet<>();
        Set<Long> branchIdSet = new HashSet<>();
        Set<String> areaCodeSet = new HashSet<>();
        for (GoodsPostDto goodsPostDto : goodsPostDtoList) {
            userIdSet.add(goodsPostDto.getSigner());
            corpIdSet.add(goodsPostDto.getConsignCorp());
            corpIdSet.add(goodsPostDto.getReceiveCorp());
            branchIdSet.add(goodsPostDto.getConsignBranch());
            branchIdSet.add(goodsPostDto.getReceiveBranch());
            areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getConsignArea()));
            areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getReceiveArea()));
        }
        userIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> userMap = null;
        Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet));
        if (Result.isSucceed(userMapResult)) {
            userMap = userMapResult.getData();
            userMap = userMap == null ? new HashMap<>() : userMap;
        }
        corpIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> corpMap = null;
        Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet));
        if (Result.isSucceed(corpMapResult)) {
            corpMap = corpMapResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;
        }
        branchIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> serviceBranchMap = serviceBranchService.mapServiceBranchByBranchIdList(branchIdSet);
        serviceBranchMap = serviceBranchMap == null ? new HashMap<>() : serviceBranchMap;
        Map<Long, String> deviceBranchMap = serviceBranchService.mapServiceBranchByBranchIdList(branchIdSet);
        deviceBranchMap = deviceBranchMap == null ? new HashMap<>() : deviceBranchMap;

        Result<Map<String, String>> areaMapResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeSet));
        Map<String, String> areaMap = null;
        if (Result.isSucceed(areaMapResult)) {
            areaMap = areaMapResult.getData();
            areaMap = areaMap == null ? new HashMap<>() : areaMap;
        }

        for (GoodsPostDto goodsPostDto : goodsPostDtoList) {
            goodsPostDto.setSignStatusName(SignStatusEnum.getNameByCode(goodsPostDto.getSignStatus()));
            goodsPostDto.setTransportTypeName(TransportTypeEnum.getNameByCode(goodsPostDto.getTransportType()));
            goodsPostDto.setConsignTypeName(ConsignTypeEnum.getNameByCode(goodsPostDto.getConsignType()));
            goodsPostDto.setExpressTypeName(ExpressTypeEnum.getNameByCode(goodsPostDto.getExpressType()));
            goodsPostDto.setPayWayName(PayWayEnum.getNameByCode(goodsPostDto.getPayWay()));
            goodsPostDto.setConsignCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getConsignCorp())));
            if (LongUtil.isNotZero(goodsPostDto.getConsignBranch())) {
                goodsPostDto.setConsignBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getConsignBranch())));
                if (StrUtil.isNotBlank(goodsPostDto.getConsignBranchName())) {
                    goodsPostDto.setConsignBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(goodsPostDto.getConsignBranch())));
                }
            }
            goodsPostDto.setReceiveCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getReceiveCorp())));
            if (LongUtil.isNotZero(goodsPostDto.getReceiveBranch())) {
                goodsPostDto.setReceiveBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getReceiveBranch())));
                if (StrUtil.isNotBlank(goodsPostDto.getReceiveBranchName())) {
                    goodsPostDto.setReceiveBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(goodsPostDto.getReceiveBranch())));
                }
            }
            goodsPostDto.setSignerName(StrUtil.trimToEmpty(userMap.get(goodsPostDto.getSigner())));
            this.setAreaName(goodsPostDto, areaMap);
        }
        return ListWrapper.<GoodsPostDto>builder()
                .list(goodsPostDtoList)
                .total(page.getTotal())
                .build();
    }

    /**
     * 导出物品寄送单
     *
     * @param goodsPostFilter
     * @param curUserId
     * @param curCorpId
     * @return
     * @author zgpi
     * @date 2020/4/27 20:37
     */
    @Override
    public List<GoodsPostDto> exportGoodsPost(GoodsPostFilter goodsPostFilter, Long curUserId, Long curCorpId) {
        if (LongUtil.isZero(goodsPostFilter.getCorpId())) {
            return new ArrayList<>();
        }

        // 获得范围权限
        this.findUserRight(goodsPostFilter, curUserId, curCorpId);

        List<GoodsPostDto> goodsPostDtoList = goodsPostService.queryGoodsPost(null, goodsPostFilter);
        Set<Long> userIdSet = new HashSet<>();
        Set<Long> corpIdSet = new HashSet<>();
        Set<Long> branchIdSet = new HashSet<>();
        Set<String> areaCodeSet = new HashSet<>();
        for (GoodsPostDto goodsPostDto : goodsPostDtoList) {
            userIdSet.add(goodsPostDto.getSigner());
            corpIdSet.add(goodsPostDto.getConsignCorp());
            corpIdSet.add(goodsPostDto.getReceiveCorp());
            branchIdSet.add(goodsPostDto.getConsignBranch());
            branchIdSet.add(goodsPostDto.getReceiveBranch());
            areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getConsignArea()));
            areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getReceiveArea()));
        }
        userIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> userMap = null;
        Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet));
        if (Result.isSucceed(userMapResult)) {
            userMap = userMapResult.getData();
            userMap = userMap == null ? new HashMap<>() : userMap;
        }
        corpIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> corpMap = null;
        Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet));
        if (Result.isSucceed(corpMapResult)) {
            corpMap = corpMapResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;
        }
        branchIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> serviceBranchMap = serviceBranchService.mapServiceBranchByBranchIdList(branchIdSet);
        serviceBranchMap = serviceBranchMap == null ? new HashMap<>() : serviceBranchMap;
        Map<Long, String> deviceBranchMap = serviceBranchService.mapServiceBranchByBranchIdList(branchIdSet);
        deviceBranchMap = deviceBranchMap == null ? new HashMap<>() : deviceBranchMap;

        Result<Map<String, String>> areaMapResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeSet));
        Map<String, String> areaMap = null;
        if (Result.isSucceed(areaMapResult)) {
            areaMap = areaMapResult.getData();
            areaMap = areaMap == null ? new HashMap<>() : areaMap;
        }

        for (GoodsPostDto goodsPostDto : goodsPostDtoList) {
            goodsPostDto.setSignStatusName(SignStatusEnum.getNameByCode(goodsPostDto.getSignStatus()));
            goodsPostDto.setTransportTypeName(TransportTypeEnum.getNameByCode(goodsPostDto.getTransportType()));
            goodsPostDto.setConsignTypeName(ConsignTypeEnum.getNameByCode(goodsPostDto.getConsignType()));
            goodsPostDto.setExpressTypeName(ExpressTypeEnum.getNameByCode(goodsPostDto.getExpressType()));
            goodsPostDto.setPayWayName(PayWayEnum.getNameByCode(goodsPostDto.getPayWay()));
            goodsPostDto.setConsignCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getConsignCorp())));
            if (LongUtil.isNotZero(goodsPostDto.getConsignBranch())) {
                goodsPostDto.setConsignBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getConsignBranch())));
                if (StrUtil.isNotBlank(goodsPostDto.getConsignBranchName())) {
                    goodsPostDto.setConsignBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(goodsPostDto.getConsignBranch())));
                }
            }
            goodsPostDto.setReceiveCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getReceiveCorp())));
            if (LongUtil.isNotZero(goodsPostDto.getReceiveBranch())) {
                goodsPostDto.setReceiveBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getReceiveBranch())));
                if (StrUtil.isNotBlank(goodsPostDto.getReceiveBranchName())) {
                    goodsPostDto.setReceiveBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(goodsPostDto.getReceiveBranch())));
                }
            }
            goodsPostDto.setSignerName(StrUtil.trimToEmpty(userMap.get(goodsPostDto.getSigner())));
            this.setAreaName(goodsPostDto, areaMap);
        }
        return goodsPostDtoList;
    }

    /**
     * 查看物品寄送单详情
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/22 13:52
     */
    @Override
    public GoodsPostDto viewGoodsPost(Long postId) {
        GoodsPost goodsPost = goodsPostService.getById(postId);
        List<GoodsPostWork> goodsPostWorkList = goodsPostWorkService
                .list(new QueryWrapper<GoodsPostWork>().eq("post_id", postId)
                        .orderByAsc("work_id"));
        List<GoodsPostFile> goodsPostFileList = goodsPostFileService
                .list(new QueryWrapper<GoodsPostFile>().eq("post_id", postId)
                        .orderByAsc("post_id"));
        List<GoodsPostDetail> goodsPostDetailList = goodsPostDetailService
                .list(new QueryWrapper<GoodsPostDetail>().eq("post_id", postId)
                        .orderByAsc("goods_name, id"));
        List<GoodsPostDetailFile> goodsPostDetailFileList = goodsPostDetailFileService
                .list(new QueryWrapper<GoodsPostDetailFile>().eq("post_id", postId));
        GoodsPostDto goodsPostDto = new GoodsPostDto();
        if (goodsPost != null) {
            BeanUtils.copyProperties(goodsPost, goodsPostDto);
        }
        if (CollectionUtil.isNotEmpty(goodsPostWorkList)) {
            List<GoodsPostWorkDto> goodsPostWorkDtoList = new ArrayList<>();
            GoodsPostWorkDto goodsPostWorkDto;
            for (GoodsPostWork goodsPostWork : goodsPostWorkList) {
                goodsPostWorkDto = new GoodsPostWorkDto();
                BeanUtils.copyProperties(goodsPostWork, goodsPostWorkDto);
                goodsPostWorkDtoList.add(goodsPostWorkDto);
            }
            goodsPostDto.setGoodsPostWorkDtoList(goodsPostWorkDtoList);
        }
        if (CollectionUtil.isNotEmpty(goodsPostFileList)) {
            goodsPostDto.setGoodsPostFileList(goodsPostFileList);
        }
        Set<Long> userIdSet = new HashSet<>();
        if (CollectionUtil.isNotEmpty(goodsPostDetailList)) {
            userIdSet = goodsPostDetailList.stream().map(e -> e.getSigner()).collect(Collectors.toSet());
        }
        Set<Long> corpIdSet = new HashSet<>();
        Set<Long> branchIdSet = new HashSet<>();
        Set<String> areaCodeSet = new HashSet<>();
        corpIdSet.add(goodsPostDto.getConsignCorp());
        corpIdSet.add(goodsPostDto.getReceiveCorp());
        branchIdSet.add(goodsPostDto.getConsignBranch());
        branchIdSet.add(goodsPostDto.getReceiveBranch());
        areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getConsignArea()));
        areaCodeSet.addAll(this.listAreaSet(goodsPostDto.getReceiveArea()));
        userIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> userMap = null;
        Result<Map<Long, String>> userMapResult = uasFeignService.mapUserIdAndNameByUserIdList(JsonUtil.toJson(userIdSet));
        if (Result.isSucceed(userMapResult)) {
            userMap = userMapResult.getData();
            userMap = userMap == null ? new HashMap<>() : userMap;
        }
        corpIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> corpMap = null;
        Result<Map<Long, String>> corpMapResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdSet));
        if (Result.isSucceed(corpMapResult)) {
            corpMap = corpMapResult.getData();
            corpMap = corpMap == null ? new HashMap<>() : corpMap;
        }
        Result<Map<String, String>> areaMapResult = uasFeignService.mapAreaCodeAndNameByCodeList(JsonUtil.toJson(areaCodeSet));
        Map<String, String> areaMap = null;
        if (Result.isSucceed(areaMapResult)) {
            areaMap = areaMapResult.getData();
            areaMap = areaMap == null ? new HashMap<>() : areaMap;
        }
        this.setAreaName(goodsPostDto, areaMap);
        branchIdSet.removeIf(LongUtil::isZero);
        Map<Long, String> serviceBranchMap = serviceBranchService.mapServiceBranchByBranchIdList(branchIdSet);
        serviceBranchMap = serviceBranchMap == null ? new HashMap<>() : serviceBranchMap;

        goodsPostDto.setSignStatusName(SignStatusEnum.getNameByCode(goodsPostDto.getSignStatus()));
        goodsPostDto.setTransportTypeName(TransportTypeEnum.getNameByCode(goodsPostDto.getTransportType()));
        goodsPostDto.setConsignTypeName(ConsignTypeEnum.getNameByCode(goodsPostDto.getConsignType()));
        goodsPostDto.setExpressTypeName(ExpressTypeEnum.getNameByCode(goodsPostDto.getExpressType()));
        goodsPostDto.setPayWayName(PayWayEnum.getNameByCode(goodsPostDto.getPayWay()));
        goodsPostDto.setConsignCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getConsignCorp())));
        if (LongUtil.isNotZero(goodsPostDto.getConsignBranch())) {
            goodsPostDto.setConsignBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getConsignBranch())));
        }
        goodsPostDto.setReceiveCorpName(StrUtil.trimToEmpty(corpMap.get(goodsPostDto.getReceiveCorp())));
        if (LongUtil.isNotZero(goodsPostDto.getReceiveBranch())) {
            goodsPostDto.setReceiveBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(goodsPostDto.getReceiveBranch())));
        }
        goodsPostDto.setSignerName(StrUtil.trimToEmpty(userMap.get(goodsPostDto.getSigner())));

        Set<Long> fileIdSet = new HashSet<>();
        Map<Long, List<Long>> detailIdAndFileIdListMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(goodsPostDetailFileList)) {
            for (GoodsPostDetailFile goodsPostDetailFile : goodsPostDetailFileList) {
                detailIdAndFileIdListMap.computeIfAbsent(goodsPostDetailFile.getDetailId(),
                        k -> new ArrayList<>()).add(goodsPostDetailFile.getFileId());
                fileIdSet.add(goodsPostDetailFile.getFileId());
            }
        }

        if (CollectionUtil.isNotEmpty(goodsPostFileList)) {
            List<Long> fileIdList = goodsPostFileList.stream().map(e -> e.getFileId()).collect(Collectors.toList());
            goodsPostDto.setFileIdList(fileIdList);
            fileIdSet.addAll(fileIdList);
        }

        Result fileResult = fileFeignService.listFileDtoByIdList(JsonUtil.toJson(fileIdSet));
        List<FileInfoDto> fileInfoDtoList = null;
        if (Result.isSucceed(fileResult)) {
            fileInfoDtoList = JsonUtil.parseArray(JsonUtil.toJson(fileResult.getData()), FileInfoDto.class);
        }
        // 组装文件编号与文件映射
        Map<Long, FileInfoDto> fileIdAndFileMap = new HashMap<>();
        if (CollectionUtil.isNotEmpty(fileInfoDtoList)) {
            fileIdAndFileMap = fileInfoDtoList.stream().collect(Collectors.toMap(FileInfoDto::getFileId,
                    a -> a, (k1, k2) -> k1));
        }
        // 组装寄送单明细编号与文件列表映射
        Map<Long, List<FileInfoDto>> detailIdAndFileListMap = new HashMap<>();
        for (Long detailId : detailIdAndFileIdListMap.keySet()) {
            List<Long> fileIdList = detailIdAndFileIdListMap.get(detailId);
            for (Long fileId : fileIdList) {
                detailIdAndFileListMap.computeIfAbsent(detailId,
                        k -> new ArrayList<>()).add(fileIdAndFileMap.get(fileId));
            }
        }
        // 组装寄送单文件列表
        if (CollectionUtil.isNotEmpty(goodsPostDto.getFileIdList())) {
            List<FileInfoDto> fileList = new ArrayList<>();
            for (Long fileId : goodsPostDto.getFileIdList()) {
                fileList.add(fileIdAndFileMap.get(fileId));
            }
            goodsPostDto.setFileList(fileList);
        }
        // 组装寄送单明细列表
        List<GoodsPostDetailDto> goodsPostDetailDtoList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(goodsPostDetailList)) {
            GoodsPostDetailDto goodsPostDetailDto;
            for (GoodsPostDetail goodsPostDetail : goodsPostDetailList) {
                goodsPostDetailDto = new GoodsPostDetailDto();
                BeanUtils.copyProperties(goodsPostDetail, goodsPostDetailDto);
                goodsPostDetailDto.setSignerName(StrUtil.trimToEmpty(userMap.get(goodsPostDetail.getSigner())));
                goodsPostDetailDto.setFileIdList(detailIdAndFileIdListMap.get(goodsPostDetail.getId()));
                goodsPostDetailDto.setFileList(detailIdAndFileListMap.get(goodsPostDetail.getId()));
                goodsPostDetailDtoList.add(goodsPostDetailDto);
            }
        }
        goodsPostDto.setGoodsPostDetailDtoList(goodsPostDetailDtoList);
        return goodsPostDto;
    }

    /**
     * 添加物品寄送单
     *
     * @param goodsPostDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/20 16:57
     */
    @Override
    public void addGoodsPost(GoodsPostDto goodsPostDto, Long curUserId) {
        if (CollectionUtil.isNotEmpty(goodsPostDto.getConsignAreaList())) {
            String consignArea = "";
            if (goodsPostDto.getConsignAreaList().size() >= 3
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(2))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(2));
            } else if (goodsPostDto.getConsignAreaList().size() >= 2
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(1))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(1));
            } else if (goodsPostDto.getConsignAreaList().size() >= 1
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(0))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(0));
            }
            goodsPostDto.setConsignArea(consignArea);
        }
        if (CollectionUtil.isNotEmpty(goodsPostDto.getReceiveAreaList())) {
            String receiveArea = "";
            if (goodsPostDto.getReceiveAreaList().size() >= 3
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(2))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(2));
            } else if (goodsPostDto.getReceiveAreaList().size() >= 2
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(1))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(1));
            } else if (goodsPostDto.getReceiveAreaList().size() >= 1
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(0))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(0));
            }
            goodsPostDto.setReceiveArea(receiveArea);
        }
        Long postId = KeyUtil.getId();
        // 添加物品寄送单
        GoodsPost goodsPost = new GoodsPost();
        BeanUtils.copyProperties(goodsPostDto, goodsPost);
        goodsPost.setId(postId);
        goodsPost.setCreator(curUserId);
        goodsPost.setCreateTime(DateUtil.date());
        goodsPost.setPostNo(businessCodeGenerator.getGoodsPostNoCode());
        goodsPost.setSignStatus(SignStatusEnum.NOT_SIGN.getCode());

        // 添加物品寄送单基本信息
        goodsPostService.save(goodsPost);

        List<Long> tempFileIdList = new ArrayList<>(); // 临时文件编号列表

        // 添加物品寄送明细
        List<GoodsPostDetailDto> goodsPostDetailDtoList = goodsPostDto.getGoodsPostDetailDtoList();
        List<GoodsPostDetail> goodsPostDetailList = new ArrayList<>();
        List<GoodsPostDetailFile> goodsPostDetailFileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(goodsPostDetailDtoList)) {
            GoodsPostDetail goodsPostDetail;
            int i = 1;
            for (GoodsPostDetailDto goodsPostDetailDto : goodsPostDetailDtoList) {
                if (StrUtil.isBlank(goodsPostDetailDto.getGoodsName())) {
                    throw new AppException("物品清单中第" + i + "行的物品名称不能为空！");
                }
                goodsPostDetail = new GoodsPostDetail();
                BeanUtils.copyProperties(goodsPostDetailDto, goodsPostDetail);
                goodsPostDetail.setId(KeyUtil.getId());
                goodsPostDetail.setPostId(postId);
                goodsPostDetailList.add(goodsPostDetail);
                List<Long> fileIdList = goodsPostDetailDto.getFileIdList();
                if (CollectionUtil.isNotEmpty(fileIdList)) {
                    tempFileIdList.addAll(fileIdList);
                    GoodsPostDetailFile goodsPostDetailFile;
                    for (Long fileId : fileIdList) {
                        goodsPostDetailFile = new GoodsPostDetailFile();
                        goodsPostDetailFile.setDetailId(goodsPostDetail.getId());
                        goodsPostDetailFile.setPostId(postId);
                        goodsPostDetailFile.setFileId(fileId);
                        goodsPostDetailFileList.add(goodsPostDetailFile);
                    }
                }
            }
            i++;
        }

        // 添加物品寄送工单信息
        if (CollectionUtil.isNotEmpty(goodsPostDto.getGoodsPostWorkDtoList())) {
            List<GoodsPostWork> goodsPostWorkList = new ArrayList<>();
            GoodsPostWork goodsPostWork;
            for (GoodsPostWorkDto goodsPostWorkDto : goodsPostDto.getGoodsPostWorkDtoList()) {
                goodsPostWork = new GoodsPostWork();
                BeanUtils.copyProperties(goodsPostWorkDto, goodsPostWork);
                goodsPostWork.setPostId(postId);
                goodsPostWorkList.add(goodsPostWork);
            }
            goodsPostWorkService.saveBatch(goodsPostWorkList);
        }
        // 添加物品寄送文件信息
        if (CollectionUtil.isNotEmpty(goodsPostDto.getGoodsPostFileList())) {
            for (GoodsPostFile goodsPostFile : goodsPostDto.getGoodsPostFileList()) {
                goodsPostFile.setPostId(postId);
            }
            List<Long> fileIdList = goodsPostDto.getGoodsPostFileList().stream()
                    .map(e -> e.getFileId()).collect(Collectors.toList());
            tempFileIdList.addAll(fileIdList);
            goodsPostFileService.saveBatch(goodsPostDto.getGoodsPostFileList());
        }
        // 添加物品寄送单明细
        if (CollectionUtil.isNotEmpty(goodsPostDetailList)) {
            goodsPostDetailService.saveBatch(goodsPostDetailList);
        }
        // 添加物品寄送单明细文件
        if (CollectionUtil.isNotEmpty(goodsPostDetailFileList)) {
            goodsPostDetailFileService.saveBatch(goodsPostDetailFileList);
        }
        // 添加快递公司
        if (StrUtil.isNotBlank(goodsPost.getExpressCorpName())) {
            expressCompanyService.addExpressCorpByPost(goodsPost.getConsignCorp(),
                    goodsPost.getExpressCorpName(), curUserId);
        }

        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(tempFileIdList)) {
            fileFeignService.deleteTempFileByFileIdList(JsonUtil.toJson(tempFileIdList));
        }
    }

    /**
     * 编辑物品寄送单
     *
     * @param goodsPostDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/23 14:36
     */
    @Override
    public void editGoodsPost(GoodsPostDto goodsPostDto, Long curUserId) {
        goodsPostDto.setConsignBranch(LongUtil.isZero(goodsPostDto.getConsignBranch()) ? 0L : goodsPostDto.getConsignBranch());
        goodsPostDto.setReceiveBranch(LongUtil.isZero(goodsPostDto.getReceiveBranch()) ? 0L : goodsPostDto.getReceiveBranch());
        goodsPostDto.setPayWay(IntUtil.isZero(goodsPostDto.getPayWay()) ? 0 : goodsPostDto.getPayWay());

        if (CollectionUtil.isNotEmpty(goodsPostDto.getConsignAreaList())) {
            String consignArea = "";
            if (goodsPostDto.getConsignAreaList().size() >= 3
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(2))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(2));
            } else if (goodsPostDto.getConsignAreaList().size() >= 2
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(1))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(1));
            } else if (goodsPostDto.getConsignAreaList().size() >= 1
                    && StrUtil.isNotBlank(goodsPostDto.getConsignAreaList().get(0))) {
                consignArea = StrUtil.trimToEmpty(goodsPostDto.getConsignAreaList().get(0));
            }
            goodsPostDto.setConsignArea(consignArea);
        }
        if (CollectionUtil.isNotEmpty(goodsPostDto.getReceiveAreaList())) {
            String receiveArea = "";
            if (goodsPostDto.getReceiveAreaList().size() >= 3
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(2))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(2));
            } else if (goodsPostDto.getReceiveAreaList().size() >= 2
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(1))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(1));
            } else if (goodsPostDto.getReceiveAreaList().size() >= 1
                    && StrUtil.isNotBlank(goodsPostDto.getReceiveAreaList().get(0))) {
                receiveArea = StrUtil.trimToEmpty(goodsPostDto.getReceiveAreaList().get(0));
            }
            goodsPostDto.setReceiveArea(receiveArea);
        }
        // 添加物品寄送单
        GoodsPost goodsPost = goodsPostService.getById(goodsPostDto.getId());
        if (goodsPost == null) {
            throw new AppException("物品寄送单已不存在！");
        }
        BeanUtils.copyProperties(goodsPostDto, goodsPost);
        goodsPost.setEditor(curUserId);
        goodsPost.setEditTime(DateUtil.date());

        List<GoodsPostDetail> dbGoodsPostDetailList = goodsPostDetailService
                .list(new QueryWrapper<GoodsPostDetail>().eq("post_id", goodsPost.getId())
                        .orderByAsc("goods_name, id"));

        // 修改物品寄送单基本信息
        goodsPostService.updateById(goodsPost);

        List<Long> dbFileIdList = new ArrayList<>(); // 数据库中的文件编号列表
        List<Long> tempFileIdList = new ArrayList<>(); // 临时文件编号列表

        List<GoodsPostFile> dbGoodsPostFileList = goodsPostFileService
                .list(new QueryWrapper<GoodsPostFile>().eq("post_id", goodsPost.getId()));
        if (CollectionUtil.isNotEmpty(dbGoodsPostFileList)) {
            List<Long> fileIdList = dbGoodsPostFileList.stream()
                    .map(e -> e.getFileId()).collect(Collectors.toList());
            dbFileIdList.addAll(fileIdList);
        }
        List<GoodsPostDetailFile> dbGoodsPostDetailFileList = goodsPostDetailFileService
                .list(new QueryWrapper<GoodsPostDetailFile>().eq("post_id", goodsPost.getId()));
        if (CollectionUtil.isNotEmpty(dbGoodsPostDetailFileList)) {
            List<Long> fileIdList = dbGoodsPostDetailFileList.stream()
                    .map(e -> e.getFileId()).collect(Collectors.toList());
            dbFileIdList.addAll(fileIdList);
        }

        // 修改物品寄送明细
        List<GoodsPostDetailDto> goodsPostDetailDtoList = goodsPostDto.getGoodsPostDetailDtoList();
        List<GoodsPostDetail> goodsPostDetailList = new ArrayList<>();
        List<GoodsPostDetailFile> goodsPostDetailFileList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(goodsPostDetailDtoList)) {
            GoodsPostDetail goodsPostDetail;
            int i = 1;
            for (GoodsPostDetailDto goodsPostDetailDto : goodsPostDetailDtoList) {
                if (StrUtil.isBlank(goodsPostDetailDto.getGoodsName())) {
                    throw new AppException("物品清单中第" + i + "行的物品名称不能为空！");
                }
                goodsPostDetail = new GoodsPostDetail();
                BeanUtils.copyProperties(goodsPostDetailDto, goodsPostDetail);
                if (LongUtil.isZero(goodsPostDetail.getId())) {
                    goodsPostDetail.setId(KeyUtil.getId());
                }
                goodsPostDetail.setPostId(goodsPost.getId());
                goodsPostDetailList.add(goodsPostDetail);
                List<Long> fileIdList = goodsPostDetailDto.getFileIdList();
                if (CollectionUtil.isNotEmpty(fileIdList)) {
                    tempFileIdList.addAll(fileIdList);
                    GoodsPostDetailFile goodsPostDetailFile;
                    for (Long fileId : fileIdList) {
                        goodsPostDetailFile = new GoodsPostDetailFile();
                        goodsPostDetailFile.setDetailId(goodsPostDetail.getId());
                        goodsPostDetailFile.setPostId(goodsPost.getId());
                        goodsPostDetailFile.setFileId(fileId);
                        goodsPostDetailFileList.add(goodsPostDetailFile);
                    }
                }
            }
            i++;
        }

        // 修改物品寄送工单信息
        if (CollectionUtil.isNotEmpty(goodsPostDto.getGoodsPostWorkDtoList())) {
            List<GoodsPostWork> goodsPostWorkList = new ArrayList<>();
            GoodsPostWork goodsPostWork;
            for (GoodsPostWorkDto goodsPostWorkDto : goodsPostDto.getGoodsPostWorkDtoList()) {
                goodsPostWork = new GoodsPostWork();
                BeanUtils.copyProperties(goodsPostWorkDto, goodsPostWork);
                goodsPostWork.setPostId(goodsPostDto.getId());
                goodsPostWorkList.add(goodsPostWork);
            }
            goodsPostWorkService.remove(new UpdateWrapper<GoodsPostWork>()
                    .eq("post_id", goodsPost.getId()));
            goodsPostWorkService.saveBatch(goodsPostWorkList);
        }
        // 修改物品寄送文件信息
        if (CollectionUtil.isNotEmpty(goodsPostDto.getGoodsPostFileList())) {
            for (GoodsPostFile goodsPostFile : goodsPostDto.getGoodsPostFileList()) {
                goodsPostFile.setPostId(goodsPost.getId());
            }
            List<Long> fileIdList = goodsPostDto.getGoodsPostFileList().stream()
                    .map(e -> e.getFileId()).collect(Collectors.toList());
            tempFileIdList.addAll(fileIdList);
            goodsPostFileService.remove(new UpdateWrapper<GoodsPostFile>()
                    .eq("post_id", goodsPost.getId()));
            goodsPostFileService.saveBatch(goodsPostDto.getGoodsPostFileList());
        }
        // 修改物品寄送单明细
        if (CollectionUtil.isNotEmpty(goodsPostDetailList)) {
            List<GoodsPostDetail> detailList = goodsPostDetailList.stream()
                    .filter(e -> !"Y".equalsIgnoreCase(e.getSigned()))
                    .collect(Collectors.toList());
            List<Long> detailIdList = detailList.stream().map(e -> e.getId())
                    .collect(Collectors.toList());
            goodsPostDetailService.remove(new UpdateWrapper<GoodsPostDetail>()
                    .in("id", detailIdList));
            goodsPostDetailService.saveBatch(detailList);
        }
        // 修改物品寄送单明细文件
        if (CollectionUtil.isNotEmpty(goodsPostDetailFileList)) {
            List<Long> fileIdList = goodsPostDetailFileList.stream()
                    .map(e -> e.getFileId()).collect(Collectors.toList());
            goodsPostDetailFileService.removeByIds(fileIdList);
            goodsPostDetailFileService.saveBatch(goodsPostDetailFileList);
        }

        // 添加快递公司
        if (StrUtil.isNotBlank(goodsPost.getExpressCorpName())) {
            expressCompanyService.addExpressCorpByPost(goodsPost.getConsignCorp(),
                    goodsPost.getExpressCorpName(), curUserId);
        }
        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(tempFileIdList)) {
            fileFeignService.deleteTempFileByFileIdList(JsonUtil.toJson(tempFileIdList));
        }
        // 删除文件
        List<Long> deleteFileIdList = this.listDeleteFile(dbFileIdList, tempFileIdList);
        System.out.println(deleteFileIdList);
    }

    /**
     * 删除物品寄送单
     *
     * @param postId
     * @return
     * @author zgpi
     * @date 2020/4/22 15:07
     */
    @Override
    public void delGoodsPost(Long postId) {
        GoodsPost goodsPost = goodsPostService.getById(postId);
        List<GoodsPostDetail> goodsPostDetailList = goodsPostDetailService
                .list(new QueryWrapper<GoodsPostDetail>().eq("post_id", postId)
                        .orderByAsc("goods_name, id"));
        if (goodsPost != null && SignStatusEnum.PART_SIGN.getCode() == goodsPost.getSignStatus()) {
            throw new AppException("该物品寄送单已部分签收，不能删除！");
        }
        if (goodsPost != null && SignStatusEnum.ALL_SIGN.getCode() == goodsPost.getSignStatus()) {
            throw new AppException("该物品寄送单已全部签收，不能删除！");
        }
        Set<String> signedSet;
        if (CollectionUtil.isNotEmpty(goodsPostDetailList)) {
            signedSet = goodsPostDetailList.stream().map(e -> e.getSigned()).collect(Collectors.toSet());
            if (signedSet.size() == 1 && signedSet.contains("Y")) {
                throw new AppException("该物品寄送单已全部签收，不能删除！");
            }
            if (signedSet.size() > 1 && signedSet.contains("Y")) {
                throw new AppException("该物品寄送单已部分签收，不能删除！");
            }
        }
        goodsPostService.removeById(postId);
        goodsPostWorkService.remove(new UpdateWrapper<GoodsPostWork>().eq("post_id", postId));
        goodsPostFileService.remove(new UpdateWrapper<GoodsPostFile>().eq("post_id", postId));
        goodsPostDetailService.remove(new UpdateWrapper<GoodsPostDetail>().eq("post_id", postId));
        goodsPostDetailFileService.remove(new UpdateWrapper<GoodsPostDetailFile>().eq("post_id", postId));
    }

    /**
     * 签收物品寄送单
     *
     * @param goodsPostSignDto
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/24 10:01
     */
    @Override
    public void signGoodsPost(GoodsPostSignDto goodsPostSignDto, Long curUserId) {
        List<Long> detailIdList = goodsPostSignDto.getDetailIdList();
        if (CollectionUtil.isEmpty(detailIdList)) {
            throw new AppException("没有选择物品明细签收！");
        }
        List<GoodsPostDetail> dbGoodsPostDetailList =
                (List<GoodsPostDetail>) goodsPostDetailService.listByIds(detailIdList);
        if (CollectionUtil.isEmpty(dbGoodsPostDetailList)) {
            throw new AppException("选择签收的物品明细不存在！");
        } else {
            List<Long> dbIdList = dbGoodsPostDetailList.stream().map(e -> e.getId()).collect(Collectors.toList());
            List<Integer> msgList = new ArrayList<>();
            for (int i = 0; i < detailIdList.size(); i++) {
                if (!dbIdList.contains(detailIdList.get(i))) {
                    msgList.add(i + 1);
                }
            }
            if (CollectionUtil.isNotEmpty(msgList)) {
                throw new AppException("物品清单中第[" + CollectionUtil.join(msgList, ",") + "]行物品不存在！");
            }
            for (GoodsPostDetail goodsPostDetail : dbGoodsPostDetailList) {
                if ("Y".equalsIgnoreCase(goodsPostDetail.getSigned())) {
                    throw new AppException("[" + goodsPostDetail.getGoodsName() + "]已签收！");
                }
            }
        }
        Date curTime = DateUtil.date();
        // 修改物品寄送单明细
        List<GoodsPostDetail> goodsPostDetailList = new ArrayList<>();
        GoodsPostDetail goodsPostDetail;
        for (Long detailId : detailIdList) {
            goodsPostDetail = new GoodsPostDetail();
            goodsPostDetail.setId(detailId);
            goodsPostDetail.setSigned("Y");
            goodsPostDetail.setSigner(curUserId);
            goodsPostDetail.setSignTime(curTime);
            goodsPostDetailList.add(goodsPostDetail);
        }
        goodsPostDetailService.updateBatchById(goodsPostDetailList);

        // 修改物品寄送单
        GoodsPost goodsPost = new GoodsPost();
        goodsPost.setId(goodsPostSignDto.getPostId());
        goodsPost.setSigner(curUserId);
        goodsPost.setSignTime(curTime);
        int signStatus = goodsPostDetailService.findSignStatus(goodsPostSignDto.getPostId());
        goodsPost.setSignStatus(signStatus);
        goodsPostService.updateById(goodsPost);
    }

    /**
     * 当前用户是否属于企业人员
     *
     * @param postId
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/26 16:22
     **/
    @Override
    public GoodsPostDto findIfCorpUser(Long postId, Long curUserId) {
        GoodsPostDto goodsPostDto = new GoodsPostDto();
        GoodsPost goodsPost = goodsPostService.getById(postId);
        if (goodsPost == null) {
            return goodsPostDto;
        }
        BeanUtils.copyProperties(goodsPost, goodsPostDto);
        Long consignCorp = goodsPost.getConsignCorp();
        Long receiveCorp = goodsPost.getReceiveCorp();
        if (LongUtil.isNotZero(consignCorp)) {
            Result corpUserResult = uasFeignService.findCorpUserByUserIdAndCorpId(curUserId, consignCorp);
            if (Result.isSucceed(corpUserResult) && corpUserResult.getData() != null) {
                goodsPostDto.setIfConsignCorpUser("Y");
            }
        }
        if (LongUtil.isNotZero(receiveCorp)) {
            Result corpUserResult = uasFeignService.findCorpUserByUserIdAndCorpId(curUserId, receiveCorp);
            if (Result.isSucceed(corpUserResult) && corpUserResult.getData() != null) {
                goodsPostDto.setIfReceiveCorpUser("Y");
            }
        }
        return goodsPostDto;
    }

    /**
     * 获得企业的发货或收货地址
     * 如存在历史的发货或收货寄送单，则取上一次的地址
     * 若不存在：
     * 若是服务商，有网点则取网点的地址，没有则取公司地址
     * 若是委托商，则取公司地址
     *
     * @param goodsPostFilter
     * @return
     * @author zgpi
     * @date 2020/5/13 10:42
     **/
    @Override
    public GoodsPostDto findAddress(GoodsPostFilter goodsPostFilter) {
        Long corpId = 0L;
        Long branchId = 0L;
        if (goodsPostFilter.getAddressQueryType() == 1) {
            corpId = goodsPostFilter.getConsignCorp();
            branchId = goodsPostFilter.getConsignBranch();
        }
        if (goodsPostFilter.getAddressQueryType() == 2) {
            corpId = goodsPostFilter.getReceiveCorp();
            branchId = goodsPostFilter.getReceiveBranch();
        }
        if (LongUtil.isZero(corpId)) {
            return null;
        }
        String serviceDemander = "";
        // 收货类型
        if (goodsPostFilter.getAddressQueryType() == 2) {
            Result tenantResult = uasFeignService.findSysTenant(goodsPostFilter.getReceiveCorp());
            if (Result.isSucceed(tenantResult)) {
                String data = JsonUtil.toJson(tenantResult.getData());
                serviceDemander = StrUtil.trimToEmpty(JsonUtil.parseString(data, "serviceDemander"));
                if ("Y".equalsIgnoreCase(serviceDemander)) {
                    QueryWrapper<GoodsPost> queryWrapper = new QueryWrapper<>();
                    queryWrapper.eq("consign_corp", goodsPostFilter.getConsignCorp());
                    queryWrapper.eq("receive_corp", goodsPostFilter.getReceiveCorp());
                    queryWrapper.orderByDesc("create_time", "id");
                    List<GoodsPost> goodsPostList = goodsPostService.list(queryWrapper);
                    if (CollectionUtil.isNotEmpty(goodsPostList)) {
                        GoodsPostDto goodsPostDto = new GoodsPostDto();
                        GoodsPost goodsPost = goodsPostList.get(0);
                        goodsPostDto.setReceiver(goodsPost.getReceiver());
                        goodsPostDto.setReceiverName(goodsPost.getReceiverName());
                        goodsPostDto.setReceiverPhone(goodsPost.getReceiverPhone());
                        goodsPostDto.setReceiveAddress(goodsPost.getReceiveAddress());
                        return goodsPostDto;
                    }
                }
            }
        }

        if (LongUtil.isNotZero(branchId)) {
            ServiceBranch serviceBranch = serviceBranchService.getById(branchId);
            if (serviceBranch != null) {
                GoodsPostDto goodsPostDto = new GoodsPostDto();
                if (goodsPostFilter.getAddressQueryType() == 1) {
                    goodsPostDto.setConsignAddress(serviceBranch.getAddress());
                }
                if (goodsPostFilter.getAddressQueryType() == 2) {
                    goodsPostDto.setReceiveAddress(serviceBranch.getAddress());
                }
                return goodsPostDto;
            }
        } else {
            Result corpResult = uasFeignService.findCorpById(corpId);
            if (Result.isSucceed(corpResult)) {
                GoodsPostDto goodsPostDto = new GoodsPostDto();
                String data = JsonUtil.toJson(corpResult.getData());
                if (goodsPostFilter.getAddressQueryType() == 1) {
                    goodsPostDto.setConsignAddress(JsonUtil.parseString(data, "address"));
                }
                if (goodsPostFilter.getAddressQueryType() == 2) {
                    goodsPostDto.setReceiveAddress(JsonUtil.parseString(data, "address"));
                }
                return goodsPostDto;
            }
        }
        return null;
    }

    /**
     * 获得用户权限
     *
     * @param goodsPostFilter 查询条件
     * @param curUserId       当前用户
     * @param curCorpId       公共参数
     * @return
     * @author zgpi
     * @date 2020/4/24 16:45
     **/
    private void findUserRight(GoodsPostFilter goodsPostFilter, Long curUserId, Long curCorpId) {
        goodsPostFilter.setUserId(curUserId);
        goodsPostFilter.setCorpId(curCorpId);
        Long rightId = RightConstants.GOODS_POST_QUERY;
        // 范围权限列表
        List<RightScopeDto> rightScopeDtoList = dataScopeCompoService.listUserRightScope(curCorpId, curUserId, rightId);
        goodsPostFilter.setRightScopeList(rightScopeDtoList);
    }

    /**
     * 设置地区名称
     *
     * @param goodsPostDto
     * @param areaMap
     * @return
     * @author zgpi
     * @date 2020/4/23 16:28
     */
    private void setAreaName(GoodsPostDto goodsPostDto,
                             Map<String, String> areaMap) {
        List<String> consignAreaNameList = this.listAreaName(goodsPostDto.getConsignArea(), areaMap);
        List<String> receiveAreaNameList = this.listAreaName(goodsPostDto.getReceiveArea(), areaMap);
        if (consignAreaNameList.size() >= 1) {
            goodsPostDto.setConsignProvinceName(consignAreaNameList.get(0));
        }
        if (consignAreaNameList.size() >= 2) {
            goodsPostDto.setConsignCityName(consignAreaNameList.get(1));
        }
        if (consignAreaNameList.size() >= 3) {
            goodsPostDto.setConsignDistrictName(consignAreaNameList.get(2));
        }
        if (receiveAreaNameList.size() >= 1) {
            goodsPostDto.setReceiveProvinceName(receiveAreaNameList.get(0));
        }
        if (receiveAreaNameList.size() >= 2) {
            goodsPostDto.setReceiveCityName(receiveAreaNameList.get(1));
        }
        if (receiveAreaNameList.size() >= 3) {
            goodsPostDto.setReceiveDistrictName(receiveAreaNameList.get(2));
        }
        goodsPostDto.setConsignAreaName(
                StrUtil.trimToEmpty(goodsPostDto.getConsignProvinceName()) +
                        StrUtil.trimToEmpty(goodsPostDto.getConsignCityName()) +
                        StrUtil.trimToEmpty(goodsPostDto.getConsignDistrictName()));
        goodsPostDto.setReceiveAreaName(
                StrUtil.trimToEmpty(goodsPostDto.getReceiveProvinceName()) +
                        StrUtil.trimToEmpty(goodsPostDto.getReceiveCityName()) +
                        StrUtil.trimToEmpty(goodsPostDto.getReceiveDistrictName()));
    }

    /**
     * 获得地区编码列表
     *
     * @param areaCode
     * @return
     * @author zgpi
     * @date 2020/4/23 16:28
     */
    private Set<String> listAreaSet(String areaCode) {
        Set<String> set = new HashSet<>();
        if (StrUtil.isNotBlank(areaCode)) {
            if (areaCode.length() >= 2) {
                set.add(areaCode.substring(0, 2));
            }
            if (areaCode.length() >= 4) {
                // 如果是省直辖市
                if (areaCode.matches("\\d{2}9\\d{3}")) {
                    set.add(StrUtil.trimToEmpty(areaCode));
                } else if (areaCode.matches("5002\\d{2}")) {
                    set.add("5001");
                } else {
                    set.add(StrUtil.trimToEmpty(areaCode.substring(0, 4)));
                }
            }
            // 非省直辖市
            if (areaCode.length() >= 6 && !areaCode.matches("\\d{2}9\\d{3}")) {
                set.add(StrUtil.trimToEmpty(areaCode.substring(0, 6)));
            }
        }
        return set;
    }

    /**
     * 获得地区名称列表
     *
     * @param areaCode
     * @return
     * @author zgpi
     * @date 2020/4/23 16:48
     */
    private List<String> listAreaName(String areaCode, Map<String, String> areaMap) {
        List<String> list = new ArrayList<>();
        if (StrUtil.isNotBlank(areaCode)) {
            String provinceName = "";
            String cityName = "";
            String districtName = "";
            if (areaCode.length() >= 2) {
                provinceName = StrUtil.trimToEmpty(areaMap.get(areaCode.substring(0, 2)));
                provinceName = provinceName.replace("省", "")
                        .replace("自治区", "")
                        .replace("特别行政区", "")
                        .replace("回族", "")
                        .replace("壮族", "").replace("维吾尔", "");
                list.add(provinceName);
            }
            if (areaCode.length() >= 4) {
                // 如果是省直辖市
                if (areaCode.matches("\\d{2}9\\d{3}")) {
                    cityName = StrUtil.trimToEmpty(areaMap.get(areaCode));
                } else if (areaCode.matches("5002\\d{2}")) {
                    cityName = StrUtil.trimToEmpty(areaMap.get("5001"));
                } else {
                    cityName = StrUtil.trimToEmpty(areaMap.get(areaCode.substring(0, 4)));
                }
                list.add(cityName);
            }
            // 非省直辖市
            if (areaCode.length() >= 6 && !areaCode.matches("\\d{2}9\\d{3}")) {
                districtName = StrUtil.trimToEmpty(areaMap.get(areaCode.substring(0, 6)));
                list.add(districtName);
            }
        }
        return list;
    }

    /**
     * 获得要删除的文件编号列表
     *
     * @param dbFileIdList  数据库中存储的列表
     * @param newFileIdList 新提交的列表
     * @return
     * @author zgpi
     * @date 2020/4/27 15:09
     **/
    private List<Long> listDeleteFile(List<Long> dbFileIdList, List<Long> newFileIdList) {
        if (CollectionUtil.isEmpty(newFileIdList)) {
            return dbFileIdList;
        } else {
            dbFileIdList.removeAll(newFileIdList);
            return dbFileIdList;
        }
    }
}
