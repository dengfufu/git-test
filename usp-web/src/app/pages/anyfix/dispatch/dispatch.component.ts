import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';

@Component({
  selector: 'app-application-dispatch',
  templateUrl: 'dispatch.component.html',
  styleUrls: ['dispatch.component.less'],
})

export class DispatchComponent implements OnInit {

  @Input() demanderCorp: any;
  @Input() workId: any;
  // 是否单选按钮
  @Input() isRadio: any;

  pageNum = 1;
  pageSize = 10;
  total = 1;
  loading = true;
  serviceList: any = [];
  serviceCheckedList: any = [];
  searchName: any;
  demanderService: any;

  isIndeterminate = false;
  isAllChecked = false;

  // 提交按钮是否可点
  disable = false;

  mapOfCheckedId: { [key: string]: boolean } = {};
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private messageService: NzMessageService,
              private modalService: NzModalService,
              private httpClient: HttpClient) {

  }

  ngOnInit(): void {
    this.serviceCustom();
  }

  //  查询服务商
  serviceCustom(){
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/query', this.getPageParam())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        }))
      .subscribe((res: any) => {
        this.serviceList = res.data.list;
        this.total = res.data.total;
      });
  }

  // 分页查询
  getPageParam() {
    const params: any = {};
    params.pageNum = this.pageNum;
    params.pageSize = this.pageSize;
    params.demanderCorp =  this.demanderCorp;
    if(this.searchName !== undefined && this.searchName !== null){
      params.serviceCorpName = this.searchName;
    }
    return params;
  }

  // 提交确认
  submitFormConfirm(): void {
    this.modalService.confirm({
      nzTitle: '请确定是否将工单提交给该服务商?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.dispatch(),
      nzCancelText: '取消'
    });
  }

  // 分配
  dispatch(): void {
    this.disable = true;
    this.httpClient
      .post('/api/anyfix/work-transfer/custom/dispatch', {serviceCorp: this.demanderService.serviceCorp, workId: this.workId})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
          this.disable = false;
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.modal.destroy({code: 0});
        }
      });
  }

  // 提交
  submitForm(): void {
    this.serviceList.forEach(item => {
      if(this.mapOfCheckedId[item.id]){
        this.serviceCheckedList.push(item);
      }
    });
    if(this.serviceCheckedList.length === 0){
      this.messageService.error('服务商不能为空，请重新选择服务商');
      this.serviceCheckedList.pop();
    }else {
      this.demanderService = this.serviceCheckedList[0];
      this.submitFormConfirm();
    }
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  checkAll(value: boolean){
    this.serviceList.forEach(item => (this.mapOfCheckedId[item.id] = value));
    this.refreshServiceStatus(1);
  }

  refreshServiceStatus(id){
    if(this.isRadio === true){
      this.serviceList.filter(item => (item.id !== id))
        .forEach(item => {
          this.mapOfCheckedId[item.id] = false;
        });
    }else{
      this.isAllChecked = this.serviceList.every(item => this.mapOfCheckedId[item.id]);
      this.isIndeterminate =
        this.serviceList.some(item => this.mapOfCheckedId[item.id]) &&
        !this.isAllChecked;
    }
  }
}
