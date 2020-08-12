import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {AnyfixService} from '@core/service/anyfix.service';

@Component({
  selector: 'app-corp-regUser-edit',
  templateUrl: 'corp-regUser-edit.component.html'
})
export class CorpRegUserEditComponent implements OnInit {

  @Input() corpId: any;
  @Input() userId: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;

  regUserOptionList: any;

  nzFilterOption = () => true;

  constructor(
    public anyfixService: AnyfixService,
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private cdf: ChangeDetectorRef,
    private formBuilder: FormBuilder,
    private modalService: NzModalService,
    private messageService:NzMessageService
  ) {
    this.form = this.formBuilder.group({
      regUserId: [],
      regUser: [null, Validators.required]
    })
  }

  ngOnInit() {
    this.httpClient
      .get('/api/uas/user-info/detail/' + this.userId)
      .subscribe((res: any) => {
        if(res.data){
          const user = res.data;
          const listOfOption: Array<{value: string; text: string}> = [];
          listOfOption.push({
            value: user.userId,
            text: user.userName + '(' + user.mobile + ')'
          });
          this.regUserOptionList = listOfOption || [];
          this.form.controls.regUser.setValue(user.userId);
        }
      });
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  submit() {
    this.isLoading = true;
    const params = this.form.value;
    params.corpId = this.corpId;
    // 提交表单
    this.httpClient.post('/api/uas/corp-registry/update', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.messageService.success('修改成功');
        this.modalRef.close('submit');
      }
    });
  }

  matchRegUser(phone) {
    const params = {mobile: phone};
    this.httpClient
      .post('/api/uas/user-info/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data) {
          const list = [];
          res.data.forEach(item => {
            list.push({
              value: item.userId,
              text: item.userName + '(' + item.mobile + ')'
            });
          });
          this.regUserOptionList = list;
        }
      });
  }

  changeRegUser(event) {
    if (event !== undefined && event !== null) {
      this.form.controls.regUserId.setValue(event);
    }
  }
}
