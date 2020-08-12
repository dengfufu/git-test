package com.zjft.usp.anyfix.corp.manage.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderCustomFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 服务委托方与用户企业关系表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
public interface DemanderCustomMapper extends BaseMapper<DemanderCustom> {

    /**
     * 根据供应商查询客户列表
     *
     * @param demanderCustomFilter
     * @return
     * @author zgpi
     * @date 2019/12/11 19:22
     **/
    List<DemanderCustomDto> listCustomByDemander(@Param("demanderCustomFilter") DemanderCustomFilter demanderCustomFilter);

    /**
     * 根据委托商获得客户下拉列表
     * @param demanderCorp
     * @return
     */
    List<DemanderCustom> selectCustomByDemander(Long demanderCorp);

    /**
     * 根据供应商或服务商查询客户列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/11 19:36
     **/
    List<DemanderCustom> listCustomByCorpId(Long corpId);

    /**
     * 模糊客户名称
     * @param demanderCustomFilter
     * @param demanderCustomFilter
     * @return
     */
    List<DemanderCustomDto> matchCustomByCorp(DemanderCustomFilter demanderCustomFilter);
}
