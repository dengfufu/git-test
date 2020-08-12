import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {WmsService} from '../../../wms.service';
import {filter, finalize} from 'rxjs/operators';
import {DatePipe} from '@angular/common';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-add-ware-income',
  templateUrl: './ware-trans-apply.component.html',
  styleUrls: ['./ware-trans-apply.component.less']
})
export class WareTransApplyComponent implements OnInit {

  type = '';
  incomeDetailList: any[] = [];
  transDetailCommonSaveDtoList: any[] = [];
  normsValue: any = {};

  addDetailForm: FormGroup;
  page = new Page();
  submitForm: FormGroup;
  priorityOptions: any;
  wareOptions: any;
  normsOptions: any;
  fromDepotSelectionOptions = [];

  selectOptions: any = {
    applicantOptions: [
      {id: '76765754757', name: '内存条'},
      {id: '4678578576868', name: '显卡'},
      {id: '25656456456', name: '散热器'}
    ],
    depotOptions: [
      {id: '12341453452345', name: '库房1'},
      {id: '354645634564', name: '库房2'}
    ],
    priorityOptions: [
      {id: '1', name: '紧急'},
      {id: '2', name: '十分紧急'}
    ],
    brandOptions: [
      {id: '325345245235', name: '美光'},
      {id: '453456356', name: '华为'}
    ],
    wareOptions: [
      {id: '325345245235', name: '电脑'},
      {id: '453456356', name: '显示器'}
    ],
    modelOptions: [
      {id: '325345245235', name: 'BBMMP'},
      {id: '453456356', name: 'XXXX'}
    ],
    statusOptions: [
      {id: 10, name: '全新'},
      {id: 20, name: '待修'},
      {id: 30, name: '修复'},
      {id: 40, name: '报废'}
    ],
    normsList: [
      {attribute: '内存', value: ['2G', '4G','8G','16G','32G']},
      {attribute: '插口', value: []},
    ]
  };

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe,
    public wareTransService: WareTransService,
    public wmsService : WmsService
  ) {

    this.addDetailForm = this.formBuilder.group({
      catalog: ['', Validators.required],
      model: ['', Validators.required],
      applyQuantity: ['', Validators.required],
      brand: ['', Validators.required],
      status: ['', Validators.required],
    });

    this.submitForm = this.formBuilder.group({
      fromDepotId: ['', Validators.required],
      toDepotId: ['', Validators.required],
      applyDate: ['', Validators.required],
      applyNote: ['', Validators.required],
      priorityLevel: ['', Validators.required],
      description: [''],
      transDetailCommonSaveDtoList:[[]],
      largeClassId: [this.wareTransService.TRANS,Validators.required],
      smallClassId: [this.wareTransService.TRANS_WARE_TRANSFER,Validators.required],
    });
  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.queryDepotList('');
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryModelList('', 0, 0);
    this.wmsService.queryStatusList('');
  }

  goBack() {
    history.back();
  }


  // 查询库房下拉列表，加上分页仅为限制条数，下同
  queryDepotList(event) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.fromDepotSelectionOptions = res.data.list;
      })
  }



  clearDetailForm() {
    this.addDetailForm.reset();
    this.normsValue = {};
  }

  deleteDetail(index) {
    this.transDetailCommonSaveDtoList.splice(index, 1);
    this.setPartInfoList();
  }

  saveDetail() {

    console.log('this.addFrom.value',this.submitForm.value);
  }

  setPartInfoList (){
    this.submitForm.controls.transDetailCommonSaveDtoList.setValue(this.transDetailCommonSaveDtoList);

  }

  addSubmit() {
    this.setPartInfoList();
    if (this.submitForm.value.applyDate) {
      this.submitForm.value.applyDate = this.datePipe.transform(this.submitForm.value.applyDate, 'yyyy-MM-dd');
    }
    this.httpClient.post('/api/wms/trans-ware-common/add',this.submitForm.value)
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('申请转库成功');
          // this.goBack();
        }
      });
  }

  initSelectData(){
    const tempOptions = [
      {
        label: '非常紧急',
        value: 1
      },
      {
        label: '紧急',
        value: 2
      }
      ,
      {
        label: '一般',
        value: 3
      }
    ];
    this.priorityOptions = tempOptions;
    const tempWareOptions = [
      {
        label: '电脑',
        value: 1
      },
      {
        label: '手机',
        value: 2
      }
      ,
      {
        label: '平板',
        value: 3
      }
    ];
    this.wareOptions = tempWareOptions;
  }

  addDetail() {
    console.log('this.form.value', this.addDetailForm.value);
    if(this.addDetailForm.valid === false){
      return;
    }
    const showObj = {
      catalogId:'',
      catalogName:'',
      brandId:'',
      brandName:'',
      normsValue:'',
      modelId:'',
      modelName:'',
      applyQuantity:'',
      status:'',
      statusName:''
    };
    if(this.addDetailForm.value.catalog) {
      showObj.catalogId = this.addDetailForm.value.catalog.id;
      showObj.catalogName= this.addDetailForm.value.catalog.name;
    }
    if(this.addDetailForm.value.brand) {
      showObj.brandId = this.addDetailForm.value.brand.id;
      showObj.brandName= this.addDetailForm.value.brand.name;
    }
    if(this.addDetailForm.value.model) {
      showObj.modelId = this.addDetailForm.value.model.id;
      showObj.modelName = this.addDetailForm.value.model.name;
    }
    if(this.addDetailForm.value.status) {
      showObj.statusName = this.addDetailForm.value.status.name;
    }
    const normsValue = Object.keys(this.normsValue).map(
      key => {
        const val = this.normsValue[key];
        return key + ':' + val;
      }
    ).join(',');
    showObj.normsValue = normsValue;
    showObj.applyQuantity = this.addDetailForm.value.applyQuantity;
    this.transDetailCommonSaveDtoList = [ ...this.transDetailCommonSaveDtoList, showObj];
  }

  setList() {
    const paramsObj = {
      catalogId:'',
      normsValue:'',
      modelId: '',
      brandId:'',
      applyQuantity:'',
      status:''
    };
  }




  brandChange(event) {
    // this.addDetailForm.controls.model.reset();
    // if(event) {
    //   this.addDetailForm.controls.brand = event;
    //   this.wmsService.queryModelList('', this.addDetailForm.value.catalog.id, event.id);
    // }else {
    //   this.wmsService.selectOptions.modelList = [];
    // }
  }
}
