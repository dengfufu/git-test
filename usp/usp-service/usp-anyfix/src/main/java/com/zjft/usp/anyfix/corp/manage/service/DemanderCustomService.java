package com.zjft.usp.anyfix.corp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDetailDto;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderCustomDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderCustomFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCustom;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.vo.ReqParam;
import com.zjft.usp.uas.dto.CorpDto;

import java.util.List;
import java.util.Map;

/**
 * <p>
 * 服务委托方与用户企业关系表 服务类
 * </p>
 *
 * @author canlei
 * @since 2019-10-23
 */
public interface DemanderCustomService extends IService<DemanderCustom> {

    /**
     * 根据供应商查询客户列表
     * @param demanderCustomFilter
     * @return
     */
    List<DemanderCustomDto> listCustomByDemander(DemanderCustomFilter demanderCustomFilter);

    /**
     * 根据委托商获得客户下拉列表
     * @param demanderCorp
     * @param curCorpId
     * @return
     */
    List<DemanderCustom> selectCustomByDemander(Long demanderCorp, Long curCorpId);

    /**
     * 根据客户查询供应商列表
     * @param demanderCustomFilter
     * @return
     */
    List<DemanderCustomDto> listDemanderByCustom(DemanderCustomFilter demanderCustomFilter);

    /**
     * 根据服务委托方列出所有用户企业ID
     * @param demanderCorp
     * @return
     */
    List<Long> listCustomCorpIdByDemander(Long demanderCorp);

    /**
     * 分页查询用户企业
     * @param demanderCustomFilter
     * @param reqParam
     * @return
     */
    ListWrapper<DemanderCustomDto> pageByFilter(DemanderCustomFilter demanderCustomFilter, ReqParam reqParam);

    /**
     * 服务委托方添加用户企业
     * @param demanderCustomDto
     * @param curUserId
     */
    Long addDemanderCustom(DemanderCustomDto demanderCustomDto, Long curUserId);

    /**
     * 服务委托方更新服务商
     * @param demanderCustomDto
     * @param curUserId
     */
    void updateDemanderCustom(DemanderCustomDto demanderCustomDto, Long curUserId);

    /**
     * 根据用户企业编号查询其相关企业编号列表
     * @param customCorp
     * @return
     */
    List<Long> listRelatedCorpIdsByCustom(Long customCorp);

    /**
     * 根据服务委托方或者服务商查询所有客户
     * @param corpId
     * @return
     */
    List<DemanderCustom> listCustomByCorpId(Long corpId);

    /**
     * 根据委托商或服务商查询客户名称，并包括自己
     * @param demanderCustomFilter
     * @return
     */
    List<DemanderCustomDto> matchCustomByCorpForBranch(DemanderCustomFilter demanderCustomFilter);


    /**
     * 根据主键编号列表获取主键和客户名称映射
     * @param customIdList
     * @return
     */
    Map<Long, String> mapIdAndCustomNameByIdList(List<Long> customIdList);

    /**
     * 根据主键获得详情
     *
     * @param customId
     * @return
     * @author zgpi
     * @date 2019/10/31 18:31
     **/
    DemanderCustomDto findDtoById(Long customId);

    /**
     * 根据客户企业编号列出所有主键
     * @param customCorp
     * @return
     */
    List<Long> listIdsByCustomCorp(Long customCorp);

    /**
     * 查询客户详情
     * @param customId
     * @return
     */
    DemanderCustomDetailDto getDetailDtoByIds(Long customId);

    /**
     * 模糊匹配可选的企业
     * @param demanderCustomFilter
     * @return
     */
    List<CorpDto> listAvailableCorp (DemanderCustomFilter demanderCustomFilter);

    /**
     * 根据委托商获取客户编号和名称的映射
     *
     * @param demanderCorp
     * @return
     */
    Map<Long, String> mapCustomIdAndNameByDemander(Long demanderCorp);

    /**
     * 批量增加客户企业
     * @param customCorpNameList
     * @param demanderCorp
     * @param curUserId
     * @return
     */
    public Map<String,Map<String,Long>> batchAddDemanderCustom(List<String> customCorpNameList, Long demanderCorp, Long curUserId);
}
