package com.zjft.usp.wms.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.baseinfo.mapper.WareModelImageMapper;
import com.zjft.usp.wms.baseinfo.model.WareModelImage;
import com.zjft.usp.wms.baseinfo.service.WareModelImageService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 型号图像表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareModelImageServiceImpl extends ServiceImpl<WareModelImageMapper, WareModelImage> implements WareModelImageService {

    /**
     * 根据型号Id查找图片列表
     *
     * @param wareModelId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareModelImage>
     * @datetime 2019/11/20 19:00
     * @version
     * @author dcyu
     */
    @Override
    public List<WareModelImage> listWareModelImage(Long wareModelId) {
        Assert.notNull(wareModelId, "wareModelId 不能为 NULL");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("model_id", wareModelId);
        return this.list(wrapper);
    }

    /**
     * 新增物料型号图片
     *
     * @param wareModelImageList
     * @return void
     * @datetime 2019/11/19 11:31
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareModelImages(List<WareModelImage> wareModelImageList) {
        Assert.notNull(wareModelImageList, "wareModelImageList 不能为 Null");
        wareModelImageList.forEach( wareModelImage ->  wareModelImage.setImageId(KeyUtil.getId()));
        this.saveBatch(wareModelImageList);
    }

    /**
     * 删除物料型号图片
     *
     * @param wareModelId
     * @return void
     * @datetime 2019/11/19 11:31
     * @version
     * @author dcyu
     */
    @Override
    public void deleteWareModelImages(Long wareModelId) {
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("model_id", wareModelId);
        this.remove(wrapper);
    }
}
