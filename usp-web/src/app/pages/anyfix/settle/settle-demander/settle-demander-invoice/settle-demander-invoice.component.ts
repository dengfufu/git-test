import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService, UploadChangeParam, UploadFile} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {Result} from '@core/interceptor/result';
import {SettleService} from '../settle.service';
import {UserService} from '@core/user/user.service';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ZonValidators} from '@util/zon-validators';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-anyfix-settle-demander-invoice',
  templateUrl: './settle-demander-invoice.component.html'
})
export class SettleDemanderInvoiceComponent implements OnInit {

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
              public datePipe: DatePipe,
              public userService: UserService,
              public settleService: SettleService) {
    this.form = formBuilder.group({
      settleId: [null, [ZonValidators.required()]],
      invoiceUser: [null, [ZonValidators.required('开票人')]],
      invoiceTime: [null, [ZonValidators.required('开票时间')]],
      invoiceFileIdList: []
    });
  }

  ngOnInit() {
    this.form.patchValue({
      settleId: this.settleId,
      invoiceUser: this.userService.userInfo.userId,
      invoiceTime: new Date()
    });
    this.initSettleDemander();
    // this.matchCorpUser();
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
      .subscribe((res: any) => {
        this.userList = res.data || [];
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
      });
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 提交
  submit() {
    const fileIdList: any[] = [];
    this.fileList.forEach((file: any) => {
      if (file.response && file.response.data) {
        fileIdList.push(file.response.data.fileId);
      }
    });
    const date = this.datePipe.transform(this.form.get('invoiceTime').value, 'yyyy-MM-dd');
    this.form.patchValue({
      invoiceTime: date,
      invoiceFileIdList: fileIdList
    });
    this.spinning = true;
    this.httpClient.post('/api/anyfix/settle-demander-payment/invoice', this.form.value)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      ).subscribe((res: any) => {
      this.nzMessageService.success('开票成功！');
      this.modalRef.destroy('submit');
    });
  }

}
