import {ChangeDetectorRef, Component, ElementRef, HostListener, OnInit, ViewChild} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {environment} from '@env/environment';
import {ActivatedRoute, Router} from '@angular/router';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import { NzInputDirective } from 'ng-zorro-antd/input';
import {AnyfixService} from '@core/service/anyfix.service';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {WmsService} from '../../../wms.service';

@Component({
  selector: 'app-settle-branch-detail',
  templateUrl: './ware-trans-detail.component.html',
  styleUrls: ['./ware-trans-detail.component.less']
})
export class WareTransDetailComponent implements OnInit {

  @ViewChild(NzInputDirective, { static: false, read: ElementRef }) inputElement: ElementRef;

  id: any;
  demanderCorp: any;
  serviceCorp: any;
  detail: any = {};
  label: any = {};
  showFileUrl = environment.server_url + '/api/file/showFile?fileId=';
  // 工单评价信息
  detailEvaluate: any;
  loading = true;
  // 预约时间相关变量
  bookTime: any = {};
  // 工单操作按钮是否可点
  disable = false;
  value: any;
  previewImage: string | undefined = '';
  previewVisible = false;
  index = 2;

  partInfoList : any[] = [];
  flowInstanceNodeList: any[] = [];
  flowInstanceTraceDtoList: any[] = [];
  detailObj: any = {};
  auditDetail: any = {};
  consignDetail: any = {};
  consignList:any[] = [{}];

  flowInstanceTraceList: any[] = [];
  allowOptions = [
    {
      label:'批准',
      value:10
    },
    {
      label:'不批准',
      value:20
    }
  ];
  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private formBuilder: FormBuilder,
              public wmsService : WmsService,
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
    // 审批
  }

  handlePreview = (url: any) => {
    this.previewImage = url;
    this.previewVisible = true;
  };



  goBack(){
    history.go(-1);
  }

   queryTransDetail(){
      this.httpClient
        .get('/api/wms/trans-ware-common/detail/' +this.id,{})
        .pipe(
          finalize(() => {
          })
        )
        .subscribe((res: any) => {
          console.log('res,res,res,',res);
          // this。flowInstanceTraceList
          this.detail = res.data;
          this.flowInstanceNodeList = res.data.flowInstanceNodeList;
          this.flowInstanceTraceList = res.data.flowInstanceTraceDtoList;
          this.flowInstanceTraceDtoList = res.data.flowInstanceTraceDtoList;
          if(this.flowInstanceTraceDtoList.length >= 2){
            this.auditDetail = this.flowInstanceTraceDtoList[1];
          }
          if(res.data.consignDetailDtoList !=null && res.data.consignDetailDtoList.length >=1) {
            this.consignDetail = res.data.consignDetailDtoList[0];
            this.consignList = res.data.consignDetailDtoList;
          }
      });
  }


  editDispatchInfo() {
    this.router.navigate(['../ware-trans-dispatch-edit'],
      {queryParams: {}, relativeTo: this.activatedRoute});
  }

  checkReceived() {
    return Object.keys(this.consignDetail).length > 0
  }


}
