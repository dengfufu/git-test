package com.zjft.usp.uas.service.fallback;

import com.zjft.usp.common.model.Result;
import com.zjft.usp.uas.dto.CorpBankAccountDto;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.service.UasFeignService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author zphu
 * @date 2019/9/27 14:17
 * @Version 1.0
 **/
@Slf4j
@Component
public class UasFeignServiceFallbackFactory implements FallbackFactory<UasFeignService> {
    @Override
    public UasFeignService create(Throwable cause) {
        return new UasFeignService() {


            @Override
            public Result<Map<String, Object>> getSelectData(Long userId) {
                return null;
            }

            /**
             * 根据idList查企业信息列表
             *
             * @param json
             * @return
             */
            @Override
            public Result listCorpByIdList(String json) {
                return null;
            }

            @Override
            public Result<List<Long>> listCorpIdByFilter(String jsonFilter) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapCorpIdAndNameByCorpIdList(String jsonFilter) {
                return null;
            }

            /**
             * 获得用户信息
             *
             * @param userId
             * @return
             * @author zgpi
             * @date 2020/5/13 16:36
             **/
            @Override
            public Result findUserInfoDtoById(Long userId) {
                return null;
            }

            /**
             * 添加企业
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/1/16 09:41
             **/
            @Override
            public Result addCorp(String json) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapCorpIdAndName() {
                return null;
            }

            @Override
            public Result findCorpById(Long corpId) {
                return null;
            }

            @Override
            public Result findCorpAddrById(Long corpId) {
                return null;
            }

            @Override
            public Result getCorpByName(String corpName) {
                return null;
            }


            @Override
            public Result<Map<String, Object>> getSelectValue(Long userId) {
                return null;
            }

            /**
             * 查询地区映射
             *
             * @param json
             * @return
             */
            @Override
            public Result<Map<String, String>> mapAreaCodeAndNameByCodeList(String json) {
                return null;
            }

            @Override
            public Result<Map<String, String>> mapAreaCodeAndName() {
                return null;
            }

            @Override
            public Result findAreaByCode(String code) {
                return null;
            }

            @Override
            public Result<List<Long>> getCorpIdList(Long userId) {
                return null;
            }


            @Override
            public Result queryCorpUser(String jsonFilter) {
                return null;
            }

            /**
             * 模糊查询企业人员
             *
             * @param jsonFilter
             * @return
             * @author zgpi
             * @date 2019/12/23 14:49
             **/
            @Override
            public Result matchCorpUser(String jsonFilter) {
                return null;
            }

            @Override
            public Result matchCorp(String jsonFilter) {
                return null;
            }

            /**
             * 人员ID与名称映射
             *
             * @param jsonFilter
             * @return
             * @author zgpi
             * @date 2019/10/12 3:09 下午
             **/
            @Override
            public Result<Map<Long, String>> mapUserIdAndNameByUserIdList(String jsonFilter) {
                return null;
            }

            /**
             * 人员ID与手机号映射
             *
             * @param jsonFilter
             * @return
             * @author zgpi
             * @date 2019/10/12 3:09 下午
             **/
            @Override
            public Result<Map<Long, String>> mapUserIdAndMobileByUserIdList(String jsonFilter) {
                return null;
            }

            @Override
            public Result<Map<String, Map<String, String>>> mapCorpUserInfoByUserIdList(Collection<Long> corpIdList, Long corpId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapUserIdAndNameByCorpIdList(Collection<Long> corpIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapUserIdAndNameByCorpId(Long corpId) {
                return null;
            }

            /**
             * 根据企业编号获得人员编号列表
             *
             * @param corpId
             * @return
             */
            @Override
            public Result<List<Long>> listUserIdByCorpId(Long corpId) {
                return null;
            }

//            @Override
//            public Result findCorpUserByUserIdAndCorpId(Long userId, Long corpId) {
//                return null;
//            }

            @Override
            public Result<Boolean> checkIsRegister(String JsonFilter) {
                return null;
            }

            @Override
            public Result<Map<String, Long>> getUserIdByMobile(String mobile) {
                return null;
            }

            @Override
            public Result findUserRealDtoById(Long userId) {
                return null;
            }

            @Override
            public Result addCorpUser(String jsonObject) {
                return null;
            }

            /**
             * 删除企业员工
             *
             * @param userId
             * @param corpId
             * @return
             */
            @Override
            public Result delCorpUser(Long userId, Long corpId) {
                return null;
            }

            @Override
            public Result<Long> registryByCorpAdmin(String mobile, String userName) {
                return null;
            }

            @Override
            public Result updateByCorpAdmin(Long userId, String userName) {
                return null;
            }

            @Override
            public Result updateCorpUser(String jsonObject) {
                return null;
            }

            @Override
            public Result getAreaCodeByName(String jsonFilter) {
                return null;
            }

            @Override
            public Result listTenant(String jsonFilter) {
                return null;
            }

            /**
             * 获得用户的权限编号列表
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/2/16 14:33
             */
            @Override
            public Result<List<Long>> listUserRightId(Long userId, Long corpId) {
                return null;
            }

            /**
             * 获得人员范围权限
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/3/13 16:59
             */
            @Override
            public Result listUserRightScope(String json) {
                return null;
            }

            /**
             * 删除人员范围权限
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/3/20 09:39
             */
            @Override
            public Result delUserRightScope(String json) {
                return null;
            }


            /**
             * 获得系统租户
             *
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/3/15 17:40
             */
            @Override
            public Result findSysTenant(Long corpId) {
                return null;
            }

            /**
             * 设置系统租户
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/3/15 17:40
             */
            @Override
            public Result addSysTenantDemander(Long userId, Long corpId) {
                return null;
            }

            /**
             * 根据人员编号和企业编号获得企业人员信息
             *
             * @author Qiugm
             * @date 2020-03-17
             * @param userId
             * @param corpId
             * @return
             */
            @Override
            public Result findCorpUserByUserIdAndCorpId(Long userId, Long corpId) {
                return null;
            }

            /**
             * 根据企业编号和人员编号列表获得企业员工账号列表
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/4/7 21:03
             */
            @Override
            public Result<List<Long>> listUserIdByAccountList(String json) {
                return null;
            }

            @Override
            public Result<CorpDto> getCropDetailByID(Long corpId) {
                return null;
            }

            @Override
            public Result addVerifyByCorpId(Long corpId) {
                return null;
            }

            @Override
            public String getOpenidByUserid(Long userId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapCorpIdAndCode(String corpIdListJson) {
                return null;
            }

            @Override
            public Result findCorpBankAccount(Long corpId) {
                return null;
            }

            @Override
            public Result<Map<Long, CorpBankAccountDto>> mapCorpIdAndBankAccount(String corpIdListJson) {
                return null;
            }

            @Override
            public Result feignRegisterCorp(String jsonString) {
                return null;
            }

        };
    }
}
