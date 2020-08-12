package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.wms.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.wms.baseinfo.model.ExpressCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Map;

/**
 * <p>
 * 快递公司表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface ExpressCompanyService extends IService<ExpressCompany> {

    /**
     * 根据条件查找快递公司
     * @datetime 2019/11/21 19:29
     * @version
     * @author dcyu
     * @param expressCompanyFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.ExpressCompany>
     */
    ListWrapper<ExpressCompany> queryExpressCompany(ExpressCompanyFilter expressCompanyFilter);

    /**
     * 新增快递公司信息
     * @datetime 2019/11/25 19:46
     * @version
     * @author dcyu
     * @param expressCompany
     * @return void
     */
    void insertExpressCompany(ExpressCompany expressCompany);

    /**
     * 更新快递公司信息
     * @datetime 2019/11/25 19:46
     * @version
     * @author dcyu
     * @param expressCompany
     * @return void
     */
    void updateExpressCompany(ExpressCompany expressCompany);

    /**
     * 删除快递公司信息
     * @datetime 2019/11/25 19:45
     * @version
     * @author dcyu
     * @param companyId
     * @return void
     */
    void deleteExpressCompany(Long companyId);

    /**
     * 获取id和实体的映射
     *
     * @param corpId
     * @return java.util.Map<java.lang.Long,com.zjft.usp.wms.baseinfo.model.ExpressCompany>
     * @author zphu
     * @date 2019/12/13 18:04
     * @throws
    **/
    Map<Long,ExpressCompany> mapIdAndObject(Long corpId);
}
