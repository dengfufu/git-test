package com.zjft.usp.anyfix.baseinfo.mapper;

import com.zjft.usp.anyfix.baseinfo.model.ExpressCompany;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 快递公司 Mapper 接口
 * </p>
 *
 * @author zgpi
 * @since 2020-04-16
 */
public interface ExpressCompanyMapper extends BaseMapper<ExpressCompany> {

    /**
     * 模糊查询快递公司
     *
     * @param corpId
     * @param name
     * @return
     * @author zgpi
     * @date 2020/4/20 10:44
     **/
    List<ExpressCompany> matchExpressCorp(@Param("corpId") Long corpId,
                                          @Param("name") String name);
}
