import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';
import {IncomeBaseAddComponent} from './income-base-add/income-base-add.component';
import {ActivatedRoute} from '@angular/router';
import {IncomeMainService} from '../income-main.service';
import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-add-ware-income',
  templateUrl: './ware-income-batch-add.component.html',
  styleUrls: ['./ware-income-batch-add.component.less']
})
export class WareIncomeBatchAddComponent implements OnInit {

  // 页面类型，add=添加页面，edit=修改页面
  type = '';
  // 明细列表
  incomeDetailList: any[] = [];
  // 主表
  incomeMain: any = this.incomeMainService.incomeMain;
  // 规格
  normsValue: any = {};
  // 添加明细表单
  addDetailForm: FormGroup;
  // 分页参数
  page = new Page();

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private incomeMainService: IncomeMainService,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe,
    public wmsService: WmsService
  ) {
    this.addDetailForm = new FormGroup({});
    this.addDetailForm = this.formBuilder.group({
      catalog: [null, Validators.required],
      catalogId: [null],
      catalogName: [],
      brand: [null, Validators.required],
      brandId: [],
      brandName: [],
      model: [null, Validators.required],
      modelId: [],
      modelName: [],
      quantity: [1, Validators.required],
      statusObj: [null, Validators.required],
      status: [],
      statusName: [],
      unitPrice: [0],
      serviceEndDate: [],
      sn: [],
      barcode: [],
      normsValueObj: [{}],
      description: [null, Validators.maxLength(200)]
    });
  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.type = this.activatedRoute.snapshot.queryParams.type;
    if (this.type === 'edit') {
      const incomeMainId = this.activatedRoute.snapshot.queryParams.incomeMainId;
      this.httpClient.post(`/api/wms/income-common/detail/${incomeMainId}`, {})
        .subscribe((res: any) => {
          this.incomeMain = res.data;
          this.incomeDetailList = res.data.incomeDetailCommonSaveDtoList;
          if (this.incomeDetailList && this.incomeDetailList.length > 0) {
            this.incomeDetailList.forEach(incomeDetail => {
              const normsValueObj = JSON.parse(incomeDetail.normsValue);
              incomeDetail.normsValueName = Object.keys(normsValueObj)
                .map(key => {
                  const val = normsValueObj[key];
                  return key + ':' + val;
                })
                .join(',');
            });
          }
        });
    } else if (this.type === 'add') {
      this.incomeMain = this.incomeMainService.incomeMain;
      console.log(this.incomeMain);
      this.incomeDetailList = [];
    }
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryStatusList('');
    this.wmsService.queryModelList('', 0, 0);
  }

  catalogChange(event) {
    this.addDetailForm.controls.model.reset();
    this.normsValue = {};
    if(event) {
      this.addDetailForm.controls.catalogId.setValue(event.id);
      this.addDetailForm.controls.catalogName.setValue(event.name);
      this.wmsService.queryModelList('', event.id, this.addDetailForm.value.brandId);
      this.wmsService.queryCatalogSpecs(event.id);
    }else {
      this.wmsService.selectOptions.modelList = [];
      this.wmsService.selectOptions.normList = [];
    }
  }

  brandChange(event) {
    this.addDetailForm.controls.model.reset();
    if(event) {
      this.addDetailForm.controls.brandId.setValue(event.id);
      this.addDetailForm.controls.brandName.setValue(event.name);
      this.wmsService.queryModelList('', this.addDetailForm.value.catalogId, event.id);
    }else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  goBack() {
    history.back();
  }

  editBase() {
    this.modalService.create({
      nzTitle: '基本信息',
      nzWidth: 800,
      nzContent: IncomeBaseAddComponent,
      nzComponentParams: {incomeMain: this.incomeMain},
      nzOnOk: (res: any) => {
        console.log(res);
        console.log(res.addBaseForm.valid);
        // if(!res.addBaseForm.valid){
        //   return false;
        // }
        if (res.addBaseForm.value) {
          this.incomeMainService.incomeMain = res.addBaseForm.value;
          this.incomeMain = res.addBaseForm.value;
          this.cdf.markForCheck();
        }
      }
    });
  }

  formatterRmb = (value: number) => `￥ ${value ? value : 0}`;
  parseRmb = (value: string) => value.replace('￥ ', '');

  saveDetail() {
    if (this.addDetailForm.value.model) {
      this.addDetailForm.value.modelId = this.addDetailForm.value.model.id;
      this.addDetailForm.value.modelName = this.addDetailForm.value.model.name;
    }
    if (this.addDetailForm.value.statusObj) {
      this.addDetailForm.value.status = this.addDetailForm.value.statusObj.id;
      this.addDetailForm.value.statusName = this.addDetailForm.value.statusObj.name;
    }
    const params: any = this.addDetailForm.value;
    params.normsValue = JSON.stringify(this.normsValue);
    params.normsValueName = Object.keys(this.normsValue).map(
      key => {
        const val = this.normsValue[key];
        return key + ':' + val;
      }
    ).join(',');
    if (this.addDetailForm.value.serviceEndDate) {
      this.addDetailForm.value.serviceEndDate = this.datePipe.transform(this.addDetailForm.value.serviceEndDate, 'yyyy-MM-dd');
    }
    this.incomeDetailList = [...this.incomeDetailList, this.addDetailForm.value];
    // 清空序列号和条形码
    this.addDetailForm.patchValue({
      sn: '',
      barcode: ''
    });
    // 请空规格信息
    this.normsValue = {};
  }

  clearDetailForm() {
    this.addDetailForm.reset();
    this.normsValue = {};
  }

  deleteDetail(index) {
    this.incomeDetailList = this.incomeDetailList.filter((item, i) => i !== index);
  }

  save() {
    this.incomeMain.incomeDetailCommonSaveDtoList = this.incomeDetailList;
    // 如果是添加页面则保存，如果是修改页面则更新
    if (this.type === 'add') {
      this.httpClient.post('/api/wms/income-common/save', this.incomeMain)
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.messageService.success('保存成功！');
            history.back();
          }
        });
    } else if (this.type === 'edit') {
      this.httpClient.post('/api/wms/income-common/update', this.incomeMain)
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.messageService.success('保存成功！');
            history.back();
          }
        });
    }
  }

  addSubmit() {
    this.incomeMain.incomeDetailCommonSaveDtoList = this.incomeDetailList;
    this.httpClient.post('/api/wms/income-common/add', this.incomeMain)
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.messageService.success('添加成功！');
          this.incomeMainService.clear();
          history.back();
        }
      });
  }

}
