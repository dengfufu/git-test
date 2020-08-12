import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder,FormControl,FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-application-config-add',
  templateUrl: 'pick-corp.component.html'
})
export class PickCorpComponent implements OnInit {

  validateForm: FormGroup;
  selectedValue: any;
  searchForm: FormGroup;
  pageNum = 1;
  pageSize = 10;
  total = 1;
  dataList: any;
  loading = true;
  oneOption = [
    { label: 'Apple', value: 'Apple', checked: true },
    { label: 'Pear', value: 'Pear', checked: false },
    { label: 'Orange', value: 'Orange', checked: false },
  ]
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    this.loadList(this.getPageParam());
    this.searchForm = this.formBuilder.group({});
    this.searchForm.addControl('name', new FormControl());
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    this.httpClient
      .post('/api/uas/corp/listCorpInfo',
        value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('update');
      });
  }

  queryConfig(reset: boolean = false) {
    this.loadList(this.getParams(), reset);
  }

  loadList(params: any, reset: boolean = false): void {
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/uas/corp/listCorpInfo',
        params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        console.log('daadfasdf' , res);
        this.dataList = data;
        console.log(data.total);
        this.total = data.total;
      });
  }

  destroyModal(): void {
    this.modal.destroy();
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

  choose(data){
    this.modal.destroy(data.corpId);
  }

}
