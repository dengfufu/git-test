package com.zjft.usp.anyfix.corp.manage.mapper;

import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * <p>
 * 服务委托方与服务商关系表 Mapper 接口
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
public interface DemanderServiceMapper extends BaseMapper<DemanderService> {

    /**
     * 根据供应商或客户查询服务商列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2019/12/11 19:54
     **/
    List<DemanderServiceDto> listServiceByCorpId(Long corpId);

    /**
     * 根据委托商或服务商查询相关企业列表
     *
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/4/20 11:29
     **/
    List<DemanderServiceDto> listRelatesCorp(Long corpId);

}
