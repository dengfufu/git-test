import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareSupplierAddComponent} from '../add/ware-supplier-add.component';
import {WareSupplierEditComponent} from '../edit/ware-supplier-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-supplier-list',
  templateUrl: 'ware-supplier-list.component.html',
  styleUrls: ['ware-supplier-list.component.less']
})
export class WareSupplierListComponent implements OnInit{

  searchForm: FormGroup;
  wareSupplierList: any = [];// [{name:'紫金公司',sortNo:'',enabled:'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryWareSupplier();
  }

  queryWareSupplier(reset: boolean = false) {
    this.loadWareSupplier(this.param(), reset);
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

  loadWareSupplier(params: string, reset: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-supplier/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareSupplierList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  addWareSupplier() {
    const modal = this.modalService.create({
      nzTitle: '添加供应商',
      nzContent: WareSupplierAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareSupplier();
      }
    });
  }

  editWareSupplier(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑供应商',
      nzContent: WareSupplierEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareSupplier: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareSupplier();
      }
    });
  }

  confirmDelete(supplierId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareSupplier(supplierId),
      nzCancelText: '取消'
    });
  }

  private deleteWareSupplier(supplierId) {
    this.httpClient.delete('/api/wms/ware-supplier/' + supplierId)
      .subscribe(() => {
        this.queryWareSupplier();
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
