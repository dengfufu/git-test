import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {FlowTypeComponent} from '../flow-template-add/flow-type/flow-type.component';
import {ActivatedRoute, Router} from '@angular/router';
import {FlowTemplateService} from '../flow-template.service';
import {FlowTemplateEditComponent} from '../flow-template-edit/flow-template-edit.component';
import {FlowTemplateCopyComponent} from '../flow-template-copy/flow-template-copy.component';

@Component({
  selector: 'flow-template-list',
  templateUrl: 'flow-template-list.component.html'
})
export class FlowTemplateListComponent implements OnInit {

  flowTemplate: any = this.flowTemplateService.flowTemplate;
  pageNum = 1;
  pageSize = 10;
  total = 1;
  loading = false;

  drawerVisible = false;

  searchForm: FormGroup;

  corpId = this.userService.currentCorp.corpId;
  list: any;

  values: any[] | null = null;
  ClassOptions: any = {
    largeClass: [
      {value: 10, text: '入库'},
      {value: 20, text: '出库'},
      {value: 30, text: '调拨'},
      {value: 40, text: '盘点'},
      {value: 50, text: '维修'},
      {value: 60, text: '组装'},
      {value: 70, text: '报废'},
      {value: 80, text: '拆分'},
    ],
    smallClass: [
      {value: 10, text: '公司采购入库'},
      {value: 20, text: '厂商物料入库'},
      {value: 30, text: '现有物料入库'},
      {value: 40, text: '厂商返还入库'},
      {value: 50, text: '销售借用归还入库'},
      {value: 60, text: '领用退料入库'},
      {value: 70, text: '销售借用出库'},
      {value: 80, text: '公司销售出库'},
      {value: 90, text: '归还厂商出库'},
      {value: 100, text: '物料领用出库'},
      {value: 110, text: '物料库存调拨'},
      {value: 120, text: '物料快速转库'},
      {value: 130, text: '待修物料返还'},
    ]
  };
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private activateRoute: ActivatedRoute,
              private messageService: NzMessageService,
              private router: Router,
              private flowTemplateService: FlowTemplateService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: [],
      largeClassId: [],
      smallClassId: [],
      enabled: [],
      sortNo: []
    });
  }

  ngOnInit(): void {
    this.queryConfig(false);
  }

  getParams() {
    const params = this.searchForm.value;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    params.corpId = this.corpId;
    return params;
  }

  queryConfig(reset: boolean) {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/wms/flow-template/pageBy',
        this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        if( data == null){
          this.list = [];
          return;
        }
        this.list = data.list;
        this.total = data.total;
      });
  }


  openDrawer() {
    this.drawerVisible = true;
  }
  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
    this.queryConfig(false);
  }

  // 添加流程模板
  addFlowTemplate(): void {
    const model = this.modalService.create({
      nzTitle: '选择流程类型',
      nzWidth: 400,
      nzContent: FlowTypeComponent,
      nzComponentParams: {
        flowType: {},
        type:'add'
      },
      nzOnOk: (res: any) => {
        if (res.flowTypeForm.value) {
          if(!res.flowTypeForm.value.largeClassId){
            this.messageService.warning('流程类型不能为空，请重新填写');
            return false;
          }else  if(!res.flowTypeForm.value.smallClassId){
            this.messageService.warning('流程子类型不能为空，请重新填写');
            return false;
          }else  if(!res.flowTypeForm.value.name){
            this.messageService.warning('模板名称不能为空，请重新填写');
            return false;
          }
          this.flowTemplateService.flowTemplate = res.flowTypeForm.value;
          this.flowTemplateService.flowTemplate.largeClassName = res.largeClassName;
          this.flowTemplateService.flowTemplate.smallClassName = res.smallClassName;
        }
        this.router.navigate(['../flow-template-add'], {queryParams: {type: 'add'}, relativeTo: this.activateRoute});
      }
    });
  }

  // 流程步骤，修改流程节点，处理人
  modFlowTemplateNode(data, index): void {
    this.flowTemplate.largeClassName = data.largeClassName;
    this.flowTemplate.smallClassName = data.smallClassName;
    this.flowTemplate.id = data.id;
    this.flowTemplate.corpId = data.corpId;
    this.flowTemplate.name = data.name;
    this.flowTemplate.largeClassId = data.largeClassId;

    this.flowTemplate.smallClassId = data.smallClassId;
    this.flowTemplate.description = data.description;
    this.flowTemplate.sortNo = data.sortNo;
    this.flowTemplate.enabled = data.enabled;
    this.router.navigate(['../flow-template-add'], {queryParams: {type: 'edit', id: data.id}, relativeTo: this.activateRoute});
  }

  // 进入编辑页面
  editFlowTemplate(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑流程模板',
      nzContent: FlowTemplateEditComponent,
      nzFooter: null,
      nzWidth: 700,
      nzComponentParams: {
        id: data.id
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.messageService.success('流程模板编辑成功');
        this.queryConfig(false);
      }
    });
  }

  // 进入复制页面
  copyFlowTemplate(data): void {
    const modal = this.modalService.create({
      nzTitle: '复制模板',
      nzContent: FlowTemplateCopyComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        id: data.id
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.messageService.success('复制模板成功');
        this.queryConfig(false);
      }
    });
  }

  // 删除确认
  showDeleteConfirm(data): void {
    this.modalService.confirm({
      nzTitle: '确定删除该模板吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteFlowTemplate(data.id),
      nzCancelText: '取消'
    });
  }

  // 删除流程模板
  deleteFlowTemplate(id){
    // 如果是为第n页第一条，需要调整为n-1页才有数据显示
    if(this.list.length === 1 && this.pageNum>0){
      this.pageNum = this.pageNum - 1;
    }
    this.httpClient
      .delete('/api/wms/flow-template/deleteFlowTemplate/'+id,
        {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if(this.list.length === 1 && this.pageNum>1){
          this.pageNum = this.pageNum - 1;
        }
        this.messageService.success('删除该模板成功');
        this.queryConfig(false);
      });
  }
}
