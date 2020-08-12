import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import { NzInputDirective } from 'ng-zorro-antd/input';
import {AnyfixService} from '@core/service/anyfix.service';
import {DispatchComponent} from '../../../../anyfix/dispatch/dispatch.component';
import {SelectBranchComponent} from '../../../../anyfix/handle/select-branch/select-branch.component';
import {CustomRecallComponent} from '../../../../anyfix/handle/custom-recall/custom-recall.component';
import {ServiceReturnComponent} from '../../../../anyfix/handle/service-return/service-return.component';
import {ToAssignComponent} from '../../../../anyfix/assign/to-assign/to-assign.component';
import {RecallAssignComponent} from '../../../../anyfix/assign/recall-asssign/recall-assign.component';
import {WareDeliveryAddComponent} from '../../ware-trans/ware-delivery-add/ware-delivery-add.component';

@Component({
  selector: 'app-settle-branch-detail',
  templateUrl: './ware-return-detail.component.html',
  styleUrls: ['./ware-return-detail.component.less']
})
export class WareReturnDetailComponent implements OnInit {

  @ViewChild(NzInputDirective, { static: false, read: ElementRef }) inputElement: ElementRef;

  workId: any;
  demanderCorp: any;
  serviceCorp: any;
  work: any = {};
  label: any = {};
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  // 工单评价信息
  workEvaluate: any;
  loading = true;
  // 预约时间相关变量
  bookTime: any = {};
  // 工单操作按钮是否可点
  disable = false;
  value: any;
  previewImage: string | undefined = '';
  previewVisible = false;
  index = 2;
  examineContent: any;
  editId: string | null;

  isAllDisplayDataChecked = false;
  isAllDisplayReceiveDataChecked = false;
  isIndeterminate = false;
  listOfDisplayData:  [];
  deliveryList = [];
  mapOfCheckedId: { [key: string]: boolean } = {};
  numberOfChecked = 0;
  numberOfReceiveCheck = 0;
  node = 1;
  receiveDeliveryList =  [];
  mapOfDeliveryCheckedId: { [key: string]: boolean } = {};
  listOfData = [];
  i = 0;
  isReceiveIndeterminate = false;
  dateEditId: any;

  flowInstanceTraceList = [
    {id: '254625646', templateNoteName: '发货', completeHandlerName: '胡志鹏',
      completeTime: '2019-09-10 13:12:00', completeDescription: '核对无误，准许入库', completed: 'Y'},
    {id: '254625646', templateNoteName: '收货', completeHandlerName: '胡志鹏',
      completeTime: '2019-09-10 13:12:00', completeDescription: '核对无误，准许入库', completed: 'Y'}
  ];

  allowOptions = [
    {
      label:'批准',
      value:1
    },
    {
      label:'不批准',
      value:2
    }
  ];
  receiver: any;
  receiverSelectOptions: any;
  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private msg: NzMessageService,
              public anyfixService: AnyfixService) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.workId = queryParams.workId;
      this.node = queryParams.node;
      console.log('node',this.node);
    });
  }

  @HostListener('window:click', ['$event'])
  handleClick(e: MouseEvent): void {
  };



  ngOnInit() {
    // this.initBookTime();
    // this.query();

    this.initReceiveDelivery();
    console.log('ngOnInit listOfData',this.deliveryList)
  }


  initReceiveDelivery() {
    const tempList =[
      {
        id:'1',
        propertyName: '手机',
        normsName: '大',
        modelName: 'XXXMSJ',
        propertyId: '123442',
        barcode: '23423',
        statusName: '保内',
        count:'0',
        receiveDate:'2019-12-12 10:10:10'
      },
      {
        id:'2',
        propertyName: '手机',
        normsName: '大',
        modelName: 'XXXMSJ',
        propertyId: '123442',
        barcode: '23423',
        statusName: '保内',
        count:'0',
        receiveDate:'2019-12-12 10:10:10'
      }
    ];
    this.receiveDeliveryList = tempList;
    console.log('receiveDeliveryList',this.receiveDeliveryList);
  }

  onIndexChange(event: number): void {
    this.index = event;
  }

  handlePreview = (url: any) => {
    this.previewImage = url;
    this.previewVisible = true;
  };

  initBookTime() {
    this.bookTime.startValue = new Date();
    this.bookTime.endValue = new Date();
  }

  onStartChange(date: Date): void {
    this.bookTime.endValue = date;
  }

  goBack(){
    history.go(-1);
  }

  // 查询工单信息
  query(){
    this.loading = true;
    this.httpClient
      .get('/api/anyfix/work-request/detail/' + this.workId)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.data !== null) {
          this.work = res.data;
          this.demanderCorp = res.data.demanderCorp;
          this.serviceCorp = res.data.serviceCorp;
          if(this.work !== 'undefined'){
            switch (this.work.workStatus) {
              case this.anyfixService.TO_DISTRIBUTE: this.label = '提单';
                break;
              case this.anyfixService.TO_HANDLE: this.label = '分配';
                break;
              case this.anyfixService.TO_ASSIGN: this.label = '派单';
                break;
              case this.anyfixService.TO_CLAIM: this.label = '撤回派单';
                break;
              case this.anyfixService.TO_SIGN: this.label = '签到';
                this.disable = true;
                break;
              case this.anyfixService.TO_SERVICE: this.label = '服务';
                this.disable = true;
                break;
              case this.anyfixService.IN_SERVICE: this.label = '服务中';
                this.disable = true;
                break;
              case this.anyfixService.TO_EVALUATE: this.label = '评价';
                this.disable = true;
                break;
              case this.anyfixService.CLOSED: this.label = '已完成';
                this.disable = true;
                break;
              case this.anyfixService.RETURNED: this.label = '已退单';
                this.disable = true;
                break;
              case this.anyfixService.CANCELED: this.label = '已撤单';
                this.disable = true;
                break;
            }
            if(this.work.workStatus >= this.anyfixService.TO_SIGN){
              this.bookTime.bookingTimeChange = true;
            }
            this.bookTime.startValue = this.work.bookTimeBegin;
            this.bookTime.endValue = this.work.bookTimeEnd;
          }
        }
      });
    this.httpClient.get('/api/anyfix/work-evaluate/selectByWorkId/'+this.workId)
      .subscribe((res: any) => {
        this.workEvaluate = res.data;
        console.log(this.workEvaluate);
      })
  }

  operate(label) {
    switch (label) {
      case '提单':
        this.toDispatch();
        break;
      case '分配':
        this.toHandle();
        break;
      case '派单':
        this.toAssign();
        break;
      case '撤回派单':
        this.toRecallAssign();
        break;
    }
  }

  // 进入工单分配页面
  toDispatch(): void {
    const modal = this.modalService.create({
      nzTitle: '提单',
      nzContent: DispatchComponent,
      nzFooter: null,
      nzWidth: 850,
      nzComponentParams: {
        workId: this.workId,
        demanderCorp: this.demanderCorp,
        isRadio: true
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.modalService.success({
            nzTitle: '提单成功',
            nzContent: '等待分配.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入工单受理页面
  toHandle(): void {
    const modal = this.modalService.create({
      nzTitle: '分配',
      nzContent: SelectBranchComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        url: '/api/anyfix/work-transfer/service/handle',
        message: '分配',
        workId: this.workId,
        serviceCorp: this.serviceCorp,
        isRadio: true
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.modalService.success({
            nzTitle: '工单分配成功',
            nzContent: '等待派单.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入工单转处理页面
  turnHandleWork(): void {
    const modal = this.modalService.create({
      nzTitle: '转处理',
      nzContent: SelectBranchComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        url: '/api/anyfix/work-transfer/service/turn/handle',
        message: '转处理',
        workId: this.workId,
        serviceCorp: this.serviceCorp,
        isRadio: true
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.modalService.success({
            nzTitle: '工单转处理成功',
            nzContent: '等待派单.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入客户撤单页面
  customRecall(): void {
    const modal = this.modalService.create({
      nzTitle: '请选择撤单原因 :',
      nzContent: CustomRecallComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId,
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.modalService.success({
            nzTitle: '客户撤单成功',
            nzContent: '如还有需要，请联系客服.....'
          });
          this.query();
        }
      }
    });
  }

  // 进入客服退单页面
  serviceReturn(): void {
    const modal = this.modalService.create({
      nzTitle: '请选择退单原因:',
      nzContent: ServiceReturnComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId,
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.modalService.success({
            nzTitle: '客服退单成功',
            nzContent: '请通知客户.....'
          });
          this.query();
        }
      }
    });
  }

  // 服务商客服派工
  toAssign(){
    const modal = this.modalService.create({
      nzTitle: '工单派工',
      nzContent: ToAssignComponent,
      nzFooter: null,
      nzWidth: 500,
      nzComponentParams: {
        workId: this.workId
      }
    });

    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code === 0){
          this.query();
        }
      }
    });
  }

  // 服务商客服撤回派单
  toRecallAssign() {
    const modal = this.modalService.create({
      nzTitle: '撤回派单原因',
      nzContent: RecallAssignComponent,
      nzWidth: 350,
      nzFooter: null,
      nzComponentParams: {
        workId: this.workId
      }
    });

    modal.afterClose.subscribe(result => {
      if (result) {
        if(result.code !== null && result.code !== undefined){
          this.query();
          this.msg.success('撤回派单成功');
        }else{
          this.msg.error('撤回派单失败，请重试');
        }
      }
    });
  }

  addDelivery(data) {
    const modal = this.modalService.create({
      nzTitle: '',
      nzContent: WareDeliveryAddComponent,
      nzFooter: null,
      nzWidth: 1000,
      nzComponentParams: {
        object: data
      }
    });
    modal.afterOpen.subscribe(() => console.log('[afterOpen] emitted!'));
    modal.afterClose.subscribe(result => {
      const markUnSelected = [];
      let flag ;
      for (const index in result){
        flag = true;
        for(const item of this.deliveryList){
          if(result[index].id === item.id){
            flag = false;
            break;
          }
        }
        if(flag){
          markUnSelected.push(index);
        }
      }
      console.log('unSelected',markUnSelected);
      for(const item of markUnSelected){
        this.deliveryList.push(result[item]);
      }
      console.log('deliveryList', this.deliveryList);
    });
  }

  editWareDelivery(data: any) {

  }

  editDeliveryInfo() {

  }

  deleteReceiveDelivery() {
    const tempList = this.receiveDeliveryList;
    Object.keys(this.mapOfDeliveryCheckedId).forEach(key => {
      console.log('mapOfDeliveryCheckedId',this.mapOfDeliveryCheckedId[key]);
      if(this.mapOfDeliveryCheckedId[key]){
        for(let i=0; i<this.receiveDeliveryList.length;i++){
          if(this.receiveDeliveryList[i].id === key){
            tempList.splice(i,1);
          }
        }
      }
    });
    this.deliveryList = tempList;
  }


  checkAll(value: boolean) {
    console.log('checkAll this.deliveryList', this.deliveryList);
    this.deliveryList.filter(item => !item.disabled).forEach(item => (this.mapOfCheckedId[item.id] = value));
    this.refreshStatus();
  }




  refreshStatus() {
    this.isAllDisplayDataChecked = this.deliveryList
      .filter(item => !item.disabled)
      .every(item => this.mapOfCheckedId[item.propertyId]);
    this.isIndeterminate =
      this.deliveryList.filter(item => !item.disabled).some(item => this.mapOfCheckedId[item.propertyId]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.deliveryList.filter(item => this.mapOfCheckedId[item.propertyId]).length;
  }


  deleteDelivery() {
    const tempList = this.deliveryList;
    Object.keys(this.mapOfCheckedId).forEach(key => {
      console.log('mapOfCheckValue',this.mapOfCheckedId[key]);
      if(this.mapOfCheckedId[key]){
        for(let i=0; i<this.deliveryList.length;i++){
          if(this.deliveryList[i].id === key){
            tempList.splice(i,1);
          }
        }
      }
    });
    this.deliveryList = tempList;
  }

  setExpressInfo(result) {
    console.log('this.mapOfCheckedId',this.mapOfCheckedId);
    const tempList = this.deliveryList;
    Object.keys(this.mapOfCheckedId).forEach(key => {
      if(this.mapOfCheckedId[key]){
        for( const item of this.deliveryList){
          if(item.id === key){
            item.expressNo = result.expressNo;
            item.subBoxNum = result.subBoxNum;
            item.expressCorpName = result.expressCorpName;
          }
        }
      }
    });
    this.deliveryList = tempList;
  }

  startEdit(id: string, event: MouseEvent): void {
    event.preventDefault();
    event.stopPropagation();
    this.editId = id;
  }

  startDateEdit(id: string, event: MouseEvent): void {

    event.preventDefault();
    event.stopPropagation();
    this.dateEditId = id;
  }

  deleteRow(id: any) {
    this.listOfData = this.listOfData.filter(d => d.id !== id);
  }

  addReceive() {

  }

  checkReceiveAll(value: boolean) {
    this.receiveDeliveryList.filter(item => !item.disabled).forEach(item => (this.mapOfDeliveryCheckedId[item.id] = value));
    this.refreshReceiveStatus();
  }

  refreshReceiveStatus() {
    console.log('mapOfDeliveryCheckedId', this.mapOfDeliveryCheckedId);
    this.isAllDisplayReceiveDataChecked = this.receiveDeliveryList
      .filter(item => !item.disabled)
      .every(item => this.mapOfDeliveryCheckedId[item.id]);
    this.isReceiveIndeterminate =
      this.receiveDeliveryList.filter(item => !item.disabled).some(item => this.mapOfDeliveryCheckedId[item.id]) &&
      !this.isAllDisplayReceiveDataChecked;
    this.numberOfChecked = this.receiveDeliveryList.filter(item => this.mapOfDeliveryCheckedId[item.id]).length;
  }
}
