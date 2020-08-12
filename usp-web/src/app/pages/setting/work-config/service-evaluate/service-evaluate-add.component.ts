import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl, FormsModule} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';

@Component({
  selector: 'app-service-evaluate-add',
  templateUrl: 'service-evaluate-add.component.html'
})
export class ServiceEvaluateAddComponent implements OnInit {

  validateForm: FormGroup;
  selectedValue: any;

  scoreList: any;
  showTypeOptions: any;
  showTypeSelectValue: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private formsModule: FormsModule,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [ZonValidators.required('指标名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      showType: ['', [Validators.required]],
      score0: ['', [Validators.required]],
      description: ['', []],
      enabled: [true, []]
    });
  }

  ngOnInit(): void {
    console.log('sisi',this.validateForm.controls.name);
    this.scoreList = [
      {
        id: 'score0',
        score: '',
        label: '',
        canDelete: false
      }

    ];
    this.showTypeOptions = [
      {
        value: 1,
        text: '星星'
      },
      {
        value: 2,
        text: '列表'
      }
    ];
  }

  submitForm(value: any): void {
    const obj = this.assembleScoreAndLabel();
    value.scores = obj.scores;
    value.labels = obj.labels;
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';

    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/service-evaluate/add',
        value)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res) => {
        this.modal.destroy(res);
      });
  }

  assembleScoreAndLabel() {
    let scores = '';
    let labels = '';
    this.scoreList.forEach(item => {
      scores = scores + item.score + ',';
      labels = labels + item.label + ',';
    });
    scores = scores.substr(0, scores.length - 1);
    labels = labels.substr(0, labels.length - 1);
    return {scores, labels};
  }


  destroyModal(): void {
    this.modal.destroy();
  }

  addScoreItem() {
    const id = 'score' + this.scoreList.length;
    this.validateForm.addControl(id, new FormControl('', Validators.required));
    this.scoreList.push({
      id,
      score: '',
      label: '',
      canDelete: true
    });
  }

  removeScoreItem(item, index) {
    this.validateForm.removeControl(item.id);
    this.scoreList.splice(index, 1);
  }

  print(control) {
    console.log('control',control);
  }
}
