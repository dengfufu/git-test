import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService, UploadChangeParam, UploadFile} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {SettleService} from '../settle.service';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-anyfix-settle-demander-pay',
  templateUrl: './settle-demander-pay.component.html'
})
export class SettleDemanderPayComponent implements OnInit {

  @Input() settleId: any;

  form: FormGroup;

  // 加载中
  spinning = false;
  canSubmit = true;

  uploadAction = '/api/file/uploadFile';
  previewImage: string | undefined = '';
  previewVisible = false;

  settleDemander: any = {};
  fileList: any[] = []; // 文件列表

  userList: any[] = []; // 用户列表

  nzFilterOption = () => true;

  constructor(public formBuilder: FormBuilder,
              public httpClient: HttpClient,
              public nzMessageService: NzMessageService,
              public modalRef: NzModalRef,
              public modalService: NzModalService,
              public cdf: ChangeDetectorRef,
              public userService: UserService,
              public settleService: SettleService) {
    this.form = formBuilder.group({
      settleId: [null, [ZonValidators.required()]],
      payer: [null, [ZonValidators.required('付款人')]],
      payerName: [null, [ZonValidators.required('付款人')]],
      payTime: [null, [ZonValidators.required('付款时间')]],
      payFileIdList: []
    });
  }

  ngOnInit() {
    this.form.patchValue({
      settleId: this.settleId,
      payer: this.userService.userInfo.userId,
      payerName: this.userService.userInfo.userName,
      payTime: new Date()
    });
    this.initSettleDemander();
    this.matchCorpUser();
  }

  initSettleDemander() {
    this.spinning = true;
    this.httpClient.get('/api/anyfix/settle-demander/' + this.settleId)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      ).subscribe((result: Result) => {
      this.settleDemander = result.data;
    });
  }

  uploadHandleChange(info: UploadChangeParam): void {
    if (info.file.status === 'uploading') {
      this.canSubmit = false;
    } else {
      this.canSubmit = true;
    }
  }

  handlePreview = async (file: UploadFile) => {
    const index = file.name.lastIndexOf('.');
    const ext = file.name.substr(index + 1);
    if (this.settleService.isAssetTypeAnImage(ext)) {
      if (!file.url && !file.preview) {
        file.preview = await this.settleService.getBase64(file.originFileObj!);
      }
      this.previewImage = file.url || file.preview;
      this.previewVisible = true;
    } else {
      this.settleService.downloadConfirm(file);
    }
  };

  /**
   * 付款人改变
   */
  payerChange() {
    const payer: string = this.form.get('payer').value || '';
    if (payer.trim() !== '') {
      this.userList.forEach((user: any) => {
        if (user.userId === payer) {
          let userName: string = user.userName || '';
          if (userName.startsWith('[') && userName.endsWith(']')) {
            userName = userName.substring(1, userName.length - 1);
          }
          this.form.patchValue({
            payerName: userName
          });
        }
      });
    }
  }

  /**
   * 模糊查询企业人员
   */
  matchCorpUser(userName?: string) {
    const payload = {
      corpId: this.userService.currentCorp.corpId,
      matchFilter: userName
    };
    this.httpClient
      .post('/api/uas/corp-user/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((result: Result) => {
        this.userList = result.data || [];
        userName = userName || '';
        if (userName.trim() !== '') {
          let match = false;
          this.userList.forEach(option => {
            if (option.userName === userName) {
              match = true;
            }
          });
          if (!match) {
            this.userList.unshift({userId: '0', userName: '[' + userName + ']'});
          }
        } else {
          // 当前用户
          let match = false;
          this.userList.forEach((user: any) => {
            if (user.userId === this.userService.userInfo.userId) {
              match = true;
            }
          });
          if (!match) {
            this.userList.unshift({
              userId: this.userService.userInfo.userId,
              userName: this.userService.userInfo.userName
            });
          }
        }
      });
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 提交
  submit() {
    const fileIdList: any[] = [];
    this.fileList.forEach((file: any) => {
      if (file.response && file.response.data) {
        fileIdList.push(file.response.data.fileId);
      }
    });
    this.form.patchValue({
      payFileIdList: fileIdList
    });
    this.spinning = true;
    this.httpClient.post('/api/anyfix/settle-demander-payment/pay', this.form.value)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      ).subscribe((res: any) => {
      this.nzMessageService.success('付款成功！');
      this.modalRef.destroy('submit');
    });
  }

}
