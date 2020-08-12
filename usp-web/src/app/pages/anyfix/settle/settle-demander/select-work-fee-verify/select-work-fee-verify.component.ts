import {Component, Input, OnInit} from '@angular/core';
import {Page} from '@core/interceptor/result';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-select-work-fee-verify',
  templateUrl: './select-work-fee-verify.component.html',
  styleUrls: ['./select-work-fee-verify.component.less']
})
export class SelectWorkFeeVerifyComponent implements OnInit {

  // 查询条件
  @Input() verifyFilter: any;
  @Input() selectedVerifyList: any[];
  // 列表当前页数据
  workFeeVerifyList: any[] = [];
  // 列表加载中
  loading = false;
  // 列表分页
  page = new Page();
  // 是否全选
  isAllChecked = false;
  // 是否部分选中
  isIndeterminate = false;
  // 选中的对账单id
  setOfCheckedId = new Set<string>();

  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    public userService: UserService
  ) {
  }

  ngOnInit() {
    this.queryVerify(false);
    this.selectedVerifyList.forEach((item: any) => {
      this.setOfCheckedId.add(item.verifyId);
    })
  }

  // 查询工单
  queryVerify(reset: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    Object.assign(this.verifyFilter, {pageNum: this.page.pageNum, pageSize: this.page.pageSize});
    this.httpClient.post('/api/anyfix/work-fee-verify/queryCanSettleVerify', this.verifyFilter)
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.workFeeVerifyList = res.data.list;
      this.page.total = res.data.total;
    });
  }

  updateCheckedSet(item: any, checked: boolean): void {
    if (checked) {
      this.setOfCheckedId.add(item.verifyId);
      this.selectedVerifyList = [...this.selectedVerifyList, item];
    } else {
      this.setOfCheckedId.delete(item.verifyId);
      this.selectedVerifyList = this.selectedVerifyList.filter((data: any) => data.verifyId !== item.verifyId);
    }
  }

  onAllChecked(value: boolean): void {
    this.workFeeVerifyList.forEach(item => this.updateCheckedSet(item, value));
    this.refreshCheckedStatus();
  }

  onCurrentPageDataChange($event: any[]): void {
    this.workFeeVerifyList = $event;
    this.refreshCheckedStatus();
  }

  onItemChecked(item: any, checked: boolean): void {
    this.updateCheckedSet(item, checked);
    this.refreshCheckedStatus();
  }

  refreshCheckedStatus() {
    this.isAllChecked = this.workFeeVerifyList.every(item => this.setOfCheckedId.has(item.verifyId));
    this.isIndeterminate = this.workFeeVerifyList.some(item => this.setOfCheckedId.has(item.verifyId)) && !this.isAllChecked;
  }

  // 关闭页面
  cancel() {
    this.modalRef.destroy();
  }

  // 提交
  submit() {
    console.log(this.setOfCheckedId, this.selectedVerifyList);
    this.modalRef.destroy({
      data: [...this.selectedVerifyList],
      role: 'submit'
    });
  }

}
