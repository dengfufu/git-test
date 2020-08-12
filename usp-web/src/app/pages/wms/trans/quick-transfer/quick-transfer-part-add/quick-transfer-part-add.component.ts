import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Checkbox, Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {WareTransService} from '../../ware-trans.service';

@Component({
  selector: 'app-outcome-part-add',
  templateUrl: './quick-transfer-part-add.component.html',
  styleUrls: ['./quick-transfer-part-add.component.less']
})
export class QuickTransferPartAddComponent implements OnInit {

  @Input() partCheckedList: any[] = [];
  @Input() depotId:any;

  searchForm: FormGroup;
  page = new Page();
  checkBox = new Checkbox();
  partList: any[] = [];
  selectOptions: any ={
    wareList: [{id: '1234',wareName: '冰箱1'},{id: '01234',wareName: '冰箱2'},{id: '12348',wareName: '冰箱3'}],
    normsList: [{id: '1234',name: '规格1'},{id: '01234',name: '规格2'},{id: '12348',name: '规格3'}],
    statusList: [{id: '1234',name: '全新'},{id: '01234',name: '修复'},{id: '12348',name: '损坏'}],
    modelList: [{id: '1234',name: '型号1'},{id: '01234',name: '型号2'},{id: '12348',name: '型号3'}]
  };
  loading = true;
  submitList: any = [];

  constructor(private httpClient: HttpClient,
              private cdf: ChangeDetectorRef,
              private router: Router,
              private modal: NzModalRef,
              private activatedRoute: ActivatedRoute,
              public wareTransService: WareTransService,
              private formBuilder: FormBuilder) {
    this.searchForm = this.formBuilder.group({
      catalogId: [],
      normsValue: [],
      modelId: [],
      brandId:[],
      status: [],
      barcode: [],
      sn:[],
      propertyRight:[],
      situation:[],
      createTime:[]
    });
  }

  ngOnInit(): void {
    this.queryPartList(true);
  }

  initCheckBox() {
    if(typeof this.partList === 'object' && this.partList != null){
      this.partList.forEach(item => {
        this.checkBox.mapOfCheckedId[item.id] = false;
        this.checkBox.dataIdList.push(item.id);
      });
      this.initCheckedPart();
    }
  }

  // 初始化已经选择的记录
  initCheckedPart() {
    if(typeof this.partCheckedList === 'object' && this.partCheckedList !== null){
      this.partCheckedList.forEach(item => {
        this.partList.forEach(part => {
          if(part.id === item.id){
            part.actualQty = part.actualQty - item.applyQuantity;
            part.applyQuantity = part.actualQty;
          }
        });
        const tmpList = [];
        this.partList.forEach(part => {
          if(part.actualQty > 0){
            tmpList.push(part);
          }
        });
        this.partList = tmpList;
      });
    }
  }

  // 查询备件列表
  queryPartList( reset: boolean = false) {
    this.loading = true;
    this.httpClient.post('/api/wms/stock-common/pageBy',this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
         if(res.data != null){
           this.partList = res.data.list;
           this.page.total= res.data.total;
           if(typeof this.partList != null){
             this.partList.forEach(item => {
               item.applyQuantity = item.actualQty;
               item.stockId = item.id;
             });
           }
           this.initCheckBox();
         }
      });
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.depotId = this.depotId;
    params.pageNum = this.page.pageNum;
    // 在库
    params.situation = 10;
    params.pageSize = this.page.pageSize;
    params.largeClassId = this.wareTransService.TRANS;
    params.smallClassId = this.wareTransService.TRANS_WARE_SHIFT;
    return params;
  }

  // 当记录数量改变时，自动选中
  numChange(data){
    if(typeof data === 'object' && data !== null){
      if(data.applyQuantity > 0){
        this.checkBox.mapOfCheckedId[data.id] = true;
      }else{
        this.checkBox.mapOfCheckedId[data.id] = false;
      }
    }
  }

  clearForm() {
    this.searchForm.reset();
  }

  // 返回
  goBack(){
    this.modal.destroy({code: 1});
  }

  // 添加物料记录
  addPartList(){
    if(typeof this.partList === 'object' && this.partList !== null){
      this.partList.forEach(item => {
        if(this.checkBox.mapOfCheckedId[item.id]){
          this.submitList.push(item);
        }
      });
    }
    this.modal.destroy({code: 0,data: this.submitList});
  }

  queryCatalogList(event) {
    this.httpClient.post('/api/wms/ware-catalog/match', {name: event})
      .subscribe((res: any) => {
        this.selectOptions.catalogList = res.data;
      });
  }

  queryBrandList(event) {
    this.httpClient.post('/api/wms/ware-brand/match', {name: event})
      .subscribe((res: any) => {
        this.selectOptions.brandList = res.data;
      });
  }

  queryDepotList(event) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.depotList = res.data.list;
      });
  }

  queryPropertyRightList(event) {
    this.httpClient.post('/api/wms/ware-property-right/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.propertyRightList = res.data.list;
      });
  }

  queryStatusList(event) {
    this.httpClient.post('/api/wms/ware-status/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.statusList = res.data.list;
      });
  }

  queryModelList(event) {
    const params = {
      name: event,
      catalogId: this.searchForm.value.catalogId,
      brandId: this.searchForm.value.brandId,
      pageNum: 1,
      pageSize: 100
    };
    this.httpClient.post('/api/wms/ware-model/query', params)
      .subscribe((res: any) => {
        this.selectOptions.modelList = res.data.list;
      });
  }
}
