import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class DemanderService {

  FINISH_REQUIRE_BRAND = 1; // 服务完成设备品牌必填
  FINISH_REQUIRE_MODEL = 2; // 服务完成设备型号必填
  FINISH_REQUIRE_SPECIFICATION = 3; // 服务完成设备规格必填
  FINISH_ALLOW_MODEL = 13; // 服务完成是否允许新增设备型号
  FINISH_ALLOW_SPECIFICATION = 14; // 服务完成是否允许新增设备规格

  dataItemIdList = [this.FINISH_REQUIRE_SPECIFICATION, this.FINISH_REQUIRE_MODEL, this.FINISH_REQUIRE_BRAND,
    this.FINISH_ALLOW_MODEL, this.FINISH_ALLOW_SPECIFICATION];

  dataConfigList = [
    {
      itemId: this.FINISH_REQUIRE_SPECIFICATION,
      description: '服务完成设备规格是否必填',
      itemValue: '1',
      itemName: 'finishRequireSpecification'
    },
    {
      itemId: this.FINISH_REQUIRE_BRAND,
      description: '服务完成品牌是否必填',
      itemValue: '1',
      itemName: 'finishRequireBrand'
    },
    {
      itemId: this.FINISH_REQUIRE_MODEL,
      description: '服务完成型号是否必填',
      itemValue: '1',
      itemName: 'finishRequireModel'
    },
    {
      itemId: this.FINISH_ALLOW_MODEL,
      description: '服务完成是否允许新增设备型号',
      itemValue: '2'
    },
    {
      itemId: this.FINISH_ALLOW_SPECIFICATION,
      description: '服务完成是否允许新增设备规格',
      itemValue: '2'
    }
  ];

  // 结算方式列表
  settleTypeList = [
    {
      label: '按单结算',
      value: 1,
    },
    {
      label: '按月结算',
      value: 2,
    },
    {
      label: '按季度结算',
      value: 3,
    },
  ]

}
