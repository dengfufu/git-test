package com.zjft.usp.anyfix.corp.branch.composite.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjft.usp.anyfix.corp.branch.composite.ServiceBranchCompoService;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchDto;
import com.zjft.usp.anyfix.corp.branch.dto.ServiceBranchTreeDto;
import com.zjft.usp.anyfix.corp.branch.filter.ServiceBranchFilter;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranch;
import com.zjft.usp.anyfix.corp.branch.model.ServiceBranchUpper;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchService;
import com.zjft.usp.anyfix.corp.branch.service.ServiceBranchUpperService;
import com.zjft.usp.anyfix.corp.user.model.ServiceBranchUser;
import com.zjft.usp.anyfix.corp.user.service.ServiceBranchUserService;
import com.zjft.usp.common.exception.AppException;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.LongUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 网点聚合服务实现类
 *
 * @author zgpi
 * @version 1.0
 * @date 2019/12/10 09:20
 */
@Service
@Transactional(rollbackFor = Exception.class)
public class ServiceBranchCompoServiceImpl implements ServiceBranchCompoService {

    @Autowired
    private ServiceBranchService serviceBranchService;
    @Autowired
    private ServiceBranchUpperService serviceBranchUpperService;
    @Autowired
    private ServiceBranchUserService serviceBranchUserService;

    /**
     * 添加服务网点
     *
     * @param serviceBranchDto
     * @return
     * @author zgpi
     * @date 2020/3/10 20:15
     */
    @Override
    public void addServiceBranch(ServiceBranchDto serviceBranchDto, UserInfo userInfo) {
        serviceBranchDto.setOperator(userInfo.getUserId());
        Long branchId = serviceBranchService.addBranch(serviceBranchDto);
        Long upperBranchId = serviceBranchDto.getUpperBranchId();
        if (LongUtil.isNotZero(upperBranchId)) {
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService
                    .list(new QueryWrapper<ServiceBranchUpper>().eq("branch_id", upperBranchId));
            List<Long> upperBranchIdList = serviceBranchUpperList.stream()
                    .map(e -> e.getUpperBranchId()).collect(Collectors.toList());
            upperBranchIdList.add(upperBranchId);
            upperBranchIdList = upperBranchIdList.stream().distinct().collect(Collectors.toList());
            List<ServiceBranchUpper> upperList = new ArrayList<>();
            ServiceBranchUpper serviceBranchUpper;
            for (Long upperId : upperBranchIdList) {
                serviceBranchUpper = new ServiceBranchUpper();
                serviceBranchUpper.setBranchId(branchId);
                serviceBranchUpper.setUpperBranchId(upperId);
                upperList.add(serviceBranchUpper);
            }
            serviceBranchUpperService.saveBatch(upperList);
        }
        if (LongUtil.isNotZero(serviceBranchDto.getContactId())) {
            ServiceBranchUser serviceBranchUser = new ServiceBranchUser();
            serviceBranchUser.setUserId(serviceBranchDto.getContactId());
            serviceBranchUser.setBranchId(branchId);
            serviceBranchUser.setAddTime(DateUtil.date());
            serviceBranchUserService.save(serviceBranchUser);
        }
    }

    /**
     * 修改服务网点
     *
     * @param serviceBranchDto
     * @param userInfo
     * @return
     * @author zgpi
     * @date 2020/3/10 20:15
     */
    @Override
    public void updateServiceBranch(ServiceBranchDto serviceBranchDto, UserInfo userInfo) {
        /** 不允许自己引用自己，也不允许直接交叉引用，间接交叉引用 **/
        if (LongUtil.isNotZero(serviceBranchDto.getBranchId()) && LongUtil.isNotZero(serviceBranchDto.getUpperBranchId())) {
            if (serviceBranchDto.getBranchId().compareTo(serviceBranchDto.getUpperBranchId()) == 0) {
                throw new AppException("上级网点不能选择自己，请重新选择！");
            }
            QueryWrapper<ServiceBranch> queryWrapper = new QueryWrapper();
            queryWrapper.eq("service_corp", serviceBranchDto.getServiceCorp());
            queryWrapper.eq("branch_id", serviceBranchDto.getUpperBranchId());
            queryWrapper.eq("upper_branch_id", serviceBranchDto.getBranchId());
            List<ServiceBranch> list = serviceBranchService.list(queryWrapper);
            if (CollectionUtil.isNotEmpty(list)) {
                throw new AppException("上级网点不允许相互直接引用！");
            }

            QueryWrapper<ServiceBranchUpper> upperQueryWrapper = new QueryWrapper();
            queryWrapper.eq("branch_id", serviceBranchDto.getBranchId());
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService.list(upperQueryWrapper);
            List<Long> upperBranchIdList = serviceBranchUpperList.stream()
                    .map(e -> e.getUpperBranchId()).collect(Collectors.toList());

            if (CollectionUtil.isNotEmpty(upperBranchIdList) && upperBranchIdList.contains(serviceBranchDto.getBranchId())) {
                throw new AppException("上级网点不允许相互间接引用！");
            }
        }
        if (LongUtil.isZero(serviceBranchDto.getUpperBranchId())) {
            serviceBranchDto.setUpperBranchId(0L);
        }
        serviceBranchDto.setOperator(userInfo.getUserId());
        serviceBranchService.updateBranch(serviceBranchDto);

        serviceBranchUpperService.remove(new UpdateWrapper<ServiceBranchUpper>()
                .eq("branch_id", serviceBranchDto.getBranchId()));
        Long upperBranchId = serviceBranchDto.getUpperBranchId();
        if (LongUtil.isNotZero(upperBranchId)) {
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService
                    .list(new QueryWrapper<ServiceBranchUpper>().eq("branch_id", upperBranchId));
            List<Long> upperBranchIdList = serviceBranchUpperList.stream()
                    .map(e -> e.getUpperBranchId()).collect(Collectors.toList());
            upperBranchIdList.add(upperBranchId);
            upperBranchIdList = upperBranchIdList.stream().distinct().collect(Collectors.toList());

            List<ServiceBranchUpper> upperList = new ArrayList<>();
            ServiceBranchUpper serviceBranchUpper;
            for (Long upperId : upperBranchIdList) {
                serviceBranchUpper = new ServiceBranchUpper();
                serviceBranchUpper.setBranchId(serviceBranchDto.getBranchId());
                serviceBranchUpper.setUpperBranchId(upperId);
                upperList.add(serviceBranchUpper);
            }
            serviceBranchUpperService.saveBatch(upperList);
        }

        Long userId = serviceBranchDto.getContactId();
        if (LongUtil.isNotZero(userId)) {
            List<Long> userIdList = serviceBranchUserService.listUserIdsByBranchId(serviceBranchDto.getBranchId());
            if (CollectionUtil.isNotEmpty(userIdList) && userIdList.contains(userId)) {
                return;
            } else {
                ServiceBranchUser serviceBranchUser = new ServiceBranchUser();
                serviceBranchUser.setUserId(userId);
                serviceBranchUser.setBranchId(serviceBranchDto.getBranchId());
                serviceBranchUser.setAddTime(DateUtil.date());
                serviceBranchUserService.save(serviceBranchUser);
            }
        }
    }

    /**
     * 删除服务网点
     *
     * @param branchId
     * @return
     * @author zgpi
     * @date 2020/3/10 21:23
     */
    @Override
    public void delServiceBranch(Long branchId) {
        serviceBranchService.removeById(branchId);
        serviceBranchUserService.remove(new UpdateWrapper<ServiceBranchUser>().eq("branch_id", branchId));
    }

    /**
     * 服务网点树
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:38
     */
    @Override
    public List<ServiceBranchTreeDto> listServiceBranchTree(ServiceBranchFilter serviceBranchFilter) {
        List<ServiceBranch> serviceBranchList = serviceBranchService.listServiceBranch(serviceBranchFilter);
        return this.transferTreeList(serviceBranchList);
    }

    /**
     * 上级服务网点树
     *
     * @param serviceBranchFilter
     * @return
     * @author zgpi
     * @date 2020/3/9 14:38
     */
    @Override
    public List<ServiceBranchTreeDto> listAllUpperServiceBranchTree(ServiceBranchFilter serviceBranchFilter) {
        List<ServiceBranch> upperServiceBranchList = serviceBranchService.listAllUpperServiceBranch(serviceBranchFilter.getBranchId());
        List<ServiceBranch> serviceBranchList = serviceBranchService.listServiceBranch(serviceBranchFilter);
        return this.transferTreeList(upperServiceBranchList, serviceBranchList);
    }

    /**
     * 获得用人员所在服务网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:44
     */
    @Override
    public List<Long> listOwnBranchId(Long userId, Long corpId) {
        List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                .eq("service_corp", corpId));
        List<Long> branchIdList = serviceBranchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        List<Long> list = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserService.list(
                    new QueryWrapper<ServiceBranchUser>().in("branch_id", branchIdList)
                            .eq("user_id", userId));
            list = serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        return list.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获得人员所在服务网点的下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:45
     */
    @Override
    public List<Long> listOwnLowerBranchId(Long userId, Long corpId) {
        List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                .eq("service_corp", corpId));
        List<Long> branchIdList = serviceBranchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        List<Long> userBranchIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserService.list(
                    new QueryWrapper<ServiceBranchUser>().in("branch_id", branchIdList)
                            .eq("user_id", userId));
            userBranchIdList = serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        List<Long> lowerIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userBranchIdList)) {
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService.list(
                    new QueryWrapper<ServiceBranchUpper>().in("upper_branch_id", userBranchIdList));
            lowerIdList = serviceBranchUpperList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        return lowerIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获得人员所在服务网点及下级网点编号列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/3/13 17:45
     */
    @Override
    public List<Long> listOwnAndLowerBranchId(Long userId, Long corpId) {
        List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                .eq("service_corp", corpId));
        List<Long> branchIdList = serviceBranchList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        List<Long> userBranchIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserService.list(
                    new QueryWrapper<ServiceBranchUser>().in("branch_id", branchIdList)
                            .eq("user_id", userId));
            userBranchIdList = serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        List<Long> lowerIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(userBranchIdList)) {
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService.list(
                    new QueryWrapper<ServiceBranchUpper>().in("upper_branch_id", userBranchIdList));
            lowerIdList = serviceBranchUpperList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        userBranchIdList.addAll(lowerIdList);
        return userBranchIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获得所有下级网点编号列表
     *
     * @param branchIdList
     * @return
     * @author zgpi
     * @date 2020/6/5 09:28
     **/
    @Override
    public List<Long> listLowerBranchId(List<Long> branchIdList) {
        List<Long> lowerIdList = new ArrayList<>();
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranchUpper> serviceBranchUpperList = serviceBranchUpperService.list(
                    new QueryWrapper<ServiceBranchUpper>().in("upper_branch_id", branchIdList));
            lowerIdList = serviceBranchUpperList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        }
        return lowerIdList.stream().distinct().collect(Collectors.toList());
    }

    /**
     * 获得用人员所在服务网点列表
     *
     * @param userId
     * @param corpId
     * @return
     * @author zgpi
     * @date 2020/5/13 09:11
     **/
    @Override
    public List<ServiceBranch> listOwnBranch(Long userId, Long corpId) {
        List<ServiceBranchUser> serviceBranchUserList = serviceBranchUserService.list(
                new QueryWrapper<ServiceBranchUser>().eq("user_id", userId));
        List<Long> branchIdList = serviceBranchUserList.stream().map(e -> e.getBranchId()).collect(Collectors.toList());
        if (CollectionUtil.isNotEmpty(branchIdList)) {
            List<ServiceBranch> serviceBranchList = serviceBranchService.list(new QueryWrapper<ServiceBranch>()
                    .eq("service_corp", corpId)
                    .in("branch_id", branchIdList));
            return serviceBranchList;
        }
        return new ArrayList<>();
    }

    /**
     * 组装树状结构列表
     *
     * @param serviceBranchList
     * @return
     * @author zgpi
     * @date 2020/3/9 16:07
     **/
    private List<ServiceBranchTreeDto> transferTreeList(List<ServiceBranch> serviceBranchList) {
        List<ServiceBranchTreeDto> serviceBranchTreeDtoList = new ArrayList<>();
        ServiceBranchTreeDto serviceBranchTreeDto;
        // 一级菜单
        for (ServiceBranch serviceBranch : serviceBranchList) {
            if (LongUtil.isZero(serviceBranch.getUpperBranchId())) {
                serviceBranchTreeDto = new ServiceBranchTreeDto();
                serviceBranchTreeDto.setKey(serviceBranch.getBranchId());
                serviceBranchTreeDto.setTitle(serviceBranch.getBranchName());
                serviceBranchTreeDto.setIsLeaf(true);
                serviceBranchTreeDtoList.add(serviceBranchTreeDto);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (ServiceBranchTreeDto tree : serviceBranchTreeDtoList) {
            tree.setChildren(getChild(tree, serviceBranchList));
        }
        return serviceBranchTreeDtoList;
    }

    /**
     * 组装树状结构列表
     *
     * @param firstLevelList
     * @param serviceBranchList
     * @return
     * @author zgpi
     * @date 2020/3/9 16:07
     **/
    private List<ServiceBranchTreeDto> transferTreeList(List<ServiceBranch> firstLevelList,
                                                        List<ServiceBranch> serviceBranchList) {
        List<ServiceBranchTreeDto> serviceBranchTreeDtoList = new ArrayList<>();
        ServiceBranchTreeDto serviceBranchTreeDto;
        // 一级菜单
        for (ServiceBranch serviceBranch : firstLevelList) {
            if (LongUtil.isZero(serviceBranch.getUpperBranchId())) {
                serviceBranchTreeDto = new ServiceBranchTreeDto();
                serviceBranchTreeDto.setKey(serviceBranch.getBranchId());
                serviceBranchTreeDto.setTitle(serviceBranch.getBranchName());
                serviceBranchTreeDto.setIsLeaf(true);
                serviceBranchTreeDtoList.add(serviceBranchTreeDto);
            }
        }
        // 为一级菜单设置子菜单，getChild是递归调用的
        for (ServiceBranchTreeDto tree : serviceBranchTreeDtoList) {
            tree.setChildren(getChild(tree, serviceBranchList));
        }
        return serviceBranchTreeDtoList;
    }

    /**
     * 递归查找子节点
     *
     * @param treeDto           当前节点
     * @param serviceBranchList 要查找的列表
     * @return
     */
    private List<ServiceBranchTreeDto> getChild(ServiceBranchTreeDto treeDto,
                                                List<ServiceBranch> serviceBranchList) {
        // 子菜单
        List<ServiceBranchTreeDto> childList = new ArrayList<>();
        ServiceBranchTreeDto serviceBranchTreeDto;
        for (ServiceBranch serviceBranch : serviceBranchList) {
            // 遍历所有节点，将父菜单id与传过来的id比较
            if (LongUtil.isNotZero(serviceBranch.getUpperBranchId())) {
                if (serviceBranch.getUpperBranchId().equals(treeDto.getKey())) {
                    treeDto.setIsLeaf(false);
                    serviceBranchTreeDto = new ServiceBranchTreeDto();
                    serviceBranchTreeDto.setParentId(serviceBranch.getUpperBranchId());
                    serviceBranchTreeDto.setKey(serviceBranch.getBranchId());
                    serviceBranchTreeDto.setTitle(serviceBranch.getBranchName());
                    serviceBranchTreeDto.setIsLeaf(true);
                    childList.add(serviceBranchTreeDto);
                }
            }
        }
        // 把子菜单的子菜单再循环一遍
        for (ServiceBranchTreeDto menu : childList) {
            // 递归
            menu.setChildren(getChild(menu, serviceBranchList));
        }
        // 递归退出条件
        if (childList.size() == 0) {
            return null;
        }
        return childList;
    }

}
