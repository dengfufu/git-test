import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {CorpVerifyCheckComponent} from './corp-verify-check/corp-verify-check.component';
import {Page} from '@core/interceptor/result';
import {SYS_RIGHT} from '../../../core/right/right';
import { ZorroUtils } from '@util/zorro-utils';

@Component({
  selector: 'app-corp-verify-check',
  templateUrl: './corp-verify-list.component.html',
  styleUrls: ['./corp-verify-list.component.less']
})
export class CorpVerifyListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = SYS_RIGHT;

  // 申请状态查询条件
  statusOptionList = [{label: '申请中', value: 1},
    {label: '已通过', value: 2},
    {label: '不通过', value: 3}];
  statusList = [];
  // 行政区划查询条件
  areaList: any[];
  
  page = new Page();

  // 查询结果
  corpVerifyApplyList: any[];
  loading = true;
  visible = false;

  searchForm: FormGroup;

  constructor(private httpClient: HttpClient,
              private formBuilder: FormBuilder,
              private messageService: NzMessageService,
              private nzModalService: NzModalService,
              private cdf: ChangeDetectorRef) {
    this.searchForm = this.formBuilder.group({
      corpName: [],
      statusList: [this.statusList],
      area: [],
      province: [],
      city: [],
      larName: [],
      applyUserName: [],
      applyTimeRange: []
    });
  }

  ngOnInit() {
    this.queryCorpVerifyApp();
    this.listAreaDto();
  }

  listAreaDto(): void {
    this.httpClient
      .get('/api/uas/area/province-city')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.areaList = res.data;
      });
  }

  queryCorpVerifyApp(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/uas/corp-verify/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const page = res.data;
        this.corpVerifyApplyList = page.list;
        this.page.total = page.total;
      });
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    const applyTime = this.searchForm.controls.applyTimeRange.value;
    if (applyTime != null && applyTime.length > 0) {
      params.applyTimeStart = applyTime[0];
      params.applyTimeEnd = applyTime[1];
    }
    return params;
  }

  audit(corpVerify) {
    const modal = this.nzModalService.create({
      nzTitle: '企业认证审核',
      nzWidth: 1000,
      nzContent: CorpVerifyCheckComponent,
      nzComponentParams: {corpVerify},
      nzFooter: null
    });
    modal.afterClose.subscribe((result: string) => {
      if (result === 'submit') {
        this.queryCorpVerifyApp();
      }
    });
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }


  onChanges(values: any): void {
    if (values === null) {
      return;
    }
    this.searchForm.controls.province.setValue(values[0]);
    if(!values[1]) {
      this.searchForm.controls.city.setValue(values[1]);
    }

  }
}
