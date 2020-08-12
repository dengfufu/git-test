import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService, NzNotificationService} from 'ng-zorro-antd';
import {SelectEngineerComponent} from '../select-engineer/select-engineer.component';

@Component({
  selector: 'app-to-assign',
  templateUrl: './to-assign.component.html',
  styleUrls: ['./to-assign.component.less']
})
export class ToAssignComponent implements OnInit {

  @Input() workId: any;
  @Input() serviceBranch: any;
  validateForm: FormGroup;
  engineer: any[] = [];
  engineerName: any;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private messageService: NzMessageService) {
    this.validateForm = this.formBuilder.group({
      engineerName: ['', [Validators.required]],
      remark: ['']
    });
  }

  ngOnInit(): void {

  }

  selectEngineer() {
    const modal = this.modalService.create({
      nzTitle: '选择工程师',
      nzContent: SelectEngineerComponent,
      nzWidth: 800,
      nzFooter: null,
      nzStyle: {'margin-top': '-50px'},
      nzBodyStyle: {'padding-top': '5px', 'padding-bottom': '5px'},
      nzComponentParams: {
        workId: this.workId,
        serviceBranch: this.serviceBranch,
        engineerCheckedList: [...this.engineer]
      }
    });
    modal.afterClose.subscribe(result => {
      if (result && result.data) {
        this.engineer = [...result.data];
        if (this.engineer && this.engineer.length > 0) {
          let isFirst = true;
          this.engineer.forEach(item => {
            if (isFirst) {
              this.engineerName = item.userName;
              isFirst = false;
            } else {
              this.engineerName = this.engineerName + ',' + item.userName;
            }
          });
        } else {
          this.engineerName = '';
        }
      }
    });
  }

  submitForm(): void {
    if (typeof this.engineer === 'undefined') {
      this.messageService.error('请添加负责人');
    }
    this.httpClient
      .post('/api/anyfix/work-assign/assign', {assignEngineerList: this.engineer, workId: this.workId})
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.messageService.success('派单成功');
          this.modal.destroy({code: 0});
        }
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }

}
