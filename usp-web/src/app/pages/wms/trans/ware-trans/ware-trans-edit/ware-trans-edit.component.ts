import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {WmsService} from '../../../wms.service';
import {filter} from 'rxjs/operators';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-ware-trans-edit',
  templateUrl: './ware-trans-edit.component.html',
  styleUrls: ['./ware-trans-edit.component.less']
})
export class WareTransEditComponent implements OnInit {

  type = '';
  incomeDetailList: any[] = [];
  showList: any[] = [];
  partInfoList: any[] = [];

  normsValue: any = {};

  addDetailForm: FormGroup;
  page = new Page();
  addForm: FormGroup;
  priorityOptions: any;
  wareOptions: any;
  normsOptions: any;
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
      {id: '325345245235', name: '紧急'},
      {id: '453456356', name: '十分紧急'}
    ],
    wareOptions: [
      {id: '325345245235', name: 'GTX 1080Ti'},
      {id: '453456356', name: '天耀2330'}
    ],
    modelOptions: [
      {id: '325345245235', name: 'BBMMP'},
      {id: '453456356', name: '天耀2330'}
    ],
    statusList: [
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
    private datePipe: DatePipe
  ) {
    this.addDetailForm = new FormGroup({});
    this.addDetailForm = this.formBuilder.group({
      ware: ['', Validators.required],
      model: ['', Validators.required],
      num: ['', Validators.required],
    });

    this.addForm = new FormGroup({});
    this.addForm = this.formBuilder.group({
      applyDepotId: ['', Validators.required],
      applicant: ['', Validators.required],
      applyDate: ['', Validators.required],
      applyNote: ['', Validators.required],
      priorityLevel: ['', Validators.required],
      partInfoList:[[], Validators.required],
    });

  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
  }

  goBack() {
    history.back();
  }



  clearDetailForm() {
    this.addDetailForm.reset();
    this.normsValue = {};
  }

  deleteDetail(index) {
    // this.tmpList.splice(index, 1);
    this.showList.splice(index, 1);
    this.partInfoList.splice(index, 1);
    this.setPartInfoList();
  }

  saveDetail() {
    console.log('this.addFrom.value',this.addForm.value);
  }

  setPartInfoList (){
    this.addForm.controls.partInfoList.setValue(this.partInfoList);
  }

  addSubmit() {
    console.log('this.addForm',this.addForm.value);
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
    if(this.addDetailForm.valid === false){
      return;
    }
    const obj = {
      wareId:'',
      normsValue:'',
      modelId: '',
      num:''
    };
    if(this.addDetailForm.value.ware) {
      this.addDetailForm.value.wareId = this.addDetailForm.value.ware.id;
      this.addDetailForm.value.wareName = this.addDetailForm.value.ware.name;
      obj.wareId =  this.addDetailForm.value.ware.id;
    }
    if(this.addDetailForm.value.model) {
      this.addDetailForm.value.modelId = this.addDetailForm.value.model.id;
      this.addDetailForm.value.modelName = this.addDetailForm.value.model.name;
      obj.modelId =  this.addDetailForm.value.model.id;
    }
    const normsName = Object.keys(this.normsValue).map(
      key => {
        const val = this.normsValue[key];
        return key + ':' + val;
      }
    ).join(',');

    this.addDetailForm.value.normsName = normsName;
    obj.normsValue = normsName;
    obj.num = this.addDetailForm.value.num;

    this.partInfoList.push(obj);
    this.showList = [ ...this.showList, this.addDetailForm.value];
    console.log('this.item', this.showList);
    this.setPartInfoList();
  }


}
