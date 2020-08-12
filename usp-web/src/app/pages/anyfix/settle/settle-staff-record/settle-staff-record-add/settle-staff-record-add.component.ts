import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {ActivatedRoute, Router} from '@angular/router';
import {ChooseStaffComponent} from '../choose-staff/choose-staff.component';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-settle-staff-record-add',
  templateUrl: './settle-staff-record-add.component.html',
  styleUrls: ['./settle-staff-record-add.component.less']
})
export class SettleStaffRecordAddComponent implements OnInit {

  staffList: any[];
  selectedStaffList: any[];
  userIdList: any[] = [];
  mapOfCheckedId: { [key: string]: boolean } = {};

  addForm: FormGroup;

  constructor(private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private router: Router,
              private activateRoute: ActivatedRoute,
              private nzModalService: NzModalService,
              private messageService: NzMessageService,
              private userService: UserService,
              private modal: NzModalRef) {
    this.addForm = this.formBuilder.group({
      recordName: ['', [Validators.required, Validators.maxLength(50)]],
      settleTimeRange: [[], Validators.required],
      note: ['', Validators.maxLength(200)]
    });
  }

  ngOnInit() {
    this.httpClient.post('/api/anyfix/service-branch-user/query', {serviceCorp: this.userService.currentCorp.corpId})
      .subscribe((res: any) => {
        this.staffList = res.data.list;
      })
  }

  chooseStaff() {
    const modal = this.nzModalService.create({
      nzTitle: '选择员工',
      nzWidth:800,
      nzContent: ChooseStaffComponent,
      nzComponentParams: {mapOfCheckedId: this.mapOfCheckedId},
      nzFooter: null
    })
    modal.afterClose.subscribe(result => {
      if(result.type === 'submit'){
        this.selectedStaffList = result.selectedStaffList;
        this.selectedStaffList.forEach(item => (this.mapOfCheckedId[item.userId] = true));
      }
    });
  }

  deleteUser(index, userId) {
    this.selectedStaffList.splice(index, 1);
    this.mapOfCheckedId[userId] = false;
  }

  cancel() {
    this.modal.destroy('cancel');
  }

  addSubmit(){
    const params = this.addForm.value;
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.startDate = params.settleTimeRange[0];
    params.endDate = params.settleTimeRange[1];
    if(this.selectedStaffList.length <= 0){
      alert('请先选择员工！');
      return;
    }
    for(const staff of this.selectedStaffList){
      this.userIdList.push(staff.userId);
    }
    params.userIdList = this.userIdList;
    this.httpClient.post('/api/anyfix/settle-staff-record/add', params)
      .subscribe((res:any) => {
        this.messageService.success(res.msg);
        this.modal.destroy('submit');
      })
  }

}
