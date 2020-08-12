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
import {ServiceConfirmListFilterComponent} from './filter/service-confirm-list-filter.component';

export const CONFIRM_STATUS_BADGE: STProColumnBadge = {
  1: {text: '待确认', color: 'processing'},
  2: {text: '已通过', color: 'success'},
  3: {text: '不通过', color: 'error'}
};

@Component({
  selector: 'app-work-service-confirm-list',
  templateUrl: './service-confirm-list.component.html',
  styleUrls: ['./service-confirm-list.component.less']
})
export class ServiceConfirmListComponent implements OnInit {

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

  // 选中的工单列表
  selectedWorkList: any[] = [];

  params: any = {};

  nzFilterOption = () => true;

  @ViewChild(ServiceConfirmListFilterComponent, {static: false}) serviceConfirmListFilterComponent: ServiceConfirmListFilterComponent;
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
      title: '确认状态',
      type: 'badge',
      fixed: 'left',
      width: 100,
      index: 'finishConfirmStatus',
      badge: CONFIRM_STATUS_BADGE
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
      title: '工单状态',
      index: 'workStatusName',
      width: 100
    },
    {
      title: '省',
      index: 'provinceName',
      width: 80
    }, {
      title: '市',
      index: 'cityName',
      width: 80
    }, {
      title: '区',
      index: 'districtName',
      width: 100
    }, {
      title: '客户名称',
      index: 'customCorpName',
      width: 200
    }, {
      title: '设备类型',
      index: 'smallClassName',
      width: 150
    }, {
      title: '出厂序列号',
      index: 'serial',
      width: 150
    }, {
      title: '品牌',
      index: 'brandName',
      width: 100
    }, {
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
    }, {
      title: '服务方式',
      index: 'serviceModeName',
      width: 100
    }, {
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
    }, {
      title: '详细地址',
      index: 'address',
      width: 250
    }, {
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
      title: '服务审核人',
      index: 'finishCheckUserName',
      width: 100
    }, {
      title: '服务审核时间',
      index: 'finishCheckTime',
      type: 'date',
      dateFormat: 'YYYY-MM-DD HH:mm',
      width: 150
    }, {
      title: '服务审核备注',
      index: 'finishCheckNote',
      width: 150
    }
  ];

  overflowMode: STProOverflowMode = 'truncate';

  scrollX: string;

  url = '/api/anyfix/work-request/service-confirm/query';

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
    private messageService: NzMessageService,
    private cdf: ChangeDetectorRef
  ) {
    this.searchForm = this.formBuilder.group({
      finishConfirmStatuses: [],
      finishConfirmUser: [],
      finishConfirmTime: [],
      finishConfirmStartDate: [],
      finishConfirmEndDate: [],
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
    if (params && params._type === 'work-service-confirm') {
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
      finishConfirmStatuses: this.params.finishConfirmStatuses,
      finishConfirmUser: this.params.finishConfirmUser,
      finishConfirmTime: this.params.finishConfirmTime,
      finishConfirmStartDate: this.params.finishConfirmStartDate,
      finishConfirmEndDate: this.params.finishConfirmEndDate,
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
    this.serviceConfirmListFilterComponent.submit();
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
    // 只能查询自己为委托商的工单
    params.demanderCorp = this.userService.currentCorp.corpId;
    params.workStatuses = this.anyfixService.CLOSED;
    if (params.finishConfirmStatuses === undefined) {
      params.finishConfirmStatuses = this.workCheckService.TO_CONFIRM;
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
    this.params._type = 'work-service-confirm';
    this.anyfixService.setParams(this.params);
    const workIds = this.stPro.st._data.map((item) => {
      return item.workId;
    });
    this.anyfixService.setParamsOnce(workIds);
    this.router.navigate(['/anyfix/work-detail'], {
      relativeTo: this.activatedRoute,
      queryParams: {workId}
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
    this.httpClient.post('/api/anyfix/work-check/service/batch-confirm', params)
      .pipe(finalize(() => {
        this.loading = false;
      }))
      .subscribe((result: Result) => {
        if (result.code === 0) {
          const msg = result.data || '';
          if (msg === '') {
            this.messageService.success('批量确认成功！');
            this.queryWork();
          } else {
            this.modalService.warning({
              nzTitle: '批量确认失败',
              nzContent: msg
            });
          }
        }
      });
  }

}
