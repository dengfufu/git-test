import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'flow-template-edit',
  templateUrl: 'flow-template-edit.component.html'
})
export class FlowTemplateEditComponent implements OnInit {

  @Input() id;
  validateForm: FormGroup;
  values: any[] | null = null;

  flowTemplateNodeList: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [Validators.required]],
      sortNo: ['', [Validators.required]],
      enabled: ['', [Validators.required]],
      description: ['', [Validators.required]],
    });
  }

  ngOnInit(): void {
    if(this.id !== null && this.id !== undefined && this.id !== {}){
      this.httpClient
        .get('/api/wms/flow-template/getFlowTemplate/' + this.id)
        .pipe(
        )
        .subscribe((res: any) => {
          const data = res.data;
          if(data !== null){
            this.validateForm.controls.name.setValue(data.name);
            this.validateForm.controls.sortNo.setValue(data.sortNo);
            this.validateForm.controls.enabled.setValue(data.enabled);
            this.validateForm.controls.description.setValue(data.description);
            this.flowTemplateNodeList = data.flowTemplateNodeList
          }
        });
    }
  }

  submitForm(value: any): void {
    this.spinning = true;
    value.id = this.id;
    value.flowTemplateNodeList = this.flowTemplateNodeList;
    this.httpClient
      .post('/api/wms/flow-template/modFlowTemplateBaseInfo', value)
      .pipe(
        finalize(() => {
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.spinning = false;
        this.modal.destroy(0);
      });
  }

  destroyModal(): void {
    this.modal.destroy();
  }
}

