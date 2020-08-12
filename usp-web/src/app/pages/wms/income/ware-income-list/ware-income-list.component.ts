import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {Page} from '@core/interceptor/result';
import {FormBuilder, FormGroup} from '@angular/forms';
import {SettleBranchAddComponent} from '../../../anyfix/settle/settle-branch/settle-branch-add/settle-branch-add.component';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute, Router} from '@angular/router';
import {IncomeBaseAddComponent} from '../ware-income-batch-add/income-base-add/income-base-add.component';
import {IncomeMainService} from '../income-main.service';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-ware-income-list',
  templateUrl: './ware-income-list.component.html',
  styleUrls: ['./ware-income-list.component.less']
})
export class WareIncomeListComponent implements OnInit {

  // 表格loading
  loading = false;
  // 已提交表格数据
  incomeList: any[] = [];
  // 未提交（保存）表格数据
  incomeSaveList: any[] = [];
  // 查询条件表单
  searchForm: FormGroup;
  // 表格分页信息
  page = new Page();
  // 选中的入库状态, 0表示所有
  selectedIncomeStatus = 0;
  // 保存状态,20表示已提交，10表示未提交（保存）
  saveStatus = 20;
  // 查询条件里入库状态复选框全选状态
  incomeStatusAllChecked = true;
  // 查询条件里入库状态复选框有选中状态
  indeterminate = false;
  // 查询条件里入库状态复选框选择项
  incomeStatusCheckOptions = [
    {label: '待审批', value: 10, checked: true},
    {label: '已入库', value: 20, checked: true}
  ];

  // 查询条件drawer显示与否
  drawerVisible = false;
  // 入库状态与记录数的映射,key为0表示未提交（保存）
  statusCountMap: {[key: number]: number} = {};

  constructor(
    private nzModalService: NzModalService,
    private router: Router,
    private activateRoute: ActivatedRoute,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private incomeMainService: IncomeMainService,
    private httpClient: HttpClient,
    private userService: UserService,
    private messageService: NzMessageService,
    public wmsService: WmsService
  ) {
    this.searchForm = this.formBuilder.group({
      smallClassId: [],
      incomeStatuses: [[]],
      catalogId: [],
      depotId: [],
      brandId: [],
      modelId: [],
      sn: [],
      barcode: [],
      createBy: [],
      createTime: []
    });
  }

  ngOnInit() {
    this.queryIncomeList(false);
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
    this.wmsService.queryStatusList('');
    this.wmsService.queryModelList('', 0, 0);
    this.cdf.markForCheck();
  }

  catalogChange(event) {
    this.searchForm.controls.modelId.reset();
    if (event && event.length > 0) {
      this.wmsService.queryModelList('', event, this.searchForm.value.brandId);
    } else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  brandChange(event) {
    this.searchForm.controls.modelId.reset();
    if (event && event.length > 0) {
      this.wmsService.queryModelList('', this.searchForm.value.catalogId, event);
    } else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  queryIncomeList(reset: boolean) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    if (this.saveStatus === 10) {
      // 查询待提交状态的入库单，不需要其他查询条件
      this.httpClient.post('/api/wms/income-common/listSaveIncome', {corpId: this.userService.currentCorp.corpId})
        .pipe(
          finalize(() => {
            this.loading = false;
          })
        ).subscribe((res: any) => {
        this.incomeSaveList = res.data.list;
        this.cdf.markForCheck();
      });
    } else if (this.saveStatus === 20) {
      this.httpClient.post('/api/wms/income-common/listIncome', this.getParams())
        .pipe(
          finalize(() => {
            this.loading = false;
          })
        ).subscribe((res: any) => {
        this.incomeList = res.data.list;
        this.page.total = res.data.total;
        this.cdf.markForCheck();
      });
    }
    // 统计入库单
    this.httpClient.post('/api/wms/income-common/incomeStatus/count', this.getParams())
      .subscribe((res: any) => {
        this.statusCountMap = res.data;
        Object.keys(this.statusCountMap).map(key => {
          if (this.statusCountMap[key] && this.statusCountMap[key].length > 0) {
            this.statusCountMap[key] = parseFloat(this.statusCountMap[key]);
          } else {
            this.statusCountMap[key] = 0;
          }
        });
        console.log(this.statusCountMap);
      });
  }

  getParams() {
    const params = this.searchForm.value;
    params.corpId = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.incomeStatuses = this.incomeStatusCheckOptions.filter(item => item.checked)
      .map(item => item.value);
    console.log(params);
    return params;
  }

  isAuditUser(auditUserList) {
    if (auditUserList && auditUserList.length > 0) {
      return auditUserList.indexOf(this.userService.userInfo.userId) >= 0;
    }
    return false;
  }

  changeTag(saveStatus, incomeStatus) {
    // 如果查询未提交状态的入库单，则不用管其他查询条件
    this.saveStatus = saveStatus;
    if (saveStatus === 10) {
      this.selectedIncomeStatus = 0;
    } else {
      this.selectedIncomeStatus = incomeStatus;
      if (incomeStatus === 0) {
        this.indeterminate = false;
        this.incomeStatusAllChecked = true;
        this.incomeStatusCheckOptions = this.incomeStatusCheckOptions.map(item => {
          return {
            ...item,
            checked: true
          };
        });
      } else {
        this.indeterminate = true;
        this.incomeStatusAllChecked = false;
        this.incomeStatusCheckOptions.map(item => {
          if (item.value === incomeStatus) {
            item.checked = true;
          } else {
            item.checked = false;
          }
        });
      }
    }
    this.queryIncomeList(true);
  }

  changeColor(saveStatus, incomeStatus) {
    return this.selectedIncomeStatus === incomeStatus && this.saveStatus === saveStatus ? 'a-status-tag-selected' : 'a-status-tag';
  }

  updateAllChecked() {
    this.indeterminate = false;
    if (this.incomeStatusAllChecked) {
      this.incomeStatusCheckOptions = this.incomeStatusCheckOptions.map(item => {
        return {
          ...item,
          checked: true
        };
      });
    } else {
      this.incomeStatusCheckOptions = this.incomeStatusCheckOptions.map(item => {
        return {
          ...item,
          checked: false
        };
      });
    }
  }

  updateSingleChecked() {
    if (this.incomeStatusCheckOptions.every(item => !item.checked)) {
      this.incomeStatusAllChecked = false;
      this.indeterminate = false;
    } else if (this.incomeStatusCheckOptions.every(item => item.checked)) {
      this.incomeStatusAllChecked = true;
      this.indeterminate = false;
    } else {
      this.indeterminate = true;
    }
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
    this.queryIncomeList(true);
  }

  viewIncome(id) {
    this.router.navigate(['../ware-income-view'], {queryParams: {id}, relativeTo: this.activateRoute});
  }

  addIncome(smallClassId, smallClassName) {
    // 不需要销账的入库，走批量添加入库单
    if (smallClassId !== 40 && smallClassId !== 50) {
      const model = this.nzModalService.create({
        nzTitle: '基本信息',
        nzWidth: 800,
        nzContent: IncomeBaseAddComponent,
        nzComponentParams: {incomeMain: {smallClassId}},
        nzOnOk: (res: any) => {
          if (!res.addBaseForm.valid) {
            Object.keys(res.addBaseForm.controls).map(key => {
              res.addBaseForm.controls[key].markAsDirty();
              res.addBaseForm.controls[key].updateValueAndValidity();
            });
            return false;
          }
          if (res.addBaseForm.value) {
            this.incomeMainService.incomeMain = res.addBaseForm.value;
            this.incomeMainService.incomeMain.smallClassId = smallClassId;
            this.incomeMainService.incomeMain.smallClassName = smallClassName;
            this.router.navigate(['../ware-income-batch-add'], {
              queryParams: {type: 'add'},
              relativeTo: this.activateRoute
            });
          }
        }
      });
      // 需要销账的记录，不走批量
    } else {
      this.incomeMainService.incomeMain.smallClassId = smallClassId;
      this.incomeMainService.incomeMain.smallClassName = smallClassName;
      this.router.navigate(['../ware-income-add'], {queryParams: {type: 'add'}, relativeTo: this.activateRoute});
    }
  }

  audit(incomeDetail) {
    if (incomeDetail.curNodeType === 10) {
      this.router.navigate(['../ware-income-add'],
        {queryParams: {type: 'edit', incomeMainId: incomeDetail.id}, relativeTo: this.activateRoute});
    } else {
      this.router.navigate(['../ware-income-audit'],
        {queryParams: {smallClassId: incomeDetail.smallClassId, id: incomeDetail.id}, relativeTo: this.activateRoute});
    }
  }

  batchAudit() {
    this.router.navigate(['../ware-income-batch-audit'], {relativeTo: this.activateRoute});
  }

  editIncome(incomeMainId, smallClassId) {
    if (smallClassId !== 40 && smallClassId !== 50) {
      this.router.navigate(['../ware-income-batch-add'], {
        queryParams: {type: 'edit', incomeMainId},
        relativeTo: this.activateRoute
      });
    } else {
      this.router.navigate(['../ware-income-add'], {
        queryParams: {type: 'edit', incomeMainId},
        relativeTo: this.activateRoute
      });
    }
  }

  deleteIncome(incomeMainId) {
    this.httpClient.post('/api/wms/income-common/delete', {id: incomeMainId})
      .subscribe((res: any) => {
        this.messageService.success('删除成功！');
        this.queryIncomeList(false);
      });
  }

}
