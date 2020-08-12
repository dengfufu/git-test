import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-device-service-edit',
  templateUrl: 'device-service-edit.component.html'
})
export class DeviceServiceEditComponent implements OnInit {

  @Input() deviceInfo;
  validateForm: FormGroup;
  branchList: any;
  userInfoList: any;
  branchId: any;

  spinning = false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      deviceId: [],
      serviceBranch: [],
      workManager: [],
      engineer: [],
      serviceNote: []
    });
  }

  initValue() {
    this.validateForm.patchValue({
      deviceId: this.deviceInfo.deviceId,
      serviceBranch: this.deviceInfo.serviceBranch,
      workManager: this.deviceInfo.workManager,
      engineer: this.deviceInfo.engineer,
      serviceNote: this.deviceInfo.serviceNote
    });
    this.branchId = this.deviceInfo.serviceBranch;
  }

  ngOnInit(): void {
    this.initValue();
    this.loadServiceBranch();
    this.loadManagerInfoList();
    this.loadEgnInfoList();
  }

  loadServiceBranch(){
    const params = {
      serviceCorp: this.deviceInfo.serviceCorp,
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.branchList = res.data;
        let match = false;
        this.branchList.forEach(option => {
          if(option.branchId === this.deviceInfo.serviceBranch){
            match = true;
          }
        });
        if(!match && this.deviceInfo.serviceBranch !== '0'){
          this.branchList.unshift({
            branchId : this.deviceInfo.serviceBranch,
            branchName: this.deviceInfo.serviceBranchName
          });
        }               
      });
  }

  matchServiceBranch(name?: string) {
    if (this.deviceInfo.serviceCorp === 0 || this.deviceInfo.serviceCorp === null) {
      return;
    }
    const params = {
      serviceCorp: this.deviceInfo.serviceCorp,
      branchName: name
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.branchList = res.data;             
      });
  }

  loadManagerInfoList(){
    const params = {
      corpId: this.deviceInfo.serviceCorp,
      branchId: this.branchId,
    };
    this.httpClient
      .post('/api/anyfix/service-branch-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userInfoList = res.data;
        let match = false;  
        this.userInfoList.forEach(option => {
          if(option.userId === this.deviceInfo.workManager){
            match = true;
          }       
        });
        if(!match && this.deviceInfo.workManager!== '0'){
          this.userInfoList.unshift({
            userId : this.deviceInfo.workManager,
            userName : this.deviceInfo.workManagerName
          })
        }     
      });
  }

  loadEgnInfoList(){
    const params = {
      corpId: this.deviceInfo.serviceCorp,
      branchId: this.branchId,
    };
    this.httpClient
      .post('/api/anyfix/service-branch-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userInfoList = res.data;
        let match = false;  
        this.userInfoList.forEach(option => {
          if(option.userId === this.deviceInfo.engineer){
            match = true;
          }       
        });
        if(!match && this.deviceInfo.engineer!== '0'){
          this.userInfoList.unshift({
            userId : this.deviceInfo.engineer,
            userName : this.deviceInfo.engineerName
          })
        }     
      });
  }

  matchUser(filter?: string) {
    if (!this.branchId) {
      return;
    }
    const params = {
      corpId: this.deviceInfo.serviceCorp,
      branchId: this.branchId,
      matchFilter: filter
    };
    this.httpClient
      .post('/api/anyfix/service-branch-user/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userInfoList = res.data;  
        console.log("this.userInfoList:---"+this.userInfoList); 
        console.log("this.deviceInfo:---"+this.deviceInfo)
      });
  }

  branchChange(event) {
    if (!event) {
      this.userInfoList = [];
      return;
    }
    this.branchId = event;
    this.matchUser();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-service/update',
        value)
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

  destroyModal(): void {
    this.modal.destroy();
  }

}
