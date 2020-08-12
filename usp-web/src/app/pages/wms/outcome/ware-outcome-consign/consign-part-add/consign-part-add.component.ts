import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Checkbox, Page} from '@core/interceptor/result';
import {ActivatedRoute, Router} from '@angular/router';
import {finalize} from 'rxjs/operators';
import {WmsService} from '../../../wms.service';

@Component({
  selector: 'app-consign-part-add',
  templateUrl: './consign-part-add.component.html',
  styleUrls: ['./consign-part-add.component.less']
})
export class ConsignPartAddComponent implements OnInit {

  @Input() partCheckedList: any[] = [];
  @Input() depotId:any;
  @Input() totalBoxNum:any;

  searchForm: FormGroup;
  page = new Page();
  checkBox = new Checkbox();
  partList: any[] = [];
  loading = true;
  submitList: any = [];

  constructor(private httpClient: HttpClient,
              private cdf: ChangeDetectorRef,
              private router: Router,
              private modal: NzModalRef,
              private activatedRoute: ActivatedRoute,
              public wmsService: WmsService,
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
      createTimeList:[]
    });
  }

  ngOnInit(): void {
    this.queryPartList(true);
    this.initSelectOptions();
  }

  initSelectOptions(){
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryDepotList('');
    this.wmsService.queryPropertyRightList('');
    this.wmsService.queryStatusList('');
    this.wmsService.queryModelList('', 0, 0);
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
            part.sum = part.sum - item.quantity;
            part.quantity = part.sum;
          }
        });
        const tmpList = [];
        this.partList.forEach(part => {
          if(part.sum > 0){
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
    this.httpClient.post('/api/wms/outcome-common/list',this.getParams())
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
               item.sum = item.quantity;
               item.subBoxNum = 1;
             });
           }
           this.initCheckBox();
         }
      });
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.flowNodeTypeList = [40];
    params.depotId = this.depotId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 当记录数量改变时，自动选中
  numChange(data){
    if(typeof data === 'object' && data !== null){
      if(data.quantity > 0){
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

}
