import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {FormTemplateAddComponent} from '../form-template-add/form-template-add.component';
import {FormTemplateEditComponent} from '../form-template-edit/form-template-edit.component';
import {FormTemplateFieldComponent} from '../form-template-field/form-template-field.component';

@Component({
  selector: 'form-template-list',
  templateUrl: 'form-template-list.component.html'
})
export class FormTemplateListComponent implements OnInit {

  pageNum = 1;
  pageSize = 10;
  total = 1;
  loading = false;

  searchForm: FormGroup;
  corpId = this.userService.currentCorp.corpId;
  list: any;
  drawerVisible = false;
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
    ],
    tableClass: [
      {value: 10, text: '业务主表'},
      {value: 20, text: '业务明细表'},
    ],
  };
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private msg: NzMessageService,
              private messageService: NzMessageService,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: [],
      largeClassId: [],
      smallClassId: [],
      enabled: [],
      sortNo: [],
      sysBuildIn: [],
      tableClass: []
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
      .post('/api/wms/form-template/pageBy',
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

  // 进入添加表单模板页面
  addFormTemplate(): void {
    const modal = this.modalService.create({
      nzTitle: '添加表单模板',
      nzContent: FormTemplateAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        url: '/api/wms/form-template/addByCopy',
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.msg.success('表单模板添加成功');
        this.queryConfig(false);
      }
    });
  }

  // 进入编辑表单模板页面
  editFormTemplate(data): void {
    if(data.sysBuildIn === 'Y' ){
      this.messageService.error('该表单模板为内置表单模板，不可编辑');
    }else {
      const modal = this.modalService.create({
        nzTitle: '编辑表单模板',
        nzContent: FormTemplateEditComponent,
        nzFooter: null,
        nzWidth: 700,
        nzComponentParams: {
          object: {id: data.id, name: data.name, sortNo: data.sortNo,
            enabled: data.enabled, description: data.description},
          url: '/api/wms/form-template/modBaseInfo'
        }
      });
      modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
      modal.afterClose.subscribe(result => {
        if(result === 0){
          this.msg.success('表单模板编辑成功');
          this.queryConfig(false);
        }
      });
    }
  }

  // 进入字段维护页面
  editFormTemplateField(data): void {
    const modal = this.modalService.create({
      nzTitle: '字段维护',
      nzContent: FormTemplateFieldComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        id: data.id,
        url: '/api/wms/form-template/modFieldList',
        flag: data.sysBuildIn
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if(result === 0){
        this.msg.success('字段维护成功');
        this.queryConfig(false);
      }else if(result === 1){
        /*this.queryConfig();*/
      }
    });
  }

  // 删除确认
  showDeleteConfirm(data): void {
    if(data.sysBuildIn === 'Y' ){
      this.messageService.error('该表单模板为内置表单模板，不可删除');
    }else {
      this.modalService.confirm({
        nzTitle: '确定删除该模板吗?',
        nzOkText: '确定',
        nzOkType: 'danger',
        nzOnOk: () => this.deleteFormTemplate(data.id),
        nzCancelText: '取消'
      });
    }
  }

  // 删除配置
  deleteFormTemplate(id){
    // 如果是为第n页第一条，需要调整为n-1页才有数据显示
    if(this.list.length === 1 && this.pageNum>0){
      this.pageNum = this.pageNum - 1;
    }
    this.httpClient
      .delete('/api/wms/form-template/'+id,
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
        this.msg.success('删除该模板成功');
        this.queryConfig(false);
      });
  }
}
