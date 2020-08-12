package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.baseinfo.dto.CustomListMainDto;
import com.zjft.usp.wms.baseinfo.filter.CustomListMainFilter;
import com.zjft.usp.wms.baseinfo.model.CustomListMain;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 自定义列表主表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface CustomListMainService extends IService<CustomListMain> {

    /**
     * 查找已启用的自定义列表（需要按sort_no排序）
     *
     * @param corpId
     * @return
     */
    List<CustomListMain> listEnabledBy(Long corpId);

//    ListWrapper<CustomListMain> queryCustomList();

    /**
     * 分页查询自定义列表（需要按sort_no排序）
     *
     * @param  customListMainFilter
     * @return
     */
     ListWrapper<CustomListMainDto> pageBy(CustomListMainFilter customListMainFilter);

    /**
     * 查找常用列表
     * @datetime 2019-12-02 12:44
     * @version
     * @author dcyu
     * @param customListMainId
     * @return com.zjft.usp.wms.baseinfo.dto.CustomListDto
     */
    CustomListMainDto findCustomListBy(Long customListMainId);

    /**
     * 新增常用列表
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     * @param customListDto
     * @return void
     */
    void insertCustomList(CustomListMainDto customListDto, UserInfo userInfo);

    /**
     * 更新常用列表
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     * @param customListDto
     * @return void
     */
    void updateCustomList(CustomListMainDto customListDto);

    /**
     * 删除常用列表
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     * @param customListMainId
     * @return void
     */
    void deleteCustomList(Long customListMainId);

    /**
     * 修改自定义列表基础数据
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     * @param customListDto
     * @return void
     */
    void updateCustomListMain(CustomListMainDto customListDto);
}
