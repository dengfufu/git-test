package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.model.WareClassBrand;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品类型适用品牌表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WareClassBrandService extends IService<WareClassBrand> {

    /**
     * 根据服务商企业编号获取物品分类编号和品牌编号列表的映射
     * @param corpId
     * @return
     */
    Map<Long, List<Long>> mapIdAndBrandIdListByCorpId(Long corpId);

    /**
     * 根据服务商企业编号获取物品分类编号和品牌名称（多个用,分隔）的映射
     * @param corpId
     * @return
     */
    Map<Long, String> mapIdAndBrandNamesByCorpId(Long corpId);

}
