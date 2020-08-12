import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ChooseReverseDetailComponent} from './choose-reverse-detail/choose-reverse-detail.component';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {IncomeMainService} from '../income-main.service';
import {WmsService} from '../../wms.service';
import {ActivatedRoute} from '@angular/router';

@Component({
  selector: 'app-ware-income-add',
  templateUrl: './ware-income-add.component.html',
  styleUrls: ['./ware-income-add.component.less']
})
export class WareIncomeAddComponent implements OnInit {

  // 页面类型，add=添加页面，edit=编辑页面
  type: any;
  // 销账明细列表
  reverseDetailList: any[] = [];
  mapOfReverseDetailId: any = {};
  // 入库信息
  incomeCommon: any = {};
  incomeDetail: any = {};
  // 表单
  addForm: FormGroup;
  // 规格
  normsValue: any = {};

  constructor(
    private httpClient: HttpClient,
    private modalService: NzModalService,
    private messageService: NzMessageService,
    private formBuilder: FormBuilder,
    private incomeMainService: IncomeMainService,
    public wmsService: WmsService,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute
  ) {
    this.addForm = this.formBuilder.group({
      incomeId: [''],
      smallClassId: ['', [Validators.required]],
      depotId: ['', [Validators.required]],
      propertyRight: ['', [Validators.required]],
      contId: [],
      purchaseDetailId: [],
      secondHand: [],
      catalogId: [null, [Validators.required]],
      brandId: [null, [Validators.required]],
      modelId: [null, [Validators.required]],
      quantity: [1, Validators.required],
      status: [null, [Validators.required]],
      unitPrice: [0],
      serviceEndDate: [],
      sn: [],
      barcode: [],
      normsValueObj: [{}],
      description: [null, [Validators.maxLength(200)]]
    });
  }

  ngOnInit() {
    this.type = this.activatedRoute.snapshot.queryParams.type;
    this.incomeCommon = this.incomeMainService.incomeMain;
    if (this.type === 'add') {
      this.addForm.patchValue({
        smallClassId: this.incomeCommon.smallClassId
      });
    } else if (this.type === 'edit') {
      const incomeMainId = this.activatedRoute.snapshot.queryParams.incomeMainId;
      this.httpClient.post(`/api/wms/income-common/detail/${incomeMainId}`, {})
        .subscribe((res: any) => {
          this.incomeCommon = res.data;
          this.addForm.patchValue({
            smallClassId: this.incomeCommon.smallClassId
          });
          // 已提交
          if (res.data.incomeStatus === 10) {
            this.incomeDetail = res.data;
            // 保存未提交
          } else {
            this.incomeDetail = res.data.incomeDetailCommonSaveDtoList[0];
          }
          this.reverseDetailList = this.incomeDetail.bookSaleBorrowResultDtoList;
          // 初始化表单
          this.initForm();
          // 初始化规格
          this.initNormsValue();
          this.cdf.markForCheck();
        });
    }
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryModelList('', 0, 0);
    this.wmsService.queryStatusList('');
  }

  initForm() {
    this.addForm.patchValue({
      catalogId: this.incomeDetail.catalogId,
      brandId: this.incomeDetail.brandId,
      modelId: this.incomeDetail.modelId,
      quantity: this.incomeDetail.quantity,
      status: this.incomeDetail.status,
      unitPrice: this.incomeDetail.unitPrice,
      serviceEndDate: this.incomeDetail.serviceEndDate,
      sn: this.incomeDetail.sn,
      barcode: this.incomeDetail.barcode,
      description: this.incomeDetail.description
    });
    this.addForm.patchValue({
      incomeId: this.incomeCommon.id,
      depotId: this.incomeCommon.depotId,
      propertyRight: this.incomeCommon.propertyRight,
      contId: this.incomeCommon.contId,
      purchaseDetailId: this.incomeCommon.purchaseDetailId,
      secondHand: this.incomeCommon.secondHand,
    });
  }

  initNormsValue() {
    if (this.incomeDetail.normsValue && this.incomeDetail.normsValue.length > 0) {
      this.normsValue = JSON.parse(this.incomeDetail.normsValue);
      this.incomeDetail.normsValueName = Object.keys(this.normsValue)
        .map(key => {
          const val = this.normsValue[key];
          return key + ':' + val;
        })
        .join(',');
    }
    if (this.reverseDetailList && this.reverseDetailList.length > 0) {
      this.reverseDetailList.map(reverseDetail => {
        if (reverseDetail.normsValue && reverseDetail.normsValue.length > 0) {
          const normsValueObj = JSON.parse(reverseDetail.normsValue);
          reverseDetail.normsValueName = Object.keys(normsValueObj)
            .map(key => {
              return key + ':' + this.normsValue[key];
            })
            .join(',');
        }
      });
    }
  }

  catalogChange(event) {
    this.addForm.controls.modelId.reset();
    this.normsValue = {};
    if (event && event.length > 0) {
      this.wmsService.queryModelList('', event, this.addForm.value.brandId);
      this.wmsService.queryCatalogSpecs(event);
    } else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  brandChange(event) {
    this.addForm.controls.modelId.reset();
    if (event && event.length > 0) {
      this.wmsService.queryModelList('', this.addForm.value.catalogId, event);
    } else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  // 选择冲账明细
  chooseReverseDetail() {
    this.modalService.create({
      nzTitle: '选择冲账明细',
      nzWidth: 1200,
      nzContent: ChooseReverseDetailComponent,
      nzComponentParams: {
        smallClassId: this.incomeCommon.smallClassId,
        checkedIdMap: this.mapOfReverseDetailId
      },
      nzOnOk: (res: any) => {
        if (res.checkbox.getCheckedDataList().length <= 0) {
          this.messageService.warning('请至少选择一条销账明细！');
          return false;
        } else {
          this.reverseDetailList = [];
          res.bookSaleBorrowList.forEach(saleBorrow => {
            if (res.checkbox.mapOfCheckedId[saleBorrow.id]) {
              this.reverseDetailList = [...this.reverseDetailList, saleBorrow];
              this.mapOfReverseDetailId = res.checkbox.mapOfCheckedId;
            }
          });
        }
      }
    });
  }

  deleteReverseDetail(id) {
    this.reverseDetailList = this.reverseDetailList.filter(item => item.id !== id);
    this.mapOfReverseDetailId = Object.keys(this.mapOfReverseDetailId).filter(item => item !== id);
  }

  goBack() {
    history.back();
  }

  save() {
    // 保存时不做校验
    this.incomeCommon.depotId = this.addForm.value.depotId;
    this.incomeCommon.propertyRight = this.addForm.value.propertyRight;
    if (this.reverseDetailList && this.reverseDetailList.length > 0) {
      this.addForm.value.bookIdList = this.reverseDetailList.map((reverseDetail) => reverseDetail.id);
    }
    this.addForm.value.normsValue = JSON.stringify(this.normsValue);
    this.incomeCommon.incomeDetailCommonSaveDtoList = [this.addForm.value];
    if (this.type === 'add') {
      this.httpClient.post('/api/wms/income-common/save', this.incomeCommon)
        .subscribe((res: any) => {
          this.messageService.success('保存成功！');
          this.goBack();
        });
      // 编辑页面的保存为更新
    } else if (this.type === 'edit') {
      this.httpClient.post('/api/wms/income-common/update', this.incomeCommon)
        .subscribe((res: any) => {
          this.messageService.success('保存成功！');
          this.goBack();
        });
    }
  }

  addSubmit() {
    // 表单校验
    if (!this.addForm.valid) {
      Object.keys(this.addForm.controls).map(key => {
        this.addForm.controls[key].markAsDirty();
        this.addForm.updateValueAndValidity();
      });
      return;
    }
    // 校验销账明细
    if (this.incomeCommon.smallClassId === 50 || this.incomeCommon.smallClassId === 40) {
      let reverseErrorTip = '';
      let reverseNum = 0;
      if (this.reverseDetailList && this.reverseDetailList.length > 0) {
        this.reverseDetailList.map((reverseDetail, index) => {
          if (reverseDetail.modelId !== this.addForm.value.modelId) {
            reverseErrorTip += '销账明细第' + (index + 1) + '行物料型号与表单填写物料型号不一致，请检查！<br>';
            reverseNum += reverseDetail.saleQty;
          }
        });
        if (reverseErrorTip.length > 0) {
          this.messageService.warning(reverseErrorTip);
          return;
        }
        if (reverseNum !== this.addForm.value.quantity) {
          this.messageService.warning('销账明细总数量与入库数量不一致，请检查！');
          return;
        }
      }
      this.addForm.value.bookIdList = this.reverseDetailList.map((reverseDetail) => reverseDetail.id);
    }
    this.incomeCommon.depotId = this.addForm.value.depotId;
    this.incomeCommon.propertyRight = this.addForm.value.propertyRight;
    this.addForm.value.normsValue = JSON.stringify(this.normsValue);
    this.incomeCommon.incomeDetailCommonSaveDtoList = [this.addForm.value];
    if (this.type === 'add') {
      this.httpClient.post('/api/wms/income-common/add', this.incomeCommon)
        .subscribe((res: any) => {
          this.messageService.success('提交成功！');
          this.goBack();
        });
    } else if (this.type === 'edit') {
      // 可能是保存编辑，也可能是修改申请
      Object.keys(this.addForm.value).map(key => {
        this.incomeCommon[key] = this.addForm.value[key];
      });
      this.incomeCommon.nodeEndTypeId = 10;
      this.httpClient.post('/api/wms/income-common/update', this.incomeCommon)
        .subscribe((res: any) => {
          this.messageService.success('编辑成功！');
          this.goBack();
        });
    }
  }

}
