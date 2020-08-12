import {ChangeDetectorRef, Component, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormGroup, FormControl} from '@angular/forms';
import {HttpClient, HttpResponse} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzMessageService, NzModalRef, NzTreeNodeOptions} from 'ng-zorro-antd';
import {environment} from '@env/environment';
import {UploadFile} from 'ng-zorro-antd/upload';
import {saveAs} from 'file-saver';
import {WorkSysTypeEnum} from '@core/service/enums.service';

/**
 * 修改附件
 */
@Component({
  selector: 'app-file-edit',
  templateUrl: 'file-edit.component.html',
  styleUrls: ['file-edit.component.less']
})
export class FileEditComponent implements OnInit {

  @Input() workId: any;
  @Input() groupId  = null;
  @Input() demanderCorp  = null;
  @Input() serviceCorp  = null;
  @Input() configId  = null;
  @Input() workType  = null;

  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  filesStatus = 0;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  previewImage: any;
  previewVisible = false;
  fileList: any[] = [];
  imgFileList: any[] = [];
  notImgFileList: any[] = [];
  mapForUpdate: any = {};
  work: any;
  finishTipItem: any = {};
  groupFilesDto :any = {};
  PASS_STATUS = 1;
  FAIL_STATUS = 2;
  forFinish = false;
  description = '';
  constructor(
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private nzMessageService: NzMessageService
  ) {
    this.form = this.formBuilder.group({
      files: [], // 附件,
    })
  }

  ngOnInit() {
    this.forFinish = this.configId == null;
    if(!this.configId) {
      this.getDataForWorkDetail();
      this.setFinishFileConfig();
    } else {
      this.getDataForGroup();
    }
  }

  getDataForWorkDetail() {
    this.isLoading = true;
    this.httpClient
      .get('/api/anyfix/work-request/detail/' + this.workId)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res && res.data) {
          this.work = res.data;
          const list = this.work.finishFileList || [];
          this.initFileList(list);
          this.formDataBuild();
        }
      });
  }

  getDataForGroup() {
    this.isLoading = true;
    this.httpClient
      .get('/api/anyfix/work-files/list/' + this.configId + '/' + this.workId)
      .pipe(
        finalize(() => {
          this.isLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.groupFilesDto = res.data;
        if (res.data && res.data.fileInfoDtosList) {
          const list: any[] = res.data.fileInfoDtosList;
          this.initFileList(list);
        }
      });
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  submit() {
    if(this.configId) {
      this.submitForFileGroup()
    } else {
      this.submitForFile();
    }
  }

  submitForFileGroup() {
    if (!this.validateFileGroup()){
      return false;
    }
    const params = this.buildFileGroupParams();
    this.isLoading = true;
    // 提交表单
    this.httpClient.post('/api/anyfix/work-files/update', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.nzMessageService.success('修改成功');
        this.modalRef.close('submit');
      }
    });
  }

  buildFileGroupParams() {
    const param: any = {};
    const newFileIdList = [];
    const deletedFileIdList = [];
    param.nowFileIdList = this.fileList;
    param.id = this.groupId;
    Object.keys(this.mapForUpdate).map(key => {
      if (!this.mapForUpdate[key].isOld) {
        newFileIdList.push(key);
      }
      if(this.mapForUpdate[key].isDeleted) {
        deletedFileIdList.push(key);
      }
    });
    param.newFileIdList = newFileIdList;
    param.deletedFileIdList = deletedFileIdList;
    param.configId = this.groupFilesDto.configId;
    param.workId = this.workId;
    param.id = this.groupId;
    param.serviceCorp = this.serviceCorp;
    param.demanderCorp = this.demanderCorp;
    param.workType = this.workType;
    return param;
  }

  submitForFile() {
    // 组装form 值
    const isValid = this.validateFinishFile(this.fileList);
    if(!isValid) {
      this.nzMessageService.error(this.finishTipItem.description);
      return ;
    }
    this.formDataBuild();
    const params = this.form.value;
    params.workId = this.workId;
    params.filesStatus = this.filesStatus;
    this.isLoading = true;
    // 提交表单
    this.httpClient.post('/api/anyfix/work-finish/mod?type=file', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.nzMessageService.success('修改成功');
        this.modalRef.close('submit');
      }
    });
  }


  validateFinishFile(fileList) {
    if (fileList.length >= this.finishTipItem.minNum) {
      this.filesStatus = this.PASS_STATUS;
      return true;
    } else {
      this.filesStatus = this.FAIL_STATUS;
      return false;
    }
  }

  setFinishFileConfig() {
      let minNum ;
      let description;
      if (this.workType === WorkSysTypeEnum.MAINTENANCE) {
        minNum = 3;
        description = '请至少上传一张设备故障照片、一张设备正常照片、一张维护工单照片！';
      } else if (this.workType === WorkSysTypeEnum.INSTALL) {
        minNum = 2;
        description = '请至少上传一张设备照片和一张安装验收单照片！';
      } else if (this.workType === WorkSysTypeEnum.PATROL) {
        minNum = 2;
        description = '请至少上传一张设备照片和一张巡检验收单照片！';
      } else {
        minNum = 1;
        description = '请至少上传一张照片！';
      }
      this.finishTipItem = {
        minNum,
        description
      };
      this.description = this.finishTipItem.description;
  }

  initFileList(list) {
    if(!list || list.length < 1) {
      return;
    }
    const idList: any[] = [];
    const imgList: any[] = [];
    const notImgList: any[] = [];
    list.forEach(file => {
      const nzFile: any = {};
      nzFile.uid = file.fileId;
      nzFile.fileId = file.fileId;
      nzFile.status = 'done';
      nzFile.ifDb = 'Y';
      nzFile.url = this.baseFileUrl + file.fileId;
      if (file.format === 1) {
        nzFile.name = '';
        nzFile.thumbUrl = this.baseFileUrl + file.fileId;
        imgList.push(nzFile);
      } else {
        nzFile.name = file.fileName;
        notImgList.push(nzFile);
      }
      idList.push(file.fileId);
      this.mapForUpdate[file.fileId] = {
        isOld: true,
        isDeleted: false
      }
    });
    this.imgFileList = [...imgList];
    this.notImgFileList = [...notImgList];
    this.fileList = [...idList];
  }

  handleChange(info: {file: UploadFile}) {
    switch (info.file.status) {
      case 'uploading':
        break;
      case 'done':
        if (info.file && info.file.response && info.file.response.data) {
          if (info.file.response.data.format !== 1) {
            this.notImgFileList.push(this.changeFile(info.file.response.data));
            this.imgFileList.splice(this.imgFileList.length - 1, 1);
          }
        }
        this.getFileIdList(info, 1);
        break;
      case 'removed':
        this.getFileIdList(info, 2);
        break;
      case 'error':
        break;
    }
    this.notImgFileList = [...this.notImgFileList];
  }

  /**
   * 获得最终的文件编号列表
   * @param file 文件
   * @param type 类型，1=添加 2=删除
   */
  getFileIdList(info: any, type: number) {
    if (info) {
      let fileId;
      if (info.file.ifDb === 'Y') {
        fileId = info.file.fileId;
      } else {
        if (info.file.response && info.file.response.data) {
          fileId = info.file.response.data.fileId;
        }
      }
      if (type === 1) {
        this.fileList.push(fileId);
        this.mapForUpdate[fileId] = {
          isOld: false,
          isDeleted: false
        };
      } else if (type === 2) {
        const index = this.fileList.indexOf(fileId);
        if (index > -1) {
          this.mapForUpdate[fileId].isDeleted = true;
          this.fileList.splice(index, 1);
        }
      }
    }
  }

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };


  // 下载非图片格式文件
  downloadFile = (file: UploadFile) => {
    let isFileSaverSupported = false;
    try {
      isFileSaverSupported = !!new Blob();
    } catch {
    }
    if (!isFileSaverSupported) {
      console.log('浏览器版本过低');
      return;
    }
    this.httpClient.request('get', file.url, {
      params: {},
      responseType: 'blob',
      observe: 'response'
    }).subscribe(
      (res: HttpResponse<Blob>) => {
        if (res.status !== 200 || (res.body && res.body.size <= 0)) {
          return;
        }
        const disposition = this.getDisposition(res.headers.get('content-disposition'));
        let fileName = file.name;
        fileName =
          fileName || disposition[`filename*`] || disposition[`filename`] || res.headers.get('filename') || res.headers.get('x-filename');
        saveAs(res.body, decodeURI(fileName as string));
      },
      err => this.nzMessageService.error(err)
    );
  };

  changeFile(file) {
    const nzFile: any = {};
    nzFile.uid = file.fileId;
    nzFile.fileId = file.fileId;
    nzFile.status = 'done';
    nzFile.url = this.baseFileUrl + file.fileId;
    nzFile.name = file.fileName;
    return nzFile;
  }

  private getDisposition(data: string | null) {
    const arr: Array<{}> = (data || '').split(';').filter(i => i.includes('=')).map(v => {
      const strArr = v.split('=');
      const utfId = `UTF-8''`;
      let value = strArr[1];
      if (value.startsWith(utfId)) {
        value = value.substr(utfId.length);
      }
      return {[strArr[0].trim()]: value};
    });
    // tslint:disable-next-line:variable-name
    return arr.reduce((_o, item) => item, {});
  }

  formDataBuild(): void {
    // 组装文件
    let filesString = '';
    this.fileList.forEach((fileId: any) => {
      if (filesString) {
        filesString = filesString + ',' + fileId;
      } else {
        filesString = fileId;
      }
    });
    this.form.patchValue({
      files: filesString
    });
  }

  beforeUpload = (file: UploadFile) => {
    if (file.size / 1024 / 1024 > 20) {
      this.nzMessageService.error('文件大小不能超过20MB!');
      return false;
    }
    // console.log(file.type);
    const types = ['image/png', 'image/jpeg'];
    if (types.includes(file.type.toLowerCase())) {
      return true;
    }
    this.nzMessageService.error('请上传JPG或PNG图片文件!');
    return false;
  };

  getFileConfigData() {
    // this.httpClient
    //   .post('/api/anyfix/file-config/demander/list', this.getFileConfigParams())
    //   .pipe(
    //     finalize(() => {
    //     })
    //   )
    //   .subscribe((res: any) => {
    //     this.fileConfigList = res.data || [];
    //     this.setFileConfigForModel();
    //   });
  }

  getFileConfigParams() {
    const params: any = {};
    params.serviceCorp = this.work.serviceCorp;
    params.demanderCorp = this.work.demanderCorp;
    params.workType = this.work.workSysType;
    params.formType = 1;
    return params;
  }

  validateFileGroup() {
    if(this.imgFileList.length < this.groupFilesDto.minNum) {
      this.nzMessageService.error(this.groupFilesDto.groupName + '至少需要' + this.groupFilesDto.minNum + '张');
      return false;
    }
    if(this.imgFileList.length > this.groupFilesDto.maxNum) {
      this.nzMessageService.error(this.groupFilesDto.groupName + '最多允许' + this.groupFilesDto.maxNum + '张')
      return false;
    }
    return true;
  }

}

