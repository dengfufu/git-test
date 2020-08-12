import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NzIconService, NzMessageService} from 'ng-zorro-antd';
import {Page} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-ware-income-view',
  templateUrl: './ware-trans-receive.component.html',
  styleUrls: ['./ware-trans-receive.component.less']
})
export class WareTransReceiveComponent implements OnInit {

  isIndeterminate = false;
  numberOfChecked = 0;
  mapOfCheckedId: { [key: string]: boolean } = {};
  isAllDisplayDataChecked = false;

  transDetail : any = {};
  flowInstanceTraceList: any[] = [];
  flowInstanceNodeList: any[] = [];
  id: any;
  objectKeys = Object.keys;
  consignList : any[] = [];
  page = new Page();
  receiveDetail: any = {};
  checkedList = [];
  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private iconService: NzIconService,
    private cdf: ChangeDetectorRef
  ) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id;
    });
  }

  ngOnInit() {

    this.queryDetail();
    this.queryTrans();
  }

  goBack() {
    history.back();
  }

  checkAll(value: boolean) {
    this.consignList.filter(item => !item.disabled).forEach(item => (this.mapOfCheckedId[item.id] = value));
    this.refreshStatus();
  }

  queryDetail(){
    this.httpClient
      .get('/api/wms/consign/detail/' + this.id)
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        console.log('receiveDetail',res);
        if(res.data.length > 0 ) {
          this.receiveDetail = res.data[0];

        }
      });
  }

  queryTrans() {
    this.httpClient
      .get('/api/wms/trans-ware-common/detail/' + this.id)
      .pipe(
        finalize(() => {

        })
      )
      .subscribe((res: any) => {
        console.log('trans Detail',res);
        this.transDetail = res.data;
        this.consignList = res.data.consignDetailDtoList;
      });
  }

  refreshStatus() {
    console.log('mapOfCheckedId', this.mapOfCheckedId);
    this.isAllDisplayDataChecked = this.consignList
      .filter(item => !item.disabled)
      .every(item => this.mapOfCheckedId[item.id]);
    this.isIndeterminate =
      this.consignList.filter(item => !item.disabled).some(item => this.mapOfCheckedId[item.id]) &&
      !this.isAllDisplayDataChecked;
    this.numberOfChecked = this.consignList.filter(item => this.mapOfCheckedId[item.id]).length;
    this.setFormList();
  }

  setFormList() {
    this.checkedList = [];
    Object.keys(this.mapOfCheckedId).forEach(key => {
      console.log('mapOfCheckValue',this.mapOfCheckedId[key]);
      if(this.mapOfCheckedId[key]){
        for(const item of this.consignList ){
          if(item.id === key ){
            this.checkedList.push(item);
            break;
          }
        }
      }
    });
    console.log('checkList', this.checkedList);
  }


  submit() {
    const params = {
      largeClassId : 30,
      smallClassId : 110,
      consignDetailDtoList: this.checkedList
    };
    this.httpClient
      .post('/api/wms/consign/receive' ,params)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        this.messageService.success('收货成功');
        // this.goBack();
      });
  }
}
