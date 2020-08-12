package com.zjft.usp.anyfix.work.ware.service;

import com.zjft.usp.anyfix.work.ware.WareFilter;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.model.WorkWareRecycle;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 工单回收物品表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WorkWareRecycleService extends IService<WorkWareRecycle> {

    /**
     * 添加回收物品列表
     *
     * @param wareDtoList
     * @param userId
     * @param workId
     * @return
     * @author zgpi
     * @date 2019/10/14 2:50 下午
     **/
    void addWorkRecycleList(List<WareDto> wareDtoList, Long userId, Long workId);

    /**
     * 根据工单编号查询
     * @param workId
     * @return
     */
    List<WareDto> listByWorkId(Long workId);

    /**
     * 添加
     *
     * @author canlei
     * @param wareDto
     * @param curUserId
     */
    void add(WareDto wareDto, Long curUserId);

    /**
     * 更新
     *
     * @author canlei
     * @param wareDto
     * @param curUserId
     */
    void update(WareDto wareDto, Long curUserId);

    /**
     * 获取物品分类名称列表
     *
     * @param wareFilter
     * @return
     */
    List<WareDto> listCatalog(WareFilter wareFilter);

    /**
     * 获取物品品牌名称列表
     *
     * @param wareFilter
     * @return
     */
    List<WareDto> listBrand(WareFilter wareFilter);


    /**
     * 获取物品型号名称列表
     *
     * @param wareFilter
     * @return
     */
    List<WareDto> listModel(WareFilter wareFilter);

    /**
     * 根据workId删除
     *
     * @param workId
     */
    void deleteByWorkId(Long workId);

}
