import {ChangeDetectorRef, Component, EventEmitter, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {Page} from '@core/interceptor/result';
import {SYS_RIGHT} from '../../../core/right/right';
import { ZorroUtils } from '@util/zorro-utils';
import {ActivatedRoute, Router} from '@angular/router';
import {CorpListService} from './service/corp-list.service';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';

@Component({
  selector: 'app-corp-list',
  templateUrl: 'corp-list.component.html',
  styleUrls: ['corp-list.component.less']
})
export class CorpListComponent implements OnInit {
  ZorroUtils = ZorroUtils;
  aclRight = SYS_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  corpLoading = false;
  nzOptions: any;

  corpList = [];
  drawerVisible: boolean;
  corpFormValue: any;
  areaInfoOption = [];

  nzFilterOption = () => true;


  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private router: Router,
              private corpListService : CorpListService,
              private anyfixModuleService: AnyfixModuleService,
              private activatedRoute : ActivatedRoute) {
    this.searchForm = this.formBuilder.group({
      corpId: [],
      verify: [],
      area: [],
      district: [],
    });
  }

  ngOnInit(): void {
    this.getParamsValue();
    this.anyfixModuleService.getAreaInfoOption().subscribe((res: any) => {
      this.areaInfoOption = res.data;
    });
    this.queryCorpList();
    this.listCorp();
  }

  getParamsValue() {
    const params = this.corpListService.paramsValue;
    if( params) {
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.corpListService.paramsValue = null;
    }
  }

  saveParamsValue() {
    const params = {
      formValue : this.searchForm.value,
      page : this.page,
    };
    this.corpFormValue = params;
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  queryCorpList(reset?: boolean) {
    if(reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
      .post('/api/uas/corp-registry/query',this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.list = res.data.list;
        this.page.total = res.data.total;
      });
  }

  listCorp(corpName?: any){
    const params = {matchFilter: corpName};
    this.corpLoading = true;
    this.httpClient
      .post('/api/uas/corp-registry/match', params)
      .pipe(
        finalize(() => {
          this.corpLoading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const list = [];
        res.data.forEach(item => {
          list.push({
            value: item.corpId,
            text: item.corpName
          });
        });
        this.corpList = list;
      });
  }

  matchCorp(corpName) {
    this.listCorp(corpName);
  }

  openDrawer() {
    this.drawerVisible = true;
  }

  closeDrawer() {
    this.drawerVisible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }

  toDetail(corpId) {
   /* this.corpListService.paramsValue = this.params;*/
    this.corpListService.paramsValue = this.corpFormValue;
    this.router.navigate(['../corp-list/detail'],
      {queryParams: {corpId}, relativeTo: this.activatedRoute});
  }

  // 行政区划改变
  areaChange(event) {
    if (event === null || event.length === 0) {
      this.searchForm.controls.district.setValue(null);
      return;
    }
    if (event !== undefined){
      if(event.length > 2) {
        this.searchForm.controls.district.setValue(event[2]);
      }else if(event.length > 1) {
        this.searchForm.controls.district.setValue(event[1]);
      } else if(event.length === 1) {
        this.searchForm.controls.district.setValue(event[0]);
      }
    }
  }
}
