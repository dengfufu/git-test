package com.zjft.usp.wms.baseinfo.service;

import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.wms.baseinfo.filter.WareClassFilter;
import com.zjft.usp.wms.baseinfo.model.WareClass;
import com.zjft.usp.wms.baseinfo.dto.WareClassDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * <p>
 * 物品分类表 服务类
 * </p>
 *
 * @author zgpi
 * @since 2019-10-14
 */
public interface WareClassService extends IService<WareClass> {

    /**
     * 物品分类列表
     *
     * @author zgpi
     * @date 2019/10/14 1:18 下午
     * @param wareClassFilter
     * @return
     **/
    List<WareClassDto> listWareClass(WareClassFilter wareClassFilter);

    /**
     * 根据品牌和小类获得物品列表
     * @author zgpi
     * @date 2019/9/26 3:57 下午
     * @param corpId
     * @param brandId
     * @param smallClassId
     * @return
     **/
    List<WareClass> listWareClassBy(Long corpId, Long brandId, Long smallClassId);

    /**
     * 添加物品分类
     * @param wareClassDto
     * @param curUserId
     * @param reqParam
     */
    void addWareClass(WareClassDto wareClassDto, Long curUserId, ReqParam reqParam);

    /**
     * 更新物品分类
     * @param wareClassDto
     * @param curUserId
     */
    void updateWareClass(WareClassDto wareClassDto, Long curUserId);

    /**
     * 根据id删除
     * @param id
     */
    void deleteById(Long id);

}
