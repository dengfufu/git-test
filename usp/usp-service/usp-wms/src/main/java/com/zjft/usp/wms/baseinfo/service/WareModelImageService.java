package com.zjft.usp.wms.baseinfo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.wms.baseinfo.model.WareModelImage;

import java.util.List;

/**
 * <p>
 * 型号图像表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface WareModelImageService extends IService<WareModelImage> {

    /**
     * 根据型号Id查找图片列表
     * @datetime 2019/11/20 19:00
     * @version
     * @author dcyu
     * @param wareModelId
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareModelImage>
     */
    List<WareModelImage> listWareModelImage(Long wareModelId);

    /**
     * 新增物料型号图片
     * @datetime 2019/11/19 11:31
     * @version
     * @author dcyu
     * @param wareModelImageList
     * @return void
     */
    void insertWareModelImages(List<WareModelImage> wareModelImageList);

    /**
     * 删除物料型号图片
     * @datetime 2019/11/19 11:31
     * @version
     * @author dcyu
     * @param wareModelId
     * @return void
     */
    void deleteWareModelImages(Long wareModelId);
}
