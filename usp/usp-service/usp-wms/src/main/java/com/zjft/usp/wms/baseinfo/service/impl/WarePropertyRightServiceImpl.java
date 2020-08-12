package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
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
import com.zjft.usp.wms.baseinfo.filter.WarePropertyRightFilter;
import com.zjft.usp.wms.baseinfo.mapper.WarePropertyRightMapper;
import com.zjft.usp.wms.baseinfo.model.WarePropertyRight;
import com.zjft.usp.wms.baseinfo.service.WarePropertyRightService;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 产权表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WarePropertyRightServiceImpl extends ServiceImpl<WarePropertyRightMapper, WarePropertyRight> implements WarePropertyRightService {

    /***
     * 根据corpId获取产权编号与名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Integer, java.lang.String>
     */
    @Override
    public Map<Long, String> mapIdAndName(Long corpId) {
        Map<Long, String> mapIdAndName = new HashMap<>(16);
        List<WarePropertyRight> warePropertyRightList = this.list(new QueryWrapper<WarePropertyRight>().eq("corp_id",
                corpId));
        if (CollectionUtil.isNotEmpty(warePropertyRightList)) {
            for (WarePropertyRight warePropertyRight : warePropertyRightList) {
                mapIdAndName.put(warePropertyRight.getId(), warePropertyRight.getName());
            }
        }
        return mapIdAndName;
    }

    /**
     * 物料产权信息列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WarePropertyRight>
     * @datetime 2019/11/26 10:09
     * @version
     * @author dcyu
     */
    @Override
    public List<WarePropertyRight> listWarePropertyRight() {
        return null;
    }

    /**
     * 根据条件查找物料产权信息
     *
     * @param warePropertyRightFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.model.WarePropertyRight>
     * @datetime 2019/11/26 10:09
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WarePropertyRight> queryWarePropertyRight(WarePropertyRightFilter warePropertyRightFilter) {
        Assert.notNull(warePropertyRightFilter, "warePropertyRightFilter 不能为 Null");
        ListWrapper<WarePropertyRight> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(warePropertyRightFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(warePropertyRightFilter.getName())){
            wrapper.like("name", warePropertyRightFilter.getName());
        }
        wrapper.eq("corp_id", warePropertyRightFilter.getCorpId());
        Page page = new Page(warePropertyRightFilter.getPageNum(), warePropertyRightFilter.getPageSize());
        IPage<WarePropertyRight> iPage = this.page(page, wrapper);
        if(iPage != null){
            wrapperList.setList(iPage.getRecords());
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 新增物料产权信息
     *
     * @param propertyRight
     * @return void
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     */
    @Override
    public void insertWarePropertyRight(WarePropertyRight propertyRight) {
        Assert.notNull(propertyRight, "warePropertyRight 不能为空");
        // 检查相同名称 物料产权 是否已存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", propertyRight.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "物料产权名称【%s】已存在", propertyRight.getName());
        propertyRight.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(propertyRight.getSortNo())){
            int sortNo = 0;
            Optional<WarePropertyRight> optional = this.list().stream().max(Comparator.comparing(WarePropertyRight::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            propertyRight.setSortNo(sortNo + 10);
        }
        this.save(propertyRight);
    }

    /**
     * 更新物料产权信息
     *
     * @param propertyRight
     * @return void
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     */
    @Override
    public void updateWarePropertyRight(WarePropertyRight propertyRight) {
        Assert.notNull(propertyRight, "warePropertyRight 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", propertyRight.getName());
        WarePropertyRight isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(propertyRight.getId()), "物料产权名称【%w】已存在", propertyRight.getName());
        this.updateById(propertyRight);
    }

    /**
     * 删除物料产权信息
     *
     * @param rightId
     * @return void
     * @datetime 2019/11/26 8:58
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWarePropertyRight(Long rightId) {
        Assert.notNull(rightId, "warePropertyRightId 不能为空");
        this.removeById(rightId);
    }
}
