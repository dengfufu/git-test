import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareStatusAddComponent} from '../add/ware-status-add.component';
import {WareStatusEditComponent} from '../edit/ware-status-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-status-list',
  templateUrl: 'ware-status-list.component.html',
  styleUrls: ['ware-status-list.component.less']
})
export class WareStatusListComponent implements OnInit{

  searchForm: FormGroup;
  wareStatusList: any = [];// [{name:'库存',sortNo:'',enabled:'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryWareStatus();
  }

  queryWareStatus(reset: boolean = false) {
    this.loadWareStatus(this.param(), reset);
  }

  param(){
    let param: any = {};
    if(this.searchForm){
      param = this.searchForm.value;
    }
    param.pageNum = this.page.pageNum;
    param.pageSize = this.page.pageSize;
    return param;
  }

  loadWareStatus(params: string, reset: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-status/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareStatusList = res.data.list;
        this.page.total = res.data.total;
      })
  }

  addWareStatus() {
    const modal = this.modalService.create({
      nzTitle: '添加物料状态',
      nzContent: WareStatusAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareStatus();
      }
    });
  }

  editWareStatus(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑物料状态',
      nzContent: WareStatusEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareStatus: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareStatus();
      }
    });
  }

  confirmDelete(wreStatusId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareStatus(wreStatusId),
      nzCancelText: '取消'
    });
  }

  private deleteWareStatus(wreStatusId) {
    this.httpClient.delete('/api/wms/ware-status/' + wreStatusId)
      .subscribe(() => {
        this.queryWareStatus();
      });
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
