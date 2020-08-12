package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.WareBrandDto;
import com.zjft.usp.wms.baseinfo.filter.WareBrandFilter;
import com.zjft.usp.wms.baseinfo.model.WareBrand;
import com.zjft.usp.wms.baseinfo.mapper.WareBrandMapper;
import com.zjft.usp.wms.baseinfo.service.WareBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * <p>
 * 物料品牌表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareBrandServiceImpl extends ServiceImpl<WareBrandMapper, WareBrand> implements WareBrandService {
    /**
     * 物料品牌名称映射
     *
     * @param corpId
     * @return java.util.Map
     * @datetime 2019/11/22 10:37
     * @version
     * @author dcyu
     */
    @Override
    public Map mapBrandIdAndName(Long corpId) {
        Map<Long, String> map = new LinkedHashMap<>();
        QueryWrapper wrapper = new QueryWrapper<>().eq("corp_id", corpId).eq("enabled", "Y");
        List<WareBrand> brandList = this.list(wrapper);
        if(brandList != null && brandList.size() > 0){
            brandList.forEach(wareBrand -> {
                map.put(wareBrand.getId(), wareBrand.getName());
            });
        }
        return map;
    }

    /**
     * 分页查询
     * @param wareBrandFilter
     * @return
     */
    @Override
    public ListWrapper<WareBrandDto> queryWareBrand(WareBrandFilter wareBrandFilter) {
        ListWrapper<WareBrandDto> listWrapper = new ListWrapper<>();
        if(LongUtil.isZero(wareBrandFilter.getCorpId())){
            return listWrapper;
        }
        QueryWrapper<WareBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", wareBrandFilter.getCorpId());
        if(StrUtil.isNotBlank(wareBrandFilter.getName())){
            queryWrapper.like("name", wareBrandFilter.getName());
        }
        if(StrUtil.isNotBlank(wareBrandFilter.getEnabled())){
            queryWrapper.eq("enabled", wareBrandFilter.getEnabled());
        }
        Page page = new Page(wareBrandFilter.getPageNum(), wareBrandFilter.getPageSize());
        IPage<WareBrand> wareBrandIPage = this.page(page, queryWrapper);
        if(wareBrandIPage != null && wareBrandIPage.getRecords() != null && wareBrandIPage.getRecords().size() > 0){
            List<WareBrandDto> wareBrandIPageDtoList = new ArrayList<>();
            for(WareBrand deviceBrand : wareBrandIPage.getRecords()){
                /** Mapper转换 */
                WareBrandDto tmpDto = new WareBrandDto();
                BeanUtils.copyProperties(deviceBrand, tmpDto);
                wareBrandIPageDtoList.add(tmpDto);
            }
            listWrapper.setList(wareBrandIPageDtoList);
        }
        listWrapper.setTotal(wareBrandIPage.getTotal());
        return listWrapper;
    }

    /**
     * 物料品牌信息列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareBrand>
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     */
    @Override
    public List<WareBrand> listWareBrand(Long corpId) {
        QueryWrapper<WareBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        return this.list(queryWrapper);
    }

    /**
     * 根据ID查找物料品牌
     *
     * @param id
     * @return com.zjft.usp.wms.baseinfo.model.WareBrand
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     */
    @Override
    public WareBrand findWareBrandBy(Long id) {
        WareBrand wareBrand = new WareBrand();
        return wareBrand.selectById(id);
    }

    /**
     * 新增物料品牌信息
     *
     * @param wareBrandDto
     * @return void
     * @datetime 2019/11/18 16:33
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareBrand(WareBrand wareBrandDto) {
        Assert.notNull(wareBrandDto, "wareBrandDto 不能为Null");
        // 检查新增品牌名称是否已存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareBrandDto.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "品牌名称【%s】已存在！", wareBrandDto.getName());
        WareBrand wareBrand = new WareBrand();
        BeanUtils.copyProperties(wareBrandDto, wareBrand);
        wareBrand.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareBrandDto.getSortNo())){
            int sortNo = 0;
            Optional<WareBrand> optional = this.list().stream().max(Comparator.comparing(WareBrand::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareBrand.setSortNo(sortNo + 10);
        }
        this.save(wareBrand);
    }

    /**
     * 更新物料品牌信息
     *
     * @param wareBrandDto
     * @return void
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     */
    @Override
    public void updateWareBrand(WareBrand wareBrandDto) {
        Assert.notNull(wareBrandDto.getId(), "wareBrandId 不能为 NULL");
        // 检查新增品牌名称是否已存在
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareBrandDto.getName());
        WareBrand isExist = this.getOne(wrapper);
        Assert.isFalse(((isExist != null) && !(isExist.getId().equals(wareBrandDto.getId()))), "品牌名称【%s】已存在！", wareBrandDto.getName());
        WareBrand wareBrand = new WareBrand();
        BeanUtils.copyProperties(wareBrandDto, wareBrand);
        wareBrand.updateById();
    }

    /**
     * 删除物料品牌信息
     *
     * @param id
     * @return void
     * @datetime 2019/11/18 16:25
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareBrand(Long id) {
        Assert.notNull(id, "wareBrandId 不能为 NULL");
        this.removeById(id);
    }

    /**
     * 模糊匹配
     *
     * @param wareBrandFilter
     * @return
     */
    @Override
    public List<WareBrand> match(WareBrandFilter wareBrandFilter) {
        List<WareBrand> list = new ArrayList<>();
        if(wareBrandFilter == null || LongUtil.isZero(wareBrandFilter.getCorpId())) {
            return list;
        }
        QueryWrapper<WareBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", wareBrandFilter.getCorpId());
        if(StrUtil.isNotBlank(wareBrandFilter.getName())) {
            queryWrapper.like("name", wareBrandFilter.getName());
        }
        queryWrapper.eq("enabled", "Y");
        queryWrapper.orderByAsc("sort_no");
        queryWrapper.last("limit 100");
        list = this.list(queryWrapper);
        return list;
    }
}
