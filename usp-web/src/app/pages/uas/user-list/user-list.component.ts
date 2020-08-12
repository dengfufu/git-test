import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-user-list',
  templateUrl: 'user-list.component.html',
  styleUrls: ['user-list.component.less']
})
export class UserListComponent implements OnInit {

  searchForm: FormGroup;
  showMore = false;
  userList: any;
  pageNum = 1;
  pageSize = 10;
  total = 1;
  loading = true;

  resetForm(): void {
    this.searchForm.reset();
  }

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.loadUserList(this.getPageParam());
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('userName', new FormControl());
    this.searchForm.addControl('sex', new FormControl());
    this.searchForm.addControl('mobile', new FormControl());
    this.searchForm.addControl('regDate', new FormControl());
  }

  loadUserList(params: any, reset: boolean = false): void {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/uas/account/list',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const page = res.data;
        this.userList = page.records;
        this.total = page.total;
      });
  }

  queryUser(reset: boolean = false) {
    this.loadUserList(this.getParams(), reset);
  }

  getParams() {
    const params = this.searchForm.value;
    params.current = this.pageNum;
    params.size = this.pageSize;
    return params;
  }

  getPageParam() {
    const params: any = {};
    params.current = this.pageNum;
    params.size = this.pageSize;
    return params;
  }

}
