package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.filter.ExpressCompanyFilter;
import com.zjft.usp.wms.baseinfo.mapper.ExpressCompanyMapper;
import com.zjft.usp.wms.baseinfo.model.ExpressCompany;
import com.zjft.usp.wms.baseinfo.service.ExpressCompanyService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 快递公司表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class ExpressCompanyServiceImpl extends ServiceImpl<ExpressCompanyMapper, ExpressCompany> implements ExpressCompanyService {
    /**
     * 根据条件查找快递公司
     *
     * @param expressCompanyFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.ExpressCompany>
     * @datetime 2019/11/21 19:29
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<ExpressCompany> queryExpressCompany(ExpressCompanyFilter expressCompanyFilter) {
        ListWrapper<ExpressCompany> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(expressCompanyFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(expressCompanyFilter.getName())){
            wrapper.like("name", expressCompanyFilter.getName());
        }
        wrapper.eq("corp_id", expressCompanyFilter.getCorpId());
        Page page = new Page(expressCompanyFilter.getPageNum(), expressCompanyFilter.getPageSize());
        IPage iPage = this.page(page, wrapper);
        if(iPage != null){
            wrapperList.setList(iPage.getRecords());
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 新增快递公司信息
     *
     * @param expressCompany
     * @return void
     * @datetime 2019/11/25 19:46
     * @version
     * @author dcyu
     */
    @Override
    public void insertExpressCompany(ExpressCompany expressCompany) {
        Assert.notNull(expressCompany, "expressCompany 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", expressCompany.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "快递公司名称【%s】已存在", expressCompany.getName());
        expressCompany.setId(KeyUtil.getId());
        // 插入顺序号
        if(IntUtil.isZero(expressCompany.getSortNo())){
            int sortNo = 0;
            Optional<ExpressCompany> optional = this.list().stream().max(Comparator.comparing(ExpressCompany::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            expressCompany.setSortNo(sortNo + 10);
        }
        this.save(expressCompany);
    }

    /**
     * 更新快递公司信息
     *
     * @param expressCompany
     * @return void
     * @datetime 2019/11/25 19:46
     * @version
     * @author dcyu
     */
    @Override
    public void updateExpressCompany(ExpressCompany expressCompany) {
        Assert.notNull(expressCompany, "expressCompany 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", expressCompany.getName());
        ExpressCompany isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(expressCompany.getId()), "快递公司名称【%s】已存在", expressCompany.getName());
        this.updateById(expressCompany);
    }

    /**
     * 删除快递公司信息
     *
     * @param companyId
     * @return void
     * @datetime 2019/11/25 19:45
     * @version
     * @author dcyu
     */
    @Override
    public void deleteExpressCompany(Long companyId) {
        Assert.notNull(companyId, "expressCompanyId 不能为空");
        this.removeById(companyId);
    }

    @Override
    public Map<Long, ExpressCompany> mapIdAndObject(Long corpId) {
        Map<Long, ExpressCompany> map = new HashMap<>();
        if (LongUtil.isZero(corpId)) {
            return map;
        }
        QueryWrapper<ExpressCompany> queryWrapper = new QueryWrapper<ExpressCompany>().eq("corp_id", corpId);
        List<ExpressCompany> list = this.list(queryWrapper);
        list.forEach(expressCompany -> {
            map.put(expressCompany.getId(), expressCompany);
        });
        return map;
    }
}
