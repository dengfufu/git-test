import {ChangeDetectorRef, Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {HttpClient} from '@angular/common/http';
import {NzMessageService, NzModalService} from 'ng-zorro-antd';
import {ConfigItemEditComponent} from './config-item-edit.component';
import {Page, Result} from '@core/interceptor/result';
import {ANYFIX_RIGHT} from '@core/right/right';
import {UserService} from '@core/user/user.service';
import {WorkConfigService} from '../work-config.service';


@Component({
  selector: 'app-config-item-list',
  templateUrl: 'config-item-list.component.html',
  styleUrls: ['config-item-list.component.less']
})
export class ConfigItemListComponent implements OnInit {

  aclRight = ANYFIX_RIGHT;

  searchForm: FormGroup;

  page = new Page();
  configList: any;
  loading = false;
  useFormValue = true;
  drawerVisible: boolean;
  // 委托商列表
  demanderCorpList = [];

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private httpClient: HttpClient,
              private userService: UserService,
              private msgService: NzMessageService,
              private modalService: NzModalService,
              private workConfigService: WorkConfigService) {
    this.searchForm = this.formBuilder.group({
      demanderCorp: [],
      name: [],
      enabled: []
    });
  }

  ngOnInit(): void {
    this.getConfigData();
  }

  // 加载委托商列表
  getConfigData() {
    this.httpClient.post(`/api/anyfix/service-config/corp/getConfig`,
      {
        corpId: this.userService.currentCorp.corpId,
        itemIdList: this.workConfigService.dataItemIdList
      }).subscribe((result: Result) => {
        this.configList = result.data;
        this.convert(result.data);
    });
  }

  convert(list) {
    for(const item of list) {
      item.isForInput = item.itemId === this.workConfigService.CHECK_WORK_CODE_TEMPLATE;
      if (item.isForInput ) {
          item.showDefaultValue = item.defaultValue === '' ? '无': item.defaultValue;
          item.showItemValue = item.itemValue === '' ? '无': item.itemValue;
      } else {
        item.showDefaultValue = item.defaultValue === '1' ? '否': '是';
        item.showItemValue = item.itemValue === '1' ? '否': '是';
      }
    }
  }


  // 进入编辑页面
  editModal(data): void {
    const width = data.isForInput ? 600: 400;
    const modal = this.modalService.create({
      nzTitle: '编辑数据项',
      nzContent: ConfigItemEditComponent,
      nzFooter: null,
      nzWidth: width,
      nzComponentParams: {
        data,
        id: this.userService.currentCorp.corpId
      }
    });
    modal.afterClose.subscribe(result => {
      if (result === 'submit') {
        this.getConfigData();
      }
    });
  }
}
