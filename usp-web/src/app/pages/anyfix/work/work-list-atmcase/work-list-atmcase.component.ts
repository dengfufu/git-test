import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {AnyfixService} from '@core/service/anyfix.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';
import {Page} from '@core/interceptor/result';
import {UserService} from '@core/user/user.service';

export class WorkType {
  id: number;
  name: string;

  [key: string]: any;
}

export class WorkStatus {
  id: number;
  name: string;

  [key: string]: any;
}

@Component({
  selector: 'app-work-list-atmcase',
  templateUrl: 'work-list-atmcase.component.html',
  styleUrls: ['work-list-atmcase.component.less']
})
export class WorkListAtmcaseComponent implements OnInit {
  searchForm: FormGroup;
  workList: any = [];
  page = new Page();
  loading = true;
  visible = false;
  optionList: any = {};
  params: any = {};
  queryCount = 0;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              public userService: UserService,
              private nzModalService: NzModalService,
              private activatedRoute: ActivatedRoute,
              public anyfixService: AnyfixService,
              private nzMessageService: NzMessageService,
              private anyfixModuleService: AnyfixModuleService
  ) {

    this.searchForm = this.formBuilder.group({
      caseId: [],
      workTypes: [],
      customCorpName: [],
      serviceBranches: [],
      deviceBrandName: [],
      deviceModelName: [],
      createTime: [],
      machineCode: [],
      provinceName: [],
      finishTime: [],
      ycTime: [],
      workStatuses: []
    });
  }

  ngOnInit(): void {
    this.initOptionList();
    const params = this.anyfixService.getParams();
    if (params && params._type === 'work-list-atmcase') {
      this.params = params;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      this.loadWorkList(params);
      this.anyfixService.setParams(null);
    } else {
      this.loadWorkList(this.getPageParam());
    }
  }

  initOptionList() {
    this.initWorkTypeOption();
    this.initWorkStatusOption();
    this.anyfixModuleService.getAtmCaseOption().subscribe((res: any) => {
      this.optionList.provinceOption = res.data.provinceMap;
      this.optionList.customCorpOption = res.data.customMap;
      this.optionList.serviceBranchOption = res.data.branchMap;
      this.optionList.deviceBrandOption = res.data.brandMap;
      this.optionList.deviceModelOption = res.data.deviceModelMap;
    });
  }

  // 初始化工单类型列表
  initWorkTypeOption() {
    // 暂时改死工单类型
    this.optionList.workTypeOption = [];
    let workType = new WorkType();
    workType.id = 1;
    workType.name = '维护';
    this.optionList.workTypeOption.push(workType);
    workType = new WorkType();
    workType.id = 2;
    workType.name = '巡检';
    this.optionList.workTypeOption.push(workType);
    workType = new WorkType();
    workType.id = 3;
    workType.name = '安装';
    this.optionList.workTypeOption.push(workType);
    workType = new WorkType();
    workType.id = 4;
    workType.name = '其他';
    this.optionList.workTypeOption.push(workType);
  }

  // 初始化工单状态列表
  initWorkStatusOption() {
    // 工单状态
    this.optionList.workStatusOption = [];
    let workStatus = new WorkStatus();
    workStatus.id = 50;
    workStatus.name = '待签到';
    this.optionList.workStatusOption.push(workStatus);
    workStatus = new WorkType();
    workStatus.id = 70;
    workStatus.name = '服务中';
    this.optionList.workStatusOption.push(workStatus);
    workStatus = new WorkType();
    workStatus.id = 90;
    workStatus.name = '已完成';
    this.optionList.workStatusOption.push(workStatus);
    workStatus = new WorkType();
    workStatus.id = 110;
    workStatus.name = '取消';
    this.optionList.workStatusOption.push(workStatus);
  }

  loadWorkList(params: any, reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.queryCount = this.queryCount + 1;
    params.queryCount = this.queryCount;
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-request/query/atmcase', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.page.total = data.total;
        this.workList = data.list;
        if (typeof this.workList === 'object') {
          this.workList.forEach(item => {
            switch (item.workStatus) {
              case this.anyfixService.TO_SIGN:
                item.buttonColor = {'background-color': '#48B8FF'};
                break;
              case this.anyfixService.IN_SERVICE:
                item.buttonColor = {'background-color': '#FF9F00'};
                break;
              case this.anyfixService.CLOSED:
                item.buttonColor = {'background-color': '#B5D46F'};
                break;
              case this.anyfixService.CANCELED:
                item.buttonColor = {'background-color': '#D3D3D3'};
                break;
            }
          });
        }
      });
  }

  queryWork(reset: boolean = false, isPage) {
    if (isPage) {
      this.queryCount = 0;
    }
    const params = this.getParams();
    this.params = params;
    this.loadWorkList(params, reset);
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    // 创建时间
    const crreteTime = this.searchForm.controls.createTime.value;
    if (crreteTime != null && crreteTime.length > 0) {
      params.startDate = crreteTime[0];
      params.endDate = crreteTime[1];
    }
    // 关闭时间
    const finishTime = this.searchForm.controls.finishTime.value;
    if (finishTime != null && finishTime.length > 0) {
      params.finishStartDate = finishTime[0];
      params.finishEndDate = finishTime[1];
    }
    // 预计出发时间
    const ycTime = this.searchForm.controls.ycTime.value;
    if (ycTime != null && ycTime.length > 0) {
      params.ycStartDate = ycTime[0];
      params.ycEndDate = ycTime[1];
    }
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
    this.loadWorkList(this.getPageParam());
  }

  toWorkDetail(caseId) {
    this.params._type = 'work-list-atmcase';
    this.params.pageNum = this.page.pageNum;
    this.params.pageSize = this.page.pageSize;
    this.anyfixService.setParams(this.params);
    this.router.navigate(['../work-list-atmcase/work-detail-atmcase'], {
      queryParams: {caseId},
      relativeTo: this.activatedRoute
    });
  }

}
