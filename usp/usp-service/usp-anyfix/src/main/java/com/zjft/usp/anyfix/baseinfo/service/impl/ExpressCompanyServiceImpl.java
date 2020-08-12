package com.zjft.usp.anyfix.baseinfo.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.anyfix.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.anyfix.baseinfo.model.ExpressCompany;
import com.zjft.usp.anyfix.baseinfo.mapper.ExpressCompanyMapper;
import com.zjft.usp.anyfix.baseinfo.service.ExpressCompanyService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 快递公司 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ExpressCompanyServiceImpl extends ServiceImpl<ExpressCompanyMapper, ExpressCompany> implements ExpressCompanyService {

    /**
     * 模糊查询快递公司
     *
     * @param expressCompanyFilter
     * @return
     * @author zgpi
     * @date 2020/4/20 10:44
     **/
    @Override
    public List<ExpressCompany> matchExpressCorp(ExpressCompanyFilter expressCompanyFilter) {
        if (LongUtil.isZero(expressCompanyFilter.getCorpId())) {
            return new ArrayList<>();
        }
        return this.baseMapper.matchExpressCorp(expressCompanyFilter.getCorpId(),
                StrUtil.trimToEmpty(expressCompanyFilter.getName()));
    }

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
    @Override
    public void addExpressCorpByPost(Long corpId, String name, Long curUserId) {
        ExpressCompany dbCompany = this.getOne(new QueryWrapper<ExpressCompany>()
                .eq("corp_id", corpId)
                .eq("name", StrUtil.trimToEmpty(name)));
        if (dbCompany == null) {
            ExpressCompany expressCompany = new ExpressCompany();
            expressCompany.setId(KeyUtil.getId());
            expressCompany.setCorpId(corpId);
            expressCompany.setName(StrUtil.trimToEmpty(name));
            expressCompany.setCreator(curUserId);
            expressCompany.setCreateTime(DateUtil.date());
            this.save(expressCompany);
        }
    }
}
