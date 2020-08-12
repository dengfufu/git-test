import {ChangeDetectorRef, Component, Input, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzModalRef} from 'ng-zorro-antd';
import {UserService} from '@core/user/user.service';


@Component({
  selector: 'app-corp-service-demander-config',
  templateUrl: 'service-demander-config.component.html'
})
export class ServiceDemanderConfigComponent implements OnInit {

  @Input() id: any;
  validateForm: FormGroup;
  demanderCorpOptionLoading = false;
  demanderCorpOptions = [];
  spinning = false;
  detail : any = {};
  FINISH_REQUIRE_BRAND = 1;
  FINISH_REQUIRE_MODEL = 2;
  FINISH_REQUIRE_SPECIFICATION = 3;
  itemIdMap = {
    finishRequireBrand : this.FINISH_REQUIRE_BRAND,
    finishRequireModelName : this.FINISH_REQUIRE_MODEL,
    finishRequireSpecification : this.FINISH_REQUIRE_SPECIFICATION
  };
  DEMANDER_TYPE = 1;
  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private userService: UserService,
              private httpClient: HttpClient) {
    this.validateForm = this.formBuilder.group({
      finishRequireBrand: [false, []],
      finishRequireModelName: [false, []],
      finishRequireSpecification: [false, []],
    });
  }

  ngOnInit(): void {
    this.getData();
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    const params = this.convertToParams();
    this.spinning = true;
    this.httpClient
      .post('/api/anyfix/service-config/demander/add',
        params)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe(() => {
        this.modal.destroy('submit');
      });
  }

  getData() {
    this.httpClient
      .post('/api/anyfix/service-config/getConfig', this.getQueryParams())
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        const data = res.data;
        if(data && data.length > 0) {
          data.forEach( item => {
            const key =  this.findKey(this.itemIdMap,item.itemId);
            if(key && item.itemValue === '2') {
              this.validateForm.controls[key].setValue(true);
            }
          });
        }
      });
  }

  findKey (obj,value, compare = (a, b) => a === b) {
    return Object.keys(obj).find(k => compare(obj[k], value))
  }

  destroyModal(): void {
    this.modal.destroy();
  }

  convertToParams() {
    const params = [];
    Object.keys(this.validateForm.value).forEach(key => {
      let value = this.validateForm.value[key];
      value =  value ? '2' : '1';
      const obj = {
        id : this.id,
        itemId : this.itemIdMap[key],
        itemValue : value,
        type: this.DEMANDER_TYPE
      };
      params.push(obj);
    });
    return params;
  }

  getQueryParams() {
    const params : any ={};
    params.id = this.id;
    params.itemIdList = [
      this.FINISH_REQUIRE_BRAND,
      this.FINISH_REQUIRE_MODEL,
      this.FINISH_REQUIRE_SPECIFICATION
    ];
    return params;
  }
}
