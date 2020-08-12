import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-ware-region-edit',
  templateUrl: 'ware-area-edit.component.html'
})
export class WareAreaEditComponent implements OnInit{

  @Input() wareArea;
  validateForm: FormGroup;
  enabledValue = true;
  userInfoList;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private modal: NzModalRef,
              private userService: UserService,
              private cdf: ChangeDetectorRef) {
    this.validateForm = this.formBuilder.group({
      id: [],
      name: ['', [Validators.required]],
      users: ['', [Validators.required]],
      description: [],
      sortNo: [],
      enabled: ['', [Validators.required]]
    });
  }
  ngOnInit(): void {
    this.validateForm.patchValue(this.wareArea);
    // 后端Y N 转换成前端true false
    this.enabledValue = this.wareArea.enabled === 'Y' ? true : false;
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

  submitForm(value) {
    // switch控件的True、False转换成Y N
    value.enabled = value.enabled ? 'Y' : 'N';
    this.httpClient.post('/api/wms/ware-area/update', value)
      .subscribe(() => {
        this.modal.destroy('submit');
      })
  }

  destroyModal() {
    this.modal.destroy();
  }
}
