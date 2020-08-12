package com.zjft.usp.anyfix.oa.cost.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.zjft.usp.anyfix.baseinfo.service.WorkTypeService;
import com.zjft.usp.anyfix.common.feign.dto.UserRealDto;
import com.zjft.usp.anyfix.corp.branch.service.DeviceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderCustomService;
import com.zjft.usp.anyfix.oa.cost.composite.WorkCostCompoService;
import com.zjft.usp.anyfix.oa.cost.dto.WorkCostDto;
import com.zjft.usp.anyfix.oa.cost.filter.WorkCostFilter;
import com.zjft.usp.anyfix.work.deal.filter.WorkDealFilter;
import com.zjft.usp.anyfix.work.deal.mapper.WorkDealMapper;
import com.zjft.usp.anyfix.work.deal.model.WorkDeal;
import com.zjft.usp.anyfix.work.request.dto.WorkDto;
import com.zjft.usp.anyfix.work.request.enums.ServiceModeEnum;
import com.zjft.usp.anyfix.work.request.enums.TrafficEnum;
import com.zjft.usp.anyfix.work.request.enums.WorkStatusEnum;
import com.zjft.usp.anyfix.work.request.filter.WorkFilter;
import com.zjft.usp.anyfix.work.request.mapper.WorkRequestMapper;
import com.zjft.usp.anyfix.work.sign.filter.WorkSignFilter;
import com.zjft.usp.anyfix.work.sign.model.WorkSign;
import com.zjft.usp.anyfix.work.sign.service.WorkSignService;
import com.zjft.usp.anyfix.work.transfer.enums.WorkTransferEnum;
import com.zjft.usp.anyfix.work.transfer.filter.WorkTransferFilter;
import com.zjft.usp.anyfix.work.transfer.model.WorkTransfer;
import com.zjft.usp.anyfix.work.transfer.service.WorkTransferService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.JsonUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.uas.service.UasFeignService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author: JFZOU
 * @Date: 2020-02-29 14:08
 */
@Slf4j
@Service
public class WorkCostCompoServiceImpl implements WorkCostCompoService {

    @Resource
    private UasFeignService uasFeignService;

    @Autowired
    private WorkSignService workSignService;

    @Autowired
    private WorkTransferService workTransferService;

    @Autowired
    private DeviceBranchService deviceBranchService;

    @Autowired
    private ServiceBranchService serviceBranchService;

    @Resource
    private DeviceFeignService deviceFeignService;

    @Resource
    private WorkRequestMapper workRequestMapper;

    @Resource
    private WorkDealMapper workDealMapper;

    @Autowired
    private WorkTypeService workTypeService;

    @Autowired
    private DemanderCustomService demanderCustomService;

    private Map<Long, String> demanderCustomCorpNameMap;
    private Map<Integer, String> workTypeMap;
    private Map<Long, String> corpMap;
    private Map<Long, String> deviceBrandMap;
    private Map<Long, String> deviceModelMap;
    private Map<Long, String> serviceBranchMap;
    private Map<Long, String> deviceBranchMap;
    private Map<Long, String> customDeviceBranchMap;

    /**
     * 1、已完成、已评价：工单状态为【已完成、已评价】，【服务完成时间】=归属报销所属月份，服务模式=【现场】的工单。协同人员是以英文逗号分开，要模糊匹配，参考CASE的模糊匹配工程师。
     * 第一点中除了查出工程师外，还要根据查出的工单ID(需要排除工程师条件)在签到表中再查找签到用户，两部分数据求并集，即work_deal表中的engineer,together_engineers，加上work_sign表中的operator。
     * <p>
     * 2、已撤单：由于已撤单是最终状态，即work_transfer中mode = 客户撤单，operate_time=归属报销所属月份查出work_id，再根据work_id，在work_sign表中查找operator，计入报销。
     * <p>
     * 3、已退单：由于已退单下一步是已撤单，委托商可能会撤单也可能会重新提单，重新提单后续采用新单的方式，原单不受影响。
     * 因此，即work_transfer中mode = 客服退单，operate_time=归属报销所属月份查出work_id，再根据work_id，在work_sign表中查找operator，计入报销。
     * <p>
     * 4、协同工程师在第2、3中暂时无法填写，后续如无法报销需要协同工程师的功能进行优化后才能实现。
     * <p>
     * 5、工单系统对上述返回的数据需要根据工单ID+手机编号去掉重复后再返回。
     * <p>
     * 6、OA报销系统防止重复报销需要有一张表，根据工单ID+手机编号去重复，一个相同的[工单ID+手机号]只允许报销一次。
     * <p>
     * 其他说明：
     * 查找有签到记录的服务主工程师，反复签到work_deal中存在被清空的情况
     * 查找服务协同工程师，协同工程师是在服务完成时填写的，不存在被清空的情况
     * 协同人员是在服务完成的时候填的，那时候都完成工单了，不存在退回去的情况了
     *
     * @param workCostFilter
     * @return
     */
    @Override
    public List<WorkCostDto> queryByMobile(WorkCostFilter workCostFilter) {

        /**检查数据合法性*/
        checkDataNotNull(workCostFilter);

        /**获得工单系统用户名*/
        Long engineerUserId = this.getUserIdByMobile(workCostFilter.getMobile());
        if (LongUtil.isZero(engineerUserId)) {
            throw new AppException("手机号【" + workCostFilter.getMobile() + "】在新一代云服务平台对应的用户不存在！");
        }

        /**检查工程师姓名*/
        checkUserReal(engineerUserId, workCostFilter.getUserName());

        /**存储符合条件的工程师全部工单ID，需要去重复*/
        Set<Long> engineerTotalWorkIdSet = new TreeSet<Long>();
        /**临时存储workId，需要去重复*/
        Set<Long> tempTotalWorkIdSet = new TreeSet<Long>();
        /**工单ID与指定流转的映射，同一个工单，客户撤单、客服退单仅会流转一次*/
        Map<Long, WorkTransfer> workTransferMap = new HashMap<Long, WorkTransfer>(512);
        /**工单ID与签到的映射，一个工单可以签到多次*/
        Map<Long, WorkSign> workSignMap = new HashMap<Long, WorkSign>(512);

        /**1、查找已完成、已评价的现场工单，并且服务完成时间归属报销月份*/
        WorkDealFilter workDealFilter = new WorkDealFilter();

        workDealFilter.setFinishMonth(workCostFilter.getMonth());
        workDealFilter.setServiceMode(ServiceModeEnum.LOCALE_SERVICE.getCode());
        workDealFilter.setWorkStatusList(WorkStatusEnum.getCostNormalCloseList());


        /**1、查找归属报销月份的已完成、已评价的现场服务工单，设置engineerTotalWorkIdSet，tempWorkIdSet*/
        doNormalCloseWork(engineerUserId, engineerTotalWorkIdSet, tempTotalWorkIdSet, workDealFilter);

        /**2、查找流转表获得工单ID,查询条件是work_transfer中 mode = 客服退单 OR mode = 客户撤单 operate_time=归属报销所属月份*/
        doReturnCloseWork(workCostFilter, tempTotalWorkIdSet, workTransferMap);

        /**3、tempWorkIdSet，加上engineer查找签到记录，有签到记录表明是现场服务，需要报销*/
        doQueryWorkSign(workCostFilter.getMonth(), engineerUserId, engineerTotalWorkIdSet, tempTotalWorkIdSet, workSignMap);

        /**4、根据engineerTotalWorkIdSet查找实体信息并返回*/
        List<WorkCostDto> workCostDtoList = new ArrayList<>();

        if (CollectionUtil.isNotEmpty(engineerTotalWorkIdSet)) {

            List<WorkDto> queryWorkDtoList = this.queryWorkDtoList(engineerTotalWorkIdSet);
            /**初始化MAP*/
            initData(queryWorkDtoList);

            for (WorkDto workDto : queryWorkDtoList) {
                WorkCostDto workCostDto = new WorkCostDto();

                workCostDto.setWorkId(StrUtil.toString(workDto.getWorkId()));
                workCostDto.setWorkCode(workDto.getWorkCode());
                workCostDto.setDemandedCorpName(StrUtil.trimToEmpty(corpMap.get(workDto.getDemanderCorp())));
                workCostDto.setWorkTypeName(StrUtil.trimToEmpty(workTypeMap.get(workDto.getWorkType())));
                workCostDto.setWorkStatusName(WorkStatusEnum.getNameByCode(workDto.getWorkStatus()));
                workCostDto.setMobile(workCostFilter.getMobile());
                workCostDto.setUserName(workCostFilter.getUserName());
                workCostDto.setCustomName(StrUtil.trimToEmpty(demanderCustomCorpNameMap.get(workDto.getCustomId())));

                if(LongUtil.isZero(workDto.getDeviceBranch())){
                    workCostDto.setDeviceBranchName("");
                }

                if (LongUtil.isNotZero(workDto.getDeviceBranch())) {
                    workCostDto.setDeviceBranchName(StrUtil.trimToEmpty(deviceBranchMap.get(workDto.getDeviceBranch())));
                }

                if (StrUtil.isBlank(workCostDto.getDeviceBranchName()) && LongUtil.isNotZero(workDto.getDeviceBranch())) {
                    workCostDto.setDeviceBranchName(StrUtil.trimToEmpty(customDeviceBranchMap.get(workDto.getDeviceBranch())));
                }

                workCostDto.setDeviceBranchAddress(StrUtil.trimToEmpty(workDto.getAddress()));
                workCostDto.setServiceBranchName(StrUtil.trimToEmpty(serviceBranchMap.get(workDto.getServiceBranch())));

                /**设置签到时间、交通工具、完成时间，此处需要进行单元测试*/
                setSignAndFinishTime(engineerUserId, workTransferMap, workSignMap, workDto, workCostDto);

                workCostDtoList.add(workCostDto);
            }
        }
        return workCostDtoList;
    }

    private void setSignAndFinishTime(Long engineerUserId, Map<Long, WorkTransfer> workTransferMap, Map<Long, WorkSign> workSignMap, WorkDto workDto, WorkCostDto workCostDto) {
        if (workDto.getWorkStatus() == WorkStatusEnum.CLOSED.getCode()
                || workDto.getWorkStatus() == WorkStatusEnum.TO_EVALUATE.getCode()) {
            /**如果当前工程师在work_deal中，取work_deal的完成时间，交通工具，签到时间*/

            boolean useWorkDeal = false;
            if (engineerUserId.compareTo(workDto.getEngineer()) == 0) {
                useWorkDeal = true;
            }

            /**协同工程师*/
            String engineerUserIdString = StrUtil.toString(engineerUserId);
            String togetherEngineers = StrUtil.trimToEmpty(workDto.getTogetherEngineers());
            List<String> togetherEngineersList = Arrays.stream(togetherEngineers.split(","))
                    .filter(StrUtil::isNotBlank).collect(Collectors.toList());
            if (CollectionUtil.isNotEmpty(togetherEngineersList)) {
                if (togetherEngineersList.contains(engineerUserIdString)) {
                    useWorkDeal = true;
                }
            }

            /**说明本单属于当前工程师，直接取work_deal的记录*/
            if (useWorkDeal) {
                setByNormalFinishTime(workDto, workCostDto);
            } else {
                /**说明本单不属于当前工程师，需要在另外处理*/
                setByReturnFinishTime(workTransferMap, workSignMap, workDto.getWorkId(), workCostDto);
            }

        } else {
            /**说明本单不属于当前工程师，需要在另外处理*/
            setByReturnFinishTime(workTransferMap, workSignMap, workDto.getWorkId(), workCostDto);

        }
    }

    /**
     * 设置非正常关闭工单签到时间，完成时间
     *
     * @param workTransferMap
     * @param workSignMap
     * @param workId
     * @param workCostDto
     */
    private void setByReturnFinishTime(Map<Long, WorkTransfer> workTransferMap, Map<Long, WorkSign> workSignMap, Long workId, WorkCostDto workCostDto) {
        /**取work_transfer的操作时间作为完成时间*/
        WorkTransfer tempWorkTransfer = workTransferMap.get(workId);
        /**取work_sign的交通工具*/
        WorkSign tempWorkSign = workSignMap.get(workId);

        String goTime = "";
        if (tempWorkSign != null) {
            /**交通工具*/
            workCostDto.setTrafficName(TrafficEnum.getNameByCode(tempWorkSign.getTraffic()));
            /**签到时间*/
            if (tempWorkSign.getSignTime() != null) {
                goTime = DateUtil.format(tempWorkSign.getSignTime(), "yyyy-MM-dd HH:mm");
            }
        }
        workCostDto.setGoTime(goTime);

        /**完成时间*/
        String finishTime = "";
        if (tempWorkTransfer != null) {
            if (tempWorkTransfer.getOperateTime() != null) {
                finishTime = DateUtil.format(tempWorkTransfer.getOperateTime(), "yyyy-MM-dd HH:mm");
            }
        }
        workCostDto.setFinishTime(finishTime);
    }

    /**
     * 设置正常关闭的完成时间
     *
     * @param workDto
     * @param workCostDto
     */
    private void setByNormalFinishTime(WorkDto workDto, WorkCostDto workCostDto) {

        workCostDto.setTrafficName(TrafficEnum.getNameByCode(workDto.getTraffic()));
        String goTime = "";
        if (workDto.getSignTime() != null) {
            goTime = DateUtil.format(workDto.getSignTime(), "yyyy-MM-dd HH:mm");
        }
        workCostDto.setGoTime(goTime);

        String finishTime = "";
        if (workDto.getFinishTime() != null) {
            finishTime = DateUtil.format(workDto.getFinishTime(), "yyyy-MM-dd HH:mm");
        }
        workCostDto.setFinishTime(finishTime);
    }

    private void doQueryWorkSign(String month, Long engineerUserId, Set<Long> engineerTotalWorkIdSet, Set<Long> tempTotalWorkIdSet, Map<Long, WorkSign> workSignMap) {
        WorkSignFilter workSignFilter = new WorkSignFilter();
        workSignFilter.setSignMonth(month);
        workSignFilter.setOperator(engineerUserId);
        workSignFilter.setWorkIdSet(tempTotalWorkIdSet);

        /**如果这个条件不存在，则不需要查找签到记录,这个条件不能去掉，否则会造成查找签到记录是错的*/
        if (CollectionUtil.isEmpty(tempTotalWorkIdSet)) {
            return;
        }

        List<WorkSign> workSignList = workSignService.queryWorkSignForCost(workSignFilter);

        if (CollectionUtil.isNotEmpty(workSignList)) {
            for (WorkSign workSign : workSignList) {

                engineerTotalWorkIdSet.add(workSign.getWorkId());
                /**如果同一个工单有最新的签到记录，仅保留最新那一条，签到记录可能存在跨月的情况*/
                workSignMap.put(workSign.getWorkId(), workSign);
            }
        }
    }

    /**
     * 处理客服退单、客户撤单
     *
     * @param workCostFilter
     * @param tempTotalWorkIdSet
     * @param workTransferMap
     */
    private void doReturnCloseWork(WorkCostFilter workCostFilter, Set<Long> tempTotalWorkIdSet, Map<Long, WorkTransfer> workTransferMap) {

        List<Integer> modeList = new ArrayList<>();
        modeList.add(WorkTransferEnum.MANUAL_RETURN.getCode());
        modeList.add(WorkTransferEnum.CUSTOM_RECALL.getCode());

        WorkTransferFilter workTransferFilter = new WorkTransferFilter();
        workTransferFilter.setModeList(modeList);
        /**operateMonth时间不会改变，稳定可靠*/
        workTransferFilter.setOperateMonth(workCostFilter.getMonth());
        List<WorkTransfer> workTransferList = workTransferService.queryWorkTransferForCost(workTransferFilter);
        if (CollectionUtil.isNotEmpty(workTransferList)) {
            /**保存工单ID与流转对象，如果同一个月份同一个工单有2单，仅保留最新的那一条，数据库查询的数据已按照升序排序，
             * 按照目前的结构，workTransferMap是获得最新的。*/
            for (WorkTransfer workTransfer : workTransferList) {
                tempTotalWorkIdSet.add(workTransfer.getWorkId());
                workTransferMap.put(workTransfer.getWorkId(), workTransfer);
            }
        }
    }

    /**
     * 处理正常关闭的工单(engineerTotalWorkIdSet用来保存当前工程师有效工单，tempTotalWorkIdSet用来保存所有符合条件的工单)
     *
     * @param engineerUserId
     * @param engineerTotalWorkIdSet
     * @param tempTotalWorkIdSet
     * @param workDealFilter
     */
    private void doNormalCloseWork(Long engineerUserId, Set<Long> engineerTotalWorkIdSet, Set<Long> tempTotalWorkIdSet, WorkDealFilter workDealFilter) {

        List<WorkDeal> normalCloseWorkList = workDealMapper.queryCloseWorkForCost(workDealFilter);

        if (CollectionUtil.isNotEmpty(normalCloseWorkList)) {

            for (WorkDeal workDeal : normalCloseWorkList) {

                /**说明当前工单符合条件，另外保存的目的是为了查出有签到记录但不在服务工程师、协同工程师的人员*/
                tempTotalWorkIdSet.add(workDeal.getWorkId());

                /**说明当前服务工程师符合条件，可计入报销*/
                if (engineerUserId.compareTo(workDeal.getEngineer()) == 0) {
                    engineerTotalWorkIdSet.add(workDeal.getWorkId());
                }

                /**协同工程师*/
                String engineerUserIdString = StrUtil.toString(engineerUserId);
                String togetherEngineers = StrUtil.trimToEmpty(workDeal.getTogetherEngineers());
                List<String> togetherEngineersList = Arrays.stream(togetherEngineers.split(","))
                        .filter(StrUtil::isNotBlank).collect(Collectors.toList());
                if (CollectionUtil.isNotEmpty(togetherEngineersList)) {
                    if (togetherEngineersList.contains(engineerUserIdString)) {
                        /**说明当前服务工程师符合条件，可计入报销*/
                        engineerTotalWorkIdSet.add(workDeal.getWorkId());
                    }
                }
            }
        }
    }

    /**
     * 初始化数据
     *
     * @param workDtoList
     */
    private void initData(List<WorkDto> workDtoList) {
        if (CollectionUtil.isNotEmpty(workDtoList)) {

            /**企业列表*/
            List<Long> corpIdList = new ArrayList<>();
            List<Long> customIdList = new ArrayList<>();
            for (WorkDto workDto : workDtoList) {
                corpIdList.add(workDto.getDemanderCorp());
                corpIdList.add(workDto.getServiceCorp());
                corpIdList.add(workDto.getCustomCorp());
                customIdList.add(workDto.getCustomId());
            }

            /**工单类型映射，可以获得全部映射，此数据不会影响性能*/
            workTypeMap = workTypeService.mapWorkType();

            /**企业ID与名称映射*/
            Result<Map<Long, String>> corpResult = uasFeignService.mapCorpIdAndNameByCorpIdList(JsonUtil.toJson(corpIdList));
            if (corpResult.getCode() == Result.SUCCESS) {
                corpMap = corpResult.getData();
                corpMap = corpMap == null ? new HashMap<>(0) : corpMap;
            }

            /**委托商客户企业与名称映射*/
            demanderCustomCorpNameMap = demanderCustomService.mapIdAndCustomNameByIdList(customIdList);
            demanderCustomCorpNameMap = demanderCustomCorpNameMap == null ? new HashMap<>(0) : demanderCustomCorpNameMap;

            /**客户设备品牌与名称映射*/
            Result<Map<Long, String>> deviceBrandResult = deviceFeignService.mapDeviceBrandByJsonCorpIds(JsonUtil.toJson(corpIdList));
            if (deviceBrandResult.getCode() == Result.SUCCESS) {
                deviceBrandMap = deviceBrandResult.getData();
                deviceBrandMap = deviceBrandMap == null ? new HashMap<>(0) : deviceBrandMap;
            }

            /**客户设备型号与名称映射*/
            Result<Map<Long, String>> deviceModelResult = deviceFeignService.mapDeviceModelByJsonCorpIds(JsonUtil.toJson(corpIdList));
            if (deviceModelResult.getCode() == Result.SUCCESS) {
                deviceModelMap = deviceModelResult.getData();
                deviceModelMap = deviceModelMap == null ? new HashMap<>(0) : deviceModelMap;
            }

            /**服务网点与名称映射*/
            serviceBranchMap = serviceBranchService.mapServiceBranchByCorpIdList(corpIdList);
            /**设备网点与名称映射*/
            deviceBranchMap = deviceBranchService.mapDeviceBranchByCorpIdList(corpIdList);
            customDeviceBranchMap = deviceBranchService.mapCustomDeviceBranchByCustomIdList(customIdList);
        }
    }

    private List<WorkDto> queryWorkDtoList(Set<Long> engineerTotalWorkIdSet) {
        WorkFilter workFilter = new WorkFilter();
        List<Long> engineerTotalWorkIds = engineerTotalWorkIdSet.stream().collect(Collectors.toList());
        workFilter.setListWorkIds(engineerTotalWorkIds);

        /**必须判断，否则没有此条件，下述查询语句queryEngineerWork会存在严重的性能问题。*/
        if (CollectionUtil.isEmpty(engineerTotalWorkIds)) {
            return null;
        }

        List<WorkDto> workDtoList = workRequestMapper.queryEngineerWorkCost(workFilter);
        if (CollectionUtil.isNotEmpty(workDtoList)) {
            return workDtoList;
        }

        return null;
    }


    private Long getUserIdByMobile(String mobile) {
        Result<Map<String, Long>> mapResult = this.uasFeignService.getUserIdByMobile(mobile);
        if (mapResult.getCode() == Result.SUCCESS) {
            Map<String, Long> map = mapResult.getData() != null ? mapResult.getData() : new HashMap<>();
            return map.get("userId");
        } else {
            throw new AppException(mapResult.getMsg());
        }
    }

    /**
     * 检查两边系统的姓名
     *
     * @param engineer
     * @param requestUserName
     */
    private void checkUserReal(Long engineer, String requestUserName) {
        Result userResult = uasFeignService.findUserRealDtoById(engineer);
        UserRealDto userRealDto = null;
        if (userResult != null && userResult.getCode() == Result.SUCCESS) {
            userRealDto = JsonUtil.parseObject(JsonUtil.toJson(userResult.getData()), UserRealDto.class);
        }

        if (userRealDto == null) {
            throw new AppException("请先在新一代云服务平台进行实名认证，实名认证后再重试");
        }

        if (StrUtil.isEmpty(userRealDto.getUserName())) {
            throw new AppException("新一代云服务平台中实名认证姓名不能为空");
        }

        String newReqUserName = StrUtil.trimToEmpty(requestUserName);
        String realUserName = StrUtil.trimToEmpty(userRealDto.getUserName());
        if (newReqUserName.equalsIgnoreCase(realUserName) || newReqUserName.contains(realUserName) || realUserName.contains(newReqUserName)) {
            /**由于旧系统会出现重名的工程师，命名规则上多数是A某，A某（01），而新系统不对重名的进行校验，因此判断算法需要考虑包含的关系，只要有包含就算通过*/
        } else {
            throw new AppException("姓名不一致，新一代云服务平台实名认证为【" + realUserName + "】，当前发起报销系统姓名为【" + newReqUserName + "】，请重新调整");
        }

    }

    /**
     * 检查请求数据是否为空
     *
     * @param workCostFilter
     */
    private void checkDataNotNull(WorkCostFilter workCostFilter) {
        if (workCostFilter == null) {
            throw new AppException("请求参数传输错误，请重试");
        }

        if (StrUtil.isEmpty(workCostFilter.getMonth())) {
            throw new AppException("报销所属月份不能为空，请重试");
        }

        if (StrUtil.isEmpty(workCostFilter.getUserName())) {
            throw new AppException("工程师姓名不能为空，请重试");
        }

        if (StrUtil.isEmpty(workCostFilter.getMobile())) {
            throw new AppException("工程师手机号不能为空，请重试");
        }
    }


}
