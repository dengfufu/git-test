package com.zjft.usp.anyfix.work.ware.mapper;

import com.zjft.usp.anyfix.work.ware.WareFilter;
import com.zjft.usp.anyfix.work.ware.dto.WareDto;
import com.zjft.usp.anyfix.work.ware.model.WorkWareRecycle;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 工单回收物品表 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WorkWareRecycleMapper extends BaseMapper<WorkWareRecycle> {

    /**
     * 查询物品分类
     *
     * @author canlei
     * @param wareFilter
     * @return
     */
    List<WareDto> listCatalog(@Param("wareFilter") WareFilter wareFilter);

    /**
     * 查询物品品牌
     *
     * @author canlei
     * @param wareFilter
     * @return
     */
    List<WareDto> listBrand(@Param("wareFilter") WareFilter wareFilter);

    /**
     * 查询物品型号
     *
     * @author canlei
     * @param wareFilter
     * @return
     */
    List<WareDto> listModel(@Param("wareFilter") WareFilter wareFilter);

}
