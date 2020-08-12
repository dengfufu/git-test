import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareBrandAddComponent} from '../add/ware-brand-add.component';
import {WareBrandEditComponent} from '../edit/ware-brand-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';

@Component({
  selector: 'app-ware-brand-list',
  templateUrl: 'ware-brand-list.component.html',
  styleUrls: ['ware-brand-list.component.less']
})
export class WareBrandListComponent implements OnInit{

  searchForm: FormGroup;
  wareBrandList: any = [];// [{name: '日立', sortNo: 1, enabled: 'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      name: []
    });
  }

  ngOnInit(): void {
    this.queryWareBrand();
  }

  queryWareBrand(reset: boolean = false) {
    this.loadWareBrand(this.params(), reset);
  }

  loadWareBrand(params: string, reset?: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-brand/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareBrandList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  params() {
    let param: any = {};
    if(this.searchForm){
      param = this.searchForm.value;
    }
    param.pageNum = this.page.pageNum;
    param.pageSize = this.page.pageSize;
    return param;
  }

  addWareBrand() {
    const modal = this.modalService.create({
      nzTitle: '添加品牌',
      nzContent: WareBrandAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareBrand(true);
      }
    });
  }

  editWareBrand(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑品牌',
      nzContent: WareBrandEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareBrand: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareBrand(true);
      }
    });
  }

  confirmDelete(wareBrandId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareBrand(wareBrandId),
      nzCancelText: '取消'
    });
  }

  private deleteWareBrand(wareBrandId) {
    this.httpClient.delete('/api/wms/ware-brand/' + wareBrandId)
      .subscribe(() => {
        this.queryWareBrand();
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
