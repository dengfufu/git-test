import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WarePropertyRightAddComponent} from '../add/ware-property-right-add.component';
import {WarePropertyRightEditComponent} from '../edit/ware-property-right-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-property-right-list',
  templateUrl: 'ware-property-right-list.component.html',
  styleUrls: ['ware-property-right-list.component.less']
})
export class WarePropertyRightListComponent implements OnInit{

  searchForm: FormGroup;
  warePropertyRightList: any = []; // [{name:'紫金',sortNo:'10',enabled:'Y'}];
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
    this.queryWarePropertyRight();
  }

  queryWarePropertyRight(reset: boolean = false) {
    this.loadWarePropertyRight(this.param(), reset);
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

  loadWarePropertyRight(params: string, reset: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/ware-property-right/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.warePropertyRightList = res.data.list;
        this.page.total = res.data.total;
      });
  }

  addWarePropertyRight() {
    const modal = this.modalService.create({
      nzTitle: '添加物料产权',
      nzContent: WarePropertyRightAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWarePropertyRight();
      }
    });
  }

  editWarePropertyRight(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑物料产权',
      nzContent: WarePropertyRightEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        warePropertyRight: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWarePropertyRight();
      }
    });
  }

  confirmDelete(rightId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWarePropertyRight(rightId),
      nzCancelText: '取消'
    });
  }

  private deleteWarePropertyRight(rightId) {
    this.httpClient.delete('/api/wms/ware-property-right/' + rightId)
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
