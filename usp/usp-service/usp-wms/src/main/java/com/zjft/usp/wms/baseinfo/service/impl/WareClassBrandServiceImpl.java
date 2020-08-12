package com.zjft.usp.wms.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.wms.baseinfo.mapper.WareClassBrandMapper;
import com.zjft.usp.wms.baseinfo.model.WareClassBrand;
import com.zjft.usp.wms.baseinfo.model.WareClassDevice;
import com.zjft.usp.wms.baseinfo.service.WareClassBrandService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品类型适用品牌表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WareClassBrandServiceImpl extends ServiceImpl<WareClassBrandMapper, WareClassBrand> implements WareClassBrandService {

    @Resource
    private DeviceFeignService deviceFeignService;

    @Override
    public Map<Long, List<Long>> mapIdAndBrandIdListByCorpId(Long corpId) {
        Map<Long, List<Long>> map = new HashMap<>();
        if(LongUtil.isZero(corpId)){
            return map;
        }
        QueryWrapper<WareClassBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("ware_class_id", "select id from ware_class where corp_id="+ corpId);
        List<WareClassBrand> list = this.list(queryWrapper);
        if(list != null && list.size() > 0){
            for(WareClassBrand wareClassBrand: list){
                if(map.containsKey(wareClassBrand.getWareClassId())){
                    List<Long> brandIds = map.get(wareClassBrand.getWareClassId());
                    brandIds.add(wareClassBrand.getBrandId());
                    map.put(wareClassBrand.getWareClassId(), brandIds);
                }else{
                    List<Long> brandIds = new ArrayList<>();
                    brandIds.add(wareClassBrand.getBrandId());
                    map.put(wareClassBrand.getWareClassId(), brandIds);
                }
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapIdAndBrandNamesByCorpId(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if(LongUtil.isZero(corpId)){
            return map;
        }
        Map<Long, String> brandMap = this.deviceFeignService.mapDeviceBrandByCorp(corpId).getData();
        if(brandMap == null){
            return map;
        }
        QueryWrapper<WareClassBrand> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("ware_class_id", "select id from ware_class where corp_id="+ corpId);
        List<WareClassBrand> list = this.list(queryWrapper);
        if(list != null && list.size() > 0){
            for(WareClassBrand wareClassBrand: list){
                if(map.containsKey(wareClassBrand.getWareClassId())){
                    String brandNames = map.get(wareClassBrand.getWareClassId());
                    String brandName = brandMap.get(wareClassBrand.getBrandId());
                    brandNames += "," + (brandName == null ? "" : brandName);
                    map.put(wareClassBrand.getWareClassId(), brandNames);
                }else{
                    String brandName = brandMap.get(wareClassBrand.getBrandId());
                    map.put(wareClassBrand.getWareClassId(), brandName == null ? "" : brandName);
                }
            }
        }
        return map;
    }

}
