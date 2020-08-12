import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup, Validators} from '@angular/forms';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {HttpClient} from '@angular/common/http';
import {finalize} from 'rxjs/operators';
import uuidV1 from 'uuid/v1';
import {DeviceSpecification, DeviceSpecificationEditComponent} from './specification/device-specification-edit.component';
import {ZonValidators} from '@util/zon-validators';
import {ValueEnum} from '@core/service/enums.service';
import {Result} from '@core/interceptor/result';

@Component({
  selector: 'app-device-small-class-add',
  templateUrl: 'device-small-class-add.component.html'
})
export class DeviceSmallClassAddComponent implements OnInit {

  validateForm: FormGroup;
  largeClassList: any;
  deviceSpecificationList: DeviceSpecification[] = []; // 设备规格列表

  spinning = false;

  demanderCorpList: any[] = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private messageService: NzMessageService) {
    this.validateForm = this.formBuilder.group({
      largeClassId: [null, [Validators.required]],
      name: [null, [ZonValidators.required('设备类型名称'), ZonValidators.maxLength(50), ZonValidators.notEmptyString()]],
      sortNo: [null, [Validators.required, Validators.max(ValueEnum.MAX_INT)]],
      description: [''],
      enabled: [true],
      deviceSpecificationList: [],
      corp: [null, ZonValidators.required('委托商')]
    });
  }


  ngOnInit(): void {
    this.getLargeClass();
    this.getMaxSortNo();
    this.listDemanderCorp();
  }

  getLargeClass() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient.post('/api/device/device-large-class/list', {corp, enabled: 'Y'})
      .subscribe((res: any) => {
        this.largeClassList = res.data;
      });
  }

  getMaxSortNo() {
    const corp = this.validateForm.value.corp || 0;
    this.httpClient
      .get('/api/device/device-small-class/max/sortNo/' + corp)
      .subscribe((res: any) => {
        let sortNo = res.data;
        sortNo = sortNo ? (sortNo + 1) : 1;
        this.validateForm.patchValue({
          sortNo
        });
      });
  }

  /**
   * 委托商列表
   */
  listDemanderCorp() {
    this.httpClient.get(`/api/anyfix/demander/list`).subscribe((result: Result) => {
      if (result.code === 0) {
        this.demanderCorpList = result.data || [];
        if (this.demanderCorpList.length === 1) {
          this.validateForm.patchValue({
            corp: this.demanderCorpList[0].demanderCorp
          });
        }
      }
    });
  }

  /**
   * 委托商变化重新获取编号值
   */
  demanderCorpChange(e) {
    this.validateForm.controls.sortNo.reset();
    this.validateForm.controls.largeClassId.reset();
    this.getLargeClass();
    this.getMaxSortNo();
  }

  /**
   * 添加设备规格
   */
  addSpecification(): void {
    this.modalService.create({
      nzTitle: '添加设备规格',
      nzWidth: 700,
      nzContent: DeviceSpecificationEditComponent,
      nzOnOk: (res: any) => {
        const deviceSpecification: DeviceSpecification = res.validateForm.value;
        if (!deviceSpecification.name || deviceSpecification.name.trim() === '') {
          this.messageService.warning('请输入规格名称！');
          return false;
        }
        if (deviceSpecification.name.trim().length > 50) {
          this.messageService.warning('规格名称：:长度需小于等于50个字！');
          return false;
        }
        for (const item of this.deviceSpecificationList) {
          if (item.name === deviceSpecification.name) {
            this.messageService.warning('规格名称已存在！');
            return false;
          }
        }
        deviceSpecification.id = uuidV1();
        deviceSpecification.enabled = deviceSpecification.enabled ? 'Y' : 'N';
        this.editSpecificationList(deviceSpecification);
      }
    });
  }

  /**
   * 修改设备规格
   * @param obj 设备规格对象
   */
  modSpecification(obj): void {
    this.modalService.create({
      nzTitle: '修改设备规格',
      nzWidth: 700,
      nzContent: DeviceSpecificationEditComponent,
      nzComponentParams: {
        object: obj
      },
      nzOnOk: (res: any) => {
        const deviceSpecification: DeviceSpecification = res.validateForm.value;
        if (!deviceSpecification.name || deviceSpecification.name.trim() === '') {
          this.messageService.warning('请输入规格名称！');
          return false;
        }
        if (deviceSpecification.name.trim().length > 50) {
          this.messageService.warning('规格名称：:长度需小于等于50个字！');
          return false;
        }
        for (const item of this.deviceSpecificationList) {
          if (item.name === deviceSpecification.name && item.id !== deviceSpecification.id) {
            this.messageService.warning('规格名称已存在！');
            return false;
          }
        }
        deviceSpecification.enabled = deviceSpecification.enabled ? 'Y' : 'N';
        this.editSpecificationList(deviceSpecification);
      }
    });
  }

  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该规格吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteSpecification(id),
      nzCancelText: '取消'
    });
  }

  /**
   * 删除设备规格
   * @param id 规格ID
   */
  deleteSpecification(id) {
    this.deviceSpecificationList = this.deviceSpecificationList.filter(d => d.id !== id);
  }

  /**
   * 编辑设备规格列表
   * @param value 设备规格
   */
  editSpecificationList(value: DeviceSpecification) {
    const index: number = this.deviceSpecificationList.findIndex(item => {
      return item.id === value.id;
    });
    // 存在
    if (index > -1) {
      const specification = this.deviceSpecificationList[index];
      specification.name = value.name;
      specification.enabled = value.enabled;
      this.deviceSpecificationList[index] = specification;
    } else {
      this.deviceSpecificationList = [...this.deviceSpecificationList, value];
    }
  }

  submitForm(value: any): void {
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    value.enabled = this.validateForm.controls.enabled.value ? 'Y' : 'N';
    this.deviceSpecificationList.forEach(item => {
      item.id = '';
    });
    value.deviceSpecificationList = this.deviceSpecificationList;
    this.spinning = true;
    this.httpClient
      .post('/api/device/device-small-class/add',
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

}
