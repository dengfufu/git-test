import {Injectable} from '@angular/core';

export class FlowTemplate {

  largeClassName: string;             // 业务大类名称
  smallClassName: string;             // 业务小类名称
  // 获得业务类型节点列表（含节点基本信息+处理人列表）
  flowTemplateNodeDtoList: any[] = [
    {
      mainFormTemplate: {},             // 表单模板-业务主表
      mainFormTemplateFieldList:  [],   // 表单模板-业务主表字段列表
      detailFormTemplate:  {},          // 表单模板-业务明细表
      detailFormTemplateFieldList:  [], // 表单模板-业务明细表字段列表
      handlerList:  [                   // 处理人列表
        {
          handlerTypeId: null,  // 处理人类型(10=发起人20=指定角色30=指定用户)
          handlerId: null,      // 处理人ID(如果是发起人，则为当前用户ID；如果是角色，则为角色ID；如果是指定处理人，则为指定处理人ID)
          isMainDirector: null, // 是否为主处理人 (其他非会签节点，默认都是主处理人；
                                // 如果是会签节点则需要手工指定主处理人，主处理人操作才能结束当前节点，Y=是,N=否)
        }
      ],

      name: null,                // 节点名称
      nodeType: null,            // 节点类型(10=填写节点20=普通审批节点30=会签审批节点40=发货节点50=收货节点60=确认节点)
      formMainId: null,          // 表单基本信息模板ID
      formDetailId: null,        // 表单明细信息模板ID
      description: null,         // 描述
      sortNo: null,              // "顺序号，顺序号在前端调整，即顺序号在前端为可输入项
      enabled: null,             // 是否可用 (Y=是,N=否)
      isCore: null,              // 是否核心节点 (Y=是,N=否,为Y则不能删除)
    }
  ];

  id:any;                             // 流程模板ID
  corpId:any;                         // 企业ID
  name: string;                       // 流程模板名称
  largeClassId: number;               // 业务大类ID
  smallClassId: number;               // 业务小类ID
  description: string;                // 描述
  sortNo: number;                     // 顺序号
  enabled: string;                    // 是否可用 (Y=是,N=否)
}

@Injectable({
  providedIn: 'root'
})
export class FlowTemplateService {

  flowTemplate: FlowTemplate = new FlowTemplate();

  constructor() {
  }

  clear() {
    this.flowTemplate = new FlowTemplate();
  }

}
