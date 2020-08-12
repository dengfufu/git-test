import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class ImplementTypeService {

  // 工单支出费用类别
  SUBURB_TRAFFIC = 1;   // 郊区交通费
  LONG_TRAFFIC = 2;     // 长途交通费
  HOTEL_EXPENSE = 3;    // 住宿费
  TRAVEL_EXPENSE = 4;   // 出差补助
  POST_EXPENSE = 5;     // 邮寄费

  // 类别列表
  implementTypeList: any[] = [this.SUBURB_TRAFFIC, this.LONG_TRAFFIC, this.HOTEL_EXPENSE, this.TRAVEL_EXPENSE, this.POST_EXPENSE];
  // 类别映射
  implementTypeMap: {[key: string]: string} = {};
  // 类别下拉选项
  implementTypeOptions: any[] = [];

  constructor() {

    this.implementTypeMap[this.SUBURB_TRAFFIC] = '郊区交通费';
    this.implementTypeMap[this.LONG_TRAFFIC] = '长途交通费';
    this.implementTypeMap[this.HOTEL_EXPENSE] = '住宿费';
    this.implementTypeMap[this.TRAVEL_EXPENSE] = '出差补助';
    this.implementTypeMap[this.POST_EXPENSE] = '邮寄费';

    this.implementTypeOptions = [
      {id: this.SUBURB_TRAFFIC, name: this.implementTypeMap[this.SUBURB_TRAFFIC]},
      {id: this.LONG_TRAFFIC, name: this.implementTypeMap[this.LONG_TRAFFIC]},
      {id: this.HOTEL_EXPENSE, name: this.implementTypeMap[this.HOTEL_EXPENSE]},
      {id: this.TRAVEL_EXPENSE, name: this.implementTypeMap[this.TRAVEL_EXPENSE]},
      {id: this.POST_EXPENSE, name: this.implementTypeMap[this.POST_EXPENSE]}
    ]
  }

}
