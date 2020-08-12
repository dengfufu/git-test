import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareModelAddComponent} from '../add/ware-model-add.component';
import {WareModelEditComponent} from '../edit/ware-model-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-model-list',
  templateUrl: 'ware-model-list.component.html',
  styleUrls: ['ware-model-list.component.less']
})
export class WareModelListComponent implements OnInit{

  searchForm: FormGroup;
  wareModelList: any = [];// [{name:'RB',catalogId:'机器',brandId:'日立',havaSn:'Y',havaBarcode:'Y',description:'',enabled:'Y'}];
  page = new Page();
  loading = false;
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
    this.queryWareModel();
  }

  queryWareModel(reset: boolean = false) {
    this.loadWareModel(this.param(), reset);
  }

  loadWareModel(param: string, reset: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-model/query', param)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareModelList = res.data.list;
        this.page.total = res.data.total;
      })
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

  addWareModel() {
    const modal = this.modalService.create({
      nzTitle: '添加型号',
      nzContent: WareModelAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareModel();
      }
    });
  }

  editWareModel(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑型号',
      nzContent: WareModelEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareModel: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareModel();
      }
    });
  }

  confirmDelete(wareModelId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareModel(wareModelId),
      nzCancelText: '取消'
    });
  }

  private deleteWareModel(wareModelId) {
    this.httpClient.delete('/api/wms/ware-model/' + wareModelId)
      .subscribe(() => {
        this.queryWareModel();
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
