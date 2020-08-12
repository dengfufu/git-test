import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-work-support',
  templateUrl: './work-support.component.html',
  styleUrls: ['./work-support.component.less'],
})
export class WorkSupportComponent implements OnInit {

  @Input() work: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;

  contactUserList: any[] = [];

  Options: any = {
    severity: [
      {value: 1, text: '轻微'},
      {value: 2, text: '一般'},
      {value: 3, text: '严重'},
      {value: 4, text: '非常严重'}
    ]
  };

  nzFilterOption = () => true;

  constructor(
    public anyfixService: AnyfixService,
    private cdf: ChangeDetectorRef,
    private userService: UserService,
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder
  ) {
    this.form = this.formBuilder.group({
      handler: [],
      severity: ['', [Validators.required]],
      description: ['', [Validators.required]]
    })
  }

  ngOnInit() {
    this.queryCorpUser();
  }

  // 取消
  cancel() {
    this.modalRef.destroy({code: 1});
  }

  submit() {
    this.isLoading = true;
    const params = this.form.value;
    params.workId = this.work.workId;
    // 提交表单
    this.httpClient.post('/api/anyfix/work-support/add', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.modalRef.destroy({code: 0});
      }
    });
  }

  // 完成工单的技术支持
  /*completeSupport() {
    this.isLoading = true;
    const params = this.form.value;
    params.workId = this.work.workId;
    // 提交表单
    this.httpClient.post('/api/anyfix/work-support/finish', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.modalRef.destroy({code: 1});
      }
    });
  }*/

  // 查询企业人员
  queryCorpUser() {
    this.httpClient
      .post('/api/anyfix/work-support/support/user', {corpId: this.userService.currentCorp.corpId})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const listOfOption: Array<{value: string; text: string}> = [];
        res.data.forEach(item => {
          listOfOption.push({
            value: item.userId,
            text: item.userName
          });
        });
        this.contactUserList = listOfOption || [];
      })
  }
}
