package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.WareDepotDto;
import com.zjft.usp.wms.baseinfo.enums.DepotUserEnum;
import com.zjft.usp.wms.baseinfo.filter.WareDepotFilter;
import com.zjft.usp.wms.baseinfo.mapper.WareDepotMapper;
import com.zjft.usp.wms.baseinfo.model.WareDepot;
import com.zjft.usp.wms.baseinfo.model.WareDepotUser;
import com.zjft.usp.wms.baseinfo.service.WareDepotService;
import com.zjft.usp.wms.baseinfo.service.WareDepotUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * <p>
 * 库房表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-06
 */
@Service
public class WareDepotServiceImpl extends ServiceImpl<WareDepotMapper, WareDepot> implements WareDepotService {

    @Autowired
    WareDepotUserService wareDepotUserService;

    /**
     * 根据corpId获取库房编号和名称映射Map
     *
     * @author Qiugm
     * @date 2019-11-18
     * @param corpId
     * @return java.util.Map<java.lang.Long, java.lang.String>
     */
    @Override
    public Map<Long, String> mapDepotIdAndName(Long corpId) {
        Map<Long, String> depotIdAndNameMap = new HashMap<>(16);
        QueryWrapper<WareDepot> queryWrapper = new QueryWrapper<WareDepot>();
        queryWrapper.eq("corp_id", corpId);
        List<WareDepot> wareDepotList = this.list(queryWrapper);
        if (CollectionUtil.isNotEmpty(wareDepotList)) {
            for (WareDepot wareDepot : wareDepotList) {
                depotIdAndNameMap.put(wareDepot.getId(), wareDepot.getName());
            }
        }
        return depotIdAndNameMap;
    }

    /**
     * 物料库房列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareDepot>
     * @datetime 2019/11/19 9:55
     * @version
     * @author dcyu
     */
    @Override
    public List<WareDepot> listWareDepot() {
        return this.list();
    }

    /**
     * 物料库房结构型列表（树状）
     *
     * @param corpId
     * @datetime 2019/11/28 11:23
     * @version
     * @author dcyu
     * @return java.util.List<com.zjft.usp.wms.baseinfo.dto.WareDepotDto>
     */
    @Override
    public List<WareDepotDto> treeWareDepot(Long corpId) {
        Assert.notNull(corpId, "corpId 不能为 Null");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("corp_id", corpId);
        // TODO 考虑是否需要按照时间顺序排序
        List<WareDepot> wareDepotList = this.list(wrapper);
        List<WareDepotDto> wareDepotDtoList = new ArrayList<>();
        if(CollectionUtil.isNotEmpty(wareDepotList)){
            wareDepotList.forEach(wareDepot -> {
                WareDepotDto wareDepotDto = new WareDepotDto();
                BeanUtils.copyProperties(wareDepot, wareDepotDto);
                List<WareDepotUser> depotUserList =  wareDepotUserService.listWareDepotUser(wareDepot.getId(), corpId);
                // 用户名
                String userNames = "";
                if(CollectionUtil.isNotEmpty(depotUserList)){
                    Long[] users = new Long[depotUserList.size()];
                    for(int i = 0; i< depotUserList.size(); i++){
                        if(i != 0){
                            userNames += ",";
                        }
                        WareDepotUser depotUser = depotUserList.get(i);
                        userNames += depotUser.getUserName();
                        users[i] = depotUser.getUserId();
                    }
                    wareDepotDto.setUsers(users);
                }
                wareDepotDto.setUserNames(userNames);
                wareDepotDto.setWareDepotUserList(depotUserList);
                // 树状列表
                this.setWareDepotDtoTree(wareDepotDto, wareDepotDtoList);
            });
        }
        return wareDepotDtoList;
    }

    /**
      * 把库房对象放到结构列表对应位置（树状）
      * @datetime 2019/11/28 13:48
      * @version
      * @author dcyu
      * @param wareDepotDto
      * @param wareDepotDtoList
      * @return void
      */
    private void setWareDepotDtoTree(WareDepotDto wareDepotDto, List<WareDepotDto> wareDepotDtoList){

        // 有父
        if(LongUtil.isNotZero(wareDepotDto.getParentId())){
            wareDepotDtoList.forEach(depotDto -> {
                List<WareDepotDto> childrenList = depotDto.getChildren();
                // 找到 命中
                if(depotDto.getId().equals(wareDepotDto.getParentId())){
                    if(CollectionUtil.isEmpty(childrenList)){
                        childrenList = new ArrayList<>();
                    }
                    childrenList.add(wareDepotDto);
                    depotDto.setChildren(childrenList);
                }
                // 没找到
                else{
                    if(CollectionUtil.isNotEmpty(childrenList)){
                        this.setWareDepotDtoTree(wareDepotDto, childrenList);
                    }
                }
            });
        }
        // 不存在父 既自己就是父
        else{
            wareDepotDtoList.add(wareDepotDto);
        }
    }

    /**
     * 根据条件查找物料库房信息
     *
     * @param wareDepotFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareDepotDto>
     * @datetime 2019/11/20 14:58
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ListWrapper<WareDepotDto> queryWareDepot(WareDepotFilter wareDepotFilter) {
        ListWrapper<WareDepotDto> wrapperList = new ListWrapper<>();
        if(LongUtil.isZero(wareDepotFilter.getCorpId())){
            return wrapperList;
        }
        QueryWrapper wrapper = new QueryWrapper();
        if(StrUtil.isNotBlank(wareDepotFilter.getName())){
            wrapper.like("name", wareDepotFilter.getName());
        }
        Page page = new Page(wareDepotFilter.getPageNum(), wareDepotFilter.getPageSize());
        IPage<WareDepot> iPage = this.page(page, wrapper);
        if(iPage != null){
            if(iPage.getRecords() != null && iPage.getSize() > 0){
                List<WareDepotDto> dtoList = new ArrayList<>();
                WareDepotDto dto;
                List<WareDepotUser> depotUserList;
                Map<Long, String> mapDepotIdAndName = this.mapDepotIdAndName(wareDepotFilter.getCorpId());
                for(WareDepot depot : iPage.getRecords()){
                    dto = new WareDepotDto();
                    BeanUtils.copyProperties(depot, dto);
                    depotUserList = wareDepotUserService.listWareDepotUser(depot.getId(), wareDepotFilter.getCorpId());
                    // 用户名
                    String userNames = "";
                    if(CollectionUtil.isNotEmpty(depotUserList)){
                        for(int i=0; i<depotUserList.size(); i++){
                            if(i != 0){
                                userNames += ",";
                            }
                            userNames += depotUserList.get(i).getUserName();
                        }
                    }
                    dto.setUserNames(userNames);
                    // 上级库房名
                    String parentName = "";
                    if(CollectionUtil.isNotEmpty(mapDepotIdAndName)){
                        parentName = mapDepotIdAndName.get(depot.getParentId());
                    }
                    dto.setParentName(parentName);
                    dto.setWareDepotUserList(depotUserList);
                    dtoList.add(dto);
                }
                wrapperList.setList(dtoList);
            }
            wrapperList.setTotal(iPage.getTotal());
        }
        return wrapperList;
    }

    /**
     * 根据ID查找物料库房
     *
     * @param wareDepotId
     * @return com.zjft.usp.wms.baseinfo.dto.WareDepotDto
     * @datetime 2019/11/19 9:55
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public WareDepotDto findWareDepotBy(Long wareDepotId) {
        WareDepot depot = this.getById(wareDepotId);
        WareDepotDto dto = new WareDepotDto();
        BeanUtils.copyProperties(depot, dto);
        /* TODO 映射 */
        dto.setWareDepotUserList(wareDepotUserService.listWareDepotUser(wareDepotId));
        return dto;
    }

    /**
     * 新增物料库房信息
     *
     * @param wareDepotDto
     * @return void
     * @datetime 2019/11/19 9:55
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void insertWareDepot(WareDepotDto wareDepotDto) {
        Assert.notNull(wareDepotDto, "wareDepotDto 不能为空");
        if(LongUtil.isZero(wareDepotDto.getId())){
            wareDepotDto.setId(KeyUtil.getId());
        }
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareDepotDto.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "库房名称【%s】已存在", wareDepotDto.getName());

        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareDepotDto.getSortNo())){
            int sortNo = 0;
            Optional<WareDepot> optional = this.list().stream().max(Comparator.comparing(WareDepot::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareDepotDto.setSortNo(sortNo + 10);
        }
        this.save(wareDepotDto);
        wareDepotUserService.insertWareDepotUser(wareDepotDto.getId(), wareDepotDto.getUsers(), DepotUserEnum.MANAGER.getCode());
    }

    /**
     * 更新物料库房信息
     *
     * @param wareDepotDto
     * @return void
     * @datetime 2019/11/19 9:55
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWareDepot(WareDepotDto wareDepotDto) {
        Assert.notNull(wareDepotDto, "wareDepotDto");
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareDepotDto.getName());
        WareDepot isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareDepotDto.getId()), "库房名称【%s】已存在", wareDepotDto.getName());
        this.updateById(wareDepotDto);
        // 更新策略 先删除 再添加
        wareDepotUserService.deleteWareDepotUser(wareDepotDto.getId());
        wareDepotUserService.insertWareDepotUser(wareDepotDto.getId(), wareDepotDto.getUsers(), DepotUserEnum.MANAGER.getCode());
    }

    /**
     * 删除物料库房信息
     *
     * @param wareDepotId
     * @return void
     * @datetime 2019/11/19 9:55
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWareDepot(Long wareDepotId) {
        this.removeById(wareDepotId);
        wareDepotUserService.deleteWareDepotUser(wareDepotId);
    }
}
