package com.zjft.usp.anyfix.baseinfo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.baseinfo.model.WorkCondition;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author ljzhu
 * @Package com.zjft.anyfix.work.mapper
 * @date 2019-09-26 17:49
 * @note 条件配置表 work_condition
 */
public interface WorkConditionMapper extends BaseMapper<WorkCondition> {

    List<WorkCondition> listWorkCondition(@Param("customCorp") Long customCorp,
                                          @Param("workType") String workType,
                                          @Param("largeClassId") Long largeClassId,
                                          @Param("smallClassId") String smallClassId,
                                          @Param("brandId") String brandId,
                                          @Param("modelId") String modelId,
                                          @Param("district") String district,
                                          @Param("deviceBranch") String deviceBranch,
                                          @Param("deviceId") Long deviceId);
}
