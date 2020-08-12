import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {AppConfigAddComponent} from './app-config-add.component';

@Component({
  selector: 'app-application-config-list',
  templateUrl: 'app-config-list.component.html'
})
export class AppConfigListComponent implements OnInit {

  searchForm: FormGroup;
  pageNum = 1;
  pageSize = 10;
  total = 1;
  configList: any;
  loading = true;

  resetForm(): void {
    this.searchForm.reset();
  }

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService) {
  }

  ngOnInit(): void {
    this.loadList(this.getPageParam());
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('configKey', new FormControl());
    this.searchForm.addControl('appId', new FormControl());
  }

  loadList(params: any, reset: boolean = false): void {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/config/config/list',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.configList = data.records;
        this.total = data.total;
      });
  }

  queryConfig(reset: boolean = false) {
    this.loadList(this.getParams(), reset);
  }

  getParams() {
    const params = this.searchForm.value;
    params.current = this.pageNum;
    params.size = this.pageSize;
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.current = this.pageNum;
    params.size = this.pageSize;
    return params;
  }

  // 进入添加配置页面
  addConfigModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加配置',
      nzContent: AppConfigAddComponent,
      nzFooter: null,
      nzWidth: 800
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => console.log('[afterClose] The result is:', result));
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
  deleteConfig(configId){
    this.httpClient
      .post('/api/config/config/delete/'+configId,
        null)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.queryConfig();
      });
  }
}
