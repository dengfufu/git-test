import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {NzModalService} from 'ng-zorro-antd';
import {DeviceInfoAddComponent} from './device-info-add.component';
import {ActivatedRoute, Router} from '@angular/router';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {DEVICE_RIGHT} from '../../../core/right/right';
import {DeviceInfoService} from './service/device-info.service';
import {DeviceInfoImportComponent} from './device-info-import/device-info-import.component';
import {CustomListComponent} from '@shared/components/custom-list/custom-list.component';

@Component({
  selector: 'app-device-info-list',
  templateUrl: 'device-info-list.component.html',
  styleUrls: ['device-info-list.component.less']
})
export class DeviceInfoListComponent implements OnInit {

  aclRight = DEVICE_RIGHT;

  searchForm: FormGroup;
  deviceInfoList: any;
  page = new Page();

  deviceClassList: any[];
  brandModelList: any[];
  deviceBranchList: any = [];
  serviceBranchList: any = [];
  serviceCorpList: any;
  demanderCorpList: any;
  customCorpList: any;

  loading = false;
  drawerVisible = false;
  savedPramsValue: any;

  // 自定义列
  customTileList : any = [];
  showRowData : any = [];
  titleList : any = ['设备品牌','设备型号','出厂序列号','设备大类','设备规格',
    '省','市','区','设备网点','联系人','联系电话','详细地址','设备说明',
    '委托商','服务商','派单主管','工程师','安装日期','保修状态','保修截止日','设备状态'];
  widthList : any = [100,150,150,100,100,50,80,100,150,100,150,250,250,
    100,100,100,100,100,80,100,80];
  fieldList: any = ['brandName','modelName','serial','largeClassName','specificationName',
    'provinceName','cityName','districtName','branchName','contactName','contactPhone','address','description',
    'demanderCorpName','serviceCorpName','workManagerName','engineerName',
    'installDate','warrantyStatusName','warrantyEndDate','statusName'];
  customListKey = 'deviceCustomList';
  defaultCols = 14;
  totalWidth = 400;
  listNum = 0;
  isDemanderCorp: boolean = this.userService.currentCorp.serviceDemander === 'Y' ? true : false;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private router: Router,
              private deviceInfoService : DeviceInfoService,
              private activateRoute: ActivatedRoute,
              public userService: UserService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      deviceClass: [],
      largeClassId: [],
      smallClassId: [],
      brandModel: [],
      brandId: [],
      modelId: [],
      serial: [],
      deviceCode: [],
      customId: [],
      branchId: [],
      serviceCorp: [],
      serviceBranch: [],
      warrantyStatus: [],
      status: []
    });
  }

  ngOnInit(): void {
    this.getParamsValue();
    this.listDemanderCorp();
    this.queryDeviceInfo();
    // 自定义列
    this.initShowTitleAndColumn();
  }

  getParamsValue() {
    const params = this.deviceInfoService.paramsValue;
    if( params) {
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.deviceInfoService.paramsValue = null;
    }
  }

  saveParamsValue() {
    const params = {
      formValue : this.searchForm.value,
      page : this.page,
    };
    this.savedPramsValue = params;
  }

  // 导入页面
  import() {
    const modal = this.modalService.create({
      nzTitle: '导入设备档案',
      nzContent: DeviceInfoImportComponent,
      nzFooter: null,
      nzWidth: '600px'
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDeviceInfo();
      }
    });
  }

  // 委托商列表
  listDemanderCorp() {
    this.httpClient.get('/api/anyfix/demander/list')
    .subscribe((res: any) => {
      this.demanderCorpList = res.data;
      if (this.demanderCorpList.length && this.demanderCorpList.length === 1) {
        setTimeout(() => {
          this.searchForm.patchValue({
            demanderCorp: this.demanderCorpList[0].demanderCorp
          });
        }, 1);
      }
    });
  }

  demanderCorpChange(event) {
    this.searchForm.controls.deviceClass.reset();
    this.searchForm.controls.brandModel.reset();
    this.searchForm.controls.customId.reset();
    this.searchForm.controls.serviceCorp.reset();
    if (event && event.length > 0) {
      this.listDeviceClass(event);
      this.listServiceCorp(event);
      this.listCustomCorp(event);
    } else {
      this.deviceClassList = [];
      this.brandModelList = [];
      this.customCorpList = [];  // 重置客户名称列表
      this.serviceCorpList = []; // 重置服务商列表
    }
  }

  // 设备分类列表
  listDeviceClass(demanderCorp) {
    this.httpClient.post(`/api/device/device-class/list`, {corpId: demanderCorp})
    .subscribe((res: any) => {
      const tempList: any[] = [];
      res.data.forEach(largeClass => {
        const largeObject =
          {
            value: largeClass.id,
            label: largeClass.name,
            children: []
          };
        if (largeClass.deviceSmallClassDtoList && largeClass.deviceSmallClassDtoList.length > 0) {
          largeClass.deviceSmallClassDtoList.forEach(smallClass => {
            const smallObject =
              {
                value: smallClass.id,
                label: smallClass.name,
                isLeaf: true
              };
            largeObject.children.push(smallObject);
          });
        }
        tempList.push(largeObject);
      });
      this.deviceClassList = tempList;
    });
  }

  serialChange() {
    if (this.searchForm.value.serial) {
      let serial = this.searchForm.value.serial || '';
      serial = serial.replace(/，/ig, '').replace(/,/ig, '').trim().toLocaleUpperCase();
      if (serial === this.searchForm.value.serial) {
      } else {
        setTimeout(() => {
          this.searchForm.patchValue({
            serial
          });
        }, 1);
      }
    }
  }

  deviceClassChange(event) {
    this.searchForm.patchValue({
      brandModel: null,
      brandId: null,
      modelId: null
    });
    if (event && event.length > 0) {
      this.listDeviceModel(event[1]);
      this.searchForm.patchValue({
        largeClassId: event[0],
        smallClassId: event[1]
      });
    } else {
      this.brandModelList = [];
      this.searchForm.patchValue({
        largeClassId: null,
        smallClassId: null
      });
    }
  }

  // 设备型号
  listDeviceModel(smallClassId) {
    this.httpClient.post(`/api/device/device-brand/brand/model/list`,
      {
        corp: this.searchForm.value.demanderCorp,
        smallClassId
      })
    .subscribe((res: any) => {
      const tmpList: any[] = [];
      res.data.forEach(brand => {
        const brandObj = {
          value: brand.id,
          label: brand.name,
          children: []
        };
        if (brand.deviceModelDtoList && brand.deviceModelDtoList.length > 0) {
          brand.deviceModelDtoList.forEach(model => {
            const modelObject = {
              value: model.id,
              label: model.name,
              isLeaf: true
            };
            brandObj.children.push(modelObject);
          });
        }
        tmpList.push(brandObj);
      });
      this.brandModelList = tmpList;
    });
  }

  brandModelChange(event) {
    if (event && event.length > 0) {
      this.searchForm.patchValue({
        brandId: event[0],
        modelId: event[1]
      });
    } else {
      this.searchForm.controls.brandId.reset();
      this.searchForm.controls.modelId.reset();
    }
  }

  // 服务商列表
  listServiceCorp(demanderCorp) {
    this.httpClient.get('/api/anyfix/demander-service/service/list/' + demanderCorp)
    .subscribe((res: any) => {
      this.serviceCorpList = res.data;
    });
  }

  // 服务商改变
  serviceCorpChange(event) {
    this.searchForm.controls.serviceBranch.reset();
    this.matchSearchBranch(event);
  }

  // 客户列表
  listCustomCorp(demanderCorp) {
    this.httpClient.get('/api/anyfix/demander-custom/custom/list/' + demanderCorp)
    .subscribe((res: any) => {
      this.customCorpList = res.data;
    });
  }

  // 客户改变
  customCorpChange(event) {
    this.searchForm.controls.branchId.reset();
    this.matchDeviceBranch(event);
  }

  // 设备网点
  matchDeviceBranch(event, branchName?: string) {
    if (!event) {
      this.deviceBranchList = [];
      return;
    }
    const params = {
      customId: event,
      matchFilter: branchName,
    };
    this.httpClient.post('/api/anyfix/device-branch/match', params)
    .subscribe((res: any) => {
      this.deviceBranchList = res.data;
    });
  }

  // 服务网点
  matchSearchBranch(corpId, name?: string) {
    if (!corpId) {
      this.serviceBranchList = [];
      return;
    }
    const params = {
      branchName: name,
      serviceCorp: corpId
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

  // 查询设备档案
  queryDeviceInfo(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
    .post('/api/device/device-info/query', this.getParams())
    .pipe(
      finalize(() => {
        this.loading = false;
        this.cdf.markForCheck();
      })
    )
    .subscribe((res: any) => {
      this.deviceInfoList = res.data.list;
      this.listNum = this.deviceInfoList.length;
      this.page.total = res.data.total;
    });
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加设备页面
  addDeviceInfo(): void {
    const modal = this.modalService.create({
      nzTitle: '添加设备',
      nzContent: DeviceInfoAddComponent,
      nzFooter: null,
      nzWidth: '1000px'
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDeviceInfo();
      }
    });
  }

  detail(deviceId) {
    this.deviceInfoService.paramsValue = this.savedPramsValue;
    this.router.navigate(['../device-info/device-detail'], {relativeTo: this.activateRoute, queryParams: {deviceId}});
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }


  initShowTitleAndColumn(){
    let saved = false;
    const submitList =  localStorage.getItem(this.customListKey);
    if(submitList !== null && submitList !== undefined){
      this.customTileList =JSON.parse(submitList);
      if (this.customTileList.length === this.titleList.length) {
        saved = true;
        for(let i=0;i< this.customTileList.length;i++)  {
          this.customTileList[i].width = this.widthList[i];
          this.customTileList[i].title = this.titleList[i];
          if( this.customTileList[i].checked === false){
            this.showRowData[this.fieldList[i]] = false;
          }else if( this.customTileList[i].checked === true ){
            this.showRowData[this.fieldList[i]] = true;
            this.totalWidth += this.customTileList[i].width;
          }
        }
      }
    }
    if (!saved) {
      const objList = [];
      for(let i=0;i< this.titleList.length;i++)  {
        const obj = {
          title : this.titleList[i],
          checked : true,
          width: this.widthList[i]
        };
        if(i>=this.defaultCols){
          obj.checked = false;
          this.showRowData[this.fieldList[i]] = false;
        }else {
          this.showRowData[this.fieldList[i]] = true;
          this.totalWidth += this.widthList[i];
        }
        objList.push(obj);
        this.customTileList = objList;
      }
    }
  }

  // 自定义列
  openCustomList() {
    let titleList =  JSON.stringify(this.customTileList)
    titleList = JSON.parse(titleList);
    const modal = this.modalService.create({
      nzTitle: '自定义列',
      nzContent: CustomListComponent,
      nzFooter: null,
      nzWidth: 900,
      nzComponentParams: {
        titleList,
        type: this.customListKey
      }
    });
    modal.afterClose.subscribe((res: any) => {
      if (res) {
        this.resetShowTable(res);
      }
    });
  }

  resetShowTable(titleList) {
    this.resetTotalWidth();
    let width = 0;
    this.customTileList = titleList;
    for(let i=0;i<titleList.length;i++)  {
      if(titleList[i].checked) {
        this.showRowData[this.fieldList[i]] = true;
        // 重新调整滑动窗口大小
        width += titleList[i].width
      } else {
        this.showRowData[this.fieldList[i]] = false;
      }
    }
    this.totalWidth+=width;
  }

  resetTotalWidth() {
    this.totalWidth = 650;
  }
}
