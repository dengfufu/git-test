package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.CustomListMainDto;
import com.zjft.usp.wms.baseinfo.enums.EnabledEnum;
import com.zjft.usp.wms.baseinfo.filter.CustomListMainFilter;
import com.zjft.usp.wms.baseinfo.model.CustomListDetail;
import com.zjft.usp.wms.baseinfo.model.CustomListMain;
import com.zjft.usp.wms.baseinfo.mapper.CustomListMainMapper;
import com.zjft.usp.wms.baseinfo.service.CustomListDetailService;
import com.zjft.usp.wms.baseinfo.service.CustomListMainService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 自定义列表主表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class CustomListMainServiceImpl extends ServiceImpl<CustomListMainMapper, CustomListMain> implements CustomListMainService {

    @Resource
    private CustomListMainMapper customListMainMapper;
    @Resource
    private CustomListDetailService customListDetailService;

    @Override
    public ListWrapper<CustomListMainDto> pageBy(CustomListMainFilter customListMainFilter) {
        IPage<CustomListMain> page = new Page(customListMainFilter.getPageNum(), customListMainFilter.getPageSize());
        QueryWrapper<CustomListMain> queryWrapper = new QueryWrapper();
        if (customListMainFilter != null) {
            /**查询条件后面陆续加*/
            if (LongUtil.isNotZero(customListMainFilter.getCorpId())) {
                queryWrapper.eq("corp_id", customListMainFilter.getCorpId());
            }
            if (!StringUtils.isEmpty(customListMainFilter.getName())) {
                queryWrapper.like("name", customListMainFilter.getName());
            }
            // 查询已启用
            /*queryWrapper.eq("enabled", EnabledEnum.YES.getCode());*/

            queryWrapper.orderByAsc("sort_no");
        }
        IPage<CustomListMain> iPage = this.page(page, queryWrapper);
        List<CustomListMain> customListMainList = iPage.getRecords();
        List<CustomListMainDto> customListMainDtoList = getCustomListMainDtoList(customListMainList);
        return ListWrapper.<CustomListMainDto>builder()
                .list(customListMainDtoList)
                .total(iPage.getTotal())
                .build();
    }

    @Override
    public List<CustomListMain> listEnabledBy(Long corpId) {
        QueryWrapper<CustomListMain> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("corp_id", corpId);
        queryWrapper.eq("enabled", EnabledEnum.YES.getCode());
        queryWrapper.orderByAsc("sort_no");
        return this.customListMainMapper.selectList(queryWrapper);
    }

    /**
     * 查找常用列表
     *
     * @param customListMainId
     * @return com.zjft.usp.wms.baseinfo.dto.CustomListDto
     * @datetime 2019-12-02 12:44
     * @version
     * @author dcyu
     */
    @Override
    public CustomListMainDto findCustomListBy(Long customListMainId) {
        Assert.notNull(customListMainId, "customListMainId 不能为 Null");
        CustomListMain customListMain = this.getById(customListMainId);
        CustomListMainDto customListDto = new CustomListMainDto();
        BeanUtils.copyProperties(customListMain, customListDto);
        // TODO 常用列表明细
        List<CustomListDetail> customListDetails = customListDetailService.listCustomListDetail(customListMainId);
        if(CollectionUtil.isNotEmpty(customListDetails)){
            customListDto.setCustomListDetailList(customListDetails);
        }
        return customListDto;
    }

    /**
     * 新增常用列表
     *
     * @param customListMainDto
     * @return void
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     */
    @Override
    public void insertCustomList(CustomListMainDto customListMainDto, UserInfo userInfo) {
        if (customListMainDto == null ) {
            throw new AppException("customListDetailList 不能为空！");
        }
        if (StringUtils.isEmpty(customListMainDto.getName())) {
            throw new AppException("列表值名称不能为空！");
        }

        CustomListMain customListMain = new CustomListMain();
        BeanUtils.copyProperties(customListMainDto, customListMain);
        customListMain.setId(KeyUtil.getId());

        this.save(customListMain);
        // TODO
        customListDetailService.insertCustomListDetail(customListMainDto.getCustomListDetailList(), customListMain.getId(), userInfo.getUserId());
    }

    /**
     * 更新常用列表
     *
     * @param customListDto
     * @return void
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     */
    @Override
    public void updateCustomList(CustomListMainDto customListDto) {
        Assert.notNull(customListDto, "customListMain 不能为空");
        this.updateById(customListDto);
        // 明细更新策略 先删除 再添加
        customListDetailService.deleteCustomListDetail(customListDto.getId());
        // TODO
        customListDetailService.insertCustomListDetail(customListDto.getCustomListDetailList(),customListDto.getId(), customListDto.getUserId());
    }

    /**
     * 删除常用列表
     *
     * @param customListMainId
     * @return void
     * @datetime 2019-12-02 10:30
     * @version
     * @author dcyu
     */
    @Override
    public void deleteCustomList(Long customListMainId) {
        Assert.notNull(customListMainId, "customListId 不能为 Null");
        this.removeById(customListMainId);
        // TODO 删除子表数据
        customListDetailService.deleteCustomListDetail(customListMainId);
    }

    /**
     * 内部通用方法（将model转换成dto）
     *
     * @param customListMainList
     */
    private List<CustomListMainDto> getCustomListMainDtoList(List<CustomListMain> customListMainList) {
        List<CustomListMainDto> customListMainDtoList = new ArrayList<>();
        if (customListMainList != null && customListMainList.size() > 0) {
            for (CustomListMain entity : customListMainList) {
                /** Dto转换 */
                CustomListMainDto Dto = new CustomListMainDto();
                BeanUtils.copyProperties(entity, Dto);
                customListMainDtoList.add(Dto);
            }
        }
        return customListMainDtoList;
    }

    /**
     * 修改自定义列表基础数据
     * @param customListDto
     */
    @Override
    public void updateCustomListMain(CustomListMainDto customListDto) {
        Assert.notNull(customListDto, "customListMain 不能为空");
        this.updateById(customListDto);
    }
}
