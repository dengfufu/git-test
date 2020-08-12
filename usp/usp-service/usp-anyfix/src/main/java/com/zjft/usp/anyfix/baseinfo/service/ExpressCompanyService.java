package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.anyfix.baseinfo.model.ExpressCompany;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 * <p>
 * 快递公司 服务类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface ExpressCompanyService extends IService<ExpressCompany> {

    /**
     * 模糊查询快递公司
     *
     * @param expressCompanyFilter
     * @return
     * @author zgpi
     * @date 2020/4/20 10:44
     **/
    List<ExpressCompany> matchExpressCorp(ExpressCompanyFilter expressCompanyFilter);

    /**
     * 物品寄送单添加快递公司
     *
     * @param corpId
     * @param name
     * @param curUserId
     * @return
     * @author zgpi
     * @date 2020/4/20 17:14
     **/
    void addExpressCorpByPost(Long corpId, String name, Long curUserId);
}
