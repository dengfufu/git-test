package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.wms.baseinfo.dto.LargeClassDto;
import com.zjft.usp.wms.baseinfo.model.LargeClass;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 业务大类表 服务类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
public interface LargeClassService extends IService<LargeClass> {
    /**
     * 获得排序后的大类列表
     * @param largeClassDto
     * @return
     */
    List<LargeClassDto> query(LargeClassDto largeClassDto);

    /**
     * 获得业务大类ID与名称映射
     * @param corpId
     * @return
     */
    public Map<Integer,String> mapClassIdAndName(Long corpId);
}
