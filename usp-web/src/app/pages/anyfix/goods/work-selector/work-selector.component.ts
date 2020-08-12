import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {Checkbox, Page, Result} from '@core/interceptor/result';
import {AnyfixModuleService} from '@core/service/anyfix-module.service';

@Component({
  selector: 'app-anyfix-goods-work-selector',
  templateUrl: './work-selector.component.html',
  styleUrls: ['./work-selector.component.less']
})
export class WorkSelectorComponent implements OnInit {

  @Input() consignCorpId: any;
  @Input() receiveCorpId: any;
  @Input() consignBranchId: any;
  @Input() receiveBranchId: any;
  @Input() workCheckedList: any[] = [];

  searchForm: FormGroup;

  workTypeList: any[] = [];

  // 分页
  page: Page = new Page();
  loading = false;
  workList: any = [];
  checkBox = new Checkbox();

  maxCount = 10; // 选择最多工单数量

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private nzMessageService: NzMessageService,
              private anyfixModuleService: AnyfixModuleService) {
    this.searchForm = this.formBuilder.group({
      workCode: [],
      workTypes: []
    });
  }

  ngOnInit(): void {
    this.listWorkType();
    this.workCheckedList.forEach((work: any) => {
      this.checkBox.mapOfCheckedId[work.workId] = true;
    });
    // 查询工单
    this.queryWork(true);
  }

  /**
   * 工单类型列表
   */
  listWorkType() {
    this.anyfixModuleService.getWorkType().subscribe((res: any) => {
      this.workTypeList = res.data;
    });
  }

  queryWork(reset: boolean = false): void {
    if (reset) {
      this.page.pageNum = 1;
    }
    const params = this.getPageParam();
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-request/query', params)
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        this.page.total = data.total;
        this.workList = data.list || [];
        this.checkBox.dataIdList = [];
        if (this.workList) {
          this.workList.forEach((work: any) => {
            this.checkBox.dataIdList.push(work.workId);
          });
        }
        this.checkBox.refreshStatus();
      });
  }

  getPageParam() {
    const params: any = this.searchForm.value;
    params.pageNum = this.page.pageNum;
    params.pageSize = this.page.pageSize;
    const corpIdList = [];
    if (this.consignCorpId && this.consignCorpId != 0) {
      corpIdList.push(this.consignCorpId);
    }
    if (this.receiveCorpId && this.receiveCorpId != 0) {
      corpIdList.push(this.receiveCorpId);
    }
    params.corpIdList = corpIdList;
    const branchIdList = [];
    if (this.consignBranchId && this.consignBranchId != 0) {
      branchIdList.push(this.consignBranchId);
    }
    if (this.receiveBranchId && this.receiveBranchId != 0) {
      branchIdList.push(this.receiveBranchId);
    }
    params.serviceBranchIdList = branchIdList;
    return params;
  }

  checkWork(event, work: any) {
    if (event) {
      if (this.workCheckedList.length === this.maxCount) {
        this.nzMessageService.error('选择的工单数不能超过' + this.maxCount);
        return;
      }
      this.workCheckedList = [...this.workCheckedList, work];
    } else {
      this.deleteWork(work.workId);
    }
  }

  checkAll(event) {
    if (event) {
      const addList: any[] = this.workList.filter((work: any) => !this.checkBox.mapOfCheckedId[work.workId]) || [];
      if (this.workCheckedList.length + addList.length > this.maxCount) {
        this.nzMessageService.error('选择的工单数不能超过' + this.maxCount);
        return;
      }
      this.workCheckedList = [
        ...this.workCheckedList, ...addList];
    } else {
      this.workCheckedList = [
        ...this.workCheckedList.filter((work: any) => !this.checkBox.dataIdList.includes(work.workId))
      ];
    }
  }

  /**
   * 删除选中的工单
   * @param workId
   */
  deleteWork(workId: string) {
    this.workCheckedList = this.workCheckedList.filter((work: any) => work.workId !== workId);
    this.checkBox.mapOfCheckedId[workId] = false;
    this.checkBox.refreshStatus();
  }

  submitForm(): void {
    const result = new Result();
    result.code = 0;
    result.data = this.workCheckedList;
    this.modal.destroy(result);
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
