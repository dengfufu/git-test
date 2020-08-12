package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.baseinfo.dto.CustomListDetailDto;
import com.zjft.usp.wms.baseinfo.model.CustomListDetail;
import com.zjft.usp.wms.baseinfo.mapper.CustomListDetailMapper;
import com.zjft.usp.wms.baseinfo.service.CustomListDetailService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 自定义列表子表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class CustomListDetailServiceImpl extends ServiceImpl<CustomListDetailMapper, CustomListDetail> implements CustomListDetailService {

    /**
     * 常用列表明细列表
     *
     * @param customListMainId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.CustomListDetail>
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     */
    @Override
    public List<CustomListDetail> listCustomListDetail(Long customListMainId) {
        Assert.notNull(customListMainId, "customListMainId 不能为 Null");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("list_id", customListMainId);
        return this.list(wrapper);
    }

    /**
     * 新增常用列表明细
     *
     * @param customListDetail
     * @param userId
     * @return void
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     */
    @Override
    public void insertCustomListDetail(CustomListDetail customListDetail, Long userId) {
        Assert.notNull(customListDetail, "customListDetail 不能为空");
        Assert.notNull(userId, "userId 不能为 Null");
        customListDetail.setId(KeyUtil.getId());
        customListDetail.setCreateBy(userId);
        customListDetail.setCreateTime(DateUtil.date());
        this.save(customListDetail);
    }

    /**
     * 新增常用列表明细
     *
     * @param customListDetailList
     * @param userId
     * @return void
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     */
    @Override
    public void insertCustomListDetail(List<CustomListDetail> customListDetailList, Long listId, Long userId) {
        if (customListDetailList == null || customListDetailList.isEmpty()) {
            throw new AppException("customListDetailList 不能为空！");
        }
        if (userId == null) {
            throw new AppException("userId 不能为 Null");
        }
        customListDetailList.forEach(customListDetail -> {
            customListDetail.setId(KeyUtil.getId());
            customListDetail.setListId(listId);
            customListDetail.setCreateBy(userId);
            customListDetail.setCreateTime(DateUtil.date());
            customListDetail.setUpdateBy(userId);
            customListDetail.setUpdateTime(DateUtil.date().toTimestamp());
        });
        this.saveBatch(customListDetailList);
    }

    /**
     * 删除常用列表明细
     *
     * @param customListMainId
     * @return void
     * @datetime 2019-12-02 15:53
     * @version
     * @author dcyu
     */
    @Override
    public void deleteCustomListDetail(Long customListMainId) {
        Assert.notNull(customListMainId, "customListMainId 不能为 Null");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("list_id", customListMainId);
        this.remove(wrapper);
    }

    /**
     * 修改常用列表明细
     */
    @Override
    public void updateCustomListDetail(CustomListDetailDto customListDetailDto) {
        Assert.notNull(customListDetailDto, "customListDetailDto 不能为 Null");
        super.updateById(customListDetailDto);
    }
}
