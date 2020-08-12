package com.zjft.usp.anyfix.corp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderServiceDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderServiceFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;
import com.zjft.usp.uas.dto.CorpRegistry;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务委托方与服务商关系表 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
public interface DemanderServiceService extends IService<DemanderService> {

    /**
     * 根据服务委托方查询服务商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2019/12/10 15:43
     **/
    List<DemanderServiceDto> listServiceByDemander(DemanderServiceFilter demanderServiceFilter);

    /**
     * 根据服务商查询供应商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2019/12/10 15:43
     **/
    List<DemanderServiceDto> listDemanderByService(DemanderServiceFilter demanderServiceFilter);

    /**
     * 分页查询服务商
     *
     * @param demanderServiceFilter
     * @return
     */
    ListWrapper<DemanderServiceDto> pageByFilter(DemanderServiceFilter demanderServiceFilter);

    /**
     * 服务委托方添加服务商
     *
     * @param demanderServiceDto
     * @param curUserId
     */
    void addDemanderService(DemanderServiceDto demanderServiceDto, Long curUserId);

    /**
     * 创建委托商
     *
     * @param corpRegistry
     * @param reqParam
     * @param curUserId
     */
    void createDemanderService(CorpRegistry corpRegistry, ReqParam reqParam, Long curUserId);


    /**
     * 服务委托方更新服务商
     *
     * @param demanderServiceDto
     * @param curUserId
     */
    void updateDemanderService(DemanderServiceDto demanderServiceDto, Long curUserId);

    /**
     * 根据服务委托方企业编号查询其相关企业编号列表
     *
     * @param demanderCorp
     * @return
     */
    List<Long> listRelatedCorpIdsByDemander(Long demanderCorp);

    /**
     * 根据服务商企业编号查询其相关企业编号列表
     *
     * @param serviceCorp
     * @return
     */
    List<Long> listRelatedCorpIdsByService(Long serviceCorp);

    /**
     * 根据企业编号查询所有相关企业编号列表
     *
     * @param corpId
     * @return
     */
    List<Long> listAllRelatedCorpIds(Long corpId);

    /**
     * 根据企业编号查询所有相关企业列表
     *
     * @param corpId
     * @param excludeCorpId
     * @return
     */
    List<CorpDto> listRelatesCorp(Long corpId, Long excludeCorpId);

    /**
     * 根据企业编号查询相关联的服务商
     *
     * @param corpId
     * @return
     */
    List<DemanderServiceDto> listServiceByCorpId(Long corpId);


    /**
     * 查询可供新增编辑的服务商
     *
     * @param demanderServiceFilter
     * @return
     */
    List<CorpDto> listServiceAvailableByDemander(DemanderServiceFilter demanderServiceFilter);

    /**
     * 分页查询委托商
     *
     * @param demanderServiceFilter
     * @return
     */
    ListWrapper<DemanderServiceDto> pageDemander(DemanderServiceFilter demanderServiceFilter);

    /**
     * 根据委托商获取服务商企业编号列表
     *
     * @param demanderCorp
     * @return
     */
    List<Long> listServiceCorpIdsByDemander(Long demanderCorp);


    /**
     * 根据服务商查询委托商编号
     *
     * @param corpId
     * @return
     */
    List<Long> listDemanderCorpId(Long corpId);

    /**
     * 根据编号查询委托商详情
     *
     * @param id
     */
    DemanderServiceDto getDemanderDetailById(Long id);

    /**
     * 增加协议配置
     *
     * @param demanderServiceDto
     */
    void addContConfig(DemanderServiceDto demanderServiceDto);

    /**
     * 获取协议配置
     *
     * @param id
     * @return
     */
    DemanderServiceDto getContConfigById(Long id);

    /**
     * 根据参数获取配置信息
     *
     * @return
     */
    DemanderServiceDto getContConfigByParams(DemanderService demanderService);

    /**
     * 模糊查询委托商
     *
     * @param demanderServiceFilter
     * @return
     * @author zgpi
     * @date 2020/6/3 10:07
     **/
    List<DemanderServiceDto> matchDemander(DemanderServiceFilter demanderServiceFilter);


    List<DemanderService> listDemanderService(Long service);
    /**
     * 根据委托商和服务商获取委托关系
     *
     * @param serviceCorp
     * @param demanderCorp
     */
    DemanderService findByDemanderAndService(Long serviceCorp, Long demanderCorp);

    /**
     * 根据委托商编号获取服务商企业编号与委托关系映射
     *
     * @param demanderCorp
     * @return
     */
    Map<Long, DemanderService> mapServiceAndDemanderService(Long demanderCorp);

    /**
     * 根据服务商编号获取委托商企业编号与委托关系映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, DemanderService> mapDemanderAndDemanderService(Long serviceCorp);

}
