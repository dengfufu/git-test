import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';

import {Result} from '@core/interceptor/result';
import {UploadFile} from 'ng-zorro-antd/upload';
import {environment} from '@env/environment';
import {Observable, Observer} from 'rxjs';

@Component({
  selector: 'app-file-config-add',
  templateUrl: 'cont-config.component.html',
  styleUrls: ['cont-config.component.less']
})
export class ContConfigComponent implements OnInit {

  @Input() isForUpdate = false;
  @Input() data;
  @Input() refId;
  newFileIdList: any = [];
  deleteFileIdList: any = [];
  form: FormGroup;
  fileList: Array<any> = [];
  previewVisible = false;
  previewImage : any;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  spinning = false;
  imgFileList = [];
  serviceImgFileList = [];
  ADD = 1;
  DELETE = 2;
  confConfig: any = {};
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private messageService: NzMessageService,
              private httpClient: HttpClient) {
    this.form = this.formBuilder.group({
      id: [null],
      refId: [null, Validators.required],
      contNo: [null, [ZonValidators.maxLength(50), ZonValidators.required('委托协议号')]],
      feeRuleDescription:  [null, ZonValidators.maxLength(2000)],
      feeRuleFiles: [null],
      serviceStandardNote: [null, ZonValidators.maxLength(2000)],
      startDate: [null, [ZonValidators.required('起始日期')]],
      endDate: [null, [ZonValidators.required('结束日期')]],
    });
  }

  ngOnInit(): void {
    if (this.isForUpdate) {
      this.setData();
    } else {
      this.form.controls.refId.setValue(this.refId);
    }
  }


  /**
   * 委托商列表
   */
  // getContConfig() {
  //   this.httpClient.get(`/api/anyfix/demander-service/cont/getConfig/` + this.refId).subscribe((result: Result) => {
  //     this.confConfig = result.data;
  //     const list = this.confConfig.feeRuleFileList || [];
  //     const serviceList = this.confConfig.serviceStandardFileList || [];
  //     this.initImageFileList(list,'imgFileList');
  //     this.initImageFileList(serviceList,'serviceImgFileList');
  //
  //     this.patchValue();
  //   });
  // }

  setData() {
    this.confConfig = this.data;
    this.initImageFileList(this.confConfig.feeRuleFileList,'imgFileList');
    this.initImageFileList(this.confConfig.serviceStandardFileList,'serviceImgFileList');
    this.patchValue();
  }

  patchValue() {
    this.form.patchValue({
      id: this.confConfig.id,
      refId: this.confConfig.refId,
      contNo: this.confConfig.contNo,
      feeRuleDescription: this.confConfig.feeRuleDescription,
      serviceStandardNote: this.confConfig.serviceStandardNote,
      startDate: this.confConfig.startDate,
      endDate: this.confConfig.endDate
    });
  }

  submitForm(): void {
    // tslint:disable-next-line:forin
    for (const key in this.form.controls) {
      this.form.controls[key].markAsDirty();
      this.form.controls[key].updateValueAndValidity();
    }
    const endDate = this.form.controls.endDate.value;
    const startDate = this.form.controls.startDate.value;
    if(startDate > endDate) {
      this.messageService.error('起始日期不能大于结束日期');
      return;
    }
    this.spinning = true;
    const params = this.getParams();
    const suffix = this.isForUpdate ? 'update' : 'add';
    this.httpClient
      .post('/api/anyfix/demander-cont/' + suffix,
        params)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  beforeUpload = (file: File) => {
    return new Observable((observer: Observer<boolean>) => {
      const isJPG = file.type === 'image/png' || file.type === 'image/jpeg';
      if (!isJPG) {
        this.messageService.error('只能支持png或者jpeg格式的图片!');
        observer.complete();
        return;
      }
      if(this.imgFileList.length > 9 || this.serviceImgFileList.length > 9) {
        this.messageService.error('不能超过十张图片!');
        observer.complete();
        return;
      }
      observer.next(isJPG as boolean  );
      observer.complete();
    });
  };


  initImageFileList(list, key) {
    const imgList = [];
    list.forEach(fileId => {
      const nzFile: any = {};
      nzFile.uid = fileId;
      nzFile.fileId = fileId;
      nzFile.status = 'done';
      nzFile.url = this.baseFileUrl + fileId;
      nzFile.name = '';
      nzFile.thumbUrl = this.baseFileUrl +  fileId;
      imgList.push(nzFile);
    });
    this[key] = [...imgList];
  }



  destroyModal(): void {
    this.modal.destroy();
  }

  getParams() {
    const params = Object.assign({}, this.form.value);
    params.newFileIdList = this.newFileIdList;
    params.deleteFileIdList = this.deleteFileIdList;
    const fileIdList = this.appendFiles(this.imgFileList);
    const serviceFileIdList = this.appendFiles(this.serviceImgFileList);

    params.feeRuleFileList = fileIdList;
    params.serviceStandardFileList = serviceFileIdList;
    return params;
  }

  appendFiles(list) {
    const idList = [];
    list.forEach( file => {
      let fileId;
      if (file.response && file.response.data) {
        fileId = file.response.data.fileId;
      } else {
        fileId = file.fileId;
      }
      idList.push(fileId);
    });
    return idList;
  }


  handleChange(info: {file: UploadFile}) {
    switch (info.file.status) {
      case 'uploading':
        break;
      case 'done':
        this.getFileIdList(info, 1);
        break;
      case 'removed':
        this.getFileIdList(info, 2);
        break;
      case 'error':
        break;
    }
  }

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };


  /**
   * 获得最终的文件编号列表
   * @param file 文件
   * @param type 类型，1=添加 2=删除
   */
  getFileIdList(info: any, type: number) {
    if (info) {
      let fileId ;
      // 新增的文件信息
      if (info.file.response && info.file.response.data) {
        fileId = info.file.response.data.fileId;
      } else {
        fileId = info.file.fileId;
      }
      if (type === this.ADD) {
        this.newFileIdList.push(fileId);
      } else if (type === this.DELETE) {
        // 如果是新建但又没有用到的文件,不清除缓存表
        const indexNew  = this.newFileIdList.indexOf(fileId);
        if(indexNew > -1) {
          this.newFileIdList.splice(indexNew, 1);
        }
        this.deleteFileIdList.push(fileId);
      }
    }
  }
}
