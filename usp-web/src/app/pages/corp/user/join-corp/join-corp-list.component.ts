import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd/modal';
import {JoinCorpCheckComponent} from './join-corp-check.component';
import {UserService} from '@core/user/user.service';
import {Page, Result} from '@core/interceptor/result';
import {UAS_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-join-corp-list',
  templateUrl: './join-corp-list.component.html',
  styleUrls: ['join-corp-list.component.less']
})
export class JoinCorpListComponent implements OnInit {

  aclRight = UAS_RIGHT;

  page = new Page();
  searchForm: FormGroup;

  corpApplyList: any[];
  statusOptionList = [{label: '申请中', value: 1},
    {label: '已同意', value: 2},
    {label: '已拒绝', value: 3}];
  statusList = [1];

  loading = true;
  visible = false;

  constructor(private http: HttpClient,
              private formBuilder: FormBuilder,
              private nzModalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      corpId: [],
      userName: [],
      applyTimeRange: [],
      statusList: [this.statusList]
    });
  }

  ngOnInit() {
    this.queryCorpApply();
  }

  queryCorpApply(reset?: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.http.post('/api/uas/corp-user/apply/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.corpApplyList = data.list;
        this.page.total = data.total;
      });
  }

  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.corpId = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  check(corpApply) {
    const modal = this.nzModalService.create({
      nzTitle: '加入企业审核',
      nzWidth: 800,
      nzOkText: '同意',
      nzCancelText: '拒绝',
      nzContent: JoinCorpCheckComponent,
      nzComponentParams: {
        corpApply
      },
      nzFooter: [
        {
          label: '拒绝',
          type: 'default',
          onClick: (event: any) => {
            this.checkSubmit(event, 'N');
            modal.close();
          }
        },
        {
          label: '同意',
          type: 'primary',
          onClick: (event: any) => {
            this.checkSubmit(event, 'Y');
            modal.close();
          }
        }
      ]
    });
  }

  checkSubmit(event, checkResult: string) {
    const params = event.checkForm.value;
    params.corpId = event.corpApply.corpId;
    params.userId = event.corpApply.applyUserId;
    params.checkResult = checkResult;
    params.txId = event.corpApply.txId;
    this.http.post('/api/uas/corp-user/join/audit', params).subscribe((result: Result) => {
      if (result != null && result.code === 0) {
        this.queryCorpApply(this.getParams());
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

}
