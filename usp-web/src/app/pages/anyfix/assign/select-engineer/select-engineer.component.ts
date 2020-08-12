import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {Checkbox, Page} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-select-engineer',
  templateUrl: './select-engineer.component.html',
  styleUrls: ['./select-engineer.component.less']
})
export class SelectEngineerComponent implements OnInit {

  @Input() workId: any;
  @Input() serviceBranch: any;
  @Input() engineerCheckedList: any[];

  // 分页
  page: Page = new Page();
  loading = true;
  engineerList: any = [];
  checkBox = new Checkbox();
  searchForm: FormGroup;
  serviceBranchOptions = [];
  serviceBranchOptionsLoading = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private aclService: ACLService) {
    this.searchForm = this.formBuilder.group({
      serviceBranch: [null, [Validators.required]], // 必须有服务站才能查询
      userName: []
    });
  }

  ngOnInit(): void {
    this.engineerCheckedList.forEach((item: any) => {
      this.checkBox.mapOfCheckedId[item.userId] = true;
    });
    if (this.serviceBranch) {
      this.searchForm.patchValue({
        serviceBranch: this.serviceBranch.branchId
      })
    }
    // 默认查询工单分配服务站的人员
    this.selectEngineerList(true);
    // 加载服务网点下拉数据
    this.searchServiceBranch('');
  }

  // 加载服务网点下拉框
  searchServiceBranch(event) {
    const params: any = {branchName: event || ''};
    params.pageSize = 50;
    this.serviceBranchOptionsLoading = true;
    this.httpClient.post('/api/anyfix/service-branch/query', params)
      .pipe(
        finalize(() => {
          this.serviceBranchOptionsLoading = false;
        })
      ).subscribe((res: any) => {
      this.serviceBranchOptions = res.data.list;
    });
  }

  selectEngineerList(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    const params = this.getPageParam();
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-assign/engineers', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        }))
      .subscribe((res: any) => {
        if (res.data != null) {
          this.engineerList = res.data.list;
          this.page.total = res.data.total;
          if (this.engineerList) {
            this.engineerList.forEach(item => {
              this.checkBox.dataIdList.push(item.userId);
            });
          }
        }
      });
  }

  getPageParam() {
    const params: any = this.searchForm.value;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.workId = this.workId;
    return params;
  }

  checkEngineer(event, user: any) {
    if (event) {
      this.engineerCheckedList = [...this.engineerCheckedList, user];
    } else {
      this.deleteEngineer(user.userId);
    }
  }

  checkAll(event) {
    if (event) {
      this.engineerCheckedList = [
        ...this.engineerCheckedList,
        ...this.engineerList.filter((item: any) => !this.checkBox.mapOfCheckedId[item.userId])
      ];
    } else {
      this.engineerCheckedList = [
        ...this.engineerCheckedList.filter((item: any) => !this.checkBox.dataIdList.includes(item.userId))
      ];
    }
  }

  // 删除选中的工程师
  deleteEngineer(userId: string) {
    this.engineerCheckedList = this.engineerCheckedList.filter((item: any) => item.userId !== userId);
    this.checkBox.mapOfCheckedId[userId] = false;
    this.checkBox.refreshStatus();
  }

  submitForm(): void {
    this.modal.destroy({data: this.engineerCheckedList});
  }

  destroyModal(): void {
    this.modal.destroy('cancel');
  }

}
