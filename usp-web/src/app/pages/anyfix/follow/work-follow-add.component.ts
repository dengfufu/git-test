import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-work-follow',
  templateUrl: './work-follow-add.component.html',
  styleUrls: ['./work-follow-add.component.less'],
})
export class WorkFollowAddComponent implements OnInit {

  @Input() work: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;

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
      followRecord: ['', [Validators.required]]
    })
  }

  ngOnInit() {

  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  submit() {
    this.isLoading = true;
    const params = this.form.value;
    params.workId = this.work.workId;
    // 提交表单
    this.httpClient.post('/api/anyfix/work-follow/add', params)
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
}
