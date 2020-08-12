import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {UserService} from '@core/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class WmsService{

  constructor(private httpClient: HttpClient,
              private userService: UserService) {
  }

  params: any;

  // 下拉框数据，nzValue为id，nzLabel为name(物料规格例外,nzValue为attribute,nzLabel为value)
  selectOptions: any = {
    incomeSmallClassList: [
      {id: 10, name: '公司采购入库'},
      {id: 20, name: '厂商物料入库'},
      {id: 30, name: '现有物料入库'},
      {id: 40, name: '厂商返还入库'},
      {id: 50, name: '销售借用归还入库'},
      {id: 60, name: '领用退料入库'}
    ],
    outcomeSmallClassList: [
      {id: 70, name: '销售借用出库'},
      {id: 80, name: '公司销售出库'},
      {id: 90, name: '归还厂商出库'},
      {id: 100, name: '物料领用出库'}
    ],
    transSmallClassList: [
      {id: 110, name: '物料库存调拨'},
      {id: 120, name: '物料快速转库'},
      {id: 130, name: '待修物料返还'}
    ],
    auditDealList: [
      {id: 10, name: '通过'},
      {id: 20, name: '不通过，退回上一步'},
      {id: 30, name: '不通过，退回申请'},
      {id: 40, name: '不通过，流程结束'}
    ],
    transportTypeList: [
      {id: 30, name: '快递'},
      {id: 10, name: '自提'},
      {id: 20, name: '托运'}
    ],
    expressTypeList: [
      {id: 10, name: '物流'},
      {id: 20, name: '快件'},
      {id: 30, name: '慢件'}
    ],
    cosignTypeList: [
      {id: 10, name: '大巴'},
      {id: 20, name: '火车'},
      {id: 30, name: '轮船'},
      {id: 40, name: '飞机'},
      {id: 50, name: '其他'}
      ]
  };

  mapOptions: any = {
    statusMap:{
      [10]: '待填写',
      [20]: '待审批',
      [30]: '待审批',
      [40]: '待发货',
      [50]: '待收货',
      [60]: '待确认'
    }
  };

  // 查询库房下拉列表，加上分页仅为限制条数，下同
  queryDepotList(event) {
    this.httpClient.post('/api/wms/ware-depot/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.depotList = res.data.list;
      })
  }

  // 查询物料产权下拉列表
  queryPropertyRightList(event) {
    this.httpClient.post('/api/wms/ware-property-right/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.propertyRightList = res.data.list;
      })
  }

  // 查询物料分类下拉列表
  queryCatalogList(event) {
    this.httpClient.post('/api/wms/ware-catalog/match', {name: event})
      .subscribe((res: any) => {
        this.selectOptions.catalogList = res.data;
      })
  }

  // 查询物料品牌下拉列表
  queryBrandList(event) {
    this.httpClient.post('/api/wms/ware-brand/match', {name: event})
      .subscribe((res: any) => {
        this.selectOptions.brandList = res.data;
      })
  }

  // 查询物料型号下拉列表
  queryModelList(event, catalogId, brandId) {
    const params = {
      name: event,
      catalogId,
      brandId,
      pageNum: 1,
      pageSize: 100
    };
    this.httpClient.post('/api/wms/ware-model/query', params)
      .subscribe((res: any) => {
        this.selectOptions.modelList = res.data.list;
      })
  }

  // 查询物料状态下拉列表
  queryStatusList(event) {
    this.httpClient.post('/api/wms/ware-status/query', {name: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.statusList = res.data.list;
      })
  }

  // 查询物料规格下拉列表
  queryCatalogSpecs(catalogId) {
    this.httpClient.get(`/api/wms/ware-catalog-specs/list/${catalogId}`)
      .subscribe((res: any) => {
        this.selectOptions.normList = res.data;
      })
  }

  // 查询供应商下拉列表
  queryWareSupplierlist(){
    return this.httpClient
      .get(`/api/wms/ware-supplier/list/${this.userService.currentCorp.corpId}`)
      .subscribe((res: any) => {
        this.selectOptions.supplierList = res.data;
      });
  }

  // 查询厂商返还帐下拉列表
  queryBookVendorList(event) {
    this.httpClient.post('/api/wms/book-vendor-common/query', {workId: event, pageNum: 1, pageSize: 100})
      .subscribe((res: any) => {
        this.selectOptions.bookVendorList = res.data.list;
      })
  }

  // 查询快递公司下拉列表
  queryExpressCorpList() {
    this.httpClient.get('/api/wms/express-company/list')
      .subscribe((res: any) => {
        this.selectOptions.expressCorpList = res.data;
      })
  }

  // 查询用户id和nickname下拉列表
  queryUserList(userId){
    this.httpClient
      .post('/api/uas/corp-user/user/list',{corpId:this.userService.currentCorp.corpId,userId})
      .subscribe((res: any) => {
        this.selectOptions.userList = res.data;
        if(typeof res.data === 'object' && res.data !== null){
          this.selectOptions.userMap =  {};
          res.data.forEach(item => {
            this.selectOptions.userMap[item.userId] = item;
          })
        }
      })
  }

  // 查询行政区划下拉列表
  queryAreaInfolist(){
    return this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.selectOptions.areaInfoList = res.data;
      });
  }

  getParams() {
    return this.params;
  }

  setParams( value: any) {
    this.params = value;
  }
}


