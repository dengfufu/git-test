package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.WareCatalogSpecsDto;
import com.zjft.usp.wms.baseinfo.model.WareCatalog;
import com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs;
import com.zjft.usp.wms.baseinfo.mapper.WareCatalogSpecsMapper;
import com.zjft.usp.wms.baseinfo.service.WareCatalogSpecsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 物料分类规格定义表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareCatalogSpecsServiceImpl extends ServiceImpl<WareCatalogSpecsMapper, WareCatalogSpecs> implements WareCatalogSpecsService {
    /**
     * 物料分类规格信息列表
     *
     * @param wareCatalogId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareCatalogSpecs>
     * @datetime 2019/11/18 17:14
     * @version
     * @author dcyu
     */
    @Override
    public List<WareCatalogSpecs> listWareCatalogSpecs(Long wareCatalogId) {
        Assert.notNull(wareCatalogId, "wareCatalogId 不能为 NULL");
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_id", wareCatalogId);
        WareCatalogSpecs wareCatalogSpecs = new WareCatalogSpecs();
        return wareCatalogSpecs.selectList(queryWrapper);
    }

    /**
     * 根据物料分类查询规格
     *
     * @author canlei
     * @param catalogId
     * @return
     */
    @Override
    public List<WareCatalogSpecsDto> listByCatalogId(Long catalogId) {
        List<WareCatalogSpecsDto> dtoList = new ArrayList<>();
        if(LongUtil.isZero(catalogId)) {
            return dtoList;
        }
        QueryWrapper<WareCatalogSpecs> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("class_id", catalogId);
        queryWrapper.eq("enabled", "Y");
        queryWrapper.orderByAsc("sort_no");
        List<WareCatalogSpecs> list = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(list)) {
            list.forEach(wareCatalogSpecs -> {
                WareCatalogSpecsDto wareCatalogSpecsDto = new WareCatalogSpecsDto();
                BeanUtils.copyProperties(wareCatalogSpecs, wareCatalogSpecsDto);
                if(StrUtil.isNotBlank(wareCatalogSpecs.getValue())) {
                    wareCatalogSpecsDto.setValueList(Arrays.asList(wareCatalogSpecs.getValue().split(",")));
                }
                dtoList.add(wareCatalogSpecsDto);
            });
        }
        return dtoList;
    }

    /**
     * 新增物料分类规格信息
     *
     * @param wareCatalogSpecsList
     * @return void
     * @datetime 2019/11/18 17:14
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareCatalogSpecs(List<WareCatalogSpecs> wareCatalogSpecsList) {
        Assert.notNull(wareCatalogSpecsList, "wareCatalogSpecs 不能为 NULL");
        WareCatalogSpecs wareCatalogSpecs;
        for(WareCatalogSpecs wareCatalogSpecsEntry : wareCatalogSpecsList){
            wareCatalogSpecs = new WareCatalogSpecs();
            BeanUtils.copyProperties(wareCatalogSpecsEntry, wareCatalogSpecs);
            wareCatalogSpecs.setId(KeyUtil.getId());
            wareCatalogSpecs.insert();
        }
    }

    /**
     * 删除物料分类规格定义信息
     *
     * @param wareCatalogId
     * @return void
     * @datetime 2019/11/18 17:11
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareCatalogSpecs(Long wareCatalogId) {
        Assert.notNull(wareCatalogId, "wareCatalogId 不能为 NULL");
        WareCatalogSpecs wareCatalogSpecs = new WareCatalogSpecs();
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("class_id", wareCatalogId);
        wareCatalogSpecs.delete(queryWrapper);
    }
}
