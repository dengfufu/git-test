import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, UploadFile} from 'ng-zorro-antd';
import {AreaService} from '@core/area/area.service';
import { ZorroUtils } from '@util/zorro-utils';
import {Observable, Observer} from 'rxjs';
import {environment} from '@env/environment';

@Component({
  selector: 'app-corp-verify-edit',
  templateUrl: 'corp-verify-edit.component.html'
})
export class CorpVerifyEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;

  @Input() corpId;
  @Input() key;
  spinning = false;
  validateForm: FormGroup;
  userOptions = [];

  detail: any = {};
  fileList: Array<any> = [];
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  logo = '';
  loading = false;
  previewImage: any;
  previewVisible = false;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private msg: NzMessageService,
              private areaService: AreaService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      corpId: [null, Validators.required],
      larName: [null, Validators.required],
      larIdCard:[null, Validators.required],
      licenseFileId: [],
      creditCode: [],
      address: [null, Validators.required],
      foundDate: [null, Validators.required],
      expireDate: [null, Validators.required],
      key: [],
    });
  }

  ngOnInit(): void {
    if( this.key === 'edit'){
      this.queryDetail();
    }
    this.validateForm.controls.corpId.setValue(this.corpId);
    this.validateForm.controls.key.setValue(this.key);
  }

  /**
   * 初始化企业数据
   */
  queryDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-verify/' + this.corpId )
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        this.validateForm.patchValue({
          larName: this.detail.larName,
          larIdCard: this.detail.larIdCard,
          creditCode: this.detail.creditCode,
          address: this.detail.address,
          foundDate: this.detail.foundDate,
          expireDate: this.detail.expireDate
        });
        if (this.detail.licenseFileId !== undefined&&this.detail.licenseFileId !== null&&
          this.detail.licenseFileId !== '0' &&this.detail.licenseFileId !== '') {
          this.fileList = [
            {
              uid: -1,
              name: 'Wait',
              status: 'done',
              url: this.baseFileUrl + this.detail.licenseFileId
            }
          ];
          this.logo = this.detail.licenseFileId;
        }
      });
  }

  submitForm(): void {
    // 判断是否有营业执照
    if (this.logo !== '' &&this.logo !== '0') {
      this.validateForm.patchValue({
        licenseFileId: this.logo
      });
    }else {
      this.msg.error('请上传营业执照！');
      return;
    }
    this.spinning = true;
    this.httpClient
      .post('/api/uas/corp-verify/update',
        this.validateForm.value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res:any) => {
        if( this.key === 'edit'){
          this.modal.destroy('edit');
        }else if(this.key === 'verify') {
          this.modal.destroy('verify');
        }

      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  beforeUpload = (file: File) => {
    return new Observable((observer: Observer<boolean>) => {
      const isJPG = file.type === 'image/png' || file.type === 'image/jpeg';
      if (!isJPG) {
        this.msg.error('只能支持png或者jpeg格式的图片!');
        observer.complete();
        return;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.msg.error('图片文件大小需要小于2M!');
        observer.complete();
        return;
      }

      observer.next(isJPG as boolean && isLt2M as boolean);
      observer.complete();

    });
  };

  handleChange(info: {file: UploadFile}): void {
    switch (info.file.status) {
      case 'uploading':
        this.loading = true;
        break;
      case 'done':
        this.loading = false;
        this.logo = info.file.response.data.fileId;
        const obj = document.getElementById('logo');
        const oEvt = document.createEvent('HTMLEvents');
        oEvt.initEvent('change', true, true);
        obj.dispatchEvent(oEvt);
        break;
      case 'error':
        this.msg.error('Network error');
        this.loading = false;
        break;
    }
  }

  removeFile = (file: UploadFile) => {
    this.fileList.splice(0, 1);
    this.logo = '0';
    return true;
  };

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };
}
