import {ChangeDetectorRef, Component,  OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {ZonValidators} from '@util/zon-validators';
import {catchError, map} from 'rxjs/operators';
import {throwError} from 'rxjs';


@Component({
  selector: 'app-import-work',
  templateUrl: 'import-work.component.html',
  styleUrls: ['import-work.component.less']

})


export class ImportWorkComponent implements OnInit {


  demanderCorpList: any[] = [];

  form: FormGroup;

  demanderCorpName = '';

  fileTypes = ['application/vnd.ms-excel','application/vnd.openxmlformats-officedocument.spreadsheetml.sheet'];

  errorMsg = '';


  data = {};

  num = 0;

  selectedIndex = 1;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private message: NzMessageService,
              private modalService: NzModalService) {

    this.form = formBuilder.group({
      demanderCorp: [null, [ZonValidators.required()]], // 委托商
      autoHandel: ['Y', [ZonValidators.required()]],
    });
  }


  ngOnInit() {
    this.listDemanderCorp();
  }

  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length >= 1) {
          this.form.patchValue({
            demanderCorp: this.demanderCorpList[0].demanderCorp
          });
        }
        this.demanderCorpChange();
      }
    });
  }

  demanderCorpChange(){
    const demanderCorp = this.form.controls.demanderCorp.value;
    if(demanderCorp && this.demanderCorpList != null && this.demanderCorpList.length > 0){
      const demander = this.demanderCorpList.find((item) => {
        if(item.demanderCorp === demanderCorp){
          return item
        }
      })
      if(demander){
        this.demanderCorpName = demander.demanderCorpName;
      }
    }
  }


  customReq = (item: any) => {
    const formData = new FormData();
    formData.append('file', item.file);
    formData.append('autoHandel', this.form.controls.autoHandel.value);

    this.modalService.confirm({
      nzTitle: '确认',
      nzContent: '确定导入 ' + item.file.name + ' 中数据?',
      nzWrapClassName: 'vertical-center-modal',
      nzOkText: '确定',
      nzCancelText: '取消',
      nzOnOk: () => {
        this.httpClient.post(item.action, formData)
          .subscribe((result: any) => {
            if (result.code === 0) {
              item.onSuccess({}, {status: 'done'});
              let msg = '';
              if (Number(result.data.num) > 0 && result.data.errorInfo === '') {
                this.num = result.data.num;
                msg = '导入成功,共导入' + this.num + '条工单';
              } else {
                msg = result.data.errorInfo;
              }
              if(this.num > 0) {
                this.errorMsg = '';
                this.modalService.info({
                  nzContent: msg,
                  nzOkText: '确定',
                  nzMaskClosable: false,
                  nzWidth: '600px',
                  nzStyle:{'margin-top': '-40px'},
                  nzOnOk: () => {
                    this.modal.destroy('update');
                  }
                });
              }else{
                this.errorMsg = msg;
              }

            }
          })
      },
      nzOnCancel: () => {
      }
    })

  };

  doCopy(errorInfo:string) {
    const errorMsg = errorInfo.replace(/<br\/>/g, '\n\r');
    const oInput = document.createElement('input');
    oInput.value = errorMsg;
    document.body.appendChild(oInput);
    oInput.select();
    document.execCommand('Copy');
    oInput.style.display = 'none';
    this.message.success('复制成功');
  }

  destroyModal() {
    this.modal.destroy('cancel');
  }
}
