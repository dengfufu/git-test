import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {Checkbox, Page} from '@core/interceptor/result';
import {finalize} from 'rxjs/operators';
import {WmsService} from '../../../wms.service';

@Component({
  selector: 'app-choose-reverse-detail',
  templateUrl: './choose-reverse-detail.component.html',
  styleUrls: ['./choose-reverse-detail.component.less']
})
export class ChooseReverseDetailComponent implements OnInit {

  // 传入数据，业务小类和已选中id映射
  @Input()
  smallClassId: any;
  @Input()
  checkedIdMap: any;

  // 表格loading
  loading = false;
  // 表格分页信息
  page = new Page();
  // 表格复选框
  checkbox = new Checkbox();
  // 查询条件表单
  searchForm: FormGroup;
  // 表格数据
  bookSaleBorrowList: any[] = [];

  constructor(
    private httpClient: HttpClient,
    private messageService: NzMessageService,
    private userService: UserService,
    private formBuilder: FormBuilder,
    public wmsService: WmsService
  ) {
    this.searchForm = this.formBuilder.group({
      catalogId: [],
      brandId: [],
      modelId: []
    });
  }

  ngOnInit() {
    if (this.smallClassId === 50) {
      this.queryBookSaleBorrow(false);
    }
    this.wmsService.queryCatalogList('');
    this.wmsService.queryBrandList('');
    this.wmsService.queryModelList('', 0, 0);
  }

  catalogChange(event) {
    this.searchForm.controls.modelId.reset();
    if(event && event.length > 0) {
      this.wmsService.queryModelList('', event, this.searchForm.value.brandId);
    }else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  brandChange(event) {
    this.searchForm.controls.modelId.reset();
    if(event && event.length > 0) {
      this.wmsService.queryModelList('', this.searchForm.value.brandId, event);
    }else {
      this.wmsService.selectOptions.modelList = [];
    }
  }

  queryBookSaleBorrow(reset) {
    if (reset) {
      this.page.pageNum = 1;
    }
    this.loading = true;
    this.httpClient.post('/api/wms/book-sale-borrow/query', this.getParams())
      .pipe(
        finalize(() => {
          this.loading = false;
        })
      ).subscribe((res: any) => {
      this.bookSaleBorrowList = res.data.list;
      this.page.total = res.data.total;
      this.checkbox.dataIdList = [];
      this.checkbox.mapOfCheckedId = this.checkedIdMap;
      this.bookSaleBorrowList.forEach((bookSaleBorrow: any) => {
        this.checkbox.dataIdList.push(bookSaleBorrow.id);
        const normsValueObj = JSON.parse(bookSaleBorrow.normsValue);
        bookSaleBorrow.normsValueName = Object.keys(normsValueObj).map(key => {
          const value = normsValueObj[key];
          return key + ':' + value;
        }).join(',');
      });
    });
  }

  getParams() {
    const params = this.searchForm.value;
    params.corpId = this.userService.currentCorp.corpId;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    params.reversed = 'N';
    return params;
  }

}
