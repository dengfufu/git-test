import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';
import {Checkbox, Page, Result} from '@core/interceptor/result';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-corp-user-selector',
  templateUrl: 'corp-user-selector.component.html'
})
export class CorpUserSelectorComponent implements OnInit {

  @Input() params: any;

  searchForm: FormGroup;
  page = new Page();
  list = [];
  loading = false;

  nzOptions: any;
  userOptions = [];

  corpId = this.userService.currentCorp.corpId;

  checkBox = new Checkbox();
  checkedList = [];
  branchId = '';
  fromUri = '';
  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.searchForm = this.formBuilder.group({
      userId: [],
      serviceBranch: [],
      deviceBranch: []
    });
  }

  ngOnInit(): void {
    this.branchId = this.params.urlParams.branchId;
    this.fromUri = this.params.fromUri;
    this.queryAvailableUser();
    this.matchUser('');
  }


  queryAvailableUser(reset?: boolean,clickQuery:boolean= false) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    let uri = '';
    if(clickQuery) {
      uri = '/api/uas/corp-user/query'
    } else {
      uri = '/api/anyfix/' +this.fromUri+ '/query/available';
    }
    this.httpClient
      .post(uri,
        this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.page.total = res.data.total;
        this.list = res.data.list || [];
        this.list.forEach(item => {
          this.checkBox.mapOfCheckedId[item.userId] = false;
          this.checkBox.dataIdList.push(item.userId);
          this.checkBox.isIndeterminate = false;
          this.checkBox.isAllChecked = false;
        });
      });
  }


  getParams() {
    let params: any = {};
    if (this.searchForm) {
      params = this.searchForm.value;
    }
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.corpId = this.corpId;
    params.branchId = this.branchId;
    return params;
  }

  matchUser(userName?:string) {
    const payload = {
      corpId: this.corpId,
      userName,
      branchId : this.branchId
    };
    this.httpClient
      .post('/api/anyfix/'+ this.fromUri+'/match/available', payload)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.userOptions = res.data;
      });
  }

  submitForm(): void {
    this.checkedList = [];
    this.list.forEach(item => {
      if (this.checkBox.mapOfCheckedId[item.userId]) {
        this.checkedList.push(item.userId);
      }
    });
    let pay = this.params.urlParams;
    pay = Object.assign(pay, {userIdList: this.checkedList});
    this.httpClient
      .post(this.params.url, pay)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: Result) => {
        if (res.code === 0) {
          this.modal.destroy('submit');
        }
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
