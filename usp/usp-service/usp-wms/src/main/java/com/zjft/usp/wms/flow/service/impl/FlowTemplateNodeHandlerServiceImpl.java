package com.zjft.usp.wms.flow.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.common.utils.KeyUtil;
import com.zjft.usp.wms.flow.dto.FlowTemplateNodeHandlerDto;
import com.zjft.usp.wms.flow.enums.HandlerTypeEnum;
import com.zjft.usp.wms.flow.enums.MainDirectorEnum;
import com.zjft.usp.wms.flow.enums.NodeTypeEnum;
import com.zjft.usp.wms.flow.model.FlowTemplate;
import com.zjft.usp.wms.flow.model.FlowTemplateNode;
import com.zjft.usp.wms.flow.model.FlowTemplateNodeHandler;
import com.zjft.usp.wms.flow.mapper.FlowTemplateNodeHandlerMapper;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeHandlerService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zjft.usp.wms.flow.service.FlowTemplateNodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * <p>
 * 流程模板节点处理人表 服务实现类
 * </p>
 *
 * @author jfzou
 * @since 2019-11-18
 */
@Service
public class FlowTemplateNodeHandlerServiceImpl extends ServiceImpl<FlowTemplateNodeHandlerMapper, FlowTemplateNodeHandler> implements FlowTemplateNodeHandlerService {

    @Autowired
    private FlowTemplateNodeService flowTemplateNodeService;


    @Override
    public List<FlowTemplateNodeHandler> listByTemplateNodeId(Long templateNodeId) {
        QueryWrapper<FlowTemplateNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_node_id", templateNodeId);
        return this.list(queryWrapper);
    }

    @Override
    public List<FlowTemplateNodeHandler> listAllAssignToPeople(Long templateNodeId, UserInfo userInfo) {
        List<FlowTemplateNodeHandler> newList = new ArrayList<>();
        Set<Long> userIdSet = new TreeSet<>();
        List<FlowTemplateNodeHandler> listByTemplateNodeId = this.listByTemplateNodeId(templateNodeId);

        /**判断当前节点是否为会签节点*/
        FlowTemplateNode flowTemplateNode = flowTemplateNodeService.getById(templateNodeId);
        boolean isCounterSignNode = false;
        if (flowTemplateNode.getNodeType() == NodeTypeEnum.COUNTERSIGN_APPROVAL.getCode()) {
            isCounterSignNode = true;
        }

        /**优先发起人，发起人肯定是主处理人*/
        for (FlowTemplateNodeHandler entity : listByTemplateNodeId) {
            if (entity.getHandlerTypeId() == HandlerTypeEnum.ASSIGN_TO_CREATE_BY.getCode()) {
                /**去掉重复的处理人ID*/
                if (!userIdSet.contains(userInfo.getUserId())) {
                    entity.setIsMainDirector(MainDirectorEnum.YES.getCode());
                    entity.setHandlerId(userInfo.getUserId());
                    newList.add(entity);
                    userIdSet.add(userInfo.getUserId());
                }
            }
        }

        /**优先指定处理人，目前必须在会签节点——指定用户中才能定义流程主处理人*/
        for (FlowTemplateNodeHandler entity : listByTemplateNodeId) {
            if (entity.getHandlerTypeId() == HandlerTypeEnum.ASSIGN_TO_PEOPLE.getCode()) {
                /**去掉重复的处理人ID*/
                if (!userIdSet.contains(entity.getHandlerId())) {
                    newList.add(entity);
                    userIdSet.add(entity.getHandlerId());
                }
            }
        }

        /**指定角色，将指定角色转为处理人再统一处理*/
        for (FlowTemplateNodeHandler entity : listByTemplateNodeId) {
            if (entity.getHandlerTypeId() == HandlerTypeEnum.ASSIGN_TO_ROLE.getCode()) {
                List<Long> listTempPeople = switchRoleToPeople(entity.getHandlerId());
                for (Long tempPeopleId : listTempPeople) {
                    if (!userIdSet.contains(tempPeopleId)) {
                        newList.add(entity);
                        userIdSet.add(tempPeopleId);
                    }
                }
            }
        }


        /**如果不是会签节点，则所有处理人均为专员*/
        if (!isCounterSignNode) {
            for (FlowTemplateNodeHandler entity : newList) {
                entity.setIsMainDirector(MainDirectorEnum.YES.getCode());
            }
        }

        return newList;
    }

    @Override
    public void removeByTemplateId(Long templateId) {
        QueryWrapper<FlowTemplateNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        super.remove(queryWrapper);
    }

    @Override
    public void removeByTemplateNodeId(Long templateNodeId) {
        QueryWrapper<FlowTemplateNodeHandler> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_node_id", templateNodeId);
        super.remove(queryWrapper);
    }

    @Override
    public void modHandlers(FlowTemplate flowTemplate, List<FlowTemplateNodeHandler> handlerList) {
        /**先删除*/
        this.removeByTemplateId(flowTemplate.getId());

        /**再保存*/
        this.saveBatch(handlerList);
    }

    @Override
    public void modHandlers(FlowTemplateNodeHandlerDto handlerDto) {
        /**先删除*/
        this.removeByTemplateNodeId(handlerDto.getTemplateNodeId());

        List<FlowTemplateNodeHandler> handlerList = handlerDto.getHandlerList();
        for (FlowTemplateNodeHandler handler : handlerList) {
            handler.setId(KeyUtil.getId());
            handler.setTemplateId(handlerDto.getTemplateId());
            handler.setTemplateNodeId(handlerDto.getTemplateNodeId());
        }
        /**再保存*/
        this.saveBatch(handlerList);
    }

    public List<Long> switchRoleToPeople(Long roleId) {
        List<Long> list = new ArrayList<>();
        //TODO 把指定角色转为指定具体处理人
        return list;
    }
}
