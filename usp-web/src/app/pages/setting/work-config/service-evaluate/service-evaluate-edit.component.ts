import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators, FormControl} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';


@Component({
  selector: 'app-service-evaluate-edit',
  templateUrl: 'service-evaluate-edit.component.html'
})
export class ServiceEvaluateEditComponent implements OnInit {

  @Input() serviceEvaluate;

  validateForm: FormGroup;
  selectedValue: any;
  scoreList = [];
  showTypeOptions: any;
  showTypeSelectValue: any;
  spinning = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      name: ['', [ZonValidators.required('指标名称'),ZonValidators.maxLength(20),ZonValidators.notEmptyString()]],
      showType: ['', [Validators.required]],
      serviceCorp: ['', [Validators.required]],
      score0: ['', [Validators.required]],
      description: ['', []],
      enabled: ['', []],
      id: ['', [Validators.required]]
    });
  }

  ngOnInit(): void {
    this.validateForm.controls.name.setValue(this.serviceEvaluate.name);
    this.validateForm.controls.description.setValue(this.serviceEvaluate.description);
    this.validateForm.controls.id.setValue(this.serviceEvaluate.id);
    this.validateForm.controls.serviceCorp.setValue(this.serviceEvaluate.serviceCorp);
    this.validateForm.controls.enabled.setValue(this.serviceEvaluate.enabled === 'Y');
    this.showTypeSelectValue = this.serviceEvaluate.showType;
    const scoreList = this.serviceEvaluate.scores.split(',');
    const labelList = this.serviceEvaluate.labels.split(',');
    this.assembleScoreList(scoreList, labelList);

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

  assembleScoreList(scoreList, labelList) {
    this.scoreList.push({
      id: 'score0',
      score: scoreList[0],
      label: labelList[0],
      canDelete: false
    });
    // tslint:disable-next-line:prefer-for-of
    for (let i = 1; i < scoreList.length; i++) {
      const id = 'score' + i;
      this.validateForm.addControl(id, new FormControl('', Validators.required));

      this.scoreList.push({
        id,
        score: scoreList[i],
        label: labelList[i],
        canDelete: true
      });
    }
  }

  submitForm(value: any): void {
    const obj = this.assembleScoreAndLabel();
    value.scores = obj.scores;
    value.labels = obj.labels;

    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/service-evaluate/update',
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
    console.log(id);
  }

  removeScoreItem(item, index) {
    this.validateForm.removeControl(item.id);
    this.scoreList.splice(index, 1);
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
}

