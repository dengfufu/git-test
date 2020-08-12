import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService, UploadChangeParam, UploadFile} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {Result} from '@core/interceptor/result';
import {GoodsBase} from '../goods-base';
import {UserService} from '@core/user/user.service';
import {AreaService} from '@core/area/area.service';

@Component({
  selector: 'app-anyfix-goods-detail-add',
  templateUrl: 'goods-detail-add.component.html'
})
export class GoodsDetailAddComponent extends GoodsBase implements OnInit {

  @Input() boxNum = 1;

  isLoading = false;
  canSubmit = true;

  fileList: any[] = [];// 附件列表
  fileIdList: any[] = []; // 附件编号列表

  constructor(public formBuilder: FormBuilder,
              public cdf: ChangeDetectorRef,
              public httpClient: HttpClient,
              public nzMessageService: NzMessageService,
              public modalService: NzModalService,
              public modal: NzModalRef,
              public userService: UserService,
              public areaService: AreaService) {

    super(formBuilder, httpClient, nzMessageService, modalService, cdf, userService, areaService);

    this.validateForm = this.formBuilder.group({
      goodsName: [null, [ZonValidators.required('物品名称'),
        ZonValidators.maxLength(100, '物品名称')]],
      sn: [null, [ZonValidators.maxLength(50, '序列号')]],
      quantity: [1, [ZonValidators.required('数量'),
        ZonValidators.isInt('数量'),
        ZonValidators.min(1, '数量'),
        ZonValidators.max(999999, '数量')]],
      subBoxNum: [1, [ZonValidators.required('分箱号'),
        ZonValidators.isInt('分箱号'),
        ZonValidators.min(1, '分箱号'),
        ZonValidators.max(100, '分箱号')]],
      fileList: [],
      fileIdList: []
    });
  }

  ngOnInit(): void {
  }

  handleChange(info: UploadChangeParam): void {
    if (info.file.status === 'uploading') {
      this.canSubmit = false;
    } else {
      this.canSubmit = true;
    }
    let fileList = [...info.fileList];
    fileList = fileList.map(file => {
      if (file.response) {
        file.fileId = file.response.data.fileId;
        file.url = file.response.data.url;
        file.fileName = file.name;
        const index = file.fileName.lastIndexOf('.');
        const ext = file.fileName.substr(index + 1);
        if (!this.isAssetTypeAnImage(ext)) {
          file.thumbUrl = 'assets/anyfix/file.svg';
        }
      }
      return file;
    });
    this.fileList = fileList;
  }

  /**
   * 提交
   */
  submitForm(): void {
    this.fileList.forEach((file: UploadFile) => {
      if (file.response && file.response.data) {
        this.fileIdList.push(file.response.data.fileId);
      }
    });
    this.validateForm.patchValue({
      fileList: this.fileList,
      fileIdList: this.fileIdList
    });
    if (this.validateForm.valid) {
      const result = new Result();
      result.code = 0;
      result.data = this.validateForm.value;
      this.modal.close(result);
    } else {
      // tslint:disable-next-line:forin
      for (const i in this.validateForm.controls) {
        this.validateForm.controls[i].markAsDirty();
        this.validateForm.controls[i].updateValueAndValidity();
      }
    }
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}
