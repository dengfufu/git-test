import {AfterViewInit, ChangeDetectorRef, Component, ElementRef, Input, OnInit, ViewChild} from '@angular/core';
import {FormBuilder, FormControl, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'flow-template-copy',
  templateUrl: 'flow-template-copy.component.html'
})
export class FlowTemplateCopyComponent implements OnInit {

  @Input() id;
  validateForm: FormGroup;
  values: any[] | null = null;

  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      newFlowTemplateName: ['', [Validators.required]],
      sourceEnabled: ['', [Validators.required]],
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
            this.validateForm.controls.newFlowTemplateName.setValue(data.name);
            this.validateForm.controls.sourceEnabled.setValue(data.enabled);
          }
        });
    }
  }

  submitForm(value: any): void {
    this.spinning = true;
    value.sourceFlowTemplateId = this.id;
    this.httpClient
      .post('/api/wms/flow-template/addByCopy', value)
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
