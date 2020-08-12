import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-join-corp-check',
  templateUrl: './join-corp-check.component.html'
})
export class JoinCorpCheckComponent implements OnInit {

  @Input() corpApply: any = {};
  roleList: Array<any> = [];
  serviceBranchList: Array<any> = [];
  deviceBranchList: Array<any> = [];

  checkForm: FormGroup;
  corpId = this.userService.currentCorp.corpId;
  nzFilterOption = () => true;

  constructor(
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private httpClient: HttpClient,
    private modal: NzModalService,
    private userService: UserService) {
    this.checkForm = this.formBuilder.group({
      userId: [this.corpApply.applyUserId],
      serviceBranchIdList: [],
      deviceBranchIdList: [],
      roleIdList: []
    });
  }

  ngOnInit() {
    this.matchServiceBranch();
    this.matchDeviceBranch();
    this.matchRole();
  }

  // 服务网点名称模糊下拉
  matchServiceBranch(value?: string): void {
    if (!value) {
      return;
    }
    const params = {
      serviceCorp: this.corpId,
      branchName: value
    };
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.serviceBranchList = res.data;
      });
  }

  // 设备网点模糊下拉
  matchDeviceBranch(value?: string): void {
    const payload = {
      matchFilter: value,
      customCorp: this.corpId
    };
    this.httpClient
      .post('/api/anyfix/device-branch/match', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.deviceBranchList = res.data;
      });
  }

  // 模糊查询角色下拉
  matchRole(roleName?: string) {
    const params = {
      matchFilter: roleName,
      corpId: this.corpId
    };
    this.httpClient
      .post('/api/uas/sys-role/match', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.roleList = res.data;
      });
  }
}
