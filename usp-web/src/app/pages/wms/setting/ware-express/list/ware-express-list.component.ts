import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {WareExpressAddComponent} from '../add/ware-express-add.component';
import {WareExpressEditComponent} from '../edit/ware-express-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-express-list',
  templateUrl: 'ware-express-list.component.html',
  styleUrls: ['ware-express-list.component.less']
})
export class WareExpressListComponent implements OnInit{

  searchForm: FormGroup;
  wareExpressList: any = [];// [{name: '顺丰',description:'',sortNo:'1',enabled:'Y'}];
  page = new Page();
  loading = true;
  drawerVisible = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      name: []
    })
  }

  ngOnInit(): void {
    this.queryWareExpress();
  }

  queryWareExpress(reset: boolean = false) {
    this.loadWareExpress(this.param(), reset);
  }

  loadWareExpress(params: string, reset?: boolean) {
    if(reset){
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/express-company/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.wareExpressList = res.data.list;
        this.page.total = res.data.total;
      })
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

  addWareExpress() {
    const modal = this.modalService.create({
      nzTitle: '添加快递公司',
      nzContent: WareExpressAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareExpress();
      }
    });
  }

  editWareExpress(data) {
    const modal = this.modalService.create({
      nzTitle: '编辑快递公司',
      nzContent: WareExpressEditComponent,
      nzFooter: null,
      nzWidth: 800,
      nzComponentParams: {
        wareExpress: data
      }
    });
    modal.afterOpen.subscribe();
    modal.afterClose.subscribe((result) => {
      if(result === 'submit'){
        this.queryWareExpress();
      }
    });
  }

  confirmDelete(expressId) {
    this.modalService.confirm({
      nzTitle: '确定删除该配置吗？',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteWareExpress(expressId),
      nzCancelText: '取消'
    });
  }

  private deleteWareExpress(expressId) {
    this.httpClient.delete('/api/wms/express-company/' + expressId)
      .subscribe(() => {
        this.queryWareExpress();
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
