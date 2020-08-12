import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {NzModalService} from 'ng-zorro-antd';
import {finalize} from 'rxjs/operators';
import {AnyfixService} from '@core/service/anyfix.service';

@Component({
  selector: 'app-work-detail',
  templateUrl: './work-detail-atmcase.component.html',
  styleUrls: ['./work-detail-atmcase.component.less']
})
export class WorkDetailAtmcaseComponent implements OnInit {
  workId: any;
  caseId: any;
  demanderCorp: any;
  serviceCorp: any;
  work: any = {};
  label: string | undefined = '';
  loading = true;
  previewImage: string | undefined = '';
  previewVisible = false;
  params: any = {};

  constructor(private httpClient: HttpClient,
              public router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService,
              private cdf: ChangeDetectorRef,
              public anyfixService: AnyfixService) {
    this.caseId = this.activatedRoute.snapshot.queryParams.caseId;
  }

  ngOnInit() {
    this.query();
  }

  handlePreview = (url: any) => {
    this.previewImage = url;
    this.previewVisible = true;
  };

  goBack() {
    history.go(-1);
  }

  // 查询工单信息
  query() {
    this.loading = true;
    this.params.caseId = this.caseId;
    this.httpClient
      .post('/api/anyfix/work-request/detail/atmcase', this.params)
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
          if (this.work !== 'undefined') {
            // 修改操作记录
            const re = '<br/>';
            this.work.workOperateList.forEach(item => {
              item.summary = item.summary.replace(re, ' ');
            });
          }
        }
      });
  }
}
