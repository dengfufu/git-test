package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.dto.LargeClassDto;
import com.zjft.usp.wms.baseinfo.dto.SmallClassDto;
import com.zjft.usp.wms.baseinfo.model.SmallClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务小类表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface SmallClassService extends IService<SmallClass> {

    /**
     * 获得排序后的大类列表
     * @param smallClassDto
     * @return
     */
    List<SmallClassDto> query(SmallClassDto smallClassDto);

    /**
     * 获得业务小类ID与名称映射
     * @param corpId
     * @return
     */
    public Map<Integer,String> mapClassIdAndName(Long corpId);
}
