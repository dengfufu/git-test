import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import { NzInputDirective } from 'ng-zorro-antd/input';
import {AnyfixService} from '@core/service/anyfix.service';
import {finalize} from 'rxjs/operators';

@Component({
  selector: 'app-settle-branch-detail',
  templateUrl: './quick-transfer-detail.component.html',
  styleUrls: ['./quick-transfer-detail.component.less']
})
export class QuickTransferDetailComponent implements OnInit {

  @ViewChild(NzInputDirective, { static: false, read: ElementRef }) inputElement: ElementRef;

  id: any;
  detailObj: any = {};
  flowInstanceTraceList = [
    {id: '12141342', templateNoteName: '申请', completeHandlerName: '赵明星',
      completeTime: '2019-09-09 12:09:09', completeDescription: '完成', completed: 'Y'},
    {id: '254625646', templateNoteName: '确认', completeHandlerName: '胡志鹏',
      completeTime: '2019-09-10 13:12:00', completeDescription: '核对无误，准许入库', completed: 'Y'}
  ];
  work = {
    fromDepotName:'',
    toDepotName:'',
    createByName:'',
    applyDate:'',
    confirmName:'',
    auditDate:''
  };
  partInfoList : any[] = [];
  flowInstanceNodeList: any[] = [];
  flowInstanceTraceDtoList: any[] = [];


  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              private msg: NzMessageService,
              public anyfixService: AnyfixService) {
    this.activatedRoute.queryParams.subscribe(queryParams => {
      this.id = queryParams.id;
    });
  }

  @HostListener('window:click', ['$event'])
  handleClick(e: MouseEvent): void {
  };



  ngOnInit() {
    // this.initBookTime();
    this.queryTransDetail();
  }

  queryTransDetail(){
    this.httpClient
      .get('/api/wms/trans-ware-common/detail/' +this.id)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        console.log('res,res,res,',res);
        // this。flowInstanceTraceList
        this.detailObj = res.data;
        this.flowInstanceNodeList = res.data.flowInstanceNodeList;
        this.flowInstanceTraceDtoList = res.data.flowInstanceTraceDtoList;
      });
  }


  goBack(){
    history.go(-1);
  }

}
