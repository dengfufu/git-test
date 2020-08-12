import {Component, Input, OnInit} from '@angular/core';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-settle-demander-select-work',
  templateUrl: './select-work.component.html',
  styleUrls: ['./select-work.component.less']
})
export class SelectWorkComponent implements OnInit {

  // 查询条件
  @Input() workFilter: any;
  @Input() selectedWorkList: any[];
  // 列表当前页数据
  workFeeList: any[] = [];
  // 列表所有数据
  allWorkFeeList: any[] = [];
  // 列表加载中
  loading = false;
  // 列表分页
  page = new Page();
  isAllChecked = false;
  isIndeterminate = false;
  setOfCheckedId = new Set<string>();

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    public userService: UserService
  ) {
  }

  ngOnInit() {
    this.queryWork(false);
    this.getAllWork();
    console.log(this.selectedWorkList);
    this.selectedWorkList.forEach((work: any) => {
      this.setOfCheckedId.add(work.workId);
    })
  }

  // 查询工单
  queryWork(reset: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    Object.assign(this.workFilter, {pageNum: this.page.pageNum, pageSize: this.page.pageSize});
    this.httpClient.post('/api/anyfix/work-fee/query', this.workFilter)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.workFeeList = res.data.list || [];
      this.page.total = res.data.total;
    });
  }

  // 查询所有可结算的工单
  getAllWork() {
    this.httpClient.post('/api/anyfix/work-fee-verify/listCanVerifyWork', this.workFilter)
      .subscribe((res: any) => {
        this.allWorkFeeList = res.data.workFeeDtoList || [];
      })
  }

  updateCheckedSet(id: string, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(id);
    } else {
      this.setOfCheckedId.delete(id);
    }
  }

  onAllChecked(value: boolean): void {
    this.workFeeList.forEach(item => this.updateCheckedSet(item.workId, value));
    this.refreshCheckedStatus();
  }

  onCurrentPageDataChange($event: any[]): void {
    this.workFeeList = $event;
    this.refreshCheckedStatus();
  }

  onItemChecked(id: string, checked: boolean): void {
    this.updateCheckedSet(id, checked);
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus() {
    this.isAllChecked = this.workFeeList.every(item => this.setOfCheckedId.has(item.workId));
    this.isIndeterminate = this.workFeeList.some(item => this.setOfCheckedId.has(item.workId)) && !this.isAllChecked;
  }

  // 关闭页面
  cancel() {
    this.modalRef.destroy();
  }

  // 提交
  submit() {
    const detailList = [];
    let confirmNum = 0;
    let checkNum = 0;
    this.allWorkFeeList.forEach((work: any) => {
      if (this.setOfCheckedId.has(work.workId)) {
        if (work.feeConfirmStatus === 2) {
          confirmNum++;
        }
        if (work.feeCheckStatus === 2) {
          checkNum++;
        }
        detailList.push(work);
      }
    });
    this.modalRef.destroy({
      detailList,
      checkNum,
      confirmNum
    });
  }

}
