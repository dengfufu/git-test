package com.zjft.usp.anyfix.work.assign.service;

import com.zjft.usp.anyfix.work.assign.model.WorkAssignEngineer;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 派单工程师表 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-23
 */
public interface WorkAssignEngineerService extends IService<WorkAssignEngineer> {

   Integer add(WorkAssignEngineer workAssignEngineer);

   Integer add(Long assignId,Long engineerId);

   void delete(WorkAssignEngineer workAssignEngineer);

   void delByAssignId(Long assignId);

   /**
    * 根据工单ID获得派单工程师编号列表
    *
    * @param workId
    * @return
    * @author zgpi
    * @date 2020/2/17 16:58
    */
   List<Long> listEngineerByWorkId(Long workId);

}
