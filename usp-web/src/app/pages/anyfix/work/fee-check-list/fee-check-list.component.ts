import {ChangeDetectorRef, Component, OnInit, ViewChild} from '@angular/core';
import {ANYFIX_RIGHT} from '@core/right/right';
import {Page, Result} from '@core/interceptor/result';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {AnyfixService} from '@core/service/anyfix.service';
import {WorkCheckService} from '../work-check.service';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {ZorroUtils} from '@util/zorro-utils';
import {
  STProOverflowMode,
  STProColumn,
  STProData,
  STProReq,
  STProRes,
  STProColumnTag, STProRequestOptions, STProColumnBadge
} from '@shared/components/st-pro/st.pro.interfaces';
import {StProComponent} from '@shared/components/st-pro';
import {FeeCheckListFilterComponent} from './filter/fee-check-list-filter.component';

export const CHECK_STATUS_BADGE: STProColumnBadge = {
  待审核: {text: '待审核', color: 'processing'},
  审核通过: {text: '审核通过', color: 'success'},
  审核不通过: {text: '审核不通过', color: 'error'},
  待确认: {text: '待确认', color: 'processing'},
  确认通过: {text: '确认通过', color: 'success'},
  确认不通过: {text: '确认不通过', color: 'error'}
};
@Component({
  selector: 'app-work-fee-check-list',
  templateUrl: './fee-check-list.component.html',
  styleUrls: ['./fee-check-list.component.less']
})
export class FeeCheckListComponent implements OnInit {

  ZorroUtils = ZorroUtils;
  aclRight = ANYFIX_RIGHT;

  // 列表加载中
  loading = false;
  // 查询条件表单
  searchForm: FormGroup;
  // 筛选条件弹出
  visible = false;
  // 下拉选项
  optionList: any = {};

  params: any = {};

  // 选中的工单列表
  selectedWorkList: any[] = [];

  nzFilterOption = () => true;

  @ViewChild(FeeCheckListFilterComponent, {static: false}) feeCheckListFilterComponent: FeeCheckListFilterComponent;
  @ViewChild(StProComponent, {static: true}) stPro;

  page: Page = new Page(1, 20, [20, 50, 100, 200]);

  columns: STProColumn[] = [
    {
      index: 'id',
      type: 'checkbox',
      fixed: 'left',
      width: 50
    },
    {
      title: '序号',
      index: '_no',
      type: 'no',
      fixed: 'left',
      width: 50
    },
    {
      title: '状态',
      type: 'badge',
      fixed: 'left',
      width: 110,
      index: 'feeStatusName',
      badge: CHECK_STATUS_BADGE
    },
    {
      title: '工单编号',
      index: 'workCode',
      fixed: 'left',
      width: 150,
      type: 'link',
      click: (record: STProData) => {
        this.toWorkDetail(record.workId);
      }
    },
    {
      title: '委托单号',
      index: 'checkWorkCode',
      width: 150
    },
    {
      title: '委托商',
      index: 'demanderCorpName',
      width: 100
    },
    {
      title: '总费用',
      index: 'totalFee',
      type: 'currency',
      numberDigits: '1.0-2',
      className: 'text-red-dark',
      width: 120
    },
    {
      title: '基础服务费',
      index: 'workFeeDto.basicServiceFee',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '辅助人员费',
      index: 'workFeeDto.assortSupportFee',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '郊区交通费',
      index: 'workFeeDto.suburbTrafficExpense',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '长途交通费',
      index: 'workFeeDto.longTrafficExpense',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '住宿费',
      index: 'workFeeDto.hotelExpense',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '出差补助',
      index: 'workFeeDto.travelExpense',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '邮寄费',
      index: 'workFeeDto.postExpense',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '其他费用',
      index: 'workFeeDto.otherFee',
      type: 'currency',
      numberDigits: '1.0-2',
      width: 100
    },
    {
      title: '人天',
      index: 'manDay',
      width: 80
    },
    {
      title: '省',
      index: 'provinceName',
      width: 80
    }, {
      title: '市',
      index: 'cityName',
      width: 80
    },
    {
      title: '客户名称',
      index: 'customCorpName',
      width: 200
    }, {
      title: '设备类型',
      index: 'smallClassName',
      width: 150
    },
    {
      title: '型号',
      index: 'modelName',
      width: 150
    }, {
      title: '设备规格',
      index: 'specificationName',
      width: 100
    }, {
      title: '工单来源',
      index: 'sourceName',
      width: 80
    }, {
      title: '工单类型',
      index: 'workTypeName',
      width: 80
    }, {
      title: '创建人',
      index: 'creatorName',
      width: 80
    }, {
      title: '创建时间',
      index: 'createTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '设备网点',
      index: 'deviceBranchName',
      width: 150
    }, {
      title: '服务商',
      index: 'serviceCorpName',
      width: 100
    }, {
      title: '服务网点',
      index: 'serviceBranchName',
      width: 150
    }, {
      title: '工程师',
      index: 'engineerName',
      width: 100
    }, {
      title: '协同工程师',
      index: 'togetherEngineers',
      width: 100
    }, {
      title: '外部协同工程师',
      index: 'helpNames',
      width: 150
    }, {
      title: '预约时间',
      index: 'bookTimeEnd',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '提单时间',
      index: 'dispatchTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '服务开始时间',
      index: 'startTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '服务结束时间',
      index: 'endTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '完单时间',
      index: 'endTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    },
    {
      title: '服务请求',
      index: 'serviceRequest',
      width: 250
    }, {
      title: '联系人',
      index: 'contactName',
      width: 100
    }, {
      title: '联系电话',
      index: 'contactPhone',
      width: 150
    },
    {
      title: '维保方式',
      index: 'warrantyModeName',
      width: 100
    }, {
      title: '故障现象',
      index: 'faultTypeName',
      width: 150
    }, {
      title: '接单时间',
      index: 'acceptTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '出发时间',
      index: 'goTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '到达(签到)时间',
      index: 'signTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '完成情况',
      index: 'description',
      width: 200
    }, {
      title: '费用审核人',
      index: 'feeCheckUserName',
      width: 100
    }, {
      title: '费用审核时间',
      index: 'feeCheckTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '费用审核备注',
      index: 'feeCheckNote',
      width: 150
    }
  ];

  overflowMode: STProOverflowMode = 'truncate';

  scrollX: string;

  url = '/api/anyfix/work-request/fee-check/query';

  req: STProReq = {
    process: (option: STProRequestOptions) => {
      option.body = {...option.body, ...this.getParams()};
      return option;
    }
  };
  res: STProRes = {
    process: (data: STProData[], rawData?: any) => {
      data.forEach(e => {
        e.amt = Math.random() * 1000;
      });
      return data;
    }
  };

  constructor(
    private httpClient: HttpClient,
    private userService: UserService,
    private formBuilder: FormBuilder,
    private anyfixModuleService: AnyfixModuleService,
    public anyfixService: AnyfixService,
    public workCheckService: WorkCheckService,
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private modalService: NzModalService,
    private messageService: NzMessageService) {
    this.searchForm = this.formBuilder.group({
      feeCheckStatuses: [],
      feeConfirmStatuses: [],
      feeCheckTime: [],
      feeCheckStartDate: [],
      feeCheckEndDate: [],
      feeCheckUser: [],
      hasAssortFee: [],
      workCode: [],
      checkWorkCode: [],
      demanderCorp: [],
      workTypes: [],
      workStatuses: [],
      customId: [],
      customCorp: [],
      serial: [],
      smallClassId: [],
      deviceModel: [],
      deviceBrand: [],
      area: [],
      district: [],
      serviceMode: [],
      serviceBranches: [],
      engineer: [],
      contactName: [],
      contactPhone: [],
      createTime: [],
      startDate: [],
      endDate: [],
      finishTime: [],
      finishStartDate: [],
      finishEndDate: [],
      dispatchTime: [],
      dispatchTimeStart: [],
      dispatchTimeEnd: [],
      hasWorkChat: [],
      isReview: []
    });
  }

  ngOnInit() {
    let params = this.anyfixService.getParams();
    if (params && params._type === 'work-fee-check') {
      this.params = params;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      this.stPro.pageNum = params.pageNum;
      this.stPro.pageSize = params.pageSize;
      this.anyfixService.setParams(null);
    }
    this.initFormValue();
  }

  initFormValue() {
    this.searchForm.patchValue({
      feeCheckStatuses: this.params.feeCheckStatuses,
      feeConfirmStatuses: this.params.feeConfirmStatuses,
      feeCheckUser: this.params.feeCheckUser,
      feeCheckTime: this.params.feeCheckTime,
      feeCheckStartDate: this.params.feeCheckStartDate,
      feeCheckEndDate: this.params.feeCheckEndDate,
      hasAssortFee: this.params.hasAssortFee,
      workCode: this.params.workCode,
      checkWorkCode: this.params.checkWorkCode,
      demanderCorp: this.params.demanderCorp,
      workTypes: this.params.workTypes,
      workStatuses: this.params.workStatuses,
      customId: this.params.customId,
      customCorp: this.params.customCorp,
      serial: this.params.serial,
      smallClassId: this.params.smallClassId,
      deviceModel: this.params.deviceModel,
      deviceBrand: this.params.deviceBrand,
      area: this.params.area,
      district: this.params.district,
      serviceMode: this.params.serviceMode,
      serviceBranches: this.params.serviceBranches,
      engineer: this.params.engineer,
      contactName: this.params.contactName,
      createTime: this.params.createTime,
      startDate: this.params.startDate,
      endDate: this.params.endDate,
      finishTime: this.params.finishTime,
      finishStartDate: this.params.finishStartDate,
      finishEndDate: this.params.finishEndDate,
      dispatchTime: this.params.dispatchTime,
      dispatchTimeStart: this.params.dispatchTimeStart,
      dispatchTimeEnd: this.params.dispatchTimeEnd,
      hasWorkChat: this.params.hasWorkChat,
      isReview: this.params.isReview
    });
  }

  // 查询工单
  queryWork() {
    // 工单筛选数据收集
    this.feeCheckListFilterComponent.submit();
    this.stPro.reset();
  }

  pageChange(event) {
    this.page.pageNum = event.pageNum;
    this.page.pageSize = event.pageSize;
  }

  // 获取查询参数
  getParams() {
    // 如果直接赋值会影响表单值
    const params: any = Object.assign({}, this.searchForm.value);
    // 只能查询自己为服务商的工单
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.workStatuses = this.anyfixService.CLOSED;
    if (params.feeCheckStatuses === undefined) {
      params.feeCheckStatuses = this.workCheckService.TO_CHECK + ',' + this.workCheckService.CHECK_REFUSE;
    }
    if (params.feeConfirmStatuses === undefined) {
      params.feeConfirmStatuses = this.workCheckService.CONFIRM_REFUSE;
    }
    return params;
  }

  // 收集工单筛选数据
  searchFormDataChange(searchFormData: {key: string}[]) {
    this.searchForm.patchValue(searchFormData);
  }

  dbClick(event) {
    this.toWorkDetail(event.item.workId);
  }

  // 工单明细
  toWorkDetail(workId) {
    const params = this.getParams();
    this.params = params;
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.params._type = 'work-fee-check';
    this.anyfixService.setParams(this.params);
    const workIds = this.stPro.st._data.map((item) => {
      return item.workId;
    });
    this.anyfixService.setParamsOnce(workIds);
    this.router.navigate(['/anyfix/work-detail'], {
      relativeTo: this.activatedRoute,
      queryParams: {
        workId,
        selectedTabName: 'workFee'
      }
    });
  }

  // 显示筛选条件
  openDrawer() {
    this.visible = true;
  }

  // 隐藏筛选条件
  closeDrawer() {
    this.visible = false;
  }

  // 清空筛选条件
  clearDrawer() {
    this.searchForm.reset();
    this.stPro.reset();
  }

  /**
   * 选择工单列表
   */
  selectWorkList(event) {
    this.selectedWorkList = event;
  }

  /**
   * 批量审核通过
   */
  checkPass() {
    const idList: any[] = [];
    this.selectedWorkList.forEach((work: any) => {
      idList.push(work.workId);
    });
    if (idList.length > 0) {
      this.modalService.confirm({
        nzTitle: '确认审核通过当前页选中工单？',
        nzCancelText: '取消',
        nzOkText: '确定',
        nzOnOk: () => this.checkPassSubmit(idList)
      });
    } else {
      this.messageService.warning('请至少选中一条工单记录！');
    }
  }

  // 提交
  checkPassSubmit(idList) {
    const params = {
      workIdList: idList
    };
    this.loading = true;
    this.httpClient.post('/api/anyfix/work-check/fee/batch-check', params)
      .pipe(finalize(() => {
        this.loading = false;
      }))
      .subscribe((result: Result) => {
        if (result.code === 0) {
          const msg = result.data || '';
          if (msg === '') {
            this.messageService.success('批量审核成功！');
            this.queryWork();
          } else {
            this.modalService.warning({
              nzTitle: '批量审核失败',
              nzContent: msg
            });
          }
        }
      });
  }

}
