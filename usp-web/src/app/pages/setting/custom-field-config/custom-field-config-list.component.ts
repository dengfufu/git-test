import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {CustomFieldConfigAddComponent} from './custom-field-config-add.component';


@Component({
  selector: 'custom-field-config-list',
  templateUrl: 'custom-field-config-list.component.html',
  styleUrls: ['custom-field-config-list.component.less']
})
export class CustomFieldConfigListComponent implements OnInit {

  searchForm: FormGroup;
  pageNum = 1;
  pageSize = 10;
  total = 1;
  loading = false;

  drawerVisible = false;
  corpId = this.userService.currentCorp.corpId;
  fieldName: any;
  fieldType: any;

  list: any;

  values: any[] | null = null;
  nzOptions: any;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private msgService: NzMessageService,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
    this.loadList(this.getPageParam());
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('fieldName', new FormControl());
    this.searchForm.addControl('fieldType', new FormControl());
  }

  getPageParam() {
    const params: any = {};
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    params.corpId = this.corpId;
    if(this.fieldType !== undefined && this.fieldType !== null){
      params.fieldType = this.fieldType;
    }else {
      params.fieldType = 0;
    }
    if(this.fieldName !== undefined && this.fieldName !== null){
      params.fieldName = this.fieldName;
    }
   /* console.log('params',params);*/
    return params;
  }

  queryConfig(reset: boolean = false) {

    this.loadList(this.getPageParam(), reset);
  }

  onChanges(fieldType){
    this.fieldType = fieldType;
  }

  loadList(params: any, reset: boolean = false): void {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/custom-field/query',
        params)
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
        if(this.drawerVisible === true) {
          this.drawerVisible = false;
        }
        this.list = data.list;
        this.total = data.total;
      });
  }

  // 进入添加配置页面
  addConfigModal(): void {
    const modal = this.modalService.create({
      nzTitle: '新建自定义字段',
      nzContent: CustomFieldConfigAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        object: {},
        url: '/api/anyfix/custom-field/add',
        type: 'add',
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryConfig();
      }
    });
  }

  // 进入编辑配置页面
  editConfigModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑自定义字段',
      nzContent: CustomFieldConfigAddComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        object: {fieldId: data.fieldId, common: data.common, customCorp: data.customCorp,fieldName: data.fieldName,
          fieldType: data.fieldType,serviceCorp: data.serviceCorp, required: data.required, formType: data.formType,
          customFieldDataSourceList: data.customFieldDataSourceList,corpId:data.corpId},
        url: '/api/anyfix/custom-field/update',
        type: 'update',
      }
    });
    modal.afterClose.subscribe(result => {
      if (result) {
        this.queryConfig();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(configId): void {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteConfig(configId),
      nzCancelText: '取消'
    });
  }

  // 删除配置
  deleteConfig(fieldId){
    this.httpClient
      .delete('/api/anyfix/custom-field/'+fieldId,
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
        this.msgService.success('删除自定义字段成功');
        this.queryConfig(false);
      });
  }

  clearForm() {
    this.searchForm.reset();
    this.queryConfig(false);
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }
}
