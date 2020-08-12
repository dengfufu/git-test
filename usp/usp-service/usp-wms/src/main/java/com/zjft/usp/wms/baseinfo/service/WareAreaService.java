package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.annotation.CommonReqParam;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.dto.WareAreaDto;
import com.zjft.usp.wms.baseinfo.filter.WareAreaFilter;
import com.zjft.usp.wms.baseinfo.model.WareArea;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 供应商表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
public interface WareAreaService extends IService<WareArea> {

    /**
     * 区域信息列表
     * @datetime 2019/11/18 15:56
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareArea>
     */
    List<WareArea> listWareArea();

    /**
     * 根据条件查找区域信息
     * @datetime 2019/11/20 10:36
     * @version
     * @author dcyu
     * @param wareAreaFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareAreaDto>
     */
    ListWrapper<WareAreaDto> queryWareArea(WareAreaFilter wareAreaFilter);


    /**
     * 根据ID查找区域信息
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     * @param id
     * @return com.zjft.usp.wms.baseinfo.model.WareArea
     */
    WareArea findWareAreaBy(Long id);

    /**
     * 新增区域信息
     * @datetime 2019/11/18 16:28
     * @version
     * @author dcyu
     * @param wareAreaDto
     * @return void
     */
    void insertWareArea(WareAreaDto wareAreaDto);

    /**
     * 更新区域信息
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     * @param wareAreaDto
     * @return void
     */
    void updateWareArea(WareAreaDto wareAreaDto);

    /**
     * 删除区域信息
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     * @param id
     * @return void
     */
    void deleteWareArea(Long id);
}
