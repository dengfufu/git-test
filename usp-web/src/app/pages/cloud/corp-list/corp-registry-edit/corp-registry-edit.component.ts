import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, UploadFile} from 'ng-zorro-antd';
import {format} from 'date-fns';
import {ZorroUtils} from '@util/zorro-utils';
import {ZonValidators} from '@util/zon-validators';
import {AreaService} from '@core/area/area.service';
import {Observable, Observer} from 'rxjs';
import {environment} from '@env/environment';
import {isNull} from '@util/helpers';

@Component({
  selector: 'app-corp-registry-edit',
  templateUrl: 'corp-registry-edit.component.html'
})
export class CorpRegistryEditComponent implements OnInit {

  ZorroUtils = ZorroUtils;

  @Input() corpId;
  spinning = false;
  validateForm: FormGroup;
  userOptions = [];

  detail: any = {};
  /*regDate: Date;
  regTime: Date;*/
  districtsList: any[];

  staffQtyList = [];
  industryList = [];

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
      corpName: [null, Validators.required],
      shortName: [null, Validators.required],
      telephone: [null, [Validators.required, ZonValidators.phoneOrMobile()]],
      address: [null, Validators.required],
      industry: [null, Validators.required],
      staffQty: [null, Validators.required],
      website: [],
      business: [null, Validators.required],
      /*regTime: [null, Validators.required],*/
      province: [null, [Validators.required]],
      city: [null, [Validators.required]],
      district: [],
      area: [null, [Validators.required]],
      logoImg: []
    });
    this.listIndustry();
    this.listStaff();
  }

  ngOnInit(): void {
    this.queryDetail();
    this.validateForm.controls.corpId.setValue(this.corpId);
    // 行政区划
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsList = res.data;
      });
  }

  /**
   * 初始化企业数据
   */
  queryDetail() {
    this.spinning = true;
    this.httpClient
      .get('/api/uas/corp-registry/' + this.corpId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        if (this.detail.logoImg !== undefined && this.detail.logoImg !== null
          && this.detail.logoImg !== '0' && this.detail.logoImg !== '') {
          this.fileList = [
            {
              uid: -1,
              name: 'Wait',
              status: 'done',
              url: this.baseFileUrl + this.detail.logoImg
            }
          ];
          this.logo = this.detail.logoImg;
        }
        /*if (this.detail.regTime) {
          this.regDate = new Date(format(this.detail.regTime, 'YYYY-MM-DD'));
          this.regTime = new Date(format(this.detail.regTime, 'YYYY-MM-DD HH:mm'));
          this.validateForm.controls.regTime.setValue(this.regTime);
        }*/
        this.validateForm.patchValue({
          corpName: this.detail.corpName,
          shortName: this.detail.shortName,
          telephone: this.detail.telephone,
          address: this.detail.address,
          website: this.detail.website,
          business: this.detail.business,
          province: this.detail.province,
          city: this.detail.city,
          district: this.detail.district
        });
        this.validateForm.controls.industry.setValue(this.detail.industry);
        this.validateForm.controls.staffQty.setValue(this.detail.staffQty);
        let area = this.detail.province ? this.detail.province : '';
        area = this.detail.city ? this.detail.city : area;
        area = this.detail.district ? this.detail.district : area;
        const areaArray: string[] = this.areaService.getAreaListByDistrict(area);
        this.validateForm.patchValue({
          area: areaArray
        });
      });
  }

  listIndustry() {
    this.httpClient.get('/api/uas/industry/list')
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        if (res.data) {
          res.data.forEach(item => {
            this.industryList.push(
              {
                value: item.name,
                text: item.code
              }
            );
          });
        }
      });
  }

  listStaff() {
    this.httpClient.get('/api/uas/corp-registry/staff-size/list').pipe(
      finalize(() => {

      })
    ).subscribe((res: any) => {
      if (res.data) {
        res.data.forEach(item => {
          this.staffQtyList.push(
            {
              value: item.name,
              text: item.code
            }
          );
        });
      }
    });
  }

  matchUser(userName?: string) {
    const payload = {
      corpId: this.corpId,
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
        this.userOptions = res.data;
      });
  }

  onChanges(values: any): void {
    if (!values) {
      return;
    }
    this.validateForm.controls.province.setValue(values[0]);
    this.validateForm.controls.city.setValue(values[1]);
    this.validateForm.controls.district.setValue(values[2]);
  }

  submitForm(): void {
    // 判断是否有logo
    if (this.logo !== '' && this.logo !== '0') {
      this.validateForm.patchValue({
        logoImg: this.logo
      });
    } else {
      this.msg.error('请上传企业LOGO！');
      return;
    }
    /*// 组装注册时间
    if (this.regDate && this.regTime) {
      const hour = this.regTime.getHours();
      const minute = this.regTime.getMinutes();
      this.regDate.setHours(hour, minute, 0, 0);
      this.validateForm.patchValue({
        regTime: this.regDate
      });
    } else {
      this.msg.error('请选择注册时间！');
      return;
    }*/
    this.spinning = true;
    this.httpClient
      .post('/api/uas/corp-registry/update',
        this.validateForm.value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.modal.destroy('submit');
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
