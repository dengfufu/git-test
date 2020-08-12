import {Component, Input, OnInit} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-choose-staff',
  templateUrl: './choose-staff.component.html',
  styleUrls: ['./choose-staff.component.less']
})
export class ChooseStaffComponent implements OnInit {

  @Input()
  mapOfCheckedId: { [key: string]: boolean };
  corpId = this.userService.currentCorp.corpId;
  staffList: any[];
  isAllChecked = false;
  isIndeterminate = false;

  constructor(private httpClient: HttpClient,
              private modalRef: NzModalRef,
              private userService: UserService) { }

  ngOnInit() {
    this.httpClient.post('/api/anyfix/service-branch-user/query', {pageNum: 1, pageSize: 50, corpId: this.corpId})
      .subscribe((res: any) => {
        this.staffList = res.data.list;
      })
  }

  checkAll(value: boolean): void {
    this.staffList.forEach(item => (this.mapOfCheckedId[item.userId] = value));
  }

  refreshStatus() {
    this.isAllChecked = this.staffList.every(item => this.mapOfCheckedId[item.userId]);
    this.isIndeterminate = this.staffList.some(item => this.mapOfCheckedId[item.userId]) && !this.isAllChecked;
  }

  closeModal() {
    this.modalRef.close({type: 'cancel'});
  }

  chooseSubmit() {
    const selectedStaffList = [];
    this.staffList.forEach(item => {
      if (this.mapOfCheckedId[item.userId] === true) {
        selectedStaffList.push(item);
      }
      this.modalRef.close({type: 'submit', selectedStaffList});
    })
  }

}
