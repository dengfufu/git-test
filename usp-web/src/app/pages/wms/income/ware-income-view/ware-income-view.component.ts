import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';
import {HttpClient} from '@angular/common/http';
import {NzIconService, NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ChooseReverseDetailComponent} from '../ware-income-add/choose-reverse-detail/choose-reverse-detail.component';
import {WareIncomeAuditComponent} from '../ware-income-audit/ware-income-audit.component';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-ware-income-view',
  templateUrl: './ware-income-view.component.html',
  styleUrls: ['./ware-income-view.component.less']
})
export class WareIncomeViewComponent implements OnInit {
  // 入库明细
  incomeDetail: any = {};
  // 流程操作记录
  flowInstanceTraceList: any[] = [];
  // 流程实例节点
  flowInstanceNodeList: any[] = [];
  // 入库单主键
  id: any;
  // 销账明细
  reverseDetailList: any[] = [];
  objectKeys = Object.keys;

  constructor(
    private router: Router,
    private activatedRoute: ActivatedRoute,
    private httpClient: HttpClient,
    private iconService: NzIconService,
    private cdf: ChangeDetectorRef,
    private modalService: NzModalService,
    private messageService: NzMessageService,
    private userService: UserService
  ) {
  }

  ngOnInit() {
    this.id = this.activatedRoute.snapshot.queryParams.id;
    this.initView();
  }

  initView() {
    this.httpClient.post(`/api/wms/income-common/detail/${this.id}`, {})
      .subscribe((res: any) => {
        this.incomeDetail = res.data;
        this.incomeDetail.normsValue = JSON.parse(res.data.normsValue);
        this.flowInstanceTraceList = res.data.flowInstanceTraceDtoList;
        this.flowInstanceNodeList = res.data.flowInstanceNodeList;
        this.reverseDetailList = this.incomeDetail.bookSaleBorrowResultDtoList;
        // 显示规格
        if (this.reverseDetailList && this.reverseDetailList.length > 0) {
          this.reverseDetailList.map(reverseDetail => {
            if (reverseDetail.normsValue && reverseDetail.normsValue.length > 0) {
              const normsValueObj = JSON.parse(reverseDetail.normsValue);
              reverseDetail.normsValueName = Object.keys(normsValueObj).map(key => {
                return key + ':' + normsValueObj[key];
              }).join(',');
            }
          })
        }
        this.cdf.markForCheck();
      });
  }

  goBack() {
    history.back();
  }

}
