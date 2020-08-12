package com.zjft.usp.anyfix.service;

import com.zjft.usp.anyfix.service.fallback.AnyfixFeignServiceFallbackFactory;
import com.zjft.usp.common.model.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 工单feign接口服务类
 *
 * @author zgpi
 * @date 2019/10/17 11:24 上午
 **/
@FeignClient(name = "usp-anyfix", fallbackFactory = AnyfixFeignServiceFallbackFactory.class)
public interface AnyfixFeignService {

    /**
     * 根据企业ID获得所有关联的企业ID列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/15 8:39 上午
     **/
    @RequestMapping(value = "/demander-service/feign/listAllRelatedCorpIds/{corpId}", method = RequestMethod.GET)
    Result listAllCorpIdByCorpId(@PathVariable("corpId") Long corpId);

    /**
     * 根据网点ID获得网点信息
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2019/10/22 7:21 下午
     **/
    @RequestMapping(value = "/device-branch/feign/{branchId}", method = RequestMethod.GET)
    Result findDeviceBranch(@PathVariable("branchId") Long branchId);

    /**
     * 根据客户编号列表获取设备网点映射
     *
     * @param customIdList
     * @return
     */
    @RequestMapping(value = "/device-branch/feign/mapByCustomIdList", method = RequestMethod.POST)
    Result<Map<Long, String>> mapDeviceBranchByCustomIdList(@RequestParam("customIdList") List<Long> customIdList);


    /**
     * 根据网点编号列表查询网点编号与网点名称
     *
     * @param branchIdList
     * @return
     */
    @RequestMapping(value = "/device-branch/feign/mapIdAndName", method = RequestMethod.POST)
    Result<Map<Long, String>> mapIdAndBranchName(@RequestParam("branchIdList") List<Long> branchIdList);

    /**
     * 根据网点编号列表查询网点编号与网点名称
     *
     * @param branchIdListJson
     * @return
     * @author zgpi
     * @date 2020/6/3 20:02
     **/
    @RequestMapping(value = "/service-branch/feign/mapIdAndName",
            method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapServiceBranchIdAndName(@RequestBody String branchIdListJson);

    /**
     * 根据网点ID获得服务网点信息
     *
     * @param branchId
     * @return
     * @author canlei
     * @date 2019/10/30 10:00
     **/
    @RequestMapping(value = "/service-branch/feign/{branchId}", method = RequestMethod.GET)
    Result findServiceBranch(@PathVariable("branchId") Long branchId);

    /**
     * 获得人员所在服务网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 18:14
     */
    @RequestMapping(value = "/service-branch/feign/own/list/{userId}/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listOwnBranchId(@PathVariable("userId") Long userId,
                                       @PathVariable("corpId") Long corpId);

    /**
     * 获得人员所在服务网点的下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 18:14
     */
    @RequestMapping(value = "/service-branch/feign/lower/list/{userId}/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listOwnLowerBranchId(@PathVariable("userId") Long userId,
                                            @PathVariable("corpId") Long corpId);

    /**
     * 获得人员所在服务网点及下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 18:14
     */
    @RequestMapping(value = "/service-branch/feign/own-lower/list/{userId}/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listOwnAndLowerBranchId(@PathVariable("userId") Long userId,
                                               @PathVariable("corpId") Long corpId);

    /**
     * 获得下级服务网点编号列表
     *
     * @param branchIdListJson
     * @return
     * @author zgpi
     * @date 2020/6/5 09:31
     **/
    @RequestMapping(value = "/service-branch/feign/lower/list", method = RequestMethod.POST)
    Result<List<Long>> listLowerBranchId(@RequestBody String branchIdListJson);

    /**
     * 人员编号与服务网点名称(带省份)映射
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/3/13 09:30
     */
    @RequestMapping(value = "/service-branch-user/feign/mapUserIdAndServiceBranchNames",
            method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapUserIdAndServiceBranchNames(@RequestBody String json);


    @PostMapping(value = "/device-branch/feign/batchAddDeviceBranch", headers = {"content-type=application/json"})
    Result<Map<String, Long>> batchAddDeviceBranch(@RequestBody String json);


    /**
     * 根据条件获得服务网点人员列表
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/3/13 10:27
     */
    @RequestMapping(value = "/service-branch-user/feign/user/list", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result<List<Long>> listServiceBranchUserIdByFilter(String json);

    /**
     * 根据委托方和客户关系表主键list获取主键与客户名称的映射
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/demander-custom/feign/mapIdAndNameByIdList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapIdAndCustomNameByIdList(@RequestBody String json);

    /**
     * 根据主键获得服务委托方和客户关系表详情
     *
     * @param customId
     * @return
     * @author zgpi
     * @date 2019/10/31 18:38
     **/
    @RequestMapping(value = "/demander-custom/feign/{customId}", method = RequestMethod.GET)
    Result findDemanderCustomDtoById(@PathVariable("customId") Long customId);

    /**
     * 根据客户企业编号获取供应商客户关系表主键list
     *
     * @param customCorp
     * @return
     */
    @RequestMapping(value = "/demander-custom/feign/listIdsByCustom/{customCorp}", method = RequestMethod.GET)
    Result<List<Long>> listCustomIdsByCustomCorp(@PathVariable("customCorp") Long customCorp);

    /**
     * 根据委托商获取客户编号和名称的映射
     *
     * @param demanderCorp
     * @return
     */
    @RequestMapping(value = "/demander-custom/feign/mapCustomIdAndName/{demanderCorp}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapCustomIdAndNameByDemander(@PathVariable("demanderCorp") Long demanderCorp);

    /**
     * 根据委托商获取服务商编号列表
     *
     * @param demanderCorp
     * @return
     */
    @RequestMapping(value = "/demander-service/feign/listServiceCorp/{demanderCorp}", method = RequestMethod.GET)
    Result<List<Long>> listServiceCorpIdsByDemander(@PathVariable("demanderCorp") Long demanderCorp);

    /**
     * 根据企业获取委托商编号列表
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/demander-service/feign/demanderCorpId/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listDemanderCorpId(@PathVariable("corpId") Long corpId);

    @RequestMapping(value = "/work-request/{workId}", method = RequestMethod.GET)
    Result findWorkById(@PathVariable("workId") Long workId);

    /**
     * 批量添加自定义字段数据
     *
     * @param jsonObject 自定义字段数据json
     * @return
     */
    @PostMapping(value = "/custom-field-data/feign/add", headers = {"content-type=application/json"})
    Result addCustomFieldDataList(@RequestBody String jsonObject);

    /**
     * 根据条件查询自定义字段数据
     *
     * @param jsonObject 自定义字段数据json
     * @return
     */
    @PostMapping(value = "/custom-field-data/feign/query", headers = {"content-type=application/json"})
    Result queryCustomFieldData(@RequestBody String jsonObject);

    /**
     * 根据设备档案ID删除自定义数据
     *
     * @param deviceId 设备档案id
     * @return
     */
    @RequestMapping(value = "/custom-field-data/feign/{deviceId}", method = RequestMethod.DELETE)
    Result deleteByDeviceId(@PathVariable("deviceId") Long deviceId);

    /**
     * 删除网点人员
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/25 17:13
     */
    @RequestMapping(value = "/branch-user/feign/deleteBranchUser", method = RequestMethod.POST)
    Result delBranchUser(@RequestParam("userId") Long userId,
                         @RequestParam("corpId") Long corpId,
                         @RequestParam("currentUserId") Long currentUserId,
                         @RequestParam("clientId") String clientId);


    /**
     * 根据企业编号获得关联的委托商编号名称map
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/demander/feign/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> getDemanderIdNameMap(@PathVariable("corpId") Long corpId);

    /**
     * 查询人员的网点信息
     *
     * @param userId
     * @return
     */
    @RequestMapping(value = "/service-branch-user/feign/user/branchName/{userId}", method = RequestMethod.GET)
    Result<String> getBranchesNameByUserId(@PathVariable("userId") Long userId);

    @PostMapping(value = "/demander-custom/feign/customCorp/batchSave")
    Result<Map<String, Map<String, Long>>> batchSaveCustomCorp(@RequestParam("customCorpNameList") List<String> customCropNameList,
                                                               @RequestParam("demanderCorp") Long demanderCorp,
                                                               @RequestParam("userId") Long userId);

    /**
     * 获得客户经理的委托商编号列表
     *
     * @param corpId
     * @param manager
     * @return
     * @author zgpi
     * @date 2020/6/22 14:55
     **/
    @RequestMapping(value = "/demander-service-manager/feign/demander/list", method = RequestMethod.GET)
    Result<List<Long>> listDemanderCorpByManager(@RequestParam("corpId") Long corpId,
                                                 @RequestParam("manager") Long manager);
}


