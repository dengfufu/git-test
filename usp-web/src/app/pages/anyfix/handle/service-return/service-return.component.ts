import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';

@Component({
  selector: 'app-service-return',
  templateUrl: './service-return.component.html',
  styleUrls: ['./service-return.component.less'],
})
export class ServiceReturnComponent implements OnInit {

  @Input() workId: any;

  loading = true;
  reasonList: any = [];
  mapOfCheckedId: { [key: string]: boolean } = {};
  otherReason: any;
  constructor(private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private msg: NzMessageService) {

  }
  // 提交按钮是否可点
  disable = false;
  ngOnInit(): void {
    this.getReasonList();
  }

  getReasonList(){
    this.loading = true;
    this.httpClient
      .get('/api/anyfix/service-reason/list/1')
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        }))
      .subscribe((res: any) => {
        if (res.data !== null && res.data !== 'undefined') {
          this.reasonList = res.data;
          this.reasonList.push({id: 0, name: '其他'});
        }
      });
  }
  getPageParam() {
    const params: any = {};
    params.workId = this.workId;
    this.reasonList.forEach(item => {
      if(this.mapOfCheckedId[item.id]){
        params.reasonId = item.id;
        if(item.id === 0){
          params.note = this.otherReason;
        }else {
          params.note = item.name;
        }
      }
    });
    return params;
  }
  submitForm(): void {
    if(!this.checkParam()){
      return;
    }
    this.loading = true;
    this.httpClient
      .post('/api/anyfix/work-transfer/service/return', this.getPageParam())
      .pipe(
        finalize(() => {
          this.loading = false;
          this.cdf.markForCheck();
        }))
      .subscribe((res: any) => {
        if(res.code === 0 ){
          this.modal.destroy({code: 0});
        }
      });
  }

  checkParam(){
    const isChecked = this.reasonList.some(item => this.mapOfCheckedId[item.id]);
    if(!isChecked){
      this.msg.warning('请选择一个客服退单原因！');
      return false;
    }
    return true;
  }
  destroyModal(): void {
    this.modal.destroy();
  }

  refreshStatus(reasonId) {
    this.reasonList.filter(item => (item.id !== reasonId))
      .forEach(item => {
        this.mapOfCheckedId[item.id] = false;
      });
    this.disable = this.reasonList.some(item => this.mapOfCheckedId[item.id]);
  }
}
