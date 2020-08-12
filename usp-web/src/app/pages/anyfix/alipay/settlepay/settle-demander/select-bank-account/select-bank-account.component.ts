import {Component, Input, OnInit} from '@angular/core';
import {Page} from '@core/interceptor/result';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

export class BankAccount {
  accountNumber: string;
  accountName: string;
  accountBank: string;
  serviceCorp?: string;
  checked?: boolean;
}
@Component({
  selector: 'app-select-bank-account',
  templateUrl: './select-bank-account.component.html',
  styleUrls: ['./select-bank-account.component.less']
})
export class SelectBankAccountComponent implements OnInit {

  // 选中的账户信息
  @Input() selectedAccount: BankAccount;
  // 账户信息列表
  accountList: BankAccount[];
  // 加载中
  loading = false;
  // 分页信息
  page = new Page();
  // 查询条件
  account = '';

  constructor(
    public httpClient: HttpClient,
    public modalRef: NzModalRef,
    public userService: UserService
  ) { }

  ngOnInit() {
    console.log(this.selectedAccount);
    this.queryAccount(true);
  }

  // 查询收款账户信息
  queryAccount(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/anyfix/settle-demander/list/account', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
        this.accountList = res.data.list;
        if (this.accountList && this.accountList.length > 0) {
          // 初始化选中
          if (this.selectedAccount) {
            this.accountList.forEach((account: BankAccount) => {
              if (this.selectedAccount.accountNumber === account.accountNumber
                && this.selectedAccount.accountName === account.accountName
                && this.selectedAccount.accountBank === account.accountBank) {
                account.checked = true;
              }
            })
          }
        }
        this.page.total = res.data.total;
    })
  }

  // 获取查询参数
  getParams() {
    const params = {
      pageNum: this.page.pageNum,
      pageSize: this.page.pageSize,
      serviceCorp: this.userService.currentCorp.corpId,
      accountFilter: this.account
    };
    return params;
  }

  // 更改选中
  checkChange(event, index) {
    console.log(event);
    if (event) {
      this.accountList.forEach((account: BankAccount, i) => {
        if (i === index) {
          account.checked = true;
          this.selectedAccount = account;
        } else {
          account.checked = false;
        }
      })
    } else {
      this.accountList[index].checked = false;
    }
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 选中提交
  submit() {
    this.modalRef.destroy(this.selectedAccount);
  }

}
