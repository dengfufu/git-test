import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {UserService} from '@core/user/user.service';
import {Result} from '@core/interceptor/result';
import {ImplementTypeService} from '../implement-type.service';

@Component({
  selector: 'app-implement-fee-add',
  templateUrl: './implement-fee-add.component.html',
  styleUrls: ['./implement-fee-add.component.less']
})
export class ImplementFeeAddComponent implements OnInit {

  // add表示添加页面，mod表示修改页面
  @Input() type;
  // 实施发生费用编号
  @Input() implementId;

  // 表单
  form: FormGroup;
  // 委托商列表
  demanderCorpList: any[] = [];
  // 是否已被工单引用
  used = false;
  // 工单支出费用详情
  implementFee: any = {};

  constructor(
    public formBuilder: FormBuilder,
    public httpClient: HttpClient,
    public modalService: NzModalService,
    public userService: UserService,
    public modalRef: NzModalRef,
    public messageService: NzMessageService,
    public implementTypeService: ImplementTypeService
  ) {
    this.form = this.formBuilder.group({
      implementName: [null, [ZonValidators.required('费用名称'), ZonValidators.maxLength(50, '费用名称')]],
      implementType: [null, [ZonValidators.required('类别')]],
      serviceCorp: [],
      demanderCorp: [],
      enabled: [true, [ZonValidators.required('是否可用')]],
      note: ['', [ZonValidators.maxLength(200, '说明')]]
    });
  }

  ngOnInit() {
    this.listDemanderCorp();
    // 修改页面初始化数据
    if (this.type === 'mod') {
      this.getDetail();
    }
  }

  // 获取委托商列表
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        // 添加页面且只有一个委托商，默认选中
        if (this.type === 'add' && this.demanderCorpList.length === 1) {
          this.form.patchValue({
            demanderCorp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
  }

  // 获取工单支出费用详情
  getDetail() {
    this.httpClient.get(`/api/anyfix/work-fee-implement-define/${this.implementId}`)
      .subscribe((res: any) => {
        this.implementFee = res.data;
        this.used = this.implementFee.used;
        // 修改页面表单初始化
        this.form.patchValue({
          implementName: this.implementFee.implementName,
          implementType: this.implementFee.implementType,
          serviceCorp: this.implementFee.serviceCorp,
          demanderCorp: this.implementFee.demanderCorp,
          enabled: this.implementFee.enabled === 'Y' ? true : false,
          note: this.implementFee.note
        });
      });
  }

  // 费用类别变更
  typeChange(event) {
    if (event && (this.form.value.implementName || '').length <= 0 && !this.implementFee) {
      // 若费用名称为空，则自动填充
      this.form.controls.implementName.setValue(this.implementTypeService.implementTypeMap[event]);
    }
  }

  // 取消
  destroyModal() {
    this.modalRef.destroy('cancel');
  }

  // 添加或修改提交表单
  submitForm() {
    const params = Object.assign({}, this.form.value);
    // 服务商置为当前企业
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.enabled = this.form.value.enabled ? 'Y' : 'N';
    let postUrl = '/api/anyfix/work-fee-implement-define/add';
    let msg = '添加成功！';
    // 修改页面
    if (this.type === 'mod') {
      params.implementId = this.implementId;
      postUrl = '/api/anyfix/work-fee-implement-define/update';
      msg = '修改成功！';
    }
    console.log(params);
    this.httpClient.post(postUrl, params)
      .subscribe((res: any) => {
        this.messageService.success(msg);
        this.modalRef.destroy('submit');
      });
  }

}
