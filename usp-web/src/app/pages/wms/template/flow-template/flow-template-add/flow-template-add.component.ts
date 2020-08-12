import {ChangeDetectorRef, Component, EventEmitter, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute} from '@angular/router';
import {FlowTypeComponent} from './flow-type/flow-type.component';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {FlowNodeEditComponent} from './flow-node-edit/flow-node-edit.component';
import {FlowNodeHandlerListComponent} from './flow-node-handler-list/flow-node-handler-list.component';
import {FlowTemplateService} from '../flow-template.service';

@Component({
  selector: 'flow-template-add',
  templateUrl: 'flow-template-add.component.html',
  styleUrls: ['./flow-template-add.component.less']
})
export class FlowTemplateAddComponent implements OnInit {

  type: any;
  id: any;
  flowNodeList: any[] = [];
  flowTemplate: any = this.flowTemplateService.flowTemplate;
  corpId = this.userService.currentCorp.corpId;

  mapOfMain: { [key: string]: string } =  {};
  mapOfDetail: { [key: string]: string } =  {};
  mapOfHandler: { [key: number]: string } =  {};
  handlerList: any[] = [];
  sortFlag = true;
  tmpName = '';
  tmpNode: any= {};

  flag = true;
  text = '请选择发起人';
  handler: any;
  addDetailForm: FormGroup;
  handlerForm: FormGroup;
  selectOptions: any = {
    nodeTypeList: [
      {id: 10, name: '填写节点'},
      {id: 20, name: '普通审批节点'},
      {id: 30, name: '会签审批节点'},
      {id: 40, name: '发货节点'},
      {id: 50, name: '收货节点'},
      {id: 60, name: '确认节点'}
    ],
    formMainList: [],
    formDetailList: [],
    handlerTypeList: [
      {id: 10, name: '发起人'},
      {id: 20, name: '指定角色'},
      {id: 30, name: '指定用户'}
    ],
    textList: [],
  };

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private flowTemplateService: FlowTemplateService,
    private userService: UserService,
    private msg: NzMessageService,
  ) {
    this.addDetailForm = this.formBuilder.group({
      nodeType: [null, Validators.required],
      name: [null, Validators.required],
      sortNo: [1, Validators.required],
      formMainId: [null, Validators.required],
      formDetailId: [null, Validators.required],
      handler: [null, Validators.required],
    });
    this.handlerForm = this.formBuilder.group({
      handlerTypeId: [null, Validators.required],
      handlerId: [null, Validators.required],
      isMainDirector: [null, Validators.required],
    });
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.type = queryParams.type;
      this.id = queryParams.id;
    });
  }

  ngOnInit() {
    this.FormTemplate();
    this.initMapOfHandler();
    this.handlerForm.controls.handlerTypeId.setValue(10);
    this.addDetailForm.controls.handler.setValue('发起人');
    this.handlerList.push({handlerTypeId: 10, handlerId: this.userService.userInfo.userId, isMainDirector: 'Y'});
    if(this.type === 'add') {
      this.httpClient
        .post('/api/wms/flow-class-node/listForAddTemplateBy',{largeClassId:this.flowTemplate.largeClassId
          , smallClassId:this.flowTemplate.smallClassId})
        .pipe(
        )
        .subscribe((res: any) => {
          const data = res.data;
          if( data == null){
            this.flowNodeList = [];
            return;
          }
          this.flowNodeList = data;
        });
    }else if (this.type === 'edit') {
      this.httpClient
        .get('/api/wms/flow-template/getFlowTemplate/' + this.id)
        .pipe(
        )
        .subscribe((res: any) => {
          const data = res.data;
          if( data == null){
            this.flowNodeList = [];
            return;
          }
          this.tmpNode = {};
          data.flowTemplateNodeDtoList.forEach(item => {
            this.tmpNode = item;
            item.handlerList.forEach(i => {
              if(i.handlerTypeId === 10){
                this.tmpNode.handler = '发起人';
              }else {
                if(this.tmpNode.handler === undefined){
                  if(i.isMainDirector === 'Y'){
                    this.tmpNode.handler = this.mapOfHandler[i.handlerId] + '(主)';
                  }else {
                    this.tmpNode.handler = this.mapOfHandler[i.handlerId];
                  }
                }else {
                  if(i.isMainDirector === 'Y'){
                    this.tmpNode.handler = this.tmpNode.handler + ',' + this.mapOfHandler[i.handlerId] + '(主)';
                  }else {
                    this.tmpNode.handler = this.tmpNode.handler + ',' + this.mapOfHandler[i.handlerId];
                  }
                }
              }
            });
            this.flowNodeList.push(this.tmpNode);
          });
        });
    }
  }

  initMapOfHandler(){
    this.mapOfHandler['10'] = '张三';
    this.mapOfHandler['20'] = '李四';
    this.httpClient
      .post('/api/uas/corp-user/user/list', {corpId: this.corpId})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        data.forEach(item => {
          this.mapOfHandler[item.userId] = item.userName;
        });
      });
  }

  FormTemplate() {
    this.httpClient
      .post('/api/wms/form-template/listForSelectBy', {corpId: this.corpId})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        data.forEach(item => {
          if(item.tableClass ===10){
            this.selectOptions.formMainList.push({
              value: item.id,
              text: item.name
            });
          }else  if(item.tableClass ===20){
            this.selectOptions.formDetailList.push({
              value: item.id,
              text: item.name
            });
          }
          this.mapOfMain[item.id] = item.name;
          this.mapOfDetail[item.id] = item.name;
        });
      });
  }
  goBack() {
    history.back();
  }

  editFlowType() {
    this.modalService.create({
      nzTitle: '编辑流程类型',
      nzWidth: 400,
      nzContent: FlowTypeComponent,
      nzComponentParams: {
        flowType: this.flowTemplate,
        type:'edit'
      },
      nzOnOk: (res: any) => {
        if (res.flowTypeForm.value) {
          this.flowTemplate = res.flowTypeForm.value;
          if(res.largeClassName !== null){
            this.flowTemplate.largeClassName = res.largeClassName;
          }
          if (res.smallClassName !== null){
            this.flowTemplate.smallClassName = res.smallClassName;
          }
          this.cdf.markForCheck();
        }
      }
    })
  }

  // 加入列表,添加节点
  addNode() {
    this.sortFlag = true;
    this.flowNodeList.forEach(item => {
      if(this.addDetailForm.value.sortNo === item.sortNo){
        this.msg.error('节点顺序号不能重复，请重试');
        this.sortFlag = false;
        return;
      }
    });
    if(this.sortFlag){
      const newFlowNode: any = {
        name: this.addDetailForm.value.name,
        nodeType: this.addDetailForm.value.nodeType,
        formMainId: this.addDetailForm.value.formMainId,
        formDetailId: this.addDetailForm.value.formDetailId,
        handler: this.addDetailForm.value.handler,
        handlerList: [...this.handlerList],
        sortNo: this.addDetailForm.value.sortNo,
        isCore: 'N',
        enabled: 'Y'
      };
      this.flowNodeList.push(newFlowNode);
    }
    this.addDetailForm.reset();
    this.handlerForm.reset();
    console.log(this.flowNodeList);
  }

  clearDetailForm() {
    this.addDetailForm.reset();
    this.handlerForm.reset();
  }

  editHandler(data, index) {
    if(!data.handlerList){
      this.msg.warning('该节点为内置节点，无处理人，请勿操作');
    }else {
      if(data.handler === '发起人'){
        this.msg.warning('该节点处理人为发起人，无法修改处理人');
      }else {
        const modal = this.modalService.create({
          nzTitle: '编辑处理人',
          nzContent: FlowNodeHandlerListComponent,
          nzWidth: 800,
          nzComponentParams: {
            handlerList: this.flowNodeList[index].handlerList,
            mapOfHandler: this.mapOfHandler
          },
          nzOnOk: (res: any) => {
            this.tmpName = '';
            if (res.handlerList) {
              this.flowNodeList[index].handlerList = res.handlerList;
              res.handlerList.forEach(item => {
                if(this.tmpName === ''){
                  if(item.isMainDirector === 'Y'){
                    this.tmpName = this.mapOfHandler[item.handlerId] + '(主)';
                  }else {
                    this.tmpName = this.mapOfHandler[item.handlerId];
                  }
                }else {
                  if(item.isMainDirector === 'Y'){
                    this.tmpName = this.tmpName + ',' +this.mapOfHandler[item.handlerId] + '(主)';
                  }else {
                    this.tmpName = this.tmpName + ',' +this.mapOfHandler[item.handlerId];
                  }
                }
              });
              this.flowNodeList[index].handler = this.tmpName;
              this.cdf.markForCheck();
            }
          }
        });
      }
    }
  }

  editFlowNode(data, index) {
    this.modalService.create({
      nzTitle: '编辑流程节点',
      nzWidth: 400,
      nzContent: FlowNodeEditComponent,
      nzComponentParams: {
        flowNode: data,
      },
      nzOnOk: (res: any) => {
        if (res.flowNodeForm.value) {
          this.flowNodeList[index].name= res.flowNodeForm.value.name;
          this.flowNodeList[index].nodeType= res.flowNodeForm.value.nodeType;
          this.flowNodeList[index].formMainId= res.flowNodeForm.value.formMainId;
          this.flowNodeList[index].formDetailId= res.flowNodeForm.value.formDetailId;
          this.flowNodeList[index].sortNo= res.flowNodeForm.value.sortNo;
          this.flowNodeList[index].isCore= res.flowNodeForm.value.isCore;
          this.msg.success('节点编辑成功！');
          this.cdf.markForCheck();
        }
      }
    })
  }

  swithFlowNode(data, index) {
    if(data.enabled === 'Y'){
      this.flowNodeList[index].enabled = 'N';
    }else if(data.enabled === 'N'){
      this.flowNodeList[index].enabled = 'Y';
    }
    /*this.msg.success('暂无功能！');*/
  }

  addSubmit() {
    if(!this.flowTemplate.name){
      this.msg.warning('模板名称不能为空');
    }else if(!this.flowTemplate.largeClassName&&!this.flowTemplate.smallClassName){
      this.msg.warning('流程类型不能为空');
    }else if(!this.flowTemplate.sortNo){
      this.msg.warning('顺序号不能为空');
    }else if(!this.flowTemplate.enabled){
      this.msg.warning('是否可用不能为空');
    }else if(!this.flowTemplate.description){
      this.msg.warning('描述不能为空');
    }else {
      this.flowTemplate.flowTemplateNodeDtoList = this.flowNodeList;
      if(this.type === 'add') {
        this.httpClient.post('/api/wms/flow-template/addFlowTemplate', this.flowTemplate)
          .subscribe((res: any) => {
            if(res.code === 0) {
              this.msg.success('添加成功！');
              this.flowTemplateService.clear();
              history.back();
            }
          });
      }else if (this.type === 'edit') {
        this.httpClient.post('/api/wms/flow-template/modFlowTemplateNode', this.flowTemplate)
          .subscribe((res: any) => {
            if(res.code === 0) {
              this.msg.success('修改成功！');
              this.flowTemplateService.clear();
              history.back();
            }
          });
      }
    }
    /*this.flowNodeList.forEach(item => {
      delete item.flowClassNodeList;
      delete item.handler;
    });*/
  }

  changeHandlerType(event){
    this.selectOptions.textList.splice(0);
    this.handlerList.splice(0);
    /*this.mapOfHandler = {};*/
    if(event === 10) {
      this.flag = true;
      this.text = '请选择发起人';
      this.addDetailForm.controls.handler.setValue('发起人');
      this.handlerList.push({handlerTypeId: 10, handlerId: this.userService.userInfo.userId, isMainDirector: 'Y'});
      console.log(this.handlerList);
    }else if (event === 20) {
      if(this.addDetailForm.controls.handler.value !== null){
        this.addDetailForm.controls.handler.setValue(null);
      }
      this.flag = false;
      this.text = '请选择指定角色';
      this.selectOptions.textList = [
        {id: '10', name: '张三'},
        {id: '20', name: '李四'}
      ]
      this.mapOfHandler['10'] = '张三';
      this.mapOfHandler['20'] = '李四';
    } else if (event === 30) {
      this.flag = false;
      this.text = '请选择指定用户';
      if(this.addDetailForm.controls.handler.value !== null){
        this.addDetailForm.controls.handler.setValue(null);
      }
      this.httpClient
        .post('/api/uas/corp-user/user/list', {corpId: this.corpId})
        .pipe(
        )
        .subscribe((res: any) => {
          const data = res.data;
          data.forEach(item => {
            this.selectOptions.textList.push({
              id: item.userId,
              name: item.userName
            });
            this.mapOfHandler[item.userId] = item.userName;
          });
        });
    }
  }

  addHandler(event){
    // 标记主处理人 ，并且附带电话（18270772533）
    const tmpHandler: any = {};
    tmpHandler.handlerTypeId = this.handlerForm.controls.handlerTypeId.value;
    tmpHandler.handlerId = this.handlerForm.controls.handlerId.value;
    tmpHandler.isMainDirector = this.handlerForm.controls.isMainDirector.value;
    if(this.addDetailForm.controls.handler.value === null){
      // 判断是否主处理人
      if( this.handlerForm.controls.isMainDirector.value === 'Y'){
        this.handler = this.mapOfHandler[event] + '(主)';
      }else {
        this.handler = this.mapOfHandler[event];
      }
      this.handlerList.push(tmpHandler);
    }else {
      for (const item of this.handlerList) {
        if(event === item.handlerId){
          this.msg.error('处理人添加重复，请重试');
          return;
        }
      }
      /*this.handler = JSON.stringify(this.addDetailForm.controls.handler.value) + ',' + this.mapOfHandler[event] ;*/
      if( this.handlerForm.controls.isMainDirector.value === 'Y'){
        this.handler = this.addDetailForm.controls.handler.value + ',' + this.mapOfHandler[event] + '(主)';
      }else {
        this.handler = this.addDetailForm.controls.handler.value + ',' + this.mapOfHandler[event];
      }
      this.handlerList.push(tmpHandler);
    }
    this.addDetailForm.controls.handler.setValue(this.handler);
  }

  // 删除确认
  delFlowNodeConfirm(index): void {
    this.modalService.confirm({
      nzTitle: '确定删除该节点吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.delFlowNode(index),
      nzCancelText: '取消'
    });
  }

  // 删除节点
  delFlowNode(index){
    /*this.flowNodeList.splice(index, 1);*/
    this.flowNodeList = this.flowNodeList.filter( (item, i)=> i !== index);
    this.msg.success('删除该节点成功');
  }
}
