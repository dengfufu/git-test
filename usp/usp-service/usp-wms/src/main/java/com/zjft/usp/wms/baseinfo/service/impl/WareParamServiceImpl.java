package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareParamFilter;
import com.zjft.usp.wms.baseinfo.model.WareParam;
import com.zjft.usp.wms.baseinfo.mapper.WareParamMapper;
import com.zjft.usp.wms.baseinfo.service.WareParamService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 系统参数表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareParamServiceImpl extends ServiceImpl<WareParamMapper, WareParam> implements WareParamService {
    /**
     * 系统参数列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareParam>
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     */
    @Override
    public List<WareParam> listWareParam() {
        return this.list();
    }

    /**
     * 根据条件查找系统参数
     *
     * @param wareParamFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WareParam>
     * @datetime 2019/11/21 16:44
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareParam> queryWareParam(WareParamFilter wareParamFilter) {
        ListWrapper<WareParam> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(wareParamFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareParamFilter.getParamCode())){
            wrapper.like("param_code", wareParamFilter.getParamCode());
        }
        if(StrUtil.isNotBlank(wareParamFilter.getParamName())){
            wrapper.like("param_name", wareParamFilter.getParamName());
        }
        wrapper.eq("corp_id", wareParamFilter.getCorpId());
        Page page = new Page(wareParamFilter.getPageNum(), wareParamFilter.getPageSize());
        IPage<WareParam> iPage = this.page(page, wrapper);
        if(iPage != null){
            wrapperList.setList(iPage.getRecords());
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 查找系统参数
     *
     * @param wareParamId
     * @return com.zjft.usp.wms.baseinfo.model.WareParam
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     */
    @Override
    public WareParam findWareParamBy(Long wareParamId) {
        Assert.notNull(wareParamId, "wareParamId 不能为空");
        return this.getById(wareParamId);
    }

    /**
     * 新增系统参数
     *
     * @param wareParam
     * @return void
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareParam(WareParam wareParam) {
        Assert.notNull(wareParam, "wareParam 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("param_name", wareParam.getParamName());
        Assert.isFalse(this.getOne(wrapper) != null, "参数名称【%s】已存在", wareParam.getParamName());
        wareParam.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareParam.getSortNo())){
            int sortNo = 0;
            Optional<WareParam> optional = this.list().stream().max(Comparator.comparing(WareParam::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareParam.setSortNo(sortNo + 10);
        }
        this.save(wareParam);
    }

    /**
     * 更新系统参数
     *
     * @param wareParam
     * @return void
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     */
    @Override
    public void updateWareParam(WareParam wareParam) {
        Assert.notNull(wareParam, "wareParam 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareParam.getParamName());
        WareParam isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareParam.getId()), "参数名称【%s】已存在", wareParam.getParamName());
        this.updateById(wareParam);
    }

    /**
     * 删除系统参数
     *
     * @param wareParamId
     * @return void
     * @datetime 2019/11/19 14:08
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareParam(Long wareParamId) {
        Assert.notNull(wareParamId, "wareParamId 不能为空");
        this.removeById(wareParamId);
    }
}
