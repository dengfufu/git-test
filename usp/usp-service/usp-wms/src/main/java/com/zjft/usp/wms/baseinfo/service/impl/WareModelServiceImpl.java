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
import com.zjft.usp.wms.baseinfo.dto.WareModelDto;
import com.zjft.usp.wms.baseinfo.filter.WareModelFilter;
import com.zjft.usp.wms.baseinfo.mapper.WareModelMapper;
import com.zjft.usp.wms.baseinfo.model.WareModel;
import com.zjft.usp.wms.baseinfo.service.WareBrandService;
import com.zjft.usp.wms.baseinfo.service.WareCatalogService;
import com.zjft.usp.wms.baseinfo.service.WareModelImageService;
import com.zjft.usp.wms.baseinfo.service.WareModelService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 型号表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareModelServiceImpl extends ServiceImpl<WareModelMapper, WareModel> implements WareModelService {

    @Autowired
    WareModelImageService wareModelImageService;
    @Autowired
    WareBrandService wareBrandService;
    @Autowired
    WareCatalogService wareCatalogService;

    /***
     * 备件型号编号与名称映射
     *
     * @author Qiugm
     * @date 2019-11-15
     * @param corpId
     * @return java.util.Map<java.lang.Long, java.lang.String>
     */
    @Override
    public Map<Long, String> mapModelIdAndName(Long corpId) {
        Map<Long, String> map = new HashMap<>(256);
        QueryWrapper<WareModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        List<WareModel> wareModelList = this.list(queryWrapper);
        if (wareModelList != null && wareModelList.size() > 0) {
            for (WareModel wareModel : wareModelList) {
                map.put(wareModel.getId(), wareModel.getName());
            }
        }
        return map;
    }

    /**
     * 物料型号列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareModelDto>
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     */
    @Override
    public List<WareModelDto> listWareModel(WareModelFilter wareModelFilter) {
        List<WareModelDto> dtoList = new ArrayList<>();
        if (LongUtil.isZero(wareModelFilter.getCorpId())) {
            return dtoList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareModelFilter.getName())){
            wrapper.like("name", wareModelFilter.getName());
        }
        if(LongUtil.isNotZero(wareModelFilter.getCatalogId())) {
            wrapper.eq("catalog_id", wareModelFilter.getCatalogId());
        }
        if(LongUtil.isNotZero(wareModelFilter.getBrandId())) {
            wrapper.eq("brand_id", wareModelFilter.getBrandId());
        }
        if(StrUtil.isNotBlank(wareModelFilter.getEnabled())) {
            wrapper.eq("enabled", wareModelFilter.getEnabled());
        }
        wrapper.eq("corp_id", wareModelFilter.getCorpId());
        wrapper.orderByAsc("sort_no");
        List<WareModel> list = this.list(wrapper);
        if (CollectionUtil.isNotEmpty(list)) {
            Map<Long, String> brandMap = wareBrandService.mapBrandIdAndName(wareModelFilter.getCorpId());
            Map<Long, String> catalogMap = wareCatalogService.mapCatalogIdAndName(wareModelFilter.getCorpId());
            list.forEach(wareModel -> {
                WareModelDto wareModelDto = new WareModelDto();
                BeanUtils.copyProperties(wareModel, wareModelDto);
                wareModelDto.setBrandName(brandMap.get(wareModel.getBrandId()));
                wareModelDto.setCatalogName(catalogMap.get(wareModel.getCatalogId()));
                dtoList.add(wareModelDto);
            });
        }
        return dtoList;
    }

    /**
     * 根据条件查找物料型号
     *
     * @param wareModelFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareModelDto>
     * @datetime 2019/11/20 17:30
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareModelDto> queryWareModel(WareModelFilter wareModelFilter) {
        ListWrapper<WareModelDto> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(wareModelFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareModelFilter.getName())){
            wrapper.like("name", wareModelFilter.getName());
        }
        if(LongUtil.isNotZero(wareModelFilter.getCatalogId())) {
            wrapper.eq("catalog_id", wareModelFilter.getCatalogId());
        }
        if(LongUtil.isNotZero(wareModelFilter.getBrandId())) {
            wrapper.eq("brand_id", wareModelFilter.getBrandId());
        }
        if(StrUtil.isNotBlank(wareModelFilter.getEnabled())) {
            wrapper.eq("enabled", wareModelFilter.getEnabled());
        }
        wrapper.eq("corp_id", wareModelFilter.getCorpId());
        wrapper.orderByAsc("sort_no");
        Page page = new Page(wareModelFilter.getPageNum(), wareModelFilter.getPageSize());
        IPage<WareModel> iPage = this.page(page, wrapper);
        if(iPage != null){
            if(iPage.getRecords() != null && iPage.getSize() > 0){
                List<WareModelDto> dtoList = new ArrayList();
                WareModelDto dto;
                Map<Long, String> brandMap = wareBrandService.mapBrandIdAndName(wareModelFilter.getCorpId());
                Map<Long, String> catalogMap = wareCatalogService.mapCatalogIdAndName(wareModelFilter.getCorpId());
                for(WareModel model : iPage.getRecords()){
                    dto = new WareModelDto();
                    BeanUtils.copyProperties(model, dto);
                    /*TODO 映射处理*/
                    dto.setBrandName(brandMap.get(model.getBrandId()));
                    dto.setCatalogName(catalogMap.get(model.getCatalogId()));
                    dtoList.add(dto);
                }
                wrapperList.setList(dtoList);
            }
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 根据ID查找物料型号信息
     *
     * @param wareModelId
     * @return com.zjft.usp.wms.baseinfo.dto.WareModelDto
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WareModelDto findWareModelBy(Long wareModelId) {
        WareModel model = this.getById(wareModelId);
        WareModelDto dto = new WareModelDto();
        BeanUtils.copyProperties(model, dto);
        /* TODO 映射 */
        dto.setWareModelImageList(wareModelImageService.listWareModelImage(wareModelId));
        return dto;
    }

    /**
     * 新增物料型号信息
     *
     * @param wareModelDto
     * @return void
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWareModel(WareModelDto wareModelDto) {
        Assert.notNull(wareModelDto, "wareModelDto 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareModelDto.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "型号名称【%s】已存在", wareModelDto.getName());
        if(LongUtil.isZero(wareModelDto.getId())){
            wareModelDto.setId(KeyUtil.getId());
        }
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareModelDto.getSortNo())){
            int sortNo = 0;
            Optional<WareModel> optional = this.list().stream().max(Comparator.comparing(WareModel::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareModelDto.setSortNo(sortNo + 10);
        }
        this.save(wareModelDto);
        if(CollectionUtil.isNotEmpty(wareModelDto.getWareModelImageList())){
            wareModelImageService.insertWareModelImages(wareModelDto.getWareModelImageList());
        }
    }

    /**
     * 更新物料型号信息
     *
     * @param wareModelDto
     * @return void
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWareModel(WareModelDto wareModelDto) {
        Assert.notNull(wareModelDto, "wareModelDto 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareModelDto.getName());
        WareModel isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareModelDto.getId()), "型号名称【%s】已存在", wareModelDto.getName());
        WareModel wareModel = new WareModel();
        BeanUtils.copyProperties(wareModelDto, wareModel);
        this.updateById(wareModel);
        // 更新策略 先删除 再添加
        wareModelImageService.deleteWareModelImages(wareModel.getId());
        if(CollectionUtil.isNotEmpty(wareModelDto.getWareModelImageList())){
            wareModelImageService.insertWareModelImages(wareModelDto.getWareModelImageList());
        }
    }

    /**
     * 删除物料型号信息
     *
     * @param wareModelId
     * @return void
     * @datetime 2019/11/19 11:28
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWareModel(Long wareModelId) {
        Assert.notNull(wareModelId, "wareModelId 不能为空");
        this.removeById(wareModelId);
        wareModelImageService.deleteWareModelImages(wareModelId);
    }

    /**
     * 获取id和model的映射
     * @param corpId
     * @return
     * @author canlei
     */
    @Override
    public Map<Long, WareModel> mapIdAndModel(Long corpId) {
        Map<Long, WareModel> map = new HashMap<>();
        if(LongUtil.isZero(corpId)) {
            return map;
        }
        QueryWrapper<WareModel> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        List<WareModel> wareModelList = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(wareModelList)) {
            for(WareModel wareModel: wareModelList) {
                map.put(wareModel.getId(), wareModel);
            }
        }
        return map;
    }
}
