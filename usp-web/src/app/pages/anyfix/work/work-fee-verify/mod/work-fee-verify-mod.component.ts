import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormGroup} from '@angular/forms';
import {ZonValidators} from '@util/zon-validators';
import {finalize} from 'rxjs/operators';
import {SelectWorkComponent} from '../select-work/select-work.component';
import {AnyfixService} from '@core/service/anyfix.service';
import {WorkCheckService} from '../../work-check.service';
import {DemanderContNoComponent} from '../../../settle/settle-demander/demander-cont-no/demander-cont-no.component';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-work-fee-verify-mod',
  templateUrl: './work-fee-verify-mod.component.html',
  styleUrls: ['./work-fee-verify-mod.component.less']
})
export class WorkFeeVerifyModComponent implements OnInit {

  // 对账单编号
  @Input() verifyId: string;
  // 表单
  form: FormGroup;
  // 对账单
  workFeeVerify: any = {};
  // 对账单明细
  detailList: any[] = [];
  // 数据加载中
  spinning = false;
  // 能否提交
  canSubmit = false;
  // 工单数量
  workQuantity = 0;
  // 已确认工单数量
  confirmNum = 0;
  // 已审核工单数量
  checkNum = 0;

  constructor(
    public httpClient: HttpClient,
    public modalRef: NzModalRef,
    public modalService: NzModalService,
    public messageService: NzMessageService,
    public anyfixService: AnyfixService,
    public workCheckService: WorkCheckService,
    public formBuilder: FormBuilder,
    public userService: UserService
  ) {
    this.form = this.formBuilder.group({
      note: [null, [ZonValidators.maxLength(200, '备注')]]
    });
  }

  ngOnInit() {
    this.findVerify();
  }

  // 获取对账单详情
  findVerify() {
    this.spinning = true;
    this.httpClient.get(`/api/anyfix/work-fee-verify/${this.verifyId}`)
      .pipe(
        finalize(() => {
          this.spinning = false;
        })
      )
      .subscribe((res: any) => {
        this.workFeeVerify = res.data;
        this.detailList = this.workFeeVerify.detailDtoList;
        this.form.patchValue({
          // verifyName: this.workFeeVerify.verifyName,
          note: this.workFeeVerify.note
        });
        this.confirmNum = this.workFeeVerify.confirmNum;
        this.checkNum = this.workFeeVerify.checkNum;
        this.workQuantity = this.workFeeVerify.workQuantity;
      });
  }

  // 查看委托协议
  showCont() {
    const modal = this.modalService.create({
      nzTitle: '查看委托协议',
      nzContent: DemanderContNoComponent,
      nzWidth: 600,
      nzComponentParams: {
        contId: this.workFeeVerify.contId
        // serviceCorp: this.workFeeVerify.serviceCorp,
        // demanderCorp: this.workFeeVerify.demanderCorp
      },
      nzFooter: null
    });
  }

  // 选择工单
  selectWork() {
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
        this.detailList = res.detailList || [];
        this.confirmNum = res.confirmNum;
        this.checkNum = res.checkNum;
        this.workQuantity = this.detailList.length;
      }
    });
  }

  // 获取查询工单条件
  getWorkFilter() {
    const params = {
      serviceCorp: this.workFeeVerify.serviceCorp,
      demanderCorp: this.workFeeVerify.demanderCorp,
      startDate: this.workFeeVerify.startDate,
      endDate: this.workFeeVerify.endDate,
      district: this.workFeeVerify.district,
      // 对账待评价、已完成的工单
      workStatuses: this.anyfixService.settleWorkStatusList.join(','),
      // 对账单次保和人天保的工单
      warrantyModes: '20,30',
      // feeCheckStatus: this.workCheckService.CHECK_PASS, // 已审核费用
      verifyId: this.verifyId // 包含当前对账单的工单
    }
    return params;
  }

  // 取消
  cancel() {
    this.modalRef.destroy();
  }

  // 提交
  submit() {
    const params = Object.assign({}, this.form.value);
    params.verifyId = this.verifyId;
    params.workFeeDtoList = this.detailList;
    this.canSubmit = false;
    this.httpClient.post('/api/anyfix/work-fee-verify/update', params)
      .pipe(
        finalize(() => {
          this.canSubmit = true;
        })
      ).subscribe((res: any) => {
        this.messageService.success('修改成功');
        this.modalRef.destroy('submit');
    })
  }

}
