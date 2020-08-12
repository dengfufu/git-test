package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.dto.CustomListDetailDto;
import com.zjft.usp.wms.baseinfo.model.CustomListDetail;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义列表子表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface CustomListDetailService extends IService<CustomListDetail> {

    /**
     * 常用列表明细列表
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     * @param customListMainId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.CustomListDetail>
     */
    List<CustomListDetail> listCustomListDetail(Long customListMainId);

    /**
     * 新增常用列表明细
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     * @param customListDetail
     * @param userId
     * @return void
     */
    void insertCustomListDetail(CustomListDetail customListDetail, Long userId);

    /**
     * 新增常用列表明细
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     * @param customListDetailList
     * @param userId
     * @return void
     */
    void insertCustomListDetail(List<CustomListDetail> customListDetailList, Long listId, Long userId);

    /**
     * 删除常用列表明细
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     * @param customListMainId
     * @return void
     */
    void deleteCustomListDetail(Long customListMainId);

    /**
     * 修改常用列表
     */
    void updateCustomListDetail(CustomListDetailDto customListDetailDto);
}
