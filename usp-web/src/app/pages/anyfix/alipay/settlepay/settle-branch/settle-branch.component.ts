import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {SettleBranchAddComponent} from './settle-branch-add/settle-branch-add.component';
import {SettleBranchDetailComponent} from './settle-branch-detail/settle-branch-detail.component';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-branch-settle',
  templateUrl: './settle-branch.component.html',
  styleUrls: ['./settle-branch.component.less']
})
export class SettleBranchComponent implements OnInit {

  settleBranchList: any[];
  pageSize = 10;
  pageNum = 1;
  total = 0;
  searchForm: FormGroup;
  serviceCorp = this.userService.currentCorp.corpId;
  loading = false;
  branchList: any[];
  visible = false;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private nzModalService: NzModalService,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private messageService: NzMessageService) {}

  ngOnInit() {
    this.querySettleBranch(this.getPageParam(), false);
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('branchId', new FormControl());
    this.httpClient.get('/api/anyfix/service-branch/listBranches/' + this.serviceCorp)
      .subscribe((res: any) => {
        this.branchList = res.data;
      })
  }

  resetForm(): void {
    this.searchForm.reset();
  }

  querySettleBranch(params: any, reset: boolean): void {
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-branch/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.settleBranchList = res.data.list;
        this.total = res.data.total;
    })
  }

  getParams() {
    const params = this.searchForm.value;
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.serviceCorp = this.serviceCorp;
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    return params;
  }

  addSettleBranch() {
    const modal = this.nzModalService.create({
      nzTitle: '添加网点结算',
      nzWidth: 800,
      nzContent: SettleBranchAddComponent,
      nzComponentParams: {serviceCorp: this.serviceCorp, branchList: this.branchList},
      nzFooter: null
    })
    modal.afterClose.subscribe(result => {
      this.querySettleBranch(this.getParams(), false);
    });
  }

  showDetail(settleBranch) {
    const modal = this.nzModalService.create({
      nzTitle: '网点结算明细',
      nzWidth: 1000,
      nzContent: SettleBranchDetailComponent,
      nzComponentParams: {settleBranch},
      nzFooter: null
    })
  }

  delete(settleId) {
    this.loading = true;
    this.httpClient.delete(`/api/anyfix/settle-branch/${settleId}`)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.messageService.success('删除成功');
        this.querySettleBranch(this.getParams(), false);
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
