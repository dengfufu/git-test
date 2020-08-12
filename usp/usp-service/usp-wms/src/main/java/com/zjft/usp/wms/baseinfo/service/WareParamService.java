package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareParamFilter;
import com.zjft.usp.wms.baseinfo.model.WareParam;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 系统参数表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareParamService extends IService<WareParam> {

    /**
     * 系统参数列表
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     * @param
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareParam>
     */
    List<WareParam> listWareParam();

    /**
     * 根据条件查找系统参数
     * @datetime 2019/11/21 16:44
     * @version
     * @author dcyu
     * @param wareParamFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareParam>
     */
    ListWrapper<WareParam> queryWareParam(WareParamFilter wareParamFilter);

    /**
     * 查找系统参数
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     * @param wareParamId
     * @return com.zjft.usp.wms.baseinfo.model.WareParam
     */
    WareParam findWareParamBy(Long wareParamId);

    /**
     * 新增系统参数
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     * @param wareParam
     * @return void
     */
    void insertWareParam(WareParam wareParam);

    /**
     * 更新系统参数
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     * @param wareParam
     * @return void
     */
    void updateWareParam(WareParam wareParam);

    /**
     * 删除系统参数
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     * @param wareParamId
     * @return void
     */
    void deleteWareParam(Long wareParamId);
}
