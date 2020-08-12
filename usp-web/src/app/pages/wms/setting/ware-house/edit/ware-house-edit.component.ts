import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-ware-house-edit',
  templateUrl: 'ware-house-edit.component.html'
})
export class WareHouseEditComponent implements OnInit{

  @Input() wareHouse;
  validateForm: FormGroup;
  enabledValue = true;
  depotInfoList;
  userInfoList;
  areaInfoList;
  selectedDepotValue;
  selectedUserValue;
  selectAreaValue;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef,
              private userService: UserService,
              private cdf: ChangeDetectorRef) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: ['', [Validators.required]],
      parentId: [],
      users: ['', [Validators.required]],
      enabled: ['', [Validators.required]],
      address: [],
      telephone: []
    });
  }
  ngOnInit(): void {
    this.validateForm.patchValue(this.wareHouse);
    // 后端Y N 转换成前端true false
    this.enabledValue = this.wareHouse.enabled === 'Y' ? true : false;
    this.selectedDepotValue = this.wareHouse.parentId;
    this.selectedUserValue = this.wareHouse.users;
    // this.selectAreaValue = this.wareHouse.
    this.initData();
  }

  initData(){
    this.getDepotInfo();
    this.getUserInfo();
    this.getAreaInfo();
  }

  getDepotInfo() {
    this.httpClient.get('/api/wms/ware-depot/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.depotInfoList = res.data;
      })
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

  getAreaInfo() {
    this.httpClient.get('/api/wms/ware-area/list')
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.areaInfoList = res.data;
      })
  }

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-depot/update', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }
}
