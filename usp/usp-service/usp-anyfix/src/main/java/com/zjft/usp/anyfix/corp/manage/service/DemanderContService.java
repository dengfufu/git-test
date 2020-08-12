package com.zjft.usp.anyfix.corp.manage.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
public interface DemanderContService extends IService<DemanderCont> {

    void addDemanderCont(DemanderContDto demanderContDto, UserInfo userInfo, boolean isForAdd);

    void updateDemanderCont(DemanderContDto demanderContDto, UserInfo userInfo);

    void delete(Long id);

    ListWrapper<DemanderContDto> query(DemanderContFilter filter);

    DemanderContDto queryCont(DemanderContFilter filter);

    /**
     * 根据id查询Dto
     *
     * @param id
     * @return
     */
    DemanderContDto findDetail(Long id);

    /**
     * 根据委托商获取服务商与委托协议列表的映射
     *
     * @param demanderCorp
     * @return
     */
    Map<Long, List<DemanderContDto>> mapServiceAndContList(Long demanderCorp);

    /**
     * 根据服务商获取委托商与委托协议列表的映射
     *
     * @param serviceCorp
     * @return
     */
    Map<Long, List<DemanderContDto>> mapDemanderAndContList(Long serviceCorp);

}
