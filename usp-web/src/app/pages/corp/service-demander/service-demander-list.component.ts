import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalService} from 'ng-zorro-antd';
import {ServiceDemanderAddComponent} from './service-demander-add.component';
import {UserService} from '@core/user/user.service';
import {Page} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {ServiceDemanderCreateComponent} from './service-demander-create.component';
import {ServiceDemanderConfigComponent} from './service-demander-config.component';
import {ActivatedRoute, Router} from '@angular/router';
import {ACLService} from '@delon/acl';

@Component({
  selector: 'app-corp-service-demander-list',
  templateUrl: 'service-demander-list.component.html',
  styleUrls: ['service-demander-list.component.less']
})
export class ServiceDemanderListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  nzOptions: any;
  values: any;
  searchForm: FormGroup;
  page = new Page();
  list: any;
  loading = false;
  serviceCorpList: any;
  customCorpList: any;

  visible = false;
  useFormValue = true;
  demanderCorpOptions: any = [];

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private router: Router,
              private aclService: ACLService,
              private activatedRoute: ActivatedRoute,
              private modalService: NzModalService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      enabled: ['Y', []]
    });
  }

  ngOnInit(): void {
    this.queryDemanderService();
    this.listDemanderCorp();
  }

  loadList(reset?: boolean): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/demander/query', this.getParams())
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

  queryDemanderService(reset?: boolean) {
    this.loadList(reset);
  }

  getParams() {
    let params: any = {};
    if (this.useFormValue) {
      params =  Object.assign({}, this.searchForm.value);
    }
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    return params;
  }

  // 进入添加页面
  addModal(isForUpdate,data): void {
    const modal = this.modalService.create({
      nzTitle: '添加委托商',
      nzContent: ServiceDemanderAddComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 800,
      nzComponentParams: {
        isForUpdate,
        data
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.useFormValue = false;
        this.queryDemanderService();
      }
    });
  }

  // 进入配置页面
  configModal(id): void {
    const modal = this.modalService.create({
      nzTitle: '配置选项',
      nzContent: ServiceDemanderConfigComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: 500,
      nzComponentParams: {id}
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {

      }
    });
  }

  listDemanderCorp(): void {
    this.httpClient
      .post('/api/anyfix/demander-service/demander/list', {})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.demanderCorpOptions = res.data;
      });
  }

  cutStr(description : string ) {
    if(description && description.length > 20) {
      description = description.substr(0,20) +'...';
    }
    return description;
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

  createModal() {
    const modal = this.modalService.create({
      nzTitle: '注册新委托商',
      nzContent: ServiceDemanderCreateComponent,
      nzFooter: null,
      nzMaskClosable: false,
      nzWidth: '1000px'
    });
    modal.afterClose.subscribe(result => {
      if (result === 'update') {
        this.useFormValue = false;
        this.queryDemanderService();
      }
    });
  }

  toDetail(id) {
    const rightIds = this.aclService.data.abilities;
    if (rightIds.includes(ANYFIX_RIGHT.DEMANDER_DETAIL)) {
      this.router.navigate(['../service-demander/detail'],
        {queryParams: {id}, relativeTo: this.activatedRoute});
    }
  }

}
