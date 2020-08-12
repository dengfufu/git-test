package com.zjft.usp.anyfix.service.fallback;

import com.zjft.usp.anyfix.service.AnyfixFeignService;
import com.zjft.usp.common.model.Result;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/10/17 11:27 上午
 **/
@Component
@Slf4j
public class AnyfixFeignServiceFallbackFactory implements FallbackFactory<AnyfixFeignService> {
    @Override
    public AnyfixFeignService create(Throwable cause) {
        return new AnyfixFeignService() {

            @Override
            public Result listAllCorpIdByCorpId(Long corpId) {
                return null;
            }

            @Override
            public Result findDeviceBranch(Long branchId) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapDeviceBranchByCustomIdList(List<Long> customIdList) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapIdAndBranchName(List<Long> branchIdList) {
                return null;
            }

            /**
             * 根据网点编号列表查询网点编号与网点名称
             *
             * @param branchIdListJson
             * @return
             * @author zgpi
             * @date 2020/6/3 20:02
             **/
            @Override
            public Result<Map<Long, String>> mapServiceBranchIdAndName(String branchIdListJson) {
                return null;
            }

            @Override
            public Result findServiceBranch(Long branchId) {
                return null;
            }

            /**
             * 获得人员所在服务网点编号列表
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/3/13 18:14
             */
            @Override
            public Result<List<Long>> listOwnBranchId(Long userId, Long corpId) {
                return null;
            }

            /**
             * 获得人员所在服务网点的下级网点编号列表
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/3/13 18:14
             */
            @Override
            public Result<List<Long>> listOwnLowerBranchId(Long userId, Long corpId) {
                return null;
            }

            /**
             * 获得人员所在服务网点及下级网点编号列表
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/3/13 18:14
             */
            @Override
            public Result<List<Long>> listOwnAndLowerBranchId(Long userId, Long corpId) {
                return null;
            }

            /**
             * 获得下级服务网点编号列表
             *
             * @param branchIdListJson
             * @return
             * @author zgpi
             * @date 2020/6/5 09:31
             **/
            @Override
            public Result<List<Long>> listLowerBranchId(String branchIdListJson) {
                return null;
            }

            /**
             * 人员编号与服务网点名称(带省份)映射
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/3/13 09:30
             */
            @Override
            public Result<Map<Long, String>> mapUserIdAndServiceBranchNames(String json) {
                return null;
            }

            @Override
            public Result<Map<String, Long>> batchAddDeviceBranch(String json) {
                return null;
            }

            /**
             * 根据条件获得服务网点人员列表
             *
             * @param json
             * @return
             * @author zgpi
             * @date 2020/3/13 10:27
             */
            @Override
            public Result<List<Long>> listServiceBranchUserIdByFilter(String json) {
                return null;
            }

            /**
             * 根据委托方和客户关系表主键list获取主键与客户名称的映射
             *
             * @param json
             * @return
             */
            @Override
            public Result<Map<Long, String>> mapIdAndCustomNameByIdList(String json) {
                return null;
            }

            @Override
            public Result findDemanderCustomDtoById(Long customId) {
                return null;
            }

            @Override
            public Result<List<Long>> listCustomIdsByCustomCorp(Long customCorp) {
                return null;
            }

            @Override
            public Result<Map<Long, String>> mapCustomIdAndNameByDemander(Long demanderCorp) {
                return null;
            }

            @Override
            public Result<List<Long>> listServiceCorpIdsByDemander(Long demanderCorp) {
                return null;
            }

            @Override
            public Result<List<Long>> listDemanderCorpId(Long corpId) {
                return null;
            }

            @Override
            public Result findWorkById(Long workId) {
                return null;
            }

            @Override
            public Result addCustomFieldDataList(String jsonObject) {
                return null;
            }

            @Override
            public Result queryCustomFieldData(String jsonObject) {
                return null;
            }

            @Override
            public Result deleteByDeviceId(Long deviceId) {
                return null;
            }

            /**
             * 删除网点人员
             *
             * @param userId
             * @param corpId
             * @return
             * @author zgpi
             * @date 2020/2/25 17:13
             */
            @Override
            public Result delBranchUser(Long userId, Long corpId, Long currentUserId, String clientId) {
                log.error("AnyfixFeign:delBranchUser:failed");
                return null;
            }

            @Override
            public Result<Map<Long, String>> getDemanderIdNameMap(Long corpId) {
                return null;
            }

            @Override
            public Result<String> getBranchesNameByUserId(Long userId) {
                return null;
            }

            @Override
            public Result<Map<String, Map<String, Long>>> batchSaveCustomCorp(List<String> customCropNameList, Long demanderCorp, Long userId) {
                return null;
            }

            /**
             * 获得客户经理的委托商编号列表
             *
             * @param corpId
             * @param manager
             * @return
             * @author zgpi
             * @date 2020/6/22 14:55
             **/
            @Override
            public Result<List<Long>> listDemanderCorpByManager(Long corpId, Long manager) {
                return null;
            }
        };
    }
}
