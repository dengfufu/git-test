import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UploadFile} from 'ng-zorro-antd/upload';
import {Observable, Observer} from 'rxjs';
import {NzMessageService} from 'ng-zorro-antd/message';
import {environment} from '@env/environment';
import {ZonValidators} from '@util/zon-validators';
import {FileService} from '@core/service/file.service';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-brand-edit',
  templateUrl: 'device-brand-edit.component.html'
})
export class DeviceBrandEditComponent implements OnInit {

  @Input() deviceBrand;
  validateForm: FormGroup;
  selectedEnabledValue: any;
  avatarUrl: string;
  loading = false;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  logo = '';

  fileList: Array<any> = [];
  previewImage: any;
  previewVisible = false;

  spinning = false;

  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private msg: NzMessageService,
              private fileService: FileService) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: [null, [ZonValidators.required('品牌名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      corp: ['', [Validators.required]],
      shortName: ['', []],
      website: ['', [Validators.pattern('^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$'),
                     Validators.maxLength(50)]],
      description: ['', []],
      enabled: ['', []]
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
    this.initValue();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
      }
    });
  }

  initValue(){
    this.validateForm.controls.id.setValue(this.deviceBrand.id);
    this.logo = this.deviceBrand.logo;
    this.avatarUrl = this.baseFileUrl + this.deviceBrand.logo;
    this.validateForm.controls.name.setValue(this.deviceBrand.name);
    this.validateForm.controls.shortName.setValue(this.deviceBrand.shortName);
    this.validateForm.controls.corp.setValue(this.deviceBrand.corp);
    this.validateForm.controls.website.setValue(this.deviceBrand.website);
    this.validateForm.controls.description.setValue(this.deviceBrand.description);
    this.validateForm.controls.enabled.setValue(this.deviceBrand.enabled === 'Y');
    if (this.deviceBrand.logo === '0') {
    } else {
      this.fileList = [
        {
          uid: -1,
          name: 'Wait',
          status: 'done',
          url: this.baseFileUrl + this.deviceBrand.logo
        }
      ];
    }
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.logo = this.logo;
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-brand/update',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res) => {
        this.modal.destroy(res);
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

  private getBase64(img: File, callback: (img: string) => void): void {
    const reader = new FileReader();
    // tslint:disable-next-line:no-non-null-assertion
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }

  handleChange(info: {file: UploadFile}): void {
    console.log(info);
    switch (info.file.status) {
      case 'uploading':
        this.loading = true;
        break;
      case 'done':
        // Get this url from response in real world.
        // tslint:disable-next-line:no-non-null-assertion
        // this.getBase64(info.file!.originFileObj!, (img: string) => {
        //   this.loading = false;
        //   this.avatarUrl = img;
        // });
        this.loading = false;
        this.logo = info.file.response.data.fileId;
        /**/
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
