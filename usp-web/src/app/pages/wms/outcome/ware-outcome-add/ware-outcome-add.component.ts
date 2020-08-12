import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {OutcomePartAddComponent} from './outcome-part-add/outcome-part-add.component';
import {ActivatedRoute} from '@angular/router';
import { OutcomeTypeEnum } from '@core/service/enums.service';
import {WmsService} from '../../wms.service';
@Component({
  selector: 'app-ware-outcome-add',
  templateUrl: './ware-outcome-add.component.html',
  styleUrls: ['./ware-outcome-add.component.less'],
})
export class WareOutcomeAddComponent implements OnInit {

  smallClassId:number;
  smallClassEnum = OutcomeTypeEnum;
  outcomeId:any;
  addDetailForm: FormGroup;
  page = new Page();
  partList:any = [];
  // 备件总数
  total: any = 0;
  flag:any = {
    edit: 0,
  };
  constructor( private modalService: NzModalService,
               private formBuilder: FormBuilder,
               private cdf: ChangeDetectorRef,
               public wmsService: WmsService,
               private activatedRoute: ActivatedRoute,
               private messageService: NzMessageService,
               private httpClient: HttpClient) {
    this.addDetailForm = this.formBuilder.group({
      assistUserId: [],
      depotId: [, Validators.required],
      workId: [],
      supplierId: [],
      description: []
    });

  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.outcomeId = params.outcomeId;
      this.smallClassId = params.smallClassId;
    });
    this.getSaved();
    this.initSelectOption();
  }

  setOutcomeDetailList(){
    if(typeof this.partList === 'object' && this.partList.length > 0){
      const tmpList = [];
      this.partList.forEach(item => {tmpList.push({stockId: item.id, quantity: item.quantity})});
      this.addDetailForm.controls.outcomeDetailCommonSaveDtoList.setValue(tmpList);
    }
  }

  initSelectOption() {
    this.wmsService.queryDepotList('');
    this.wmsService.queryUserList('');
    this.wmsService.queryWareSupplierlist();
    this.wmsService.queryBookVendorList('');
  }

  // 获取保存的出库信息
  getSaved() {
        if(this.outcomeId > 0){
          console.log('jinru');
          this.httpClient.get('/api/wms/outcome-common/save/detail/' + this.outcomeId)
            .subscribe((res: any) => {
              if(res.data !== null){
                const tmp = res.data;
                this.smallClassId = tmp.smallClassId;
                this.addDetailForm.controls.depotId.setValue(tmp.depotId);
                this.addDetailForm.controls.supplierId.setValue(tmp.supplierId);
                this.addDetailForm.controls.assistUserId.setValue(tmp.assistUserId);
                this.addDetailForm.controls.workId.setValue(tmp.workId);
                this.addDetailForm.controls.description.setValue(tmp.description);
                this.partList = tmp.stockCommonResultDtoList;
                if(typeof this.partList === 'object' && this.partList !== null){
                  this.partList.forEach(item => {
                    item.stockId = item.id;
                  });
                  this.sortPartList();
                  this.reduceNum();
                  this.mergeRow();
                  this.cdf.markForCheck();
                }
              }
            });
        }
  }

  addPartList() {
    if(typeof this.addDetailForm.controls.depotId.value === 'undefined' || this.addDetailForm.controls.depotId.value === null){
      this.messageService.warning('请选择库房');
      return;
    }
    const modal = this.modalService.create({
      nzTitle: '物料信息',
      nzWidth: 1300,
      nzFooter: null,
      nzContent: OutcomePartAddComponent,
      nzComponentParams: {
        partCheckedList: this.partList,
        depotId: this.addDetailForm.controls.depotId.value
      }
    });
    modal.afterClose.subscribe(res => {
      if(typeof res === 'object' && res !== null){
        if(res.code === 0){
          const tmpList = [];
          // 添加原来的记录
          if(typeof this.partList === 'object' && this.partList !== null){
            this.partList.forEach(part => {
              if(res.data.every(item => item.id !== part.id)){
                tmpList.push(part);
              }
            });
          }
          // 添加新的记录
          res.data.forEach(item => {
            this.partList.forEach(part => {
              if(item.id === part.id){
                item.quantity = item.quantity + part.quantity;
                item.actualQty = item.actualQty + part.quantity;
              }
            });
            tmpList.push(item);
          });
          this.partList = tmpList;
          this.sortPartList();
          this.reduceNum();
          this.mergeRow();
          this.cdf.markForCheck();
        }
      }
    });
  }

  // 排序
  sortPartList(){
    // 排序排序规则为wareName > status > modelName > normsValue(排序执行时间越晚，优先级越高)
    this.partList.sort((a, b) =>
      a.normsValue < b.normsValue ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.modelName > b.modelName ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.statusName < b.statusName ? 1 : -1
    );
    this.partList.sort((a, b) =>
      a.catalogName > b.catalogName ? 1 : -1
    );
  }
  // 合计
  reduceNum(){
    if(typeof this.partList === 'object' && this.partList.length > 0){
      const totalList = [];
      this.partList.forEach(item => totalList.push(Number.parseInt(item.quantity, 10)));
      this.total = totalList.reduce((pre,cur) =>{
          return pre + cur;
        }
      );
    }else {
      this.total = 0;
    }

  }

  // 合并单元格用于小计
  mergeRow(){
    let first = true;
    let firstItem = this.partList[0];
    this.partList.forEach(item  => {
      if(first){
        firstItem.all = item.quantity;
        firstItem.rowNum = 1;
        first = false;
      }else {
        if(firstItem.catalogId === item.catalogId && firstItem.status === item.status && firstItem.modelId === item.modelId &&
          firstItem.normsValue === item.normsValue){
          firstItem.all = Number.parseInt(item.quantity, 10) + Number.parseInt(firstItem.all, 10);
          firstItem.rowNum++;
        }else {
          firstItem = item;
          firstItem.all = item.quantity;
          firstItem.rowNum = 1;
        }
      }
    });
  }
  // 保存当前填写的单
  save() {
    this.httpClient.post('/api/wms/outcome-common/save',this.getParams())
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('出库单保存成功');
          this.goBack();
        }
      });
  }

  // 修改保存的单
  updateSave() {
    this.httpClient.post('/api/wms/outcome-common/save/update',this.getParams())
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('出库单修改成功');
          this.goBack();
        }
      });
  }

    // 删除物料记录
  deletePart(id) {
    this.partList = this.partList.filter(item => item.id !== id);
    this.reduceNum();
  }

  // 修改物料记录数量
  editPart(no) {
    this.flag.edit = no;
    if(no === 0){
      this.reduceNum();
    }
  }

  addSubmit() {
    if(this.checkParams()){
      return;
    }
    this.httpClient.post('/api/wms/outcome-common/add',this.getParams())
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('出库单提交成功');
          this.goBack();
        }
      });
  }

  // 校验参数
  checkParams(){
    if(this.addDetailForm.controls.depotId.value === null){
      this.messageService.warning('请选择库房');
      return true;
    }
    if(typeof this.partList !== 'object' || this.partList.length === 0){
      this.messageService.warning('请选择出库物料');
      return true;
    }
    if(this.smallClassEnum.RETURN_VENDOR ===  Number.parseInt(this.smallClassId.toString(),10)){
      if(this.addDetailForm.controls.supplierId.value === null){
        this.messageService.warning('请选择供应商');
        return true;
      }
      if(this.addDetailForm.controls.workId.value === null){
        this.messageService.warning('请选择工单');
        return true;
      }
    }
    if(this.smallClassEnum.SALE_BORROW ===  Number.parseInt(this.smallClassId.toString(),10) || this.smallClassEnum.CORP_SALE ===  Number.parseInt(this.smallClassId.toString(),10)){
      if(this.addDetailForm.controls.assistUserId.value === null){
        this.messageService.warning('请选择协助经办人');
        return true;
      }
    }
  }

  getParams() {
    const params = Object.assign({}, this.addDetailForm.value);
    params.smallClassId = this.smallClassId;
    params.mainId = this.outcomeId;
    params.largeClassId = 20;
    params.situation = 10;
    params.outcomeDetailCommonSaveDtoList = this.partList;
    return params;
  }

  goBack() {
    history.back();
  }
}
