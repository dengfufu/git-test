import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, UploadFile} from 'ng-zorro-antd';
import {Observable, Observer} from 'rxjs';
import {FileService} from '@core/service/file.service';

@Component({
  selector: 'app-ware-model-add',
  templateUrl: 'ware-model-add.component.html'
})
export class WareModelAddComponent implements OnInit{

  validateForm: FormGroup;
  drawerVisible = false;
  enabledValue = true;
  avatarUrl: string;
  loading= false;
  fileList: Array<any> = [];
  logo: string;
  previewVisible = false;
  previewImage : any;
  /*文件上传路径*/
  uploadAction = '/api/file/uploadFile';
  brandList;
  catalogList;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef,
              private msg: NzMessageService,
              private fileService: FileService) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      catalogId: ['', [Validators.required]],
      brandId: ['', [Validators.required]],
      haveSn: ['', [Validators.required]],
      haveBarcode: ['', [Validators.required]],
      description: [],
      enabled: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.initData();
  }

  initData(){
    this.getBrand();
    this.getCatalog();
  }

  getBrand(){
    this.httpClient.get('/api/wms/ware-brand/list')
      .subscribe((res: any) => {
        this.brandList = res.data;
    })
  }

  getCatalog(){
    this.httpClient.get('/api/wms/ware-catalog/list')
      .subscribe((res: any) => {
        this.catalogList = res.data;
      })
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-model/insert', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }

  clearForm() {
    this.validateForm.reset();
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  beforeUpload = (file: File) => {
    return new Observable((observer: Observer<boolean>) => {
      const isJPG = file.type === 'image/png' || file.type === 'image/jpeg';
      if (!isJPG) {
        this.msg.error('You can only upload JPG or PNG file!');
        observer.complete();
        return;
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.msg.error('Image must smaller than 2MB!');
        observer.complete();
        return;
      }
      // check height
      this.checkImageDimension(file).then(dimensionRes => {
        if (!dimensionRes) {
          this.msg.error('Image only 300x300 above');
          observer.complete();
          return;
        }

        observer.next(isJPG as boolean && isLt2M as boolean && dimensionRes as boolean);
        observer.complete();
      });
    });
  }

  private checkImageDimension(file: File) {
    return new Promise(resolve => {
      const img = new Image(); // create image
      img.src = window.URL.createObjectURL(file);
      img.onload = () => {
        const width = img.naturalWidth;
        const height = img.naturalHeight;
        // tslint:disable-next-line:no-non-null-assertion
        window.URL.revokeObjectURL(img.src!);
        resolve(width === height && width >= 100);
      };
    });
  }

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
