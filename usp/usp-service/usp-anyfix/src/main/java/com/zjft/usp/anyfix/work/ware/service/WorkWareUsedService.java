package com.zjft.usp.anyfix.work.ware.service;

import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.model.WorkWareUsed;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工单使用物品表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WorkWareUsedService extends IService<WorkWareUsed> {

    /**
     * 添加使用物品列表
     *
     * @param wareDtoList
     * @param userId
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/14 2:50 下午
     **/
    void addWorkUsedList(List<WareDto> wareDtoList, Long userId, Long workId);

    /**
     * 根据工单编号查询
     * @param workId
     * @return
     */
    List<WareDto> listByWorkId(Long workId);

    /**
     * 更新
     *
     * @param wareDto
     */
    void update(WareDto wareDto, Long curUserId);

    /**
     * 插入
     *
     * @author canlei
     * @param wareDto
     * @param curUserId
     */
    void add(WareDto wareDto, Long curUserId);

    /**
     * 删除
     *
     * @author canlei
     * @param usedId
     */
    void delete(Long usedId);

    /**
     * 根据workId删除
     *
     * @param workId
     */
    void deleteByWorkId(Long workId);

}
