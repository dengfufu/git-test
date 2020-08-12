import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import {AnyfixService} from '@core/service/anyfix.service';
import {format} from 'date-fns';
import {SelectEngineerComponent} from '../../../assign/select-engineer/select-engineer.component';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-service-content-edit',
  templateUrl: './service-content-edit.component.html',
  styleUrls: ['./service-content-edit.component.less']
})
export class ServiceContentEditComponent implements OnInit {

  @Input() work: any;
  // 表单
  form: FormGroup;
  // 加载中
  isLoading = false;
  // 是否服务完成
  finished = false;

  Options: any = {
    trafficMap: [
      {value: 1, text: '公交'},
      {value: 2, text: '摩的'},
      {value: 3, text: '步行'},
      {value: 4, text: '长途汽车'},
      {value: 5, text: '现场'},
      {value: 6, text: '出租车'},
      {value: 7, text: '火车'},
      {value: 8, text: '飞机'},
      {value: 9, text: '公司车'},
      {value: 10, text: '银行车'},
      {value: 11, text: '郊县汽车'},
      {value: 12, text: '轮渡'},
    ],
    formTypeList: [
      {value: 10, text: '建立工单'},
    ]
  };

  goDate: Date;
  goTime: Date;
  signDate: Date;
  signTime: Date;
  startDate: Date;
  startTime: Date;
  endDate: Date;
  endTime: Date;

  engineer: any[] = [];
  togetherEngineers: any[] = [];
  engineerName: any;

  nzFilterOption = () => true;

  constructor(
    public anyfixService: AnyfixService,
    private modalRef: NzModalRef,
    private httpClient: HttpClient,
    private formBuilder: FormBuilder,
    private modalService: NzModalService,
    private messageService:NzMessageService
  ) {
    this.form = this.formBuilder.group({
      traffic: [],
      trafficNote: [],
      goTime: [],
      signTime: [],
      startTime: [],
      endTime: [],
      engineerName: [],
      helpNames: [],
      finishDescription: []
    })
  }

  ngOnInit() {
    // console.log(this.work);
    if(this.work !== null && this.work !== undefined && this.work !== {}){
      this.finished = (this.work.workStatus === this.anyfixService.CLOSED);
      this.form.patchValue({
        traffic: this.work.traffic,
        trafficNote: this.work.trafficNote,
        helpNames: this.work.helpNames,
        finishDescription: this.work.finishDescription
      });
      // 组装协同工程师
      if(this.work.togetherEngineerList !== undefined&&this.work.togetherEngineerList !== null){
          this.work.togetherEngineerList.forEach(item => {
            if (!this.engineerName) {
              this.engineerName = item.engineerName;
            } else {
              this.engineerName = this.engineerName + ',' + item.engineerName;
            }
            const togetherEngineer = {
              userId: item.engineerId,
              userName: item.engineerName
            }
            this.engineer.push(togetherEngineer);
          });
        this.form.patchValue({
          engineerName: this.engineerName
        })
      }
      if (this.work.goTime) {
        this.goDate = new Date(format(this.work.goTime, 'YYYY-MM-DD'));
        this.goTime = new Date(format(this.work.goTime, 'YYYY-MM-DD HH:mm'));
      }
      if (this.work.signTime) {
        this.signDate = new Date(format(this.work.signTime, 'YYYY-MM-DD'));
        this.signTime = new Date(format(this.work.signTime, 'YYYY-MM-DD HH:mm'));
      }
      if (this.work.startTime) {
        this.startDate = new Date(format(this.work.startTime, 'YYYY-MM-DD'));
        this.startTime = new Date(format(this.work.startTime, 'YYYY-MM-DD HH:mm'));
      }
      if (this.work.endTime) {
        this.endDate = new Date(format(this.work.endTime, 'YYYY-MM-DD'));
        this.endTime = new Date(format(this.work.endTime, 'YYYY-MM-DD HH:mm'));
      }
    }
  }

  // 取消
  cancel() {
    this.modalRef.destroy('cancel');
  }

  submit() {
    if (!this.form.value.traffic) {
      this.messageService.error('请选择交通工具');
      return;
    }
    if (!this.goDate || !this.goTime) {
      this.messageService.error('请选择出发时间');
      return;
    }
    if (!this.signDate || !this.signTime) {
      this.messageService.error('请选择到达时间');
      return;
    }
    if (this.finished) {
      if (!this.startDate || !this.startTime) {
        this.messageService.error('请选择服务开始时间');
        return;
      }
      if (!this.endDate || !this.endTime) {
        this.messageService.error('请选择服务完成时间');
        return;
      }
      if (!this.form.value.finishDescription) {
        this.messageService.error('请输入请输入完成情况');
        return;
      }
    }
    this.isLoading = true;
    // 组装form 值
    this.DataBuild();
    const params = this.form.value;
    params.workId = this.work.workId;
    if (this.togetherEngineers) {
      params.togetherEngineerList = this.togetherEngineers;
    }
    // 提交表单
    this.httpClient.post('/api/anyfix/work-finish/mod?type=info', params)
      .pipe(
        finalize(() => {
          this.isLoading = false;
        })
      ).subscribe((result: any) => {
      if (result.code === 0) {
        this.messageService.success('修改成功');
        this.modalRef.close('submit');
      }
    });
  }

  // 组装form 值
  DataBuild(): void {
    // 组装出发时间
    if (this.goDate && this.goTime) {
      const hour = this.goTime.getHours();
      const minute = this.goTime.getMinutes();
      this.goDate.setHours(hour, minute, 0, 0);
      this.form.patchValue({
        goTime: this.goDate
      });
    } else {
      this.form.patchValue({
        goTime: null
      });
    }
    // 组装到达时间
    if (this.signDate && this.signTime) {
      const hour = this.signTime.getHours();
      const minute = this.signTime.getMinutes();
      this.signDate.setHours(hour, minute, 0, 0);
      this.form.patchValue({
        signTime: this.signDate
      });
    } else {
      this.form.patchValue({
        signTime: null
      });
    }
    if (this.finished) {
      // 组装服务开始时间
      if (this.startDate && this.startTime) {
        const hour = this.startTime.getHours();
        const minute = this.startTime.getMinutes();
        this.startDate.setHours(hour, minute, 0, 0);
        this.form.patchValue({
          startTime: this.startDate
        });
      } else {
        this.form.patchValue({
          startTime: null
        });
      }
      // 组装服务完成时间
      if (this.endDate && this.endTime) {
        const hour = this.endTime.getHours();
        const minute = this.endTime.getMinutes();
        this.endDate.setHours(hour, minute, 0, 0);
        this.form.patchValue({
          endTime: this.endDate
        });
      } else {
        this.form.patchValue({
          endTime: null
        });
      }
    }
  }

  selectEngineer() {
    const modal = this.modalService.create({
      nzTitle: '选择协同工程师',
      nzContent: SelectEngineerComponent,
      nzWidth: 800,
      nzFooter: null,
      nzStyle: {'margin-top': '-50px'},
      nzBodyStyle: {'padding-top': '5px', 'padding-bottom': '5px'},
      nzComponentParams: {
        workId: this.work.workId,
        serviceBranch: this.work.serviceBranch,
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
            const togetherEngineer = {
              engineerId: item.userId
            }
            this.togetherEngineers.push(togetherEngineer);
          });
        } else {
          this.engineerName = '';
        }
        this.form.patchValue({
          engineerName: this.engineerName
        })
      }
    });
  }
}
