import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ACLService} from '@delon/acl';


@Component({
  selector: 'app-work-support-record-add',
  templateUrl: 'work-support-record-add.component.html'
})
export class WorkSupportRecordAddComponent implements OnInit {

  @Input() supportId;
  @Input() severityName;
  @Input() description;

  validateForm: FormGroup;

  contactUserList: any[] = [];

  isSubmit = false;
  spinning = false;
  aclRight = ANYFIX_RIGHT;
  aclRightIdList: any[] = [];
  canCloseSupport = false;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private userService: UserService,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private aclService: ACLService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      supportType: ['Y', [Validators.required]],
      operator: ['', [Validators.required]],
      trackRecord: ['', [Validators.required, Validators.maxLength(200)]],
    });
  }

  ngOnInit() {
    this.aclRightIdList = this.aclService.data.abilities || [];
    this.canCloseSupport = this.aclRightIdList.includes(this.aclRight.WORK_SUPPORT_CLOSE);
    this.queryCorpUser();
  }


  submitForm(value: any) {
    this.isSubmit = true;
    if (this.validateForm.valid) {
      let params: {};
      params = {
        supportId:this.supportId,
        supportType:  this.validateForm.value.supportType,
        operator: this.validateForm.value.operator,
        trackRecord: this.validateForm.value.trackRecord
      };

      const url = '/api/anyfix/work-support-record/add';
      this.spinning = true;
      this.httpClient
        .post(url, params)
        .pipe(
          finalize(() => {
            this.spinning = false;
            this.cdf.markForCheck();
          })
        )
        .subscribe(() => {
          if(this.validateForm.value.supportType === 'N'){
            this.modal.destroy('close');
          }else {
            this.modal.destroy('submit');
          }
        });
    }
  }

  destroyModal() {
    this.modal.destroy({code: 1});
  }
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
