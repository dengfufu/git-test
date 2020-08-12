import {ChangeDetectorRef, Component, Input, OnInit, Output} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';
import {HttpClient} from '@angular/common/http';
@Component({
  selector: 'flow-node-edit',
  templateUrl: 'flow-node-edit.component.html'
})
export class FlowNodeEditComponent implements OnInit {

  @Input() flowNode: any;

  flowNodeForm: FormGroup;
  corpId = this.userService.currentCorp.corpId;
  selectOptions: any = {
    nodeTypeList: [
      {id: 10, name: '填写节点'},
      {id: 20, name: '普通审批节点'},
      {id: 30, name: '会签审批节点'},
      {id: 40, name: '发货节点'},
      {id: 50, name: '收货节点'},
      {id: 60, name: '确认节点'}
    ],
    formMainList: [],
    formDetailList: [],
  };
  values: any[] | null = null;


  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private userService: UserService) {
    this.flowNodeForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      nodeType: ['', [Validators.required]],
      formMainId: ['', [Validators.required]],
      formDetailId: ['', [Validators.required]],
      sortNo: ['', [Validators.required]],
      isCore: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.FormTemplate();
    this.flowNodeForm.controls.name.setValue(this.flowNode.name);
    this.flowNodeForm.controls.nodeType.setValue(this.flowNode.nodeType);
    this.flowNodeForm.controls.formMainId.setValue(this.flowNode.formMainId);
    this.flowNodeForm.controls.formDetailId.setValue(this.flowNode.formDetailId);
    this.flowNodeForm.controls.sortNo.setValue(this.flowNode.sortNo);
    this.flowNodeForm.controls.isCore.setValue(this.flowNode.isCore);
  }

  FormTemplate() {
    this.httpClient
      .post('/api/wms/form-template/listForSelectBy', {corpId: this.corpId})
      .pipe(
      )
      .subscribe((res: any) => {
        const data = res.data;
        data.forEach(item => {
          if(item.tableClass ===10){
            this.selectOptions.formMainList.push({
              value: item.id,
              text: item.name
            });
          }else  if(item.tableClass ===20){
            this.selectOptions.formDetailList.push({
              value: item.id,
              text: item.name
            });
          }
        });
      });
  }
}
