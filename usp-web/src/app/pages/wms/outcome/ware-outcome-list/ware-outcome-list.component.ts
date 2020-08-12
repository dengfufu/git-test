import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {OutcomeStatusEnum} from '@core/service/enums.service';
import {finalize} from 'rxjs/operators';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-ware-outcome-list',
  templateUrl: 'ware-outcome-list.component.html',
  styleUrls: ['ware-outcome-list.component.less']
})
export class WareOutcomeListComponent implements OnInit {

  searchForm: FormGroup;
  drawerVisible = false;
  page = new Page();
  woreOutComeList: any = [];
  outcomeSaveList: any = [];
  loading = true;
  status: any = 1;
  outcomeStatusEnum = OutcomeStatusEnum;
  statusCountMap: {[key: number]: number} = {};


  constructor(private httpClient: HttpClient,
              private cdf: ChangeDetectorRef,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private formBuilder: FormBuilder,
              public wmsService: WmsService) {
    this.searchForm = this.formBuilder.group({
      depotId: [],
      catalogId: [],
      modelId: [],
      brandId: [],
      smallClassId: [],
      serial: [],
      barcode: [],
      createBy: [],
      createTimeList: [],
      assistUserId: [],
      status: [],
    });
  }

  ngOnInit(): void {
    // 还原参数
    this.setParams();
    this.initSelectOption();
    this.cdf.markForCheck();
    this.loadWareOutComeList(this.getParams());
    this.getWoreOutComeCount();
  }

  initSelectOption() {
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
    this.wmsService.queryStatusList('');
    this.wmsService.queryModelList('', 0, 0);
    this.wmsService.queryUserList('');
  }

  loadWareOutComeList(params: any, reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/wms/outcome-common/list', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data != null){
          this.woreOutComeList = res.data.list;
          this.page.total = res.data.total;
        }
      });
  }

  loadOutComeSaveList(params: any, reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/wms/outcome-common/save/list', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data != null){
          this.outcomeSaveList = res.data.list;
          this.page.total = res.data.total;
        }
      });
  }

  // 编辑暂存的出库信息
  editSavedOutCome(outcomeId){
    this.router.navigate(['../ware-outcome-add'], {queryParams: {outcomeId},relativeTo: this.activatedRoute});
  }

  // 删除暂存的出库信息
  deleteSaveOutcome(outcomeId){
    this.httpClient.get('/api/wms/outcome-common/save/delete/' + outcomeId)
      .subscribe((res: any) => {
        this.loadOutComeSaveList(this.getParams());
        this.getWoreOutComeCount();
        this.cdf.markForCheck();
      });
  }
  // 查询出库列表
  queryWoreOutComeList(reset: boolean = false) {
    this.loadWareOutComeList(this.getParams(), reset);
  }

  // 查询出库各个状态的记录数量
  getWoreOutComeCount() {
    const params = this.getParams();
    params.outcomeStatusList = [];
    params.flowNodeTypeList = [];

    // 统计出库单
    this.httpClient.post('/api/wms/outcome-common/outcomeStatus/count', params)
      .subscribe((res: any) => {
        if(typeof res.data === 'object' && res.data !== null){
          this.statusCountMap = res.data;
          Object.keys(this.statusCountMap).map(key => {
            if (this.statusCountMap[key] && this.statusCountMap[key].length > 0) {
              this.statusCountMap[key] = parseFloat(this.statusCountMap[key]);
            } else {
              this.statusCountMap[key] = 0;
            }
          });
        }
      });
  }

  // 添加出库记录单
  addWareOutcome(smallClassId) {
    this.router.navigate(['../ware-outcome-add'], {queryParams: {smallClassId},relativeTo: this.activatedRoute});
  }

  // 批量审批出库记录
  auditWareOutcome() {
    this.saveParams();
    this.router.navigate(['../ware-outcome-batch-audit'], {relativeTo: this.activatedRoute});
  }

  // 发货
  consignWareOutcome(depotId) {
    this.saveParams();
    this.router.navigate(['../ware-outcome-consign'], {queryParams: {depotId},relativeTo: this.activatedRoute});
  }

  // 批量审批出库记录
  wareOutcomeDetail(outcomeId,flowNodeType) {
    this.saveParams();
    this.router.navigate(['../ware-outcome-detail'], {queryParams: {outcomeId,flowNodeType},relativeTo: this.activatedRoute});
  }

  // 保持相关参数
  saveParams(){
    const params = this.getParams();
    params.status = this.status;
    params._type = 'outcome-list';
    this.wmsService.setParams(params);
  }

  // 设置（还原）参数
  setParams(){
    const params = this.wmsService.getParams();
    if (params && params._type === 'outcome-list') {
      this.status = params.status;
      this.page.pageNum = params.pageNum;
      this.page.pageSize = params.pageSize;
      this.wmsService.setParams(null);
    }
  }
  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    switch (this.status) {
      case 20:
        params.outcomeStatusList = [this.outcomeStatusEnum.HAD_OUTCOME];
        break;
      case 30:
        params.flowNodeTypeList = [20,30,60];
        break;
      case 40:
        params.flowNodeTypeList = [40];
        break;
      case 50:
        params.flowNodeTypeList = [50];
        break;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // list记录条件变动修改
  changeList(status) {
    this.status = status;
    if(Number.parseInt(status.toString(),10) === this.outcomeStatusEnum.NOT_SUBMIT){
      this.loadOutComeSaveList(this.getParams(), true);
    }else{
      this.loadWareOutComeList(this.getParams(), true);
    }
  }

  // tag点击变色
  changeColor(status) {
    return status === this.status ? {color: '#20BF8E'} : {color: '#999999'};
  }

  getButtonColor(status) {
    let color;
    switch (status) {
      case 10:
        color = {'background-color': '#1E87F0'};
        break;
      case 20:
        color = {'background-color': '#5E86C8'};
        break;
      case 30:
        color = {'background-color': '#FFD617'};
        break;
      case 40:
        color = {'background-color': '#5EC894'};
        break;
      case 50:
        color = {'background-color': '#7ED321'};
        break;
      case 60:
        color = {'background-color': '#48B8FF'};
        break;
    }
    return color;
  }

  clearForm() {
    this.searchForm.reset();
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
    this.loadWareOutComeList(true);
  }
}
