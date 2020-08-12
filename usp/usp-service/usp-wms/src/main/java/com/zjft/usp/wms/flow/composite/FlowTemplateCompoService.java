package com.zjft.usp.wms.flow.composite;

import com.zjft.usp.common.model.UserInfo;
import com.zjft.usp.wms.flow.dto.FlowTemplateCopyDto;
import com.zjft.usp.wms.flow.dto.FlowTemplateDto;
import com.zjft.usp.wms.flow.model.FlowTemplate;


/**
 * 流程模板全局服务接口类
 * 界面操作步骤：
 * 1、用户选择业务大类、业务小类，点击下一步，再结合corp_id从flow_class_node中读取默认配置，required=Y表示不能删除，只能修改节点名称。
 * 2、FlowClassNodeDto返回前端后将值赋给FlowTemplateNode
 * 3、前端提交后，框架自动获得flowTemplate、flowTemplateNodeList、userInfo
 * 4、进行数据写入操作。
 *
 * @Author: JFZOU
 * @Date: 2019-11-07 16:40
 */

public interface FlowTemplateCompoService {

    /**
     * 添加流程模板（前端推荐使用，流程模板节点列表不能单独提交，
     * 必须整个流程模板（即流程模板基本信息+流程步骤）一起提交才处理，前端需要提交flowTemplateDto,nodeList,handlerList）
     * 流程表主键、节点表主键与流程ID、处理人表主键与节点ID、流程ID的关系请在前端封装好，后端不处理
     * @param flowTemplateDto
     * @param userInfo
     */
    void addFlowTemplate(FlowTemplateDto flowTemplateDto, UserInfo userInfo);

    /**
     * 通过复制已有流程模板达到快速添加目的(由于有流程实例后，流程模板即不能修改，此时可以通过复制功能创建新流程模板达到修改的目的)
     *
     * @param flowTemplateCopyDto
     * @param userInfo
     */
    void addByCopy(FlowTemplateCopyDto flowTemplateCopyDto, UserInfo userInfo);

    /**
     * 修改流程模板（前端推荐使用，流程模板节点列表不能单独提交，
     * 必须整个流程模板（即流程模板基本信息+流程步骤）一起提交才处理，前端需要提交flowTemplateDto,nodeList,handlerList）
     *
     * @param flowTemplateDto
     * @param userInfo
     */
    void modFlowTemplateNode(FlowTemplateDto flowTemplateDto, UserInfo userInfo);

    /**
     * 修改流程模板基本信息（是否可用、模板名称、顺序号）
     *
     * @param flowTemplate
     * @param userInfo
     */
    void modFlowTemplateBaseInfo(FlowTemplate flowTemplate, UserInfo userInfo);

    /**
     * 删除流程模板
     *
     * @param flowTemplateId
     */
    void delFlowTemplateBy(Long flowTemplateId);

    /**
     * 获得一个流程模板
     *
     * @param flowTemplateId
     * @return
     */
    FlowTemplateDto getFlowTemplateById(Long flowTemplateId);
}
