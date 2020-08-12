import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
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


@Component({
  selector: 'app-service-demander-create',
  templateUrl: 'service-demander-create.component.html'
})
export class ServiceDemanderCreateComponent implements OnInit {

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
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private modalService: NzModalService,
              private httpClient: HttpClient,
              private msg: NzMessageService,
              private userService: UserService) {
    this.validateForm = this.formBuilder.group({
      address : [null,[ZonValidators.required(),ZonValidators.maxLength(100)]],
      logoImg:[],
      corpName:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),ZonValidators.maxLength(50)]],
      shortName:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),ZonValidators.maxLength(8)]],
      telephone:[null,[ZonValidators.required(),ZonValidators.phoneOrMobile()]],
      industry: [null],
      business:[null,ZonValidators.required()],
      staffQty:[null],
      website:[],
      region: [null,[this.needCity,ZonValidators.required()]],
      regUserName:[null,[ZonValidators.required(), ZonValidators.notEmptyString(),ZonValidators.maxLength(6)]],
      mobile:[null,[ZonValidators.required(), ZonValidators.notEmptyString(),ZonValidators.phoneOrMobile()]],
      /*passwd:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),ZonValidators.maxLength(20),ZonValidators.minLength(6)]],
      confirm:[null,[ZonValidators.required(),ZonValidators.notEmptyString(),
        ZonValidators.maxLength(20),ZonValidators.minLength(6),this.passwordEqual]],*/
      description:[null,[ZonValidators.maxLength(200)]]
    });
  }

  ngOnInit(): void {
    this.listArea();
    this.listIndustry();
    this.listStaff();
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


  registrySubmit() {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
   this.httpClient.get('/api/uas/register/getPublicKey')
      .subscribe((res: any) => {
        this.doSubmit(res.data.publicKey);
      });
  }

  doSubmit(publicKey) {
    // 根据publicKey加密
    const params = this.getParams();
    const encrypt = new JsEncryptModule.JSEncrypt();
    encrypt.setPublicKey(publicKey);
    /*params.passwd = encrypt.encrypt(this.validateForm.value.passwd);*/
    params.publicKey = publicKey;
    // if (!params.logoImg) {
    //   this.msg.error('企业logo不能为空！请上传企业logo');
    //   return;
    // }
    this.spinning = true;
    this.httpClient.post('/api/anyfix/demander-service/create',params)
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
        this.validateForm.controls.region.setValue(rs.districts);
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
    /*params.confirm = null;*/
    params.logoImg = this.logo;
    const length = this.validateForm.value.region.length  ;
    params.district = this.validateForm.value.region[length-1];
    params.region = null;
    return params
  }
}
