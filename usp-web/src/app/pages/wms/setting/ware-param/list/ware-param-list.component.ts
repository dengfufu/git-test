import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareParamAddComponent} from '../add/ware-param-add.component';
import {WareParamEditComponent} from '../edit/ware-param-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-system-param-list',
  templateUrl: 'ware-param-list.component.html',
  styleUrls: ['ware-param-list.component.less']
})
export class WareParamListComponent implements OnInit{

  searchForm: FormGroup;
  wareParamList: any = [];// [{code:'required_prompt',name:'必填校验提示',value:'【{字段名称}】不能为空，请检查！',description:'统一管理非空提示语句'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      paramName: []
    })
  }

  ngOnInit(): void {
    this.queryWareParam();
  }

  queryWareParam(reset: boolean = false) {
    this.loadWareParam(this.param(), reset);
  }

  loadWareParam(params: string, reset: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-param/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareParamList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  param() {
    let param: any = {};
    if(this.searchForm){
      param = this.searchForm.value;
    }
    param.pageNum = this.page.pageNum;
    param.pageSize = this.page.pageSize;
    return param;
  }

  addWareParam() {
    const modal = this.modalService.create({
      nzTitle: '添加参数',
      nzContent: WareParamAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareParam();
      }
    });
  }

  editWareParam(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑参数',
      nzContent: WareParamEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareParam: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareParam();
      }
    });
  }

  confirmDelete(wareParamId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareParam(wareParamId),
      nzCancelText: '取消'
    });
  }

  private deleteWareParam(wareParamId) {
    this.httpClient.delete('/api/wms/ware-param/' + wareParamId)
      .subscribe();
  }

  clearForm() {
    this.searchForm.reset();
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }
}
