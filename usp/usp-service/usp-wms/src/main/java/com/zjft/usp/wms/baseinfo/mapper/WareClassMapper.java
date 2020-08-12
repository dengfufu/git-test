package com.zjft.usp.wms.baseinfo.mapper;

import com.zjft.usp.wms.baseinfo.model.WareClass;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 物品分类表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WareClassMapper extends BaseMapper<WareClass> {

    /**
     * 根据品牌和小类获得备件列表
     * @author zgpi
     * @date 2019/10/14 4:06 下午
     * @param corpId
     * @param brandId
     * @param smallClassId
     * @return
     **/
    List<WareClass> listWareClassBy(@Param("corpId") Long corpId,
                                    @Param("brandId") Long brandId,
                                    @Param("smallClassId") Long smallClassId);
}
