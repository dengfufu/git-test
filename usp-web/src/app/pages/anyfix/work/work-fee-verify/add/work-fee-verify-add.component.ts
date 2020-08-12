import { Component, OnInit } from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {finalize} from 'rxjs/operators';
import {AnyfixService} from '@core/service/anyfix.service';
import {SelectWorkComponent} from '../select-work/select-work.component';
import {WorkCheckService} from '../../work-check.service';
import {DemanderContNoComponent} from '../../../settle/settle-demander/demander-cont-no/demander-cont-no.component';
import {ACLService} from '@delon/acl';
import {ANYFIX_RIGHT} from '@core/right/right';

@Component({
  selector: 'app-work-fee-verify-add',
  templateUrl: './work-fee-verify-add.component.html',
  styleUrls: ['./work-fee-verify-add.component.less']
})
export class WorkFeeVerifyAddComponent implements OnInit {

  // 表单
  form: FormGroup;
  // 委托商下拉列表
  demanderOptions: any[] = [];
  // 委托商下拉列表加载中
  demanderOptionLoading = false;
  // 行政区划下拉列表
  districtOptions: any[] = [];
  // 提交加载中
  submitLoading = false;
  // 第一步
  step = 1;
  // 工单数量
  workQuantity = 0;
  // 已确认工单数
  confirmNum = 0;
  // 未确认工单数
  unConfirmNum = 0;
  // 未审核工单数
  uncheckNum = 0;
  // 明细
  detailList: any[] = [];
  // 权限列表
  rightIdList = this.aclService.data.abilities;
  // 委托协议号
  contNo = '';
  // 委托协议编号
  contId = '';

  constructor(
    public userService: UserService,
    public httpClient: HttpClient,
    public messageService: NzMessageService,
    public modalRef: NzModalRef,
    public formBuilder: FormBuilder,
    public modalService: NzModalService,
    public anyfixService: AnyfixService,
    public workCheckService: WorkCheckService,
    public aclService: ACLService
  ) {
    this.form = this.formBuilder.group({
      // verifyName: [null, [ZonValidators.required('对账单名称'), ZonValidators.maxLength(50, '对账单名称')]],
      demanderCorp: [null, [ZonValidators.required('委托商')]],
      startDate: [null, [ZonValidators.required('起始日期')]],
      endDate: [null, [ZonValidators.required('截止日期')]],
      district: [],
      note: [null, [ZonValidators.maxLength(200, '备注')]]
    });
  }

  ngOnInit() {
    this.listDemander();
    this.listDistrict();
  }

  // 加载委托商下拉选项
  listDemander() {
    this.demanderOptionLoading = true;
    // 客户经理添加只有自己负责的委托商范围
    if (this.rightIdList.includes(ANYFIX_RIGHT.WORK_FEE_VERIFY_MANAGER)
      && !this.rightIdList.includes(ANYFIX_RIGHT.WORK_FEE_VERIFY_ADD)) {
      const params = {
        managerId: this.userService.userInfo.userId,
        serviceCorp: this.userService.currentCorp.corpId
      };
      this.httpClient.post('/api/anyfix/demander-service-manager/listDemander', params)
        .pipe(
          finalize(() => {
            this.demanderOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          this.demanderOptions = res.data || [];
        });
    } else {
      this.httpClient.post('/api/anyfix/demander-service/demander/list',
        {serviceCorp: this.userService.currentCorp.corpId})
        .pipe(
          finalize(() => {
            this.demanderOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          this.demanderOptions = res.data;
        });
    }
  }

  // 委托商变更
  findCont(event) {
    if (this.form.value.demanderCorp && this.form.value.startDate && this.form.value.endDate) {
      const params = {
        serviceCorp: this.userService.currentCorp.corpId,
        demanderCorp: this.form.value.demanderCorp,
        startDate: this.form.value.startDate,
        endDate: this.form.value.endDate
      };
      this.httpClient.post('/api/anyfix/demander-cont/fee', params)
        .subscribe((res: any) => {
          if (res && res.data) {
            this.contNo = res.data.contNo || '';
            this.contId = res.data.id || '';
          } else {
            this.contNo = '';
            this.contId = '';
          }
        });
    } else {
      this.contNo = '';
      this.contId = '';
    }
  }

  // 加载行政区划下拉选项
  listDistrict() {
    this.httpClient.get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districtOptions = res.data;
      });
  }

  // 查看委托协议
  showCont() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 600,
      nzComponentParams: {
        // serviceCorp: this.userService.currentCorp.corpId,
        // demanderCorp: this.form.value.demanderCorp
        contId: this.contId
      },
      nzFooter: null
    });
  }

  // 选择工单
  selectWork() {
    if (!this.form.valid) {
      this.messageService.info('清先选择委托商，且填写对账周期！');
      return;
    }
    const modal = this.modalService.create({
      nzTitle: '选择对账工单',
      nzContent: SelectWorkComponent,
      nzWidth: 1000,
      nzComponentParams: {
        workFilter: this.getWorkFilter(),
        selectedWorkList: this.detailList
      },
      nzFooter: null
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        console.log(res);
        this.detailList = res.detailList || [];
        this.confirmNum = res.confirmNum;
        this.workQuantity = this.detailList.length;
        this.unConfirmNum = this.workQuantity - res.confirmNum;
        this.uncheckNum = this.workQuantity - res.checkNum;
      }
    });
  }

  // 更改步骤
  changeStep() {
    if (this.step === 1) {
      this.step = 2;
      this.httpClient.post('/api/anyfix/work-fee-verify/listCanVerifyWork', this.getWorkFilter())
        .subscribe((res: any) => {
          this.workQuantity = res.data.workQuantity || 0;
          this.confirmNum = res.data.confirmNum || 0;
          this.unConfirmNum = res.data.workQuantity - res.data.confirmNum;
          this.uncheckNum = res.data.workQuantity - res.data.checkNum;
          this.detailList = res.data.workFeeDtoList || [];
        })
    } else if (this.step === 2) {
      this.step = 1;
    }
  }

  getWorkFilter() {
    const params = {
      serviceCorp: this.userService.currentCorp.corpId,
      demanderCorp: this.form.value.demanderCorp,
      startDate: this.form.value.startDate,
      endDate: this.form.value.endDate,
      district: (this.form.value.district || []).length > 0 ?
        this.form.value.district[this.form.value.district.length - 1] : '',
      // 对账待评价、已完成的工单
      workStatuses: this.anyfixService.settleWorkStatusList.join(','),
      // 对账单次保和人天保的工单
      warrantyModes: '20,30',
      // feeCheckStatus: this.workCheckService.CHECK_PASS, // 查询所有已完成工单，不区分审没审核
      settleDemanderStatus: 1
    }
    return params;
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  // 提交
  submit() {
    const params = Object.assign({}, this.form.value);
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.district = params.district && params.district.length > 0 ? params.district[params.district.length - 1] : '';
    params.workFeeDtoList = this.detailList;
    if (!this.detailList || this.detailList.length <= 0) {
      this.messageService.error('请选择对账工单');
      return;
    }
    this.submitLoading = true;
    this.httpClient.post('/api/anyfix/work-fee-verify/add', params)
      .pipe(
        finalize(() => {
          this.submitLoading = false;
        })
      )
      .subscribe((res: any) => {
        this.messageService.success('添加成功！');
        this.modalRef.destroy('submit');
      })
  }

}
