import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {Page} from '@core/interceptor/result';

import {ActivatedRoute, Router} from '@angular/router';

import {HttpClient} from '@angular/common/http';
import {DatePipe} from '@angular/common';
import {TransPartDetailAddComponent} from '../trans-part-detail-add/trans-part-detail-add.component';
import {TransPartDetailListComponent} from '../trans-part-detail-list/trans-part-detail-list.component';
import {StockPartListComponent} from '../stock-part-list/stock-part-list.component';
import {WareTransService} from '../../ware-trans.service';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'to-be-received-list.component',
  templateUrl: './to-be-received-list.component.html',
  styleUrls: [ './to-be-received-list.component.less']
})
export class ToBeReceivedListComponent implements OnInit {

  list: any[] = [];

  searchForm: FormGroup;
  page = new Page();
  addForm: FormGroup;
  radioValue: any;

  isAllDisplayDataChecked = false;
  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  isBatchSelectVisible = false;

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


  storeOptions = [
    { label: '库房1', value: '1' },
    { label: '库房2', value: '2' },
    { label: '库房3', value: '3' }
  ];
  storeSelectedValue: any;
  loading = false;

  constructor(
    private modalService: NzModalService,
    private formBuilder: FormBuilder,
    private cdf: ChangeDetectorRef,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private datePipe: DatePipe,
    private router: Router,
    private wareTransService  : WareTransService,
    private activateRoute: ActivatedRoute,
  ) {
    this.searchForm = new FormGroup({});
    this.searchForm = this.formBuilder.group({
      ware: ['', Validators.required],
      model: ['', Validators.required],
      num: ['', Validators.required],
    });


  }

  ngOnInit() {
    // console.log(this.incomeMainService.incomeMain);
    this.queryList();
  }

  deleteDetail(index: number) {
    this.list.splice(index,1);
  }

  addSubmit() {

  }

  queryList( reset: boolean = false) {
    const params = this.getParams();
    if (reset) {
      this.page.pageNum = 1;
      params.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/wms/consign/list',this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      )
      .subscribe((res: any) => {
        console.log('res,res,',res);
        this.list = res.data.list;
      });
  }


  saveDetail() {

  }

  goBack() {
    history.back();
  }

  handleCancel() {
    this.isBatchSelectVisible = false;
  }



  confirmReceive(id) {
    this.router.navigate(['../ware-trans-receive'], {queryParams: {id}, relativeTo: this.activatedRoute});
  }

  reset() {
    this.searchForm.reset();
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_TRANSFER;
    params.signed = 'N';
    params.flowNodeTypeList = [this.wareTransService.RECEIVE];
    return params;
  }

  handleOk() {

  }
}
