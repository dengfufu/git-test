package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareClassFilter;
import com.zjft.usp.wms.baseinfo.mapper.WareClassMapper;
import com.zjft.usp.wms.baseinfo.model.WareClass;
import com.zjft.usp.wms.baseinfo.model.WareClassBrand;
import com.zjft.usp.wms.baseinfo.model.WareClassDevice;
import com.zjft.usp.wms.baseinfo.service.WareClassBrandService;
import com.zjft.usp.wms.baseinfo.service.WareClassDeviceService;
import com.zjft.usp.wms.baseinfo.service.WareClassService;
import com.zjft.usp.wms.baseinfo.dto.WareClassDto;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 物品分类表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
@Service
public class WareClassServiceImpl extends ServiceImpl<WareClassMapper, WareClass> implements WareClassService {

    @Autowired
    private WareClassBrandService wareClassBrandService;
    @Autowired
    private WareClassDeviceService wareClassDeviceService;

    @Resource
    private WareClassMapper wareClassMapper;

    @Override
    public List<WareClassDto> listWareClass(WareClassFilter wareClassFilter) {
        QueryWrapper<WareClass> queryWrapper = new QueryWrapper<>();
        if(wareClassFilter != null){
            if(StrUtil.isNotBlank(wareClassFilter.getName())){
                queryWrapper.like("name",wareClassFilter.getName());
            }
            if(StrUtil.isNotBlank(wareClassFilter.getEnabled())){
                queryWrapper.eq("enabled", wareClassFilter.getEnabled());
            }
            if(wareClassFilter.getCorpId() != null){
                queryWrapper.eq("corp_id", wareClassFilter.getCorpId());
            }
        }
        List<WareClass> wareClassList = this.list(queryWrapper);
        Map<Long, List<Long>> idAndBrandIdListMap = this.wareClassBrandService.mapIdAndBrandIdListByCorpId(wareClassFilter.getCorpId());
        Map<Long, List<Long>> idAndSmallClassIdListMap = this.wareClassDeviceService.mapIdAndSmallClassIdListByCorpId(wareClassFilter.getCorpId());
        Map<Long, String> idAndBrandNamesMap = this.wareClassBrandService.mapIdAndBrandNamesByCorpId(wareClassFilter.getCorpId());
        Map<Long, String> idAndSmallClassNamesMap = this.wareClassDeviceService.mapIdAndSmallClassNamesByCorpId(wareClassFilter.getCorpId());
        List<WareClassDto> wareClassDtoList = new ArrayList<>();
        if(wareClassList != null && wareClassList.size() > 0){
            /** 映射数据Map */
            for(WareClass wareClass : wareClassList){
                /** Mapper转换 */
                WareClassDto tmpDto = new WareClassDto();
                BeanUtils.copyProperties(wareClass, tmpDto);
                tmpDto.setBrandIdList(idAndBrandIdListMap.get(tmpDto.getId()));
                tmpDto.setSmallClassIdList(idAndSmallClassIdListMap.get(tmpDto.getId()));
                tmpDto.setBrandNames(idAndBrandNamesMap.get(tmpDto.getId()));
                tmpDto.setSmallClassNames(idAndSmallClassNamesMap.get(tmpDto.getId()));
                wareClassDtoList.add(tmpDto);
            }
        }
        return wareClassDtoList;
    }

    @Override
    public List<WareClass> listWareClassBy(Long corpId, Long brandId, Long smallClassId) {
        return wareClassMapper.listWareClassBy(corpId, brandId, smallClassId);
    }

    @Override
    public void addWareClass(WareClassDto wareClassDto, Long curUserId, ReqParam reqParam) {
        if(LongUtil.isZero(wareClassDto.getCorpId())){
            wareClassDto.setCorpId(reqParam.getCorpId());
        }
        WareClass wareClass = new WareClass();
        Long key = KeyUtil.getId();
        BeanUtils.copyProperties(wareClassDto, wareClass);
        wareClass.setId(key);
        wareClass.setOperator(curUserId);
        wareClass.setOperateTime(DateTime.now().toTimestamp());
        this.save(wareClass);
        if(wareClassDto.getBrandIdList() != null && wareClassDto.getBrandIdList().size() > 0){
            List<WareClassBrand> brandList = new ArrayList<>();
            for(Long brandId: wareClassDto.getBrandIdList()){
                WareClassBrand wareClassBrand = new WareClassBrand();
                wareClassBrand.setBrandId(brandId);
                wareClassBrand.setWareClassId(key);
                brandList.add(wareClassBrand);
            }
            this.wareClassBrandService.saveBatch(brandList);
        }
        if(wareClassDto.getSmallClassIdList() != null && wareClassDto.getSmallClassIdList().size() > 0){
            List<WareClassDevice> classDeviceList = new ArrayList<>();
            for(Long smallClassId: wareClassDto.getSmallClassIdList()){
                WareClassDevice wareClassDevice = new WareClassDevice();
                wareClassDevice.setSmallClassId(smallClassId);
                wareClassDevice.setWareClassId(key);
                classDeviceList.add(wareClassDevice);
            }
            this.wareClassDeviceService.saveBatch(classDeviceList);
        }
    }

    @Override
    public void updateWareClass(WareClassDto wareClassDto, Long curUserId) {
        if(LongUtil.isZero(wareClassDto.getId())){
            throw new AppException("物品编号不能为空！");
        }
        WareClass wareClass = this.getById(wareClassDto.getId());
        BeanUtils.copyProperties(wareClassDto, wareClass);
        wareClass.setOperator(curUserId);
        wareClass.setOperateTime(DateTime.now().toTimestamp());
        this.updateById(wareClass);
        //物品品牌和设备小类先删除再添加
        this.wareClassBrandService.remove(new UpdateWrapper<WareClassBrand>().eq("ware_class_id", wareClassDto.getId()));
        if(wareClassDto.getBrandIdList() != null && wareClassDto.getBrandIdList().size() > 0){
            List<WareClassBrand> brandList = new ArrayList<>();
            for(Long brandId: wareClassDto.getBrandIdList()){
                WareClassBrand wareClassBrand = new WareClassBrand();
                wareClassBrand.setBrandId(brandId);
                wareClassBrand.setWareClassId(wareClassDto.getId());
                brandList.add(wareClassBrand);
            }
            this.wareClassBrandService.saveBatch(brandList);
        }
        this.wareClassDeviceService.remove(new UpdateWrapper<WareClassDevice>().eq("ware_class_id", wareClassDto.getId()));
        if(wareClassDto.getSmallClassIdList() != null && wareClassDto.getSmallClassIdList().size() > 0){
            List<WareClassDevice> classDeviceList = new ArrayList<>();
            for(Long smallClassId: wareClassDto.getSmallClassIdList()){
                WareClassDevice wareClassDevice = new WareClassDevice();
                wareClassDevice.setSmallClassId(smallClassId);
                wareClassDevice.setWareClassId(wareClassDto.getId());
                classDeviceList.add(wareClassDevice);
            }
            this.wareClassDeviceService.saveBatch(classDeviceList);
        }
    }

    @Override
    public void deleteById(Long id) {
        if(LongUtil.isZero(id)){
            throw new AppException("主键不能为空！");
        }
        this.removeById(id);
        this.wareClassBrandService.remove(new UpdateWrapper<WareClassBrand>().eq("ware_class_id", id));
        this.wareClassDeviceService.remove(new UpdateWrapper<WareClassDevice>().eq("ware_class_id", id));
    }
}
