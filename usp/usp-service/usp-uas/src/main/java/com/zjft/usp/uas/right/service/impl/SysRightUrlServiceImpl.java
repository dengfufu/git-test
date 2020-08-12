package com.zjft.usp.uas.right.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.uas.right.dto.SysRightUrlDto;
import com.zjft.usp.uas.right.filter.SysRightUrlFilter;
import com.zjft.usp.uas.right.mapper.SysRightUrlMapper;
import com.zjft.usp.uas.right.model.SysRightUrl;
import com.zjft.usp.uas.right.service.SysRightService;
import com.zjft.usp.uas.right.service.SysRightUrlService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 权限映射表 服务实现类
 * </p>
 *
 * @author zgpi
 * @since 2019-12-12
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class SysRightUrlServiceImpl extends ServiceImpl<SysRightUrlMapper, SysRightUrl> implements SysRightUrlService {

    @Autowired
    private SysRightService sysRightService;

    /**
     * 分页查询系统权限
     *
     * @param sysRightUrlFilter
     * @return
     * @author zgpi
     * @date 2019/11/26 17:13
     **/
    @Override
    public ListWrapper<SysRightUrlDto> query(SysRightUrlFilter sysRightUrlFilter) {
        QueryWrapper<SysRightUrl> queryWrapper = new QueryWrapper<>();
        if (LongUtil.isNotZero(sysRightUrlFilter.getRightId())) {
            queryWrapper.eq("right_id", sysRightUrlFilter.getRightId());
        }
        if (StrUtil.isNotBlank(sysRightUrlFilter.getUri())) {
            queryWrapper.like("uri", StrUtil.trimToEmpty(sysRightUrlFilter.getUri()));
        }
        queryWrapper.orderByAsc("right_id");
        Page page = new Page(sysRightUrlFilter.getPageNum(), sysRightUrlFilter.getPageSize());
        IPage<SysRightUrl> sysAuthRightIPage = this.page(page, queryWrapper);
        List<SysRightUrl> sysRightUrlList = sysAuthRightIPage.getRecords();
        List<SysRightUrlDto> sysRightUrlDtoList = new ArrayList<>();
        SysRightUrlDto sysRightUrlDto;
        if (CollectionUtil.isNotEmpty(sysRightUrlList)) {
            List<Long> rightIdList = sysRightUrlList.stream()
                    .map(e -> e.getRightId()).collect(Collectors.toList());
            Map<Long, String> rightMap = sysRightService.mapIdAndName(rightIdList);
            for (SysRightUrl sysRightUrl : sysRightUrlList) {
                sysRightUrlDto = new SysRightUrlDto();
                BeanUtils.copyProperties(sysRightUrl, sysRightUrlDto);
                sysRightUrlDto.setRightName(StrUtil.trimToEmpty(rightMap.get(sysRightUrl.getRightId())));
                sysRightUrlDtoList.add(sysRightUrlDto);
            }
        }
        return ListWrapper.<SysRightUrlDto>builder().list(sysRightUrlDtoList).total(sysAuthRightIPage.getTotal()).build();

    }

    /**
     * 公共权限列表
     *
     * @return
     * @author zgpi
     * @date 2019/12/2 10:05
     **/
    @Override
    public List<SysRightUrl> listCommon() {
        return this.list(new QueryWrapper<SysRightUrl>().eq("right_type", 1));
    }

    /**
     * 添加系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    @Override
    public void addSysRightUrl(SysRightUrl sysRightUrl) {
        SysRightUrl entity = this.getById(sysRightUrl.getRightId());
        if (entity != null) {
            throw new AppException("权限已存在！");
        }
        sysRightUrl.setId(KeyUtil.getId());
        this.save(sysRightUrl);
    }

    /**
     * 修改系统权限
     *
     * @param sysRightUrl
     * @return
     * @author zgpi
     * @date 2019/11/26 18:31
     **/
    @Override
    public void updateSysRightUrl(SysRightUrl sysRightUrl) {
        this.updateById(sysRightUrl);
    }

    /**
     * 删除鉴权
     *
     * @param id
     * @return
     * @author zgpi
     * @date 2019/11/26 18:32
     **/
    @Override
    public void delSysRightUrl(Long id) {
        this.removeById(id);
    }

    /**
     * 权限url与权限列表映射
     *
     * @return
     * @author zgpi
     * @date 2019/12/3 09:16
     **/
    @Override
    public List<SysRightUrl> listAuthRight() {
        return this.list();
    }

    /**
     * 根据url获得权限列表
     *
     * @param url
     * @return
     * @author zgpi
     * @date 2019/12/26 16:25
     **/
    @Override
    public List<SysRightUrl> listByUrl(String url) {
        return this.list(new QueryWrapper<SysRightUrl>().eq("uri", url));
    }

}
