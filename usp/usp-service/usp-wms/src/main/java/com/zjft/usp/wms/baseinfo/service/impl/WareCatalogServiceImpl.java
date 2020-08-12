package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.WareCatalogDto;
import com.zjft.usp.wms.baseinfo.filter.WareCatalogFilter;
import com.zjft.usp.wms.baseinfo.model.WareCatalog;
import com.zjft.usp.wms.baseinfo.mapper.WareCatalogMapper;
import com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs;
import com.zjft.usp.wms.baseinfo.service.WareCatalogService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.baseinfo.service.WareCatalogSpecsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 物料分类表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareCatalogServiceImpl extends ServiceImpl<WareCatalogMapper, WareCatalog> implements WareCatalogService {

    @Autowired
    WareCatalogSpecsService wareCatalogSpecsService;

    /**
     * 分类名称映射
     *
     * @param corpId
     * @return java.util.Map
     * @datetime 2019/11/22 10:52
     * @version
     * @author dcyu
     */
    @Override
    public Map mapCatalogIdAndName(Long corpId) {
        Map<Long, String> map = new LinkedHashMap<>();
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("corp_id", corpId);
        wrapper.eq("enabled", "Y");
        List<WareCatalog> catalogList = this.list(wrapper);
        if(catalogList != null && catalogList.size() > 0){
            catalogList.forEach(wareCatalog -> {
                map.put(wareCatalog.getId(), wareCatalog.getName());
            });
        }
        return map;
    }

    /**
     * 物料分类信息列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     * @datetime 2019/11/18 17:02
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WareCatalogDto> listWareCatalog() {
        List<WareCatalog> wareCatalogList = this.list();
        List<WareCatalogDto> wareCatalogDtoList = JSONUtil.parseArray(wareCatalogList).toList(WareCatalogDto.class);
        wareCatalogDtoList.forEach((dto) -> {
            dto.setWareCatalogSpecsList(wareCatalogSpecsService.listWareCatalogSpecs(dto.getId()));
        });
        return wareCatalogDtoList;
    }

    /**
     * 物料分类信息列表（已中文映射）
     *
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     * @datetime 2019/11/27 10:51
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<WareCatalogDto> listWareCatalog(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为空");
        QueryWrapper<WareCatalog> wrapper = new QueryWrapper<>();
        wrapper.eq("enabled", "Y");
        wrapper.eq("corp_id", corpId);
        List<WareCatalog> wareCatalogList = this.list(wrapper);
        List<WareCatalogDto> wareCatalogDtoList = JSONUtil.parseArray(wareCatalogList).toList(WareCatalogDto.class);
        Map<Long, String> mapWareCatalogIdAndName = this.mapCatalogIdAndName(corpId);
        wareCatalogDtoList.forEach((dto) -> {
            dto.setWareCatalogSpecsList(wareCatalogSpecsService.listWareCatalogSpecs(dto.getId()));
            // 父分类名
            String parentName = "";
            if(mapWareCatalogIdAndName != null){
                parentName = mapWareCatalogIdAndName.get(dto.getParentId());
            }
            dto.setParentName(parentName);
        });
        return wareCatalogDtoList;
    }

    /**
     * 物料分类信息结构型列表
     *
     * @param corpId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     * @datetime 2019/11/27 16:07
     * @version
     * @author dcyu
     */
    @Override
    public List<WareCatalogDto> treeWareCatalog(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为空");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("enabled", "Y");
        wrapper.eq("corp_id", corpId);
        // TODO 需要按照存储顺序获取结果  不然会有问题
        List<WareCatalog> wareCatalogList = this.list(wrapper);
        List<WareCatalogDto> wareCatalogDtoList = new ArrayList<>();
        Map<Long, String> mapWareCatalogIdAndName = this.mapCatalogIdAndName(corpId);
        if(CollectionUtil.isNotEmpty(wareCatalogList)){
            wareCatalogList.forEach((wareCatalog) -> {
                WareCatalogDto catalogDto = new WareCatalogDto();
                BeanUtils.copyProperties(wareCatalog, catalogDto);
                // 规格列表
                List<WareCatalogSpecs> specsList = wareCatalogSpecsService.listWareCatalogSpecs(wareCatalog.getId());
                catalogDto.setWareCatalogSpecsList(specsList);
                String specsAttributeNames = "";
                if(CollectionUtil.isNotEmpty(specsList)){
                    for(WareCatalogSpecs spec : specsList){
                        specsAttributeNames += spec.getAttribute() + ": " + spec.getValue() + "</br>";
                    }
                }
                catalogDto.setSpecsNames(specsAttributeNames);
                // 父分类名
                String parentName = "";
                if(mapWareCatalogIdAndName != null){
                    parentName = mapWareCatalogIdAndName.get(wareCatalog.getParentId());
                }
                catalogDto.setParentName(parentName);
                // 按照结构顺序插入返回列表
                this.setWareCatalogDtoTree(catalogDto, wareCatalogDtoList);
            });
        }
        return wareCatalogDtoList;
    }

    /**
     * 对象插入到树状列表对应的位置
     * @datetime 2019/11/27 17:33
     * @version
     * @author dcyu
     * @param wareCatalogDto
     * @param wareCatalogDtoList
     * @return void
     */
    private void setWareCatalogDtoTree(WareCatalogDto wareCatalogDto, List<WareCatalogDto> wareCatalogDtoList){
        // 存在父分类
        if(LongUtil.isNotZero(wareCatalogDto.getParentId())){
            if(wareCatalogDtoList.size() > 0){
                wareCatalogDtoList.forEach(dto -> {
                    // 找到对应父分类
                    List<WareCatalogDto> childrenList = dto.getChildren();
                    if(dto.getId().equals(wareCatalogDto.getParentId())){
                        if(CollectionUtil.isEmpty(childrenList)){
                            childrenList = new ArrayList<>();
                        }
                        childrenList.add(wareCatalogDto);
                        dto.setChildren(childrenList);
                    }
                    // 没有找到 继续向下级查找
                    else{
                        // 递归查找
                        if(CollectionUtil.isNotEmpty(childrenList)){
                            this.setWareCatalogDtoTree(wareCatalogDto, childrenList);
                        }
                    }
                });
            }
        }
        // 不存在父分类 既自己就是父分类
        else{
            wareCatalogDtoList.add(wareCatalogDto);
        }
    }

    /**
     * 根据条件查找物料分类信息
     *
     * @param wareCatalogFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareCatalogDto>
     * @datetime 2019/11/20 14:56
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareCatalogDto> queryWareCatalog(WareCatalogFilter wareCatalogFilter) {
        ListWrapper<WareCatalogDto> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(wareCatalogFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper<WareCatalog> wrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(wareCatalogFilter.getName())){
            wrapper.like("name", wareCatalogFilter.getName());
        }
        Page page = new Page<>(wareCatalogFilter.getPageNum(), wareCatalogFilter.getPageSize());
        IPage<WareCatalog> iPage = this.page(page, wrapper);
        if(iPage != null){
            if(iPage.getRecords() != null && iPage.getSize() > 0){
                List<WareCatalogDto> dtoList = new ArrayList<>();
                WareCatalogDto dto;
                for(WareCatalog catalog : iPage.getRecords()){
                    dto = new WareCatalogDto();
                    BeanUtils.copyProperties(catalog, dto);
                    /* TODO 规格参数 */
                    dtoList.add(dto);
                }
                wrapperList.setList(dtoList);
            }
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 根据ID查找物料分类信息
     *
     * @param id
     * @return com.zjft.usp.wms.baseinfo.dto.WareCatalogDto
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     */
    @Override
    public WareCatalogDto findWareCatalogBy(Long id) {
        WareCatalog wareCatalog = new WareCatalog().selectById(id);
        WareCatalogDto wareCatalogDto = new WareCatalogDto();
        BeanUtils.copyProperties(wareCatalog, wareCatalogDto);
        /* TODO 装入规格信息 */
        List<WareCatalogSpecs> specsList = wareCatalogSpecsService.listWareCatalogSpecs(id);
        wareCatalogDto.setWareCatalogSpecsList(specsList);
        return wareCatalogDto;
    }

    /**
     * 新增物料分类信息
     *
     * @param wareCatalogDto
     * @return void
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWareCatalog(WareCatalogDto wareCatalogDto) {
        Assert.notNull(wareCatalogDto, "wareCatalogDto 不能为空");
        WareCatalog wareCatalog = new WareCatalog();
        BeanUtils.copyProperties(wareCatalogDto, wareCatalog);
        wareCatalog.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareCatalogDto.getSortNo())){
            int sortNo = 0;
            Optional<WareCatalog> optional = this.list().stream().max(Comparator.comparing(WareCatalog::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareCatalog.setSortNo(sortNo + 10);
        }
        wareCatalog.insert();
        wareCatalogSpecsService.insertWareCatalogSpecs(wareCatalogDto.getWareCatalogSpecsList());
    }

    /**
     * 更新物料分类信息
     *
     * @param wareCatalogDto
     * @return void
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWareCatalog(WareCatalogDto wareCatalogDto) {
        Assert.notNull(wareCatalogDto, "wareCatalog 不能为 NULL");
        WareCatalog wareCatalog = new WareCatalog();
        BeanUtils.copyProperties(wareCatalogDto, wareCatalog);
        wareCatalog.updateById();
        /* TODO 更新策略 删除再添加 */
        wareCatalogSpecsService.deleteWareCatalogSpecs(wareCatalog.getId());
        wareCatalogSpecsService.insertWareCatalogSpecs(wareCatalogDto.getWareCatalogSpecsList());
    }

    /**
     * 删除物料分类信息
     *
     * @param id
     * @return void
     * @datetime 2019/11/18 17:01
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWareCatalog(Long id) {
        WareCatalog wareCatalog = new WareCatalog();
        wareCatalog.deleteById(id);
        wareCatalogSpecsService.deleteWareCatalogSpecs(id);
    }

    /**
     * 模糊匹配
     *
     * @author canlei
     * @param wareCatalogFilter
     * @return
     */
    @Override
    public List<WareCatalog> match(WareCatalogFilter wareCatalogFilter) {
        List<WareCatalog> list = new ArrayList<>();
        if(wareCatalogFilter == null || LongUtil.isZero(wareCatalogFilter.getCorpId())) {
            return list;
        }
        QueryWrapper<WareCatalog> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", wareCatalogFilter.getCorpId());
        if(StrUtil.isNotBlank(wareCatalogFilter.getName())) {
            queryWrapper.like("name", wareCatalogFilter.getName());
        }
        queryWrapper.eq("enabled", "Y");
        queryWrapper.orderByAsc("sort_no");
        queryWrapper.last("limit 100");
        list = this.list(queryWrapper);
        return list;
    }
}
