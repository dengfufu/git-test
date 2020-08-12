package com.zjft.usp.uas.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.redis.template.RedisRepository;
import com.zjft.usp.uas.baseinfo.dto.AreaDto;
import com.zjft.usp.uas.baseinfo.dto.CfgAreaDto;
import com.zjft.usp.uas.baseinfo.mapper.CfgAreaMapper;
import com.zjft.usp.uas.baseinfo.model.AreaInfo;
import com.zjft.usp.uas.baseinfo.model.CfgArea;
import com.zjft.usp.uas.baseinfo.service.CfgAreaService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 地区服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019-08-19 10:15
 **/
@Service
@Transactional(rollbackFor = Exception.class)
public class CfgAreaServiceImpl extends ServiceImpl<CfgAreaMapper, CfgArea> implements CfgAreaService {

    @Resource
    private CfgAreaMapper cfgAreaMapper;

    @Resource
    private RedisRepository redisRepository;

    public static final String AREA_KEY = "area";

    @PostConstruct
    private void init() {
        List<CfgAreaDto> list = initAreaDto();
        redisRepository.set(AREA_KEY, list);
    }

    public List<CfgAreaDto> initAreaDto() {
        List<CfgAreaDto> cfgAreaDtoList = new ArrayList<>();
        List<AreaInfo> areaInfoList = cfgAreaMapper.listAreaInfo();
        Map<CfgAreaDto, List<CfgAreaDto>> provinceCityMap = new LinkedHashMap<>();
        Map<CfgAreaDto, List<CfgAreaDto>> cityDistrictMap = new LinkedHashMap<>();

        if (areaInfoList != null) {
            CfgAreaDto provinceDto;
            CfgAreaDto cityDto;
            CfgAreaDto districtDto;
            for (AreaInfo areaInfo : areaInfoList) {
                provinceDto = new CfgAreaDto();
                provinceDto.setValue(areaInfo.getProvinceCode());
                provinceDto.setLabel(areaInfo.getProvinceName());
                cityDto = new CfgAreaDto();
                cityDto.setValue(areaInfo.getCityCode());
                cityDto.setLabel(areaInfo.getCityName());
                districtDto = new CfgAreaDto();
                districtDto.setValue(areaInfo.getDistrictCode());
                districtDto.setLabel(areaInfo.getDistrictName());
                districtDto.setIsLeaf(true);
                List<CfgAreaDto> districtList = new ArrayList<>();
                if (cityDistrictMap.containsKey(cityDto)) {
                    districtList = cityDistrictMap.get(cityDto);
                }
                if (!districtList.contains(districtDto)) {
                    districtList.add(districtDto);
                }
                cityDistrictMap.put(cityDto, districtList);

                List<CfgAreaDto> cityList = new ArrayList<>();
                if (provinceCityMap.containsKey(provinceDto)) {
                    cityList = provinceCityMap.get(provinceDto);
                }
                if (!cityList.contains(cityDto)) {
                    cityList.add(cityDto);
                }
                provinceCityMap.put(provinceDto, cityList);
            }
        }

        for (CfgAreaDto provinceDto : provinceCityMap.keySet()) {
            List<CfgAreaDto> cityList = provinceCityMap.get(provinceDto);
            for (CfgAreaDto cityDto : cityList) {
                List<CfgAreaDto> districtList = cityDistrictMap.get(cityDto);
                // 如果地市下面没有区县，则设置为叶子节点
                if (districtList != null && districtList.size() == 1 && StringUtils.isEmpty(districtList.get(0).getValue())) {
                    cityDto.setChildren(null);
                    cityDto.setIsLeaf(true);
                } else {
                    cityDto.setChildren(districtList);
                }
            }

            if (cityList.size() == 1 && StringUtils.isEmpty(cityList.get(0).getValue())) {
                provinceDto.setIsLeaf(true);
                provinceDto.setChildren(null);
            } else {
                provinceDto.setChildren(cityList);
            }
            cfgAreaDtoList.add(provinceDto);
        }
        return cfgAreaDtoList;
    }

    @Override
    public List<CfgAreaDto> listAreaDto() {
        return (List<CfgAreaDto>) redisRepository.get(AREA_KEY);
    }

    @Override
    public List<CfgAreaDto> listProvinceAndCity() {
        List<CfgAreaDto> list = (List<CfgAreaDto>) redisRepository.get(AREA_KEY);
        for (CfgAreaDto area : list) {
            List<CfgAreaDto> citys = area.getChildren();
            if (citys != null && citys.size() > 0) {
                for (CfgAreaDto city : citys) {
                    city.setIsLeaf(true);
                    city.setChildren(null);
                }
            }
        }
        return list;
    }

    @Override
    public List<CfgAreaDto> listProvince(boolean needChild, boolean needDistrictChild) {
        List<CfgArea> cfgAreaList = this.list(new QueryWrapper<CfgArea>().eq("length(code)", 2).orderByAsc("code"));
        Map<String, Integer> map = this.getProvinceCityCountMap();
        List<CfgAreaDto> cfgAreaDtoList = new ArrayList<>();
        CfgAreaDto cfgAreaDto;
        for (CfgArea cfgArea : cfgAreaList) {
            cfgAreaDto = new CfgAreaDto();
            cfgAreaDto.setValue(cfgArea.getCode());
            cfgAreaDto.setLabel(cfgArea.getName());
            Integer cityCount = map.get(cfgArea.getCode());
            if (IntUtil.isNotZero(cityCount)) {
                cfgAreaDto.setHasChildren(true);
            } else {
                cfgAreaDto.setHasChildren(false);
                cfgAreaDto.setIsLeaf(true);
            }
            if (needChild && cfgAreaDto.isHasChildren()) {
                cfgAreaDto.setChildren(this.listCity(cfgArea.getCode(), needDistrictChild));
            }
            cfgAreaDtoList.add(cfgAreaDto);
        }
        return cfgAreaDtoList;
    }

    @Override
    public List<CfgAreaDto> listAreaInfo() {
        return this.listProvince(true, true);
    }

    @Override
    public List<CfgAreaDto> listCity(String province, boolean needChild) {
        List<CfgArea> cfgAreaList = this.baseMapper.listCityByProvince(province);
        Map<String, Integer> map = this.getCityDistrictCountMap(province);
        List<CfgAreaDto> cfgAreaDtoList = new ArrayList<>();
        CfgAreaDto cfgAreaDto;
        for (CfgArea cfgArea : cfgAreaList) {
            cfgAreaDto = new CfgAreaDto();
            cfgAreaDto.setValue(cfgArea.getCode());
            cfgAreaDto.setLabel(cfgArea.getName());
            Integer districtCount = map.get(cfgArea.getCode());
            if (IntUtil.isNotZero(districtCount)) {
                cfgAreaDto.setHasChildren(true);
            } else {
                cfgAreaDto.setHasChildren(false);
                cfgAreaDto.setIsLeaf(true);
            }
            if (needChild && cfgAreaDto.isHasChildren()) {
                cfgAreaDto.setChildren(this.listDistrict(cfgArea.getCode()));
            }
            cfgAreaDtoList.add(cfgAreaDto);
        }
        return cfgAreaDtoList;
    }

    @Override
    public List<CfgAreaDto> listDistrict(String city) {
        List<CfgArea> cfgAreaList = this.baseMapper.listDistrictByCity(city);
        List<CfgAreaDto> cfgAreaDtoList = new ArrayList<>();
        CfgAreaDto cfgAreaDto;
        for (CfgArea cfgArea : cfgAreaList) {
            cfgAreaDto = new CfgAreaDto();
            cfgAreaDto.setValue(cfgArea.getCode());
            cfgAreaDto.setLabel(cfgArea.getName());
            cfgAreaDto.setHasChildren(false);
            cfgAreaDto.setIsLeaf(true);
            cfgAreaDtoList.add(cfgAreaDto);
        }
        return cfgAreaDtoList;
    }

    @Override
    public AreaDto getAreaDto(AreaDto areaDto) {
        String provinceName = StrUtil.trimToEmpty(areaDto.getProvinceName());
        String cityName = StrUtil.trimToEmpty(areaDto.getCityName());
        String districtName = StrUtil.trimToEmpty(areaDto.getDistrictName());
        String province = "";
        String city = "";
        String district = "";
        if (StrUtil.isNotBlank(areaDto.getAreaCode())) {
            if (areaDto.getAreaCode().length() >= 2) {
                province = areaDto.getAreaCode().substring(0, 2);
                CfgArea cfgArea = this.getById(province);
                if (cfgArea != null) {
                    provinceName = cfgArea.getName();
                }
            }
            if (areaDto.getAreaCode().length() >= 4) {
                city = areaDto.getAreaCode().substring(0, 4);
                CfgArea cfgArea = this.getById(city);
                if (cfgArea != null) {
                    cityName = cfgArea.getName();
                }
            }
            if (areaDto.getAreaCode().length() >= 6) {
                district = areaDto.getAreaCode().substring(0, 6);
                CfgArea cfgArea = this.getById(district);
                if (cfgArea != null) {
                    districtName = cfgArea.getName();
                }
            }
        } else {
            province = this.parseProvince(provinceName);
            provinceName = StrUtil.isNotBlank(province) ? provinceName : "";
            city = this.parseCity(province, cityName);
            cityName = StrUtil.isNotBlank(city) ? cityName : "";
            district = this.parseDistrict(city, districtName);
            districtName = StrUtil.isNotBlank(district) ? districtName : "";
        }
        areaDto.setProvinceCode(province);
        areaDto.setCityCode(city);
        areaDto.setDistrictCode(district);
        areaDto.setProvinceName(provinceName);
        areaDto.setCityName(cityName);
        areaDto.setDistrictName(districtName);
        return areaDto;
    }

    @Override
    public AreaDto findAreaByCode(String code) {
        code = StrUtil.trimToEmpty(code);
        String province = "";
        String city = "";
        String district = "";
        List<String> codeList = new ArrayList<>();
        if (code.length() >= 2) {
            province = code.substring(0, 2);
            codeList.add(province);
        }
        if (code.length() >= 4) {
            city = code.substring(0, 4);
            codeList.add(city);
        }
        if (code.length() >= 6) {
            district = code.substring(0, 6);
            codeList.add(district);
        }
        Map<String, String> areaMap = this.mapCodeAndNameByCodeList(codeList);

        String provinceName = StrUtil.trimToEmpty(areaMap.get(province));
        String cityName = StrUtil.trimToEmpty(areaMap.get(city));
        String districtName = StrUtil.trimToEmpty(areaMap.get(district));
        AreaDto areaDto = new AreaDto();
        areaDto.setProvinceCode(province);
        areaDto.setCityCode(city);
        areaDto.setDistrictCode(district);
        areaDto.setProvinceName(provinceName);
        areaDto.setCityName(cityName);
        areaDto.setDistrictName(districtName);
        if (!cityName.equals(provinceName + "市")) {
            cityName = provinceName + cityName;
        }
        areaDto.setAreaName(cityName + districtName);
        return areaDto;
    }

    @Override
    public String parseProvince(String name) {
        name = StrUtil.trimToEmpty(name);
        CfgArea cfgArea = this.getOne(new QueryWrapper<CfgArea>().eq("name", name), true);
        if (cfgArea != null) {
            return cfgArea.getCode();
        }
        return "";
    }

    @Override
    public String parseCity(String province, String name) {
        name = StrUtil.trimToEmpty(name);
        CfgArea cfgArea = this.getOne(new QueryWrapper<CfgArea>().eq("name", name)
                .eq("left(code, 2)", province), true);
        if (cfgArea != null) {
            return cfgArea.getCode();
        }
        return "";
    }

    @Override
    public String parseDistrict(String city, String name) {
        name = StrUtil.trimToEmpty(name);
        CfgArea cfgArea = this.getOne(new QueryWrapper<CfgArea>().eq("name", name)
                .eq("left(code ,4)", city), true);
        if (cfgArea != null) {
            return cfgArea.getCode();
        }
        return "";
    }

    @Override
    public Map<String, String> selectAreaMap() {
        Map<String, String> resultMap = new HashMap<>();
        List<Map<String, String>> baseList = cfgAreaMapper.selectAreaMap();
        for (Map<String, String> map : baseList) {
            String code = null;
            String name = null;
            for (Map.Entry<String, String> entry : map.entrySet()) {
                if ("code".equals(entry.getKey())) {
                    code = String.valueOf(entry.getValue());
                } else if ("name".equals(entry.getKey())) {
                    name = String.valueOf(entry.getValue());
                }
                resultMap.put(code, name);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, String> mapAreaCodeAndName(String code) {
        Map<String, String> map = new LinkedHashMap<>();
        List<CfgArea> list = this.list(new QueryWrapper<CfgArea>().likeRight("code", code));
        for (CfgArea cfgArea : list) {
            map.put(cfgArea.getCode(), cfgArea.getName());
        }
        return map;
    }

    @Override
    public Map<String, Integer> getProvinceCityCountMap() {
        Map<String, Integer> resultMap = new HashMap<>();
        List<Map<String, Integer>> baseList = cfgAreaMapper.getProvinceCityCountMap();
        for (Map<String, Integer> map : baseList) {
            String code = "";
            Integer count = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if ("provincecode".equals(entry.getKey())) {
                    code = String.valueOf(entry.getValue());
                } else if ("citycount".equals(entry.getKey())) {
                    String value = String.valueOf(entry.getValue());
                    count = Integer.parseInt(value);
                }
                resultMap.put(code, count);
            }
        }
        return resultMap;
    }

    @Override
    public Map<String, Integer> getCityDistrictCountMap(String province) {
        Map<String, Integer> resultMap = new HashMap<>();
        List<Map<String, Integer>> baseList = cfgAreaMapper.getCityDistrictCountMap(province);
        for (Map<String, Integer> map : baseList) {
            String code = "";
            Integer count = 0;
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                if ("citycode".equals(entry.getKey())) {
                    code = String.valueOf(entry.getValue());
                } else if ("districtcount".equals(entry.getKey())) {
                    String value = String.valueOf(entry.getValue());
                    count = Integer.parseInt(value);
                }
                resultMap.put(code, count);
            }
        }
        return resultMap;
    }

    /**
     * 根据区域编号列表获得映射
     *
     * @param codeList
     * @return
     * @author zgpi
     * @date 2020/2/22 13:14
     */
    @Override
    public Map<String, String> mapCodeAndNameByCodeList(List<String> codeList) {
        if (CollectionUtil.isEmpty(codeList)) {
            return null;
        }
        List<CfgArea> cfgAreaList = this.list(new QueryWrapper<CfgArea>().in("code", codeList));
        if (CollectionUtil.isNotEmpty(cfgAreaList)) {
            return cfgAreaList.stream().collect(Collectors.toMap(CfgArea::getCode, CfgArea::getName));
        }
        return null;
    }

}
