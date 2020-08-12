import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';

@Component({
  selector: 'app-select-branch',
  templateUrl: './select-branch.component.html',
  styleUrls: ['./select-branch.component.less'],
})
export class SelectBranchComponent implements OnInit {

  @Input() workId: any;
  @Input() url;
  @Input() message;
  @Input() serviceCorp: any;
  // 是否单选按钮
  @Input() isRadio: any;
  @Input() branchName: any;
  // 提交按钮是否可点
  disable = false;

  serviceBranch: any = {};
  pageNum = 1;
  pageSize = 5;
  total = 1;
  loading = true;
  branchList: any = [];
  branchCheckedList: any = [];
  searchName: any;

  isIndeterminate = false;
  isAllChecked = false;

  mapOfCheckedId: { [key: string]: boolean } = {};
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private messageService: NzMessageService,
              private modalService: NzModalService,
              private httpClient: HttpClient) {

  }

  ngOnInit(): void {
    if (this.branchName) {
      this.searchName = this.branchName;
    }
    this.selectBranchList();
  }

  selectBranchList(reset?: boolean){
    if (reset) {
      this.pageNum = 1;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/service-branch/query', this.getPageParam())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        }))
      .subscribe((res: any) => {
        this.branchList = res.data.list;
        this.total = res.data.total;
      });
  }

  getPageParam() {
    const params: any = {};
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    params.serviceCorp =  this.serviceCorp;
    if(this.searchName !== undefined && this.searchName !== null){
      params.branchName = this.searchName;
    }
    return params;
  }

  // 提交
  submitForm(): void {
    this.branchList.forEach(item => {
      if(this.mapOfCheckedId[item.branchId]){
        this.branchCheckedList.push(item);
      }
    });
    if(this.branchCheckedList.length === 0 ){
      this.messageService.error('服务网点不能为空，请重新选择服务网点');
      this.branchCheckedList.pop();
    }else {
      this.serviceBranch = this.branchCheckedList[0];
      if(this.serviceBranch.branchId === undefined || this.serviceBranch.branchId === null){
        this.messageService.error('请添加服务网点');
      }
      this.submitFormConfirm();
    }
  }

  // 提交确认
  submitFormConfirm(): void {
    this.modalService.confirm({
      nzTitle: '请确定是否' + this.message + '该工单?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.handle(),
      nzCancelText: '取消'
    });
  }

  // 受理或转处理
  handle(): void {
    this.disable = true;
    this.httpClient
      .post(this.url, {serviceBranch: this.serviceBranch.branchId, workId: this.workId})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.disable = false;
          this.branchCheckedList.pop();
        })
      )
      .subscribe((res: any) => {
        if(res.code === 0){
          this.modal.destroy({code: 0});
        }else{
          this.messageService.error(this.message + '失败，请重试！');
        }
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  checkAll(value: boolean){
    this.branchList.forEach(item => (this.mapOfCheckedId[item.branchId] = value));
    this.refreshStatus(1);
  }
  refreshStatus(branchId){
    if(this.isRadio === true){
      this.branchList.filter(item => (item.branchId !== branchId))
        .forEach(item => {
          this.mapOfCheckedId[item.branchId] = false;
        });
    }else{
      this.isAllChecked = this.branchList.every(item => this.mapOfCheckedId[item.branchId]);
      this.isIndeterminate =
        this.branchList.some(item => this.mapOfCheckedId[item.branchId]) &&
        !this.isAllChecked;
    }
  }
}
