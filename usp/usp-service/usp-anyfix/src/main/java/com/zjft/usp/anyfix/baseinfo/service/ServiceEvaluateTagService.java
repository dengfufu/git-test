package com.zjft.usp.anyfix.baseinfo.service;

import com.zjft.usp.anyfix.baseinfo.model.ServiceEvaluateTag;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务评价标签表 服务类
 * </p>
 *
 * @author zphu
 * @since 2019-09-24
 */
public interface ServiceEvaluateTagService extends IService<ServiceEvaluateTag> {

    /**
     * 查询服务商评价标签
     *
     * @param serviceCrop
     * @return
     * @throws
     * @author zphu
     * @date 2019/9/24 16:19
     **/
    List<ServiceEvaluateTag> listServiceEvaluateTag(Long serviceCrop);

    /**
     * 根据企业编号获取tag编号和名称映射
     * @param serviceCorp
     * @return
     */
    Map<Integer, String> mapIdAndNameByCorp(Long serviceCorp);
}
