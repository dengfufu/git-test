import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {Result} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-ware-region-add',
  templateUrl: 'ware-area-add.component.html'
})
export class WareAreaAddComponent implements OnInit{

  validateForm: FormGroup;
  drawerVisible = false;
  enabledValue = true;
  userInfoList: any;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef,
              private userService: UserService,
              private cdf: ChangeDetectorRef) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      users: ['', [Validators.required]],
      description: [],
      sortNo: [],
      enabled: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.initData();
  }

  initData(){
    this.getUserInfo();
  }

  getUserInfo() {
    this.httpClient.get('/api/uas/corp-user/getCorpUser/' + this.userService.currentCorp.corpId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userInfoList = res.data;
      })
  }

  enabledChange(value){
    console.log(value);
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-area/insert',value)
      .subscribe((res: Result) => {
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
}
