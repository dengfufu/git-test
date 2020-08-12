import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NzIconService} from 'ng-zorro-antd';
import {Page} from '@core/interceptor/result';

@Component({
  selector: 'app-ware-return-receive',
  templateUrl: './ware-return-receive.component.html',
  styleUrls: ['./ware-return-receive.component.less']
})
export class WareReturnReceiveComponent implements OnInit {

  incomeDetail: any = {
    // id: '121453425345236', incomeId: '12341534523453453', catalogId: '1234153546', catalogName: '内存',
    // createBy: '1216363456356', createByName: '张大帅', incomeTypeName: '采购入库', createTime: '2019-09-09 19:09:09',
    // contId: '12345', purchaseDetailId: '0909090', smallClassName: '公司采购入库',
    // modelId: '2413352465354645', modelName:'大型内存条', incomeStatus: 10, incomeStatusName: '待审批',
    // brandId: '1241515', brandName: '华硕', supplierName: '富士康', depotName: '总库房', propertyRightName: '紫金之巅',
    // normsValue: {颜色: '红色', 内存: '4G'}, quantity: 3,serviceEndDate: '2019-12-30', sn: 'ZJkl-0918300009',
    // barcode: '201909090001', status: 1, statusName: '待入库', situation: 2, situationName:'在途', unitPrice: 19.08
  };
  flowInstanceTraceList: any[] = [];
  flowInstanceNodeList: any[] = [];
  id: any;
  objectKeys = Object.keys;
  dispatchPartList = [];
  page = new Page();
  receiveDetail = {
    applyStoreName:'深圳库房',
    outStoreName:'出库库房',
    dispatchNo:'发货单号',
    transType:'快递',
    totalBoxNum:7,
    expressNo:23452352,
    expressDate:2019-12-12,
    consignorName:'小林',
    consignorContact:'13184819999',
    receiverName:'张张',
    receiverContact:'134512351',
    receiveAddress:'广东省',
    note:'备注'
  };
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private iconService: NzIconService,
    private cdf: ChangeDetectorRef
  ) {
  }

  ngOnInit() {
    this.initDispatchPartList();
  }

  goBack() {
    history.back();
  }


  initDispatchPartList() {
    const obj =
      {
        applyNo:'12434',
        depotName:'库房122',
        applicantName:'林小小',
        partName:'备件122',
        brandName:'劳力士',
        modelName:'型号12',
        quantity: '1233',
        emergencyLevel:'十分紧急',
        applyDate:'2019-12-11 12:00:00',
        outDepotName:'出库库房12',
        transPartDetail:'备件大详情',
        count:12,
        partInfoList:[]
      };
    this.dispatchPartList = [ ...this.dispatchPartList, obj];
  }

}
