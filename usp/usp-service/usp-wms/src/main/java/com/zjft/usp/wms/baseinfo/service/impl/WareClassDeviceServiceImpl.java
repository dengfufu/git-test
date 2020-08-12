package com.zjft.usp.wms.baseinfo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.model.Result;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.device.service.DeviceFeignService;
import com.zjft.usp.wms.baseinfo.mapper.WareClassDeviceMapper;
import com.zjft.usp.wms.baseinfo.model.WareClassDevice;
import com.zjft.usp.wms.baseinfo.service.WareClassDeviceService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品类型适用设备表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class WareClassDeviceServiceImpl extends ServiceImpl<WareClassDeviceMapper, WareClassDevice> implements WareClassDeviceService {

    @Resource
    private DeviceFeignService deviceFeignService;

    @Override
    public Map<Long, List<Long>> mapIdAndSmallClassIdListByCorpId(Long corpId) {
        Map<Long, List<Long>> map = new HashMap<>();
        if(LongUtil.isZero(corpId)){
            return map;
        }
        QueryWrapper<WareClassDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("ware_class_id", "select id from ware_class where corp_id="+ corpId);
        List<WareClassDevice> list = this.list(queryWrapper);
        if(list != null && list.size() > 0){
            for(WareClassDevice wareClassDevice: list){
                if(map.containsKey(wareClassDevice.getWareClassId())){
                    List<Long> smallClassIds = map.get(wareClassDevice.getWareClassId());
                    smallClassIds.add(wareClassDevice.getSmallClassId());
                    map.put(wareClassDevice.getWareClassId(), smallClassIds);
                }else{
                    List<Long> smallClassIds = new ArrayList<>();
                    smallClassIds.add(wareClassDevice.getSmallClassId());
                    map.put(wareClassDevice.getWareClassId(), smallClassIds);
                }
            }
        }
        return map;
    }

    @Override
    public Map<Long, String> mapIdAndSmallClassNamesByCorpId(Long corpId) {
        Map<Long, String> map = new HashMap<>();
        if(LongUtil.isZero(corpId)){
            return map;
        }
        Map<Long, String> smallClassMap = this.deviceFeignService.mapSmallClassByCorp(corpId).getData();
        if(smallClassMap == null){
            return map;
        }
        QueryWrapper<WareClassDevice> queryWrapper = new QueryWrapper<>();
        queryWrapper.inSql("ware_class_id", "select id from ware_class where corp_id="+ corpId);
        List<WareClassDevice> list = this.list(queryWrapper);
        if(list != null && list.size() > 0){
            for(WareClassDevice wareClassDevice: list){
                if(map.containsKey(wareClassDevice.getWareClassId())){
                    String smallClassNames = map.get(wareClassDevice.getWareClassId());
                    String smallClassName = smallClassMap.get(wareClassDevice.getSmallClassId());
                    smallClassNames += "," + (smallClassName == null ? "" : smallClassName);
                    map.put(wareClassDevice.getWareClassId(), smallClassNames);
                }else{
                    String smallClassName = smallClassMap.get(wareClassDevice.getSmallClassId());
                    map.put(wareClassDevice.getWareClassId(), smallClassName == null ? "" : smallClassName);
                }
            }
        }
        return map;
    }
}
