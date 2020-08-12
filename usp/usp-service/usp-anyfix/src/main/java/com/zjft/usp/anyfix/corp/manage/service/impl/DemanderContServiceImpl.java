package com.zjft.usp.anyfix.corp.manage.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.anyfix.corp.manage.dto.DemanderContDto;
import com.zjft.usp.anyfix.corp.manage.filter.DemanderContFilter;
import com.zjft.usp.anyfix.corp.manage.mapper.DemanderContMapper;
import com.zjft.usp.anyfix.corp.manage.model.DemanderCont;
import com.zjft.usp.anyfix.corp.manage.model.DemanderService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderContService;
import com.zjft.usp.anyfix.corp.manage.service.DemanderServiceService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.ListWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.common.utils.LongUtil;
import com.zjft.usp.file.service.FileFeignService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author zrlin
 * @since 2020-06-29
 */
@Service
public class DemanderContServiceImpl extends ServiceImpl<DemanderContMapper, DemanderCont> implements DemanderContService {

    @Autowired
    private DemanderServiceService demanderServiceService;

    @Resource
    private FileFeignService fileFeignService;

    /**
     * 添加委托协议
     * @param demanderContDto
     * @param userInfo
     */
    @Override
    public void addDemanderCont(DemanderContDto demanderContDto, UserInfo userInfo, boolean isForAdd) {
        // 进行校验判断
        if(!isForAdd && LongUtil.isZero(demanderContDto.getId())) {
            throw new AppException("协议编号不能为空");
        }
        if( demanderContDto == null) {
            throw new AppException("委托协议不能为空");
        }
        if(LongUtil.isZero(demanderContDto.getRefId())) {
            throw new AppException("委托商关系编号不能为空");
        }
        if( demanderContDto.getStartDate() == null) {
            throw new AppException("协议起始日期不能为空");
        }
        if( demanderContDto.getEndDate() == null) {
            throw new AppException("协议结束日期不能为空");
        }
        if(demanderContDto.getEndDate().before(demanderContDto.getStartDate())) {
            throw new AppException("起始日期不能大于结束日期");
        }
        this.validateStartEndDate(demanderContDto);
        String fileIds = "";
        if(CollectionUtil.isNotEmpty(demanderContDto.getFeeRuleFileList())) {
            fileIds = StrUtil.join("," , demanderContDto.getFeeRuleFileList());
        }
        String serviceFileIds = "";
        if(CollectionUtil.isNotEmpty(demanderContDto.getServiceStandardFileList())) {
            serviceFileIds = StrUtil.join("," , demanderContDto.getServiceStandardFileList());
        }
        // 进行赋值保存到数据库
        DemanderCont demanderCont = new DemanderCont();
        BeanUtils.copyProperties(demanderContDto, demanderCont);
        demanderCont.setFeeRuleFiles(fileIds);
        demanderCont.setServiceStandardFiles(serviceFileIds);
        demanderCont.setOperator(userInfo.getUserId());
        demanderCont.setOperateTime(DateUtil.date().toTimestamp());
        if( isForAdd) {
            demanderCont.setId(KeyUtil.getId());
            this.save(demanderCont);
        } else {
            this.updateById(demanderCont);
        }
        // 删除临时文件表数据
        if (CollectionUtil.isNotEmpty(demanderContDto.getNewFileIdList())) {
            this.fileFeignService.deleteFileTemporaryByFileIdList(demanderContDto.getNewFileIdList());
        }
        //删除选择的文件
        if (CollectionUtil.isNotEmpty(demanderContDto.getDeleteFileIdList())) {
            this.fileFeignService.deleteFileList(demanderContDto.getDeleteFileIdList());
        }
    }

    @Override
    public void updateDemanderCont(DemanderContDto demanderContDto, UserInfo userInfo) {
        this.addDemanderCont(demanderContDto,userInfo, false);
    }

    @Override
    public void delete(Long id) {
        if(LongUtil.isZero(id)) {
            throw new AppException("删除委托协议编号不能为空");
        }
        this.removeById(id);
    }

    @Override
    public ListWrapper<DemanderContDto> query(DemanderContFilter filter) {
        Page page = new Page(filter.getPageNum(), filter.getPageSize());

        IPage dataPage = this.page(page, new QueryWrapper<DemanderCont>().eq("ref_id", filter.getRefId()));
        List<DemanderCont> list = dataPage.getRecords();
        if(CollectionUtil.isEmpty(list)) {
            return null;
        }
        List<DemanderContDto> demanderContDtos = new ArrayList<>();
        DemanderContDto demanderContDto ;
        for(DemanderCont demanderCont : list) {
            demanderContDto = new DemanderContDto();
            BeanUtils.copyProperties(demanderCont,demanderContDto);
            if (StrUtil.isNotEmpty(demanderCont.getFeeRuleFiles())) {
                List<Long> feeRuleFileList = Arrays.asList(demanderCont.getFeeRuleFiles().split(",")).stream()
                        .map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                demanderContDto.setFeeRuleFileList(feeRuleFileList);
            }
            if (StrUtil.isNotEmpty(demanderCont.getServiceStandardFiles())) {
                List<Long> serviceImageList = Arrays.asList(demanderCont.getServiceStandardFiles().split(",")).stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
                demanderContDto.setServiceStandardFileList(serviceImageList);
            }
            demanderContDtos.add(demanderContDto);
        }
        return ListWrapper.<DemanderContDto>builder()
                .list(demanderContDtos)
                .total(page.getTotal())
                .build();
    }

    @Override
    public DemanderContDto queryCont(DemanderContFilter filter) {
        if(LongUtil.isZero(filter.getDemanderCorp())) {
            throw new AppException("委托商不能为空");
        }
        if(LongUtil.isZero(filter.getServiceCorp())) {
            throw new AppException("服务商不能为空");
        }
        if(filter.getDispatchTime() == null &&
                (filter.getStartDate() == null || filter.getEndDate() == null)) {
            throw new AppException("提单日期或周期不能为空");
        }
        if (filter.getDispatchTime() != null) {
            filter.setDispatchTime(this.removeClock(filter.getDispatchTime()));
        }
        DemanderContDto demanderContDto = this.baseMapper.selectServiceCont(filter);
        if( demanderContDto != null) {
            this.appendDemanderContDto(demanderContDto);
        }
        return demanderContDto;
    }

    /**
     * 根据id查询Dto
     *
     * @param id
     * @return
     */
    @Override
    public DemanderContDto findDetail(Long id) {
        DemanderContDto dto = new DemanderContDto();
        if (LongUtil.isZero(id)) {
            throw new AppException("合同编号不能为空");
        }
        DemanderCont demanderCont = this.getById(id);
        if (demanderCont == null) {
            return null;
        }
        DemanderService demanderService = this.demanderServiceService.getById(demanderCont.getRefId());
        if (demanderService != null) {
            BeanUtils.copyProperties(demanderService, dto);
        }
        BeanUtils.copyProperties(demanderCont, dto);
        return dto;
    }


    public void validateStartEndDate(DemanderContDto demanderContDto) {
        QueryWrapper<DemanderCont> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("cont_no");

        if(LongUtil.isNotZero(demanderContDto.getId())) {
            queryWrapper.ne("id", demanderContDto.getId());
        }
        queryWrapper.eq("ref_id", demanderContDto.getRefId());
        queryWrapper.and( innerWrapper -> innerWrapper.eq("cont_no", demanderContDto.getContNo())
                .or(wrapper -> wrapper
                        .le("start_date", removeClock(demanderContDto.getEndDate()))
                        .ge("end_date",  removeClock(demanderContDto.getStartDate())))
                );

        List<DemanderCont> demanderConts = this.list(queryWrapper);
        if(CollectionUtil.isNotEmpty(demanderConts)) {
            for(DemanderCont demanderCont :demanderConts) {
                if(demanderCont.getContNo().equals(demanderContDto.getContNo())) {
                    throw new AppException("委托协议号重复，请修改！");
                }
            }
            throw new AppException("该协议的有效期范围与已有的协议重叠，请修改！");
        }
    }


    public Timestamp removeClock(Timestamp timestamp) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(timestamp);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        return new Timestamp(calendar.getTime().getTime());
    }

    public DemanderContDto appendDemanderContDto(DemanderContDto demanderContDto) {
        if (StrUtil.isNotBlank(demanderContDto.getFeeRuleFiles())) {
            List<Long> list = Arrays.asList(demanderContDto.getFeeRuleFiles().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            demanderContDto.setFeeRuleFileList(list);
        }
        if (StrUtil.isNotBlank(demanderContDto.getServiceStandardFiles())) {
            List<Long> list = Arrays.asList(demanderContDto.getServiceStandardFiles().split(","))
                    .stream().map(s -> Long.parseLong(s.trim())).collect(Collectors.toList());
            demanderContDto.setServiceStandardFileList(list);
        }
        return demanderContDto;
    }

    /**
     * 根据委托商获取服务商与委托协议列表的映射
     *
     * @param demanderCorp
     * @return
     */
    @Override
    public Map<Long, List<DemanderContDto>> mapServiceAndContList(Long demanderCorp) {
        Map<Long, List<DemanderContDto>> map = new HashMap<>();
        if (LongUtil.isZero(demanderCorp)) {
            return map;
        }
        DemanderContFilter filter = new DemanderContFilter();
        filter.setDemanderCorp(demanderCorp);
        List<DemanderContDto> list = this.baseMapper.listByFilter(filter);
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(DemanderContDto::getServiceCorp));
        }
        return map;
    }

    /**
     * 根据服务商获取委托商与委托协议列表的映射
     *
     * @param serviceCorp
     * @return
     */
    @Override
    public Map<Long, List<DemanderContDto>> mapDemanderAndContList(Long serviceCorp) {
        Map<Long, List<DemanderContDto>> map = new HashMap<>();
        if (LongUtil.isZero(serviceCorp)) {
            return map;
        }
        DemanderContFilter filter = new DemanderContFilter();
        filter.setServiceCorp(serviceCorp);
        List<DemanderContDto> list = this.baseMapper.listByFilter(filter);
        if (CollectionUtil.isNotEmpty(list)) {
            map = list.stream().collect(Collectors.groupingBy(DemanderContDto::getDemanderCorp));
        }
        return map;
    }

}
