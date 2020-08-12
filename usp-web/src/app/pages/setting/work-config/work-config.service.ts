import {Injectable} from '@angular/core';

export enum AllCreateEnum {
  ALLOW = '2',
  NOT_ALL = '1'
}

export enum ConfigRequireEnum {
  REQUIRED = '2',
  NOT_REQUIRED = '1'
}

@Injectable({
  providedIn: 'root'
})
export class WorkConfigService {

  WORK_ADD_REQUIRE_CHECK_WORK_CODE = 4;
  WORK_ADD_NEED_QUOTE = 6; // 建单时启用费用报价
  WORK_ADD_REQUIRE_DEVICE_BRANCH = 7; // 建单时是否需要设备网点
  WORK_ADD_ALLOW_CREATE_CUSTOM = 8;
  WORK_ADD_ALLOW_CREATE_DEVICE_BRANCH = 9;
  CHECK_WORK_CODE_TEMPLATE = 10;
  WORK_ADD_ALLOW_CREATE_MODEL = 11; // 建单时是否允许新增设备型号
  WORK_ADD_ALLOW_CREATE_SPECIFICATION = 12; // 建单时是否允许新增设备规格

  dataItemIdList = [this.WORK_ADD_REQUIRE_CHECK_WORK_CODE, this.WORK_ADD_NEED_QUOTE, this.WORK_ADD_REQUIRE_DEVICE_BRANCH,
    this.WORK_ADD_ALLOW_CREATE_CUSTOM, this.WORK_ADD_ALLOW_CREATE_DEVICE_BRANCH, this.CHECK_WORK_CODE_TEMPLATE,
    this.WORK_ADD_ALLOW_CREATE_MODEL, this.WORK_ADD_ALLOW_CREATE_SPECIFICATION];

}
