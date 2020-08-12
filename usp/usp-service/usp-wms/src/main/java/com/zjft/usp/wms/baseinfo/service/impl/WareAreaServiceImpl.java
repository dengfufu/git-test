package com.zjft.usp.wms.baseinfo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.Assert;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.IntUtil;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.wms.baseinfo.dto.WareAreaDto;
import com.zjft.usp.wms.baseinfo.enums.AreaUserEnum;
import com.zjft.usp.wms.baseinfo.filter.WareAreaFilter;
import com.zjft.usp.wms.baseinfo.model.WareArea;
import com.zjft.usp.wms.baseinfo.mapper.WareAreaMapper;
import com.zjft.usp.wms.baseinfo.model.WareAreaUser;
import com.zjft.usp.wms.baseinfo.service.WareAreaService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.baseinfo.service.WareAreaUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * 供应商表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-07
 */
@Service
public class WareAreaServiceImpl extends ServiceImpl<WareAreaMapper, WareArea> implements WareAreaService {

    @Autowired
    WareAreaUserService wareAreaUserService;

    /**
     * 区域信息列表
     *
     * @return java.util.List<com.zjft.usp.wms.baseinfo.model.WareArea>
     * @datetime 2019/11/18 15:56
     * @version
     * @author dcyu
     */
    @Override
    public List<WareArea> listWareArea() {
        WareArea wareArea = new WareArea();
        return wareArea.selectAll();
    }

    /**
     * 根据条件查找区域信息
     *
     * @param wareAreaFilter
     * @return com.zjft.usp.common.model.ListWrapper<com.zjft.usp.wms.baseinfo.dto.WareAreaDto>
     * @datetime 2019/11/20 10:36
     * @version
     * @author dcyu
     */
    @Override
    public ListWrapper<WareAreaDto> queryWareArea(WareAreaFilter wareAreaFilter) {
        ListWrapper<WareAreaDto> wareAreaList = new ListWrapper<>();
        if(LongUtil.isZero(wareAreaFilter.getCorpId())){
            return wareAreaList;
        }
        QueryWrapper<WareArea> wrapper = new QueryWrapper<>();
        wrapper.eq("corp_id", wareAreaFilter.getCorpId());
        if(StrUtil.isNotBlank(wareAreaFilter.getName())){
            wrapper.like("name", wareAreaFilter.getName());
        }
        Page page = new Page(wareAreaFilter.getPageNum(), wareAreaFilter.getPageSize());
        IPage<WareArea> iPage = this.page(page, wrapper);
        List<WareAreaDto> dtoList = new ArrayList<>();
        WareAreaDto dto;
        List<WareAreaUser> userList;
        if(iPage != null){
            if(iPage.getRecords() != null && iPage.getSize() > 0){
                for(WareArea area : iPage.getRecords()){
                    dto = new WareAreaDto();
                    BeanUtils.copyProperties(area, dto);
                    userList = wareAreaUserService.listWareAreaUser(area.getId(),wareAreaFilter.getCorpId());
                    String userNames = "";
                    if(userList != null && userList.size() > 0){
                        for(int i=0; i<userList.size(); i++){
                            if(i != 0){
                                userNames += ",";
                            }
                            userNames += userList.get(i).getUserName();
                        }
                    }
                    dto.setUserNames(userNames);
                    dtoList.add(dto);
                }
                wareAreaList.setList(dtoList);
            }
            wareAreaList.setTotal(iPage.getTotal());
        }
        return wareAreaList;
    }

    /**
     * 根据ID查找区域信息
     *
     * @param id
     * @return com.zjft.usp.wms.baseinfo.model.WareArea
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     */
    @Override
    public WareArea findWareAreaBy(Long id) {
        WareArea wareArea = new WareArea();
        wareArea.setId(id);
        return wareArea.selectById();
    }

    /**
     * 新增区域信息
     *
     * @param wareAreaDto
     * @return void
     * @datetime 2019/11/18 16:28
     * @version
     * @author dcyu
     */
    @Override
    public void insertWareArea(WareAreaDto wareAreaDto) {
        // 查找是否已存在区域名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareAreaDto.getName());
        Assert.isFalse(this.getOne(wrapper) != null, "区域名称【%s】已存在", wareAreaDto.getName());
        WareArea wareArea = new WareArea();
        BeanUtils.copyProperties(wareAreaDto, wareArea);
        wareArea.setId(KeyUtil.getId());
        // 插入顺序号 生成策略： 最大值 增10
        if(IntUtil.isZero(wareAreaDto.getSortNo())){
            int sortNo = 0;
            Optional<WareArea> optional = this.list().stream().max(Comparator.comparing(WareArea::getSortNo));
            if(optional.isPresent()){
                sortNo = optional.get().getSortNo();
            }
            wareArea.setSortNo(sortNo + 10);
        }
        this.save(wareArea);
        // 插入库房负责人
        wareAreaUserService.insertWareAreaUser(wareAreaDto.getId(), wareAreaDto.getUsers(), AreaUserEnum.MANAGER.getCode());
    }

    /**
     * 更新区域信息
     *
     * @param wareAreaDto
     * @return void
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateWareArea(WareAreaDto wareAreaDto) {
        // 查找是否已存在区域名
        QueryWrapper wrapper = new QueryWrapper();
        wrapper.eq("name", wareAreaDto.getName());
        WareArea isExist = this.getOne(wrapper);
        Assert.isFalse(isExist != null && !isExist.getId().equals(wareAreaDto.getId()), "区域名称【%s】已存在", wareAreaDto.getName());
        WareArea wareArea = new WareArea();
        BeanUtils.copyProperties(wareAreaDto, wareArea);
        wareArea.updateById();
        // 更新策略 先删除再添加
        wareAreaUserService.deleteWareAreaUserBy(wareAreaDto.getId());
        wareAreaUserService.insertWareAreaUser(wareAreaDto.getId(), wareAreaDto.getUsers(), AreaUserEnum.MANAGER.getCode());
    }

    /**
     * 删除区域信息
     *
     * @param id
     * @return void
     * @datetime 2019/11/18 16:16
     * @version
     * @author dcyu
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteWareArea(Long id) {
        Assert.notNull(id, "areaId 不能为空");
        this.removeById(id);
        wareAreaUserService.deleteWareAreaUserBy(id);
    }
}
