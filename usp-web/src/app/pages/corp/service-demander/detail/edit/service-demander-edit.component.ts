import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup,FormControl} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';
import {ZonValidators} from '@util/zon-validators';
import {AddressParser} from '@util/address-parser';
import {Observable, Observer} from 'rxjs';
import {UploadFile} from 'ng-zorro-antd/upload';
import * as JsEncryptModule from 'jsencrypt';
import { ZorroUtils } from '@util/zorro-utils';
import {environment} from '@env/environment';
import {AreaService} from '@core/area/area.service';


@Component({
  selector: 'app-service-demander-create',
  templateUrl: 'service-demander-edit.component.html'
})
export class ServiceDemanderEditComponent implements OnInit {

  @Input() detail: any = {};
  ZorroUtils = ZorroUtils;
  validateForm: FormGroup;
  branchList: Array<any>;
  serviceCorpList: Array<any>;
  customCorpList: Array<any>;
  loading= false;
  customFieldDataList: any[] = [];
  fileList: any[] = [];
  demanderCorp = this.userService.currentCorp.corpId;
  spinning = false;
  districtsList: any[];
  logo: string;
  previewVisible = false;
  previewImage : any;
  avatarUrl: string;
  industryList: any[];
  staffList : any[];
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  baseFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  isCanEditCorp =  false;
  imgList: any = [];
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private msg: NzMessageService,
              private areaService: AreaService,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      id: [null,ZonValidators.required()],
      corpId: [null,ZonValidators.required()],
      address : [null,[ZonValidators.required(),ZonValidators.maxLength(100)]],
      logoImg:[],
      corpName:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),ZonValidators.maxLength(50)]],
      shortName:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),ZonValidators.maxLength(8)]],
      telephone:[null,[ZonValidators.required(),ZonValidators.phoneOrMobile()]],
      industry: [null],
      business:[null,ZonValidators.required()],
      staffQty:[null],
      website:[],
      area: [null,[this.needCity,ZonValidators.required()]],
      description:[null,[ZonValidators.maxLength(200)]],
      enabled: [null,[ZonValidators.required()]]
    });
  }

  ngOnInit(): void {
    this.listArea();
    this.listIndustry();
    this.listStaff();
    this.initData();
  }


  initData() {
    this.validateForm.patchValue({
      id: this.detail.id,
      corpId: this.detail.demanderCorp,
      corpName: this.detail.demanderCorpName,
      shortName: this.detail.demanderShortCorpName,
      telephone: this.detail.telephone,
      address: this.detail.address,
      website: this.detail.website,
      industry: this.detail.industry,
      business: this.detail.business,
      province: this.detail.province,
      city: this.detail.city,
      district: this.detail.district,
      description: this.detail.description,
      staffQty: this.detail.staffQty,
      enabled: this.detail.enabled === 'Y'
    });
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
    let region ;
    if( this.detail.district && this.detail.district !== '') {
      region = this.detail.district
    } else if(this.detail.city && this.detail.city !== '') {
      region = this.detail.city
    }
    const areaArray: string[] = this.areaService.getAreaListByDistrict(region);
    this.validateForm.patchValue({
      area: areaArray
    });
    this.isCanEditCorp = this.detail.demanderLevel === 2
  }

  updateDemanderRequest() {
    const params: any = {};
    params.id = this.detail.id;
    params.enabled = this.validateForm.value.enabled ? 'Y' : 'N';
    params.description = this.validateForm.value.description;
    params.id = this.detail.id;
    this.httpClient
      .post('/api/anyfix/demander-service/update', params )
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

  listIndustry() {
    this.httpClient.get('/api/uas/industry/list')
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        this.industryList = res.data;
      });

  }

  listStaff() {
    this.httpClient.get('/api/uas/corp-registry/staff-size/list').pipe(
      finalize(() => {

      })
    ).subscribe((res: any) => {
      this.staffList = res.data;
    });
  }

  handleChange(info: { file: UploadFile }): void {
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

  removeFile = () => {
    this.fileList.splice(0, 1);
    this.logo = '';
    return true;
  };

  getBase64(img: File, callback: (img: string) => void): void {
    const reader = new FileReader();
    // tslint:disable-next-line:no-non-null-assertion
    reader.addEventListener('load', () => callback(reader.result!.toString()));
    reader.readAsDataURL(img);
  }

  handlePreview = (file: UploadFile) => {
    this.previewImage = file.url || file.thumbUrl;
    this.previewVisible = true;
  };

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


  listArea() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtsList = res.data;
      });
  }

  submitForm(): void {
    if (this.isCanEditCorp) {
      this.updateDemanderRequest();
      return;
    }
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    // 判断是否有logo

    this.validateForm.patchValue({
        logoImg: this.logo !== '' ? this.logo :'0'
    });
    this.spinning = true;
    this.httpClient
      .post('/api/uas/corp-registry/update',
        this.getParams())
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.updateDemanderRequest();
      });
  }


  doSubmit() {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    const params = this.getParams();
    this.spinning = true;
    this.httpClient.post('/api/anyfix/demander-service/update/corp',params)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe(() => {
        this.destroyModal('update');
      });
  }


  destroyModal(msg:string): void {
    this.modal.destroy(msg);
  }


  /**
   * 地址解析行政区划
   */
  addressParse() {
    const rs  = AddressParser.parseDistrict(this.validateForm.value.address, this.districtsList);
    if (rs) {
      if (rs.districts) {
        this.validateForm.controls.area.setValue(rs.districts);
      }
      // this.validateForm.patchValue({
      //   address: rs.address
      // });
    }
  }

   /*passwordEqual(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    // tslint:disable-next-line:no-non-null-assertion
    if (control && control.value !== control.parent.get('passwd')!.value) {
      return {equal: true, error: true, explain: '与管理密码不相同' };
    }
    return null;
  }*/

  needCity(control: FormControl) {
    if (!control || !control.parent) {
      return null;
    }
    // tslint:disable-next-line:no-non-null-assertion
    if (control && control.value && control.value.length < 2) {
      return {needCity: true, error: true, explain: '需选择城市' };
    }
    return null;
  }

  getParams() {
    const params = Object.assign({},this.validateForm.value);
    const values = this.validateForm.controls.area.value;
    if (!values) {
      return;
    }
    params.province = values[0];
    if (values[1]) {
      params.city = values[1];
    } else {
      params.city = '';
    }
    if (values[2]) {
      params.district = values[2];
    } else {
      params.district = ''
    }
    return params
  }


  getImageUrls(imageList: any[]) {
    const temp: string[] = [];
      imageList.forEach((file: any) => {
        temp.push(file.url);
      });
    return temp;
  }

}
