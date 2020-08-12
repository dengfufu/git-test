import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {NzIconService, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {ActivatedRoute, Router} from '@angular/router';
import {WareOutcomeAuditComponent} from '../ware-outcome-audit/ware-outcome-audit.component';
import {WmsService} from '../../wms.service';

@Component({
  selector: 'app-ware-outcome-detail',
  templateUrl: './ware-outcome-detail.component.html',
  styleUrls: ['./ware-outcome-detail.component.less']
})
export class WareOutcomeDetailComponent implements OnInit {

  outcomeDetail: any = {};
  outcomeId: any = {};
  flowInstanceTraceList: any[] = [];
  flowInstanceNodeList: any[] = [];
  consignList:any[] = [{}];
  loading = true;
  flowNodeType:any;
  constructor( private router: Router,
               private modalService: NzModalService,
               private activatedRoute: ActivatedRoute,
               private httpClient: HttpClient,
               private iconService: NzIconService,
               private cdf: ChangeDetectorRef,
               public wmsService: WmsService) {
    this.activatedRoute.queryParams.subscribe(params => {
      this.outcomeId = params.outcomeId;
      this.flowNodeType = params.flowNodeType;
    });
  }

  ngOnInit() {
   this.queryOutcomeDetail();
   this.queryConsignInfo();
   this.wmsService.queryUserList('');
  }

  queryOutcomeDetail(){
    this.httpClient.get('/api/wms/outcome-common/detail/' + this.outcomeId)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data !== null){
          this.outcomeDetail = res.data;
          this.flowNodeType = this.outcomeDetail.flowNodeType;
          this.flowInstanceTraceList = res.data.flowInstanceTraceDtoList;
          this.flowInstanceNodeList = res.data.flowInstanceNodeList;
          console.log('this.flowInstanceNodeList',this.flowInstanceNodeList);
        }
      });
  }

  goBack() {
    history.back();
  }

  auditWare(){
    const modal = this.modalService.create({
      nzTitle: '物料审核',
      nzWidth: 500,
      nzFooter: null,
      nzContent: WareOutcomeAuditComponent,
      nzComponentParams: {
        outcomeDetail:this.outcomeDetail
      }
    });
    modal.afterClose.subscribe(res => {
      if(typeof res === 'object' && res !== null){
        if(res.code === 0) {
          this.queryOutcomeDetail();
          this.cdf.markForCheck();
        }
      }
    });
  }

  // 获取收货清单
  queryConsignInfo(){
    this.httpClient.get('/api/wms/consign/detail/' + this.outcomeId)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data !== null){
          this.consignList = res.data;
        }
      });
  }
  // 确认收货
  receive(){
    this.loading = true;
    this.httpClient.post('/api/wms/consign/receive' , {consignDetailDtoList: this.consignList,largeClassId: this.outcomeDetail.largeClassId})
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if(res.data !== null){
          this.queryConsignInfo();
          this.queryOutcomeDetail();
        }
      });
  }

  // 发货
  consign(){
    this.router.navigate(['../ware-outcome-consign'], {relativeTo: this.activatedRoute});
  }
  getButtonColor(status) {
    let color;
    switch (Number.parseInt(status,10)) {
      case 10:
        color = {'background-color': '#1E87F0'};
        break;
      case 20:
        color = {'background-color': '#5E86C8'};
        break;
      case 30:
        color = {'background-color': '#FFD617'};
        break;
      case 40:
        color = {'background-color': '#5EC894'};
        break;
      case 50:
        color = {'background-color': '#7ED321'};
        break;
      case 60:
        color = {'background-color': '#48B8FF'};
        break;
    }
    return color;
  }
}
