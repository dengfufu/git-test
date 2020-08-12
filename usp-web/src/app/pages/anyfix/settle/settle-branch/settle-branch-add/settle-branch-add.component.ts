import {Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';

@Component({
  selector: 'app-settle-branch-add',
  templateUrl: './settle-branch-add.component.html',
  styleUrls: ['./settle-branch-add.component.less']
})
export class SettleBranchAddComponent implements OnInit {

  @Input()
  serviceCorp: any;
  @Input()
  branchList: any[];

  addForm: FormGroup;


  constructor(private modalRef: NzModalRef,
              private formBuilder: FormBuilder,
              private httpClient: HttpClient,
              private messageService: NzMessageService) {
    this.addForm = this.formBuilder.group({
      branchId: ['', Validators.required],
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
    params.serviceCorp = this.serviceCorp;
    params.startDate = params.settleTimeRange[0];
    params.endDate = params.settleTimeRange[1];
    this.httpClient.post('/api/anyfix/settle-branch/add', params)
      .subscribe((res:any) => {
        this.messageService.success(res.msg);
        this.modalRef.close();
      })
  }

}
