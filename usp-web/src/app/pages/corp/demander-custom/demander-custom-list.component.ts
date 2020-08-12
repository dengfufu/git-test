import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {DemanderCustomAddComponent} from './demander-custom-add.component';
import {DemanderCustomEditComponent} from './demander-custom-edit.component';
import {UserService} from '@core/user/user.service';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '../../../core/right/right';
import {ActivatedRoute, Router} from '@angular/router';
import {DemanderCustomService} from './service/demander-custom.service';

@Component({
  selector: 'app-corp-demander-custom-list',
  templateUrl: 'demander-custom-list.component.html',
  styleUrls: ['demander-custom-list.component.less']
})
export class DemanderCustomListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;
  page = new Page();
  loading = false;
  customCorpList: any;
  list: any;
  visible = false;
  savedPramsValue: any;
  currentCorp = this.userService.currentCorp;
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              public userService: UserService,
              private demanderCustomService: DemanderCustomService,
              private router: Router,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      customId: [],
      enabled: ['Y', []]
    });
  }

  ngOnInit(): void {
    this.getParamsValue();
    this.queryDemanderCustom();
    this.listCustomCorp();
  }

  getParamsValue() {
    const params = this.demanderCustomService.paramsValue;
    if (params) {
      this.searchForm.setValue(params.formValue);
      this.page = params.page;
      this.demanderCustomService.paramsValue = null;
    }
  }

  saveParamsValue() {
    const params = {
      formValue: this.searchForm.value,
      page: this.page
    };
    this.savedPramsValue = params;
  }

  loadList(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.saveParamsValue();
    this.httpClient
      .post('/api/anyfix/demander-custom/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        this.list = res.data.list;
        this.page.total = res.data.total;
      });
  }

  toDetail(customId) {
    this.demanderCustomService.paramsValue = this.savedPramsValue;
    this.router.navigate(['../demander-custom/detail'],
      {queryParams: {customId}, relativeTo: this.activatedRoute});
  }

  queryDemanderCustom(reset?: boolean) {
    this.loadList(reset);
  }

  getParams() {
    const params = Object.assign({}, this.searchForm.value);
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(): void {
    const modal = this.modalService.create({
      nzTitle: '添加客户',
      nzContent: DemanderCustomAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDemanderCustom();
      }
    });
  }

  listCustomCorp(params?: any): void {
    if (!params) {
      params = {};
    }
    this.httpClient
      .post('/api/anyfix/demander-custom/custom/list', params)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.customCorpList = res.data;
      });
  }

  // 进入编辑页面
  editModal(data): void {
    const modal = this.modalService.create({
      nzTitle: '编辑客户',
      nzContent: DemanderCustomEditComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        demanderCustom: data
      }
    });

    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.queryDemanderCustom();
      }
    });
  }

  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该使用商吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteDemanderCustom(id),
      nzCancelText: '取消'
    });
  }

  // 删除
  deleteDemanderCustom(id) {
    this.httpClient
      .delete('/api/anyfix/demander-custom/' + id)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        // 如果是为第n页第一条，需要调整为n-1页才有数据显示
        if (this.list.length === 1 && this.page.pageNum > 1) {
          this.page.pageNum = this.page.pageNum - 1;
        }
        this.queryDemanderCustom();
      });
  }

  areaChange(event) {
    if (event != null && event !== undefined && event.length > 0) {
      this.searchForm.patchValue({
        province: event[0],
        city: event[1],
        district: event[2]
      });
    } else {
      this.searchForm.patchValue({
        province: '',
        city: '',
        district: ''
      });
    }
  }

  openDrawer() {
    this.visible = true;
  }

  closeDrawer() {
    this.visible = false;
  }

  clearDrawer() {
    this.searchForm.reset();
  }

  matchCustom(event: string) {
    const params = {
      customCorpName: event
    };
    this.listCustomCorp(params);
  }
}
