import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {WareAreaAddComponent} from '../add/ware-area-add.component';
import {WareAreaEditComponent} from '../edit/ware-area-edit.component';
import {NzModalService} from 'ng-zorro-antd';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-area-list',
  templateUrl: 'ware-area-list.component.html',
  styleUrls: ['ware-area-list.component.less']
})
export class WareAreaListComponent implements OnInit{

  searchForm: FormGroup;
  wareAreaList: any = [];// [{name: '广东', userId: '1', description: '冲冲冲', enabled: 'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private changeDetectorRef: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      name: []
    })
  }

  ngOnInit(): void {
    this.queryWareArea();
  }

  queryWareArea(reset: boolean = false) {
    this.loadWareArea(this.params(), reset);
  }

  params(){
    let param: any = {};
    if(this.searchForm){
      param = this.searchForm.value;
    }
    param.pageNum = this.page.pageNum;
    param.pageSize = this.page.pageSize;
    return param;
  }

  loadWareArea(params: any, reset?: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-area/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.changeDetectorRef.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareAreaList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  addWareArea() {
    const modal = this.modalService.create({
      nzTitle: '添加区域',
      nzContent: WareAreaAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareArea(true);
      }
    });
  }

  editWareArea(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑区域',
      nzContent: WareAreaEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareArea : data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareArea(true);
      }
    });
  }

  confirmDelete(wareAreaId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareArea(wareAreaId),
      nzCancelText: '取消'
    });
  }

  private deleteWareArea(wareAreaId) {
    this.httpClient.delete('/api/wms/ware-area/' + wareAreaId)
      .subscribe(() => {
        this.queryWareArea();
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
