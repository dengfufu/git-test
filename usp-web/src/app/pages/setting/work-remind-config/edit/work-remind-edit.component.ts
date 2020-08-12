import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalRef, NzModalService} from 'ng-zorro-antd';
import {WorkRemindItem, WorkRemindItemEditComponent} from '../remind-item/work-remind-item-edit.component';
import {AreaService} from '@core/area/area.service';
import {Result} from '@core/interceptor/result';
import uuidV1 from 'uuid/v1';

@Component({
  selector: 'app-work-remind-edit',
  templateUrl: 'work-remind-edit.component.html'
})
export class WorkRemindEditComponent implements OnInit {

  @Input() workRemind;

  validateForm: FormGroup;

  demanderCorpOptionLoading = false;
  serviceBranchOptionLoading = false;

  largeClassOptionLoading = false;
  smallClassOptionLoading = false;
  brandOptionLoading = false;
  modelOptionLoading = false;
  deviceBranchOptionLoading = false;

  demanderCorpOption = [];
  largeClassOption = [];
  smallClassOption = [];
  brandOption = [];
  modelOption = [];
  deviceBranchOption = [];
  serviceBranchOption = [];
  districts = [];
  workTypeOptions: any[] = [];
  remindItemList: WorkRemindItem[] = []; // 设备规格列表

  isSubmit = false;
  isLoading = false;

  nzFilterOption = () => true;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private modal: NzModalRef,
              private httpClient: HttpClient,
              private modalService: NzModalService,
              private messageService: NzMessageService,
              private areaService: AreaService) {
    this.validateForm = this.formBuilder.group({
      demanderCorp: [null, []],
      serviceBranch: [null, []],
      handleType: [null, []],
      largeClassId: [null, []],
      smallClassId: [null, []],
      brandId: [null, []],
      modelId: [null, []],
      district: [null, []],
      deviceBranch: [null, []],
      workType: [null, []],
      remindName: [null, []],
      enabled: ['', []]
    });
  }

  ngOnInit() {
    this.searchServiceBranch();
    this.httpClient
      .get('/api/uas/area/list')
      .subscribe((res: any) => {
        this.districts = res.data;
        // this.serviceCorp = this.workRemind.serviceCorp;
        // this.serviceName = this.workRemind.serviceCorpName;
        this.validateForm.controls.demanderCorp.setValue(this.workRemind.demanderCorp);
        this.validateForm.controls.serviceBranch.setValue(this.workRemind.serviceBranch);
        this.validateForm.controls.handleType.setValue(this.workRemind.handleType);
        this.validateForm.controls.largeClassId.setValue(this.workRemind.largeClassId);
        this.validateForm.controls.smallClassId.setValue(this.workRemind.smallClassId);
        this.validateForm.controls.brandId.setValue(this.workRemind.brandId);
        this.validateForm.controls.modelId.setValue(this.workRemind.modelId);
        this.validateForm.controls.district.setValue(this.areaService.getAreaListByDistrict(this.workRemind.district));
        this.validateForm.controls.deviceBranch.setValue(this.workRemind.deviceBranch);
        this.validateForm.controls.workType.setValue(this.workRemind.workType);
        this.validateForm.controls.enabled.setValue(this.workRemind.enabled === 'Y');

        this.searchDemanderCorp();
        this.searchServiceBranch(this.workRemind.serviceBranchName);
        this.loadWorkType();
        this.searchLargeClass();
        this.searchSmallClass(this.workRemind.largeClassId);
        this.searchBrand();
        this.searchModel();
        this.searchDeviceBranch();
      });
    this.findDetail(this.workRemind.remindId);
  }

  submitForm(value: any) {
    this.isSubmit = true;
    // tslint:disable-next-line:forin
    for (const key in this.validateForm.controls) {
      this.validateForm.controls[key].markAsDirty();
      this.validateForm.controls[key].updateValueAndValidity();
    }
    if (this.validateForm.valid) {
      let district = '';
      if (this.validateForm.value.district && this.validateForm.value.district.length > 0) {
        district = this.validateForm.value.district[this.validateForm.value.district.length - 1];
      }

      this.remindItemList.forEach(item => {
        item.detailId = '';
      });

      let params: {};
      const url = '/api/anyfix/work-remind/mod';
      params = {
        remindId: this.workRemind.remindId,
        brandId: this.validateForm.value.brandId || 0,
        deviceBranch: this.validateForm.value.deviceBranch || 0,
        district,
        largeClassId: this.validateForm.value.largeClassId || 0,
        modelId: this.validateForm.value.modelId || 0,
        serviceCorp: this.workRemind.serviceCorp || 0,
        serviceBranch: this.validateForm.value.serviceBranch || 0,
        handleType: this.validateForm.value.handleType,
        smallClassId: this.validateForm.value.smallClassId || 0,
        demanderCorp: this.validateForm.value.demanderCorp || 0,
        workType: this.validateForm.value.workType || 0,
        remindName: this.validateForm.value.remindName,
        workRemindDetailDtoList: this.remindItemList,
        enabled: this.validateForm.controls.enabled.value ? 'Y' : 'N'
      };

      this.isLoading = true;
      this.httpClient
        .post(url, params)
        .pipe(
          finalize(() => {
            this.isLoading = false;
            this.cdf.markForCheck();
          })
        )
        .subscribe(() => {
          this.modal.destroy('submit');
        });
    }
  }

  destroyModal() {
    this.modal.destroy();
  }

  searchDemanderCorp() {
    this.demanderCorpOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/demander-service/demander/list', {enabled: 'Y'})
      .pipe(
        finalize(() => {
          this.demanderCorpOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.demanderCorpOption = res.data;
        }
      });
  }

  searchLargeClass() {
    if (this.validateForm.value.demanderCorp > 0) {
      this.largeClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-large-class/list', {corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.largeClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.largeClassOption = res.data;
          }
        });
    }
  }


  searchSmallClass(largeClass?) {
    if (this.validateForm.value.demanderCorp > 0) {
      this.smallClassOptionLoading = true;
      this.httpClient
        .post('/api/device/device-small-class/list',
          {largeClassId: largeClass, corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.smallClassOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.smallClassOption = res.data;
          }
        });
    }
  }

  searchBrand() {
    if (this.validateForm.value.demanderCorp > 0) {
      this.brandOptionLoading = true;
      this.httpClient
        .post('/api/device/device-brand/list', {corpId: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.brandOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.brandOption = res.data;
          }
        });
    }
  }

  searchModel() {
    if (this.validateForm.value.demanderCorp > 0) {
      this.modelOptionLoading = true;
      this.httpClient
        .post('/api/device/device-model/list', {corp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.modelOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.modelOption = res.data;
          }
        });
    }
  }

  searchDeviceBranch() {
    if (this.validateForm.value.demanderCorp > 0) {
      this.deviceBranchOptionLoading = true;
      this.httpClient
        .post('/api/anyfix/device-branch/list', {customCorp: this.validateForm.value.demanderCorp})
        .pipe(
          finalize(() => {
            this.deviceBranchOptionLoading = false;
          })
        )
        .subscribe((res: any) => {
          if (res.code === 0) {
            this.deviceBranchOption = res.data;
          }
        });
    }
  }

  changeServiceBranch() {
    if (!this.validateForm.value.serviceBranch) {
      this.searchServiceBranch();
    }
  }

  searchServiceBranch(name?) {
    const params = {branchName: name};
    this.serviceBranchOptionLoading = true;
    this.httpClient
      .post('/api/anyfix/service-branch/match', params)
      .pipe(
        finalize(() => {
          this.serviceBranchOptionLoading = false;
        })
      )
      .subscribe((res: any) => {
        if (res.code === 0) {
          this.serviceBranchOption = res.data;
        }
      });
  }

  demanderCorpChange() {
    // 委托商改变，选择成功的设备型号置为空
    this.validateForm.controls.modelId.setValue(null);
    this.validateForm.controls.brandId.setValue(null);
    this.validateForm.controls.deviceBranch.setValue(null);
    this.validateForm.controls.largeClassId.setValue(null);
    this.validateForm.controls.smallClassId.setValue(null);
    this.validateForm.controls.workType.setValue(null);
  }

  largeClassChange() {
    this.validateForm.controls.smallClassId.setValue(null);
  }

  /**
   * 列出工单类型
   */
  loadWorkType() {
    this.httpClient.post(`/api/anyfix/work-type/list`,
      {demanderCorp: this.validateForm.value.demanderCorp, enabled: 'Y'}).subscribe((result: Result) => {
      if (result.code === 0) {
        this.workTypeOptions = result.data;
      }
    });
  }

  areaChange(event) {
    if (event === null) {
      return;
    }
  }

  /**
   * 添加预警条件
   */
  addRemindItem(): void {
    this.modalService.create({
      nzTitle: '添加预警条件',
      nzWidth: 700,
      nzContent: WorkRemindItemEditComponent,
      nzOnOk: (res: any) => {
        const workRemindItem: WorkRemindItem = res.validateForm.value;
        if (!workRemindItem.remindTypeName || workRemindItem.remindTypeName.trim() === '') {
          this.messageService.warning('请选择预警类型！');
          return false;
        }
        for (const item of this.remindItemList) {
          if (item.remindTypeName === workRemindItem.remindTypeName && item.detailId !== workRemindItem.detailId) {
            this.messageService.warning('预警类型已存在！');
            return false;
          }
        }
        if (!workRemindItem.expireTimeMin || workRemindItem.expireTimeMin === '') {
          this.messageService.warning('请输入预警条件！');
          return false;
        }
        workRemindItem.detailId = uuidV1();
        workRemindItem.enabled = workRemindItem.enabled ? 'Y' : 'N';
        this.editRemindItemList(workRemindItem);
      }
    });
  }

  /**
   * 修改预警条件
   * @param obj 预警条件对象
   */
  modRemindItem(obj): void {
    this.modalService.create({
      nzTitle: '修改预警条件',
      nzWidth: 700,
      nzContent: WorkRemindItemEditComponent,
      nzComponentParams: {
        object: obj
      },
      nzOnOk: (res: any) => {
        const workRemindItem: WorkRemindItem = res.validateForm.value;
        if (!workRemindItem.remindTypeName || workRemindItem.remindTypeName.trim() === '') {
          this.messageService.warning('请选择预警类型！');
          return false;
        }
        for (const item of this.remindItemList) {
          if (item.remindTypeName === workRemindItem.remindTypeName && item.detailId !== workRemindItem.detailId) {
            this.messageService.warning('预警类型已存在！');
            return false;
          }
        }
        if (!workRemindItem.expireTimeMin || workRemindItem.expireTimeMin === '') {
          this.messageService.warning('请输入预警条件！');
          return false;
        }
        workRemindItem.enabled = workRemindItem.enabled ? 'Y' : 'N';
        this.editRemindItemList(workRemindItem);
      }
    });
  }

  // 删除确认
  showDeleteConfirm(id): void {
    this.modalService.confirm({
      nzTitle: '确定删除该预警条件吗?',
      nzOkText: '确定',
      nzOkType: 'danger',
      nzOnOk: () => this.deleteRemindItem(id),
      nzCancelText: '取消'
    });
  }

  /**
   * 删除预警条件
   * @param id 条件ID
   */
  deleteRemindItem(id) {
    this.remindItemList = this.remindItemList.filter(d => d.detailId !== id);
  }

  /**
   * 编辑预警条件列表
   * @param value 预警条件
   */
  editRemindItemList(value: WorkRemindItem) {
    const index: number = this.remindItemList.findIndex(item => {
      return item.detailId === value.detailId;
    });
    // 存在
    if (index > -1) {
      const workRemindItem = this.remindItemList[index];
      workRemindItem.remindType = value.remindType;
      workRemindItem.remindTypeName = value.remindTypeName;
      workRemindItem.expireTimeMin = value.expireTimeMin;
      workRemindItem.enabled = value.enabled;
      this.remindItemList[index] = workRemindItem;
    } else {
      this.remindItemList = [...this.remindItemList, value];
    }
  }

  findDetail(remindId: any) {
    this.httpClient.get('/api/anyfix/work-remind/detail/' + remindId)
      .subscribe((res: any) => {
        this.remindItemList = res.data.workRemindDetailDtoList;
      });
  }
}
