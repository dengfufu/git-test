import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ActivatedRoute} from '@angular/router';
import {ConsignPartAddComponent} from './consign-part-add/consign-part-add.component';
import {WmsService} from '../../wms.service';
@Component({
  selector: 'app-ware-outcome-consign',
  templateUrl: './ware-outcome-consign.component.html',
  styleUrls: ['./ware-outcome-consign.component.less'],
})
export class WareOutcomeConsignComponent implements OnInit {

  depotId:any;
  addDetailForm: FormGroup;
  page = new Page();
  partList:any = [];
  // 备件总数
  total: any = 0;
  flag:any = {
    // 是否可编辑
    edit: 0,
    // 下拉菜单是否打开
    open: false,
  };
  transportType: any = 30;
  constructor( private modalService: NzModalService,
               private formBuilder: FormBuilder,
               private cdf: ChangeDetectorRef,
               private activatedRoute: ActivatedRoute,
               private messageService: NzMessageService,
               public wmsService: WmsService,
               private httpClient: HttpClient) {
    this.addDetailForm = this.formBuilder.group({
      consignBy: [],
      receiverId: [],
      totalBoxNum:[],
      expressCorpId:[],
      description: [],
      transportTypeId:[],
      area:[],
      receiveDistrict: [],
      receiveAddress: [],
      consignMobile:[],
      receiverMobile: [],
      expressType: [],
      expressNo: [],
      receiveName: [],
      consignTypeId: []
    });

  }

  ngOnInit(): void {
    this.activatedRoute.queryParams.subscribe(params => {
      this.depotId = params.depotId;
    });
    this.initSelectOption();
  }

  initSelectOption() {
    this.wmsService.queryUserList('');
    this.wmsService.queryAreaInfolist();
    this.wmsService.queryExpressCorpList();
  }

  addPartList() {
    if(this.addDetailForm.controls.totalBoxNum.value === null){
      this.messageService.warning('请输入总箱数');
      return;
    }
    const modal = this.modalService.create({
      nzTitle: '物料信息',
      nzWidth: 1300,
      nzFooter: null,
      nzContent: ConsignPartAddComponent,
      nzComponentParams: {
        depotId: this.depotId,
        partCheckedList: this.partList,
        totalBoxNum: this.addDetailForm.controls.totalBoxNum.value
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
              }else if(res.data.some(item => item.id === part.id && item.subBoxNum !== part.subBoxNum)){
                tmpList.push(part);
              }
            });
          }
          // 添加新的记录
          res.data.forEach(item => {
            tmpList.push(item);
          });
          this.partList = tmpList;
          this.sortPartList();
          this.reduceNum();
          this.cdf.markForCheck();
        }
      }
    });
  }

  // 排序
  sortPartList(){
    this.partList.sort((a, b) =>
      a.outcomeCode > b.outcomeCode ? 1 : -1
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

    // 删除物料记录
  deletePart(id,subBoxNum) {
    const tmpList = [];
    // 添加原来的记录
    if(typeof this.partList === 'object' && this.partList !== null){
      this.partList.forEach(part => {
        if(part.id !== id){
          tmpList.push(part);
        }else if(part.subBoxNum !== subBoxNum){
          tmpList.push(part);
        }
      });
    }
    this.partList = tmpList;
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
    this.httpClient.post('/api/wms/consign/add',this.getParams())
      .subscribe((res: any) => {
        if(res.code === 0){
          this.messageService.success('发货单提交成功');
          this.goBack();
        }
      });
  }

  // 检查参数
  checkParams(){
    if(this.addDetailForm.controls.consignBy.value === null){
      this.messageService.warning('请选择发货人');
      return true;
    }
    if(this.addDetailForm.controls.consignMobile.value === null){
      this.messageService.warning('请输入发货人联系方式');
      return true;
    }
    if(this.addDetailForm.controls.receiverId.value === null || this.addDetailForm.controls.receiveName.value === null){
      this.messageService.warning('请输入收货人');
      return true;
    }
    if(this.addDetailForm.controls.receiveDistrict.value === null){
      this.messageService.warning('请选择收货地址');
      return true;
    }
    if(this.addDetailForm.controls.receiveAddress.value === null){
      this.messageService.warning('请输入收货详细地址');
      return true;
    }
    if(this.addDetailForm.controls.transportTypeId.value === null){
      this.messageService.warning('请选择运送方式');
      return true;
    }
    if(this.addDetailForm.controls.totalBoxNum.value === null){
      this.messageService.warning('请输入总箱数');
      return true;
    }
    this.addDetailForm.get('description').setValidators(Validators.maxLength(100));
    if(this.addDetailForm.controls.transportTypeId.value ===  20){
      if(this.addDetailForm.controls.consignTypeId.value === null){
        this.messageService.warning('请选择托运方式');
        return true;
      }
    }
    if(this.addDetailForm.controls.transportTypeId.value ===  30){
      if(this.addDetailForm.controls.expressCorpId.value === null){
        this.messageService.warning('请选择快递公司');
        return true;
      }
      if(this.addDetailForm.controls.expressType.value === null){
        this.messageService.warning('请选择快递类型');
        return true;
      }
      if(this.addDetailForm.controls.expressNo.value === null){
        this.messageService.warning('请输入快递单号');
        return true;
      }
    }
    if(this.partList === null || this.partList.length === 0){
      this.messageService.warning('请选择发货物料');
      return true;
    }
  }

  getParams() {
    const params = Object.assign({}, this.addDetailForm.value);
    params.largeClassId = 20;
    params.consignDetailDtoList = this.partList;
    return params;
  }

  // 根据选择的userid获取对应的mobile
  consignByChange(userId){
      const userInfo = this.wmsService.selectOptions.userMap[userId];
      if (userInfo) {
        this.addDetailForm.patchValue({consignMobile: userInfo.mobile});
      }
  }

  // 收货人
  changeReceiveInfo(userId){
    this.wmsService.selectOptions.userList.forEach(item => {
      if(item.userId === userId){
        this.addDetailForm.controls.receiveName.setValue(item.userName);
        this.addDetailForm.controls.receiverId.setValue(item.userId);
      }
    });
    if(userId > 0){
      const userInfo = this.wmsService.selectOptions.userMap[userId];
      this.addDetailForm.controls.receiverMobile.setValue(userInfo.mobile);
    }
  }

  goBack() {
    history.back();
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
    if (event !== undefined && event.length && event.length > 2) {
      this.addDetailForm.controls.receiveDistrict.setValue(event[2]);
    }
  }
}
