import {Component, Input, OnInit} from '@angular/core';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-settle-custom-add',
  templateUrl: './settle-custom-add.component.html',
  styleUrls: ['./settle-custom-add.component.less']
})
export class SettleCustomAddComponent implements OnInit {

  @Input()
  customList: any[];

  addForm: FormGroup;


  constructor(private modalRef: NzModalRef,
              private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private userService: UserService,
              private messageService: NzMessageService) {
    this.addForm = this.formBuilder.group({
      customCorp: ['', Validators.required],
      settleTimeRange: [[], Validators.required],
      note: ['', Validators.maxLength(200)]
    });
  }

  ngOnInit() {
  }

  destroyModal(){
    this.modalRef.close();
  }

  addSubmit(){
    const params = this.addForm.value;
    params.serviceCorp = this.userService.currentCorp.corpId;
    params.startDate = params.settleTimeRange[0];
    params.endDate = params.settleTimeRange[1];
    this.httpClient.post('/api/anyfix/settle-custom/add', params)
      .subscribe((res:any) => {
        this.messageService.success(res.msg);
        this.modalRef.close();
      })
  }

}
