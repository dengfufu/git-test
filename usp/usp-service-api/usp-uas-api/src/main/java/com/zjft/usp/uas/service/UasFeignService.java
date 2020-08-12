package com.zjft.usp.uas.service;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.dto.CorpBankAccountDto;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.fallback.UasFeignServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * uas feign接口
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/11 10:20 上午
 **/
@FeignClient(name = "usp-uas", fallbackFactory = UasFeignServiceFallbackFactory.class)
public interface UasFeignService {

    /**
     * 获得用户信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2020/5/13 16:36
     **/
    @RequestMapping(value = "/account/feign/{userId}", method = RequestMethod.GET)
    Result findUserInfoDtoById(@PathVariable("userId") Long userId);

    /**
     * 添加企业
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/1/16 09:41
     **/
    @RequestMapping(value = "/corp-registry/feign/add", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Long> addCorp(@RequestBody String json);

    /**
     * 获得企业编号与名称映射
     *
     * @param
     * @return
     * @author zgpi
     * @date 2019/10/16 7:54 下午
     **/
    @GetMapping(value = "/corp-registry/feign/map")
    Result<Map<Long, String>> mapCorpIdAndName();

    /**
     * 根据ID获得企业信息
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/11 10:21 上午
     **/
    @GetMapping(value = "/corp-registry/feign/{corpId}")
    Result findCorpById(@PathVariable("corpId") Long corpId);

    /**
     * 根据ID获得企业信息
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/11 10:21 上午
     **/
    @GetMapping(value = "/corp-registry/feign/info/{corpId}")
    Result findCorpAddrById(@PathVariable("corpId") Long corpId);

    /**
     * 根据企业名称获得企业信息
     *
     * @param corpName
     * @return
     * @author zgpi
     * @date 2019/10/11 10:21 上午
     **/
    @GetMapping(value = "/corp-registry/feign/name/{corpName}")
    Result getCorpByName(@PathVariable("corpName") String corpName);


    /**
     * 获取下拉列表
     *
     * @return
     */
    @GetMapping(value = "/corp-registry/getSelectData/{userId}")
    Result<Map<String, Object>> getSelectData(@PathVariable("userId") Long userId);

    /**
     * 根据idList查企业信息列表(参数数据量大时建议采用这一种，防止出现400错误)
     *
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/corpIds/list", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result listCorpByIdList(@RequestBody String jsonFilter);


    /**
     * 根据条件查询企业编号列表
     *
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/listCorpIdByFilter", method = RequestMethod.POST, headers = {"content-type=application/json"})
    @ResponseBody
    Result<List<Long>> listCorpIdByFilter(@RequestBody String jsonFilter);

    /**
     * 根据idList获得企业ID与名称映射
     *
     * @param jsonFilter
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/mapByCorpIdList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapCorpIdAndNameByCorpIdList(@RequestBody String jsonFilter);

    /**
     * 获取下拉列表
     *
     * @return
     */
    @GetMapping(value = "/corp-registry/getSelectValue/{userId}")
    @ResponseBody
    Result<Map<String, Object>> getSelectValue(@PathVariable("userId") Long userId);

    /**
     * 查询地区映射
     *
     * @param json
     * @return
     */
    @RequestMapping(value = "/area/feign/mapByCodeList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<String, String>> mapAreaCodeAndNameByCodeList(@RequestBody String json);

    /**
     * 查询地区映射
     *
     * @return
     */
    @GetMapping(value = "/area/feign/map")
    Result<Map<String, String>> mapAreaCodeAndName();

    /**
     * 根据编码获得地区信息
     *
     * @param code
     * @return
     * @author zgpi
     * @date 2019/10/11 2:12 下午
     **/
    @GetMapping(value = "/area/feign/{code}")
    Result findAreaByCode(@PathVariable(name = "code") String code);

    /**
     * 查询用户的企业ID
     *
     * @param userId
     * @return
     */
    @GetMapping(value = "/corp-user/feign/getCorpIdList/{userId}")
    Result<List<Long>> getCorpIdList(@PathVariable("userId") Long userId);


    /**
     * 根据条件分页查询企业员工
     *
     * @param jsonFilter
     * @return
     */
    @PostMapping(value = "/corp-user/feign/query", headers = {"content-type=application/json"})
    Result queryCorpUser(@RequestBody String jsonFilter);


    /**
     * 模糊查询企业人员
     *
     * @param jsonFilter
     * @return
     * @author zgpi
     * @date 2019/12/23 14:49
     **/
    @PostMapping(value = "/corp-user/feign/match", headers = {"content-type=application/json"})
    Result matchCorpUser(@RequestBody String jsonFilter);


    /**
     * 模糊查询企业信息
     *
     * @param jsonFilter
     * @return
     * @author zrlin
     * @date 2019/02/09 15:32
     **/
    @PostMapping(value = "/corp-registry/match", headers = {"content-type=application/json"})
    Result matchCorp(@RequestBody String jsonFilter);

    /**
     * 人员ID与名称映射
     *
     * @param jsonFilter
     * @return
     * @author zgpi
     * @date 2019/10/12 3:09 下午
     **/
    @RequestMapping(value = "/user-real/feign/mapByUserIdList", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapUserIdAndNameByUserIdList(@RequestBody String jsonFilter);

    /**
     * 人员ID与手机号映射
     *
     * @param jsonFilter
     * @return
     * @author zgpi
     * @date 2019/10/12 3:09 下午
     **/
    @RequestMapping(value = "/account/feign/mapUserIdAndMobile", method = RequestMethod.POST, headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapUserIdAndMobileByUserIdList(@RequestBody String jsonFilter);

    /**
     * 根据企业编号列表获得企业人员ID与名称映射
     *
     * @param userIdList
     * @return
     * @author zgpi
     * @date 2019/10/12 3:09 下午
     **/
    @PostMapping(value = "/corp-user/feign/mapCorpUserInfoByUserIdList")
    Result<Map<String, Map<String, String>>> mapCorpUserInfoByUserIdList(@RequestParam("userIdList") Collection<Long> userIdList,
                                                                         @RequestParam("corpId") Long corpId);

    /**
     * 根据企业编号列表获得企业人员ID与名称映射
     *
     * @param corpIdList
     * @return
     * @author zgpi
     * @date 2019/10/12 3:09 下午
     **/
    @PostMapping(value = "/corp-user/feign/mapByCorpIdList")
    Result<Map<Long, String>> mapUserIdAndNameByCorpIdList(@RequestParam("corpIdList") Collection<Long> corpIdList);

    /**
     * 根据企业编号获得企业人员ID与名称映射
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/10/12 3:09 下午
     **/
    @RequestMapping(value = "/corp-user/feign/map/{corpId}", method = RequestMethod.GET)
    Result<Map<Long, String>> mapUserIdAndNameByCorpId(@PathVariable("corpId") Long corpId);


    /**
     * 根据企业编号获得人员编号列表
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/corp-user/feign/user/list/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listUserIdByCorpId(@PathVariable("corpId") Long corpId);

    /**
     * 判断手机号是否已注册
     *
     * @param JsonFilter
     * @return
     */
    @PostMapping(value = "/register/checkIsRegister", headers = {"content-type=application/json"})
    Result<Boolean> checkIsRegister(@RequestBody String JsonFilter);

    /**
     * 根据手机号获取用户编号
     *
     * @param mobile
     * @return
     */
    @GetMapping(value = "/account/feign/getUserId/{mobile}")
    Result<Map<String, Long>> getUserIdByMobile(@PathVariable("mobile") String mobile);

    /**
     * 获得用户实名认证信息
     *
     * @param userId
     * @return
     * @author zgpi
     * @date 2019/11/13 17:34
     **/
    @GetMapping(value = "/user-real/feign/{userId}")
    Result findUserRealDtoById(@PathVariable("userId") Long userId);

    /**
     * 添加企业员工
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/corp-user/feign/addCorpUser", headers = {"content-type=application/json"})
    Result addCorpUser(@RequestBody String jsonObject);

    /**
     * 删除企业员工
     *
     * @param userId
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/corp-user/feign/{userId}/{corpId}", method = RequestMethod.DELETE)
    Result delCorpUser(@PathVariable("userId") Long userId,
                       @PathVariable("corpId") Long corpId);

    /**
     * 企业管理员添加用户时注册
     *
     * @param mobile
     * @param userName
     * @return
     */
    @PostMapping(value = "/register/feign/registryByCorpAdmin")
    Result registryByCorpAdmin(@RequestParam("mobile") String mobile,
                               @RequestParam("userName") String userName);

    /**
     * 企业管理员修改用户
     *
     * @param userId
     * @param userName
     * @return
     */
    @PostMapping(value = "/register/feign/updateByCorpAdmin")
    Result updateByCorpAdmin(@RequestParam("userId") Long userId,
                             @RequestParam("userName") String userName);

    /**
     * 更新企业用户
     *
     * @param jsonFilter
     * @return
     */
    @PostMapping(value = "/corp-user/feign/updateCorpUser")
    Result updateCorpUser(@RequestBody String jsonFilter);

    /**
     * 根据省市区名称来获取code
     *
     * @param jsonFilter
     * @return
     */
    @PostMapping(value = "/area/getArea", headers = {"content-type=application/json"})
    Result getAreaCodeByName(@RequestBody String jsonFilter);


    /**
     * 列举可选的租户
     *
     * @param jsonFilter
     * @return
     */
    @PostMapping(value = "/sys-tenant/list", headers = {"content-type=application/json"})
    Result listTenant(@RequestBody String jsonFilter);

    /**
     * 获得用户的权限编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/2/16 14:33
     */
    @RequestMapping(value = "/user-right/feign/list/{userId}/{corpId}", method = RequestMethod.GET)
    Result<List<Long>> listUserRightId(@PathVariable("userId") Long userId,
                                       @PathVariable("corpId") Long corpId);

    /**
     * 获得人员范围权限
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/3/13 16:59
     */
    @RequestMapping(value = "/sys-user-right-scope/feign/list", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result listUserRightScope(@RequestBody String json);

    /**
     * 删除人员范围权限
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/3/20 09:39
     */
    @RequestMapping(value = "/sys-user-right-scope/feign/delete", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result delUserRightScope(@RequestBody String json);

    /**
     * 获得系统租户
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/15 17:40
     */
    @RequestMapping(value = "/sys-tenant/feign/{corpId}", method = RequestMethod.GET)
    Result findSysTenant(@PathVariable("corpId") Long corpId);


    /**
     * 设置系统租户
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/15 17:40
     */
    @RequestMapping(value = "/sys-tenant/feign/addSysTenantDemander", method = RequestMethod.POST)
    Result addSysTenantDemander(@RequestParam("userId") Long userId,
                       @RequestParam("corpId") Long corpId);

    @RequestMapping(value = "/corp-registry/feign/register", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result feignRegisterCorp(@RequestBody String jsonString);

    /**
     * 根据人员编号和企业编号获得企业人员信息
     *
     * @param userId
     * @param corpId
     * @return
     * @author Qiugm
     * @date 2020-03-17
     */
    @PostMapping(value = "/corp-user/feign/findCorpUserByUserIdAndCorpId")
    Result findCorpUserByUserIdAndCorpId(@RequestParam("userId") Long userId,
                                         @RequestParam("corpId") Long corpId);

    /**
     * 根据企业编号和人员编号列表获得企业员工账号列表
     *
     * @param json
     * @return
     * @author zgpi
     * @date 2020/4/7 21:03
     */
    @RequestMapping(value = "/corp-user/feign/listUserIdByAccountList", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result<List<Long>> listUserIdByAccountList(@RequestBody String json);

    @GetMapping("/corp-registry/feign/getCorpDetail/{corpId}")
    Result<CorpDto> getCropDetailByID(@PathVariable("corpId") Long corpId);


    /**
     * 远程调用：自动认证
     * @date 2020/6/14
     * @param corpId
     * @return com.zjft.usp.common.model.Result
     */
    @GetMapping("/corp-registry/feign/register/{corpId}")
    Result addVerifyByCorpId(@PathVariable("corpId") Long corpId);


    @GetMapping(value = "/wx/feign/openid")
    String getOpenidByUserid(@RequestParam("userId") Long userId);

    /**
     * 远程获取企业系统编号与企业编号的映射
     *
     * @param corpIdListJson
     * @return
     */
    @RequestMapping(value = "/corp-registry/feign/mapCorpIdAndCode", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result<Map<Long, String>> mapCorpIdAndCode(@RequestBody String corpIdListJson);

    /**
     * 获取企业银行账号
     *
     * @param corpId
     * @return
     */
    @RequestMapping(value = "/corp-bank-account/feign/{corpId}", method = RequestMethod.GET)
    Result findCorpBankAccount(@PathVariable("corpId") Long corpId);

    /**
     * 获取企业编号以及银行账户的映射
     *
     * @param corpIdListJson
     * @return
     */
    @RequestMapping(value = "/corp-bank-account/feign/mapByCorpIdList", method = RequestMethod.POST,
            headers = {"content-type=application/json"})
    Result<Map<Long, CorpBankAccountDto>> mapCorpIdAndBankAccount(@RequestBody String corpIdListJson);

}
