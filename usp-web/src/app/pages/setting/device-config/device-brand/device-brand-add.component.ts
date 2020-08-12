import {ChangeDetectorRef, Component, OnInit} from '@angular/core'
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UploadFile} from 'ng-zorro-antd/upload';
import {Observable, Observer} from 'rxjs';
import {NzMessageService} from 'ng-zorro-antd/message';
import {ZonValidators} from '@util/zon-validators';
import {FileService} from '@core/service/file.service';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-brand-add',
  templateUrl: 'device-brand-add.component.html',
  styleUrls: ['device-brand-add.component.less']
})
export class DeviceBrandAddComponent implements OnInit {

  validateForm: FormGroup;
  selectedEnabledValue = 'Y';
  avatarUrl: string;
  loading= false;
  fileList: Array<any> = [];
  logo: string;
  previewVisible = false;
  previewImage : any;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';

  spinning = false;

  demanderCorpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private msg: NzMessageService,
              private fileService: FileService) {
    this.validateForm = this.formBuilder.group({
      logo: [],
      name: [null, [ZonValidators.required('品牌名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      shortName: ['', []],
      website: ['', [Validators.pattern('^(?=^.{3,255}$)[a-zA-Z0-9][-a-zA-Z0-9]{0,62}(\\.[a-zA-Z0-9][-a-zA-Z0-9]{0,62})+$'),
                        Validators.maxLength(50)]],
      description: ['', []],
      enabled: [true, []],
      corp:[null,ZonValidators.required('委托商')]
    });
  }

  ngOnInit(): void {
    this.listDemanderCorp();
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            corp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
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
      .post('/api/device/device-brand/add',
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

      observer.next(isJPG as boolean && isLt2M as boolean );
      observer.complete();

    });
  };

  private getBase64(img: File, callback: (img: string) => void): void {
    const reader = new FileReader();
    // tslint:disable-next-line:no-non-null-assertion
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }

  removeFile = (file: UploadFile) => {
    this.fileList.splice(0, 1);
    this.logo = '';
    return true;
  }

  handleChange(info: { file: UploadFile }): void {
    console.log(info);
    switch (info.file.status) {
      case 'uploading':
        this.loading = true;
        break;
      case 'done':
        // Get this url from response in real world.
        // tslint:disable-next-line:no-non-null-assertion
        this.getBase64(info.file!.originFileObj!, (img: string) => {
          this.loading = false;
          this.avatarUrl = img;
        });
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

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

}
