import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {SettleCustomAddComponent} from './settle-custom-add/settle-custom-add.component';
import {SettleCustomDetailComponent} from './settle-custom-detail/settle-custom-detail.component';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {Page} from '@core/interceptor/result';

@Component({
  selector: 'app-settle-custom',
  templateUrl: './settle-custom.component.html',
  styleUrls: ['./settle-custom.component.less']
})
export class SettleCustomComponent implements OnInit {

  settleCustomList: any[];
  page = new Page();
  loading = false;
  visible = false;

  serviceCorp = this.userService.currentCorp.corpId;
  customList: any[];

  searchForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private nzModalService: NzModalService,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private messageService: NzMessageService) { }

  ngOnInit() {
    this.querySettleCustom(this.getPageParams(), true);
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('customCorp', new FormControl());
    this.httpClient.get('/api/anyfix/demander-service/listEnabledDemander/' + this.userService.currentCorp.corpId)
      .subscribe((res: any) => {
        this.customList = res.data;
      })
  }

  resetForm(): void {
    this.searchForm.reset();
  }

  querySettleCustom(params: any, reset: boolean = false) : void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-custom/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.settleCustomList = res.data.list;
        this.page.total = res.data.total;
      })
  }

  addSettleCustom(){
    const modal = this.nzModalService.create({
      nzTitle: '添加客户结算',
      nzWidth: 800,
      nzContent: SettleCustomAddComponent,
      nzComponentParams: {customList: this.customList},
      nzFooter: null
    })
    modal.afterClose.subscribe(result => {
      this.querySettleCustom(this.getParams(), false);
    });
  }

  getPageParams() {
    const params: any = {};
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  getParams() {
    const params: any = this.searchForm.value;
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  showDetail(settleCustom){
    const modal = this.nzModalService.create({
      nzTitle: '客户结算明细',
      nzWidth: 1000,
      nzContent: SettleCustomDetailComponent,
      nzComponentParams: {settleCustom},
      nzFooter: null
    })
  }

  delete(settleId) {
    this.loading = true;
    this.httpClient.delete(`/api/anyfix/settle-custom/${settleId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.messageService.success('删除成功');
      this.querySettleCustom(this.getParams(), false);
    })
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

}
