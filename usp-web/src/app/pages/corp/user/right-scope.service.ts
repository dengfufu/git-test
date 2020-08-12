import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class RightScopeService {

  SERVICE_BRANCH = 10; // 服务商
  PROVINCE = 20; // 省份
  DEMANDER = 30; // 委托商

  /**
   * 范围权限类型
   */
  rightScopeTypeList = [
    {code: this.SERVICE_BRANCH, name: '服务网点', hasLower: true},
    {code: this.PROVINCE, name: '省份', hasLower: false},
    {code: this.DEMANDER, name: '委托商', hasLower: false}
  ];

  rightScopeTypeNameMap: {[key: number]: string} = {};

  constructor() {
    this.rightScopeTypeNameMap[this.SERVICE_BRANCH] = '服务网点';
    this.rightScopeTypeNameMap[this.PROVINCE] = '省份';
    this.rightScopeTypeNameMap[this.DEMANDER] = '委托商';
  }

  findScopeNames(rightScope: any) {
    let allOrgName = '';
    let ownOrgName = '';
    let ownLowerOrgName = '';
    switch (rightScope.scopeType) {
      case this.SERVICE_BRANCH:
        allOrgName = '全部';
        ownOrgName = '所在' + this.rightScopeTypeNameMap[this.SERVICE_BRANCH];
        ownLowerOrgName = '所在下级' + this.rightScopeTypeNameMap[this.SERVICE_BRANCH];
        break;
      case this.PROVINCE:
        allOrgName = '全部';
        break;
      case this.DEMANDER:
        allOrgName = '全部';
        break;
    }
    let scopeNames = '';
    if (rightScope.hasAllScope === 'Y') {
      scopeNames = allOrgName;
    }
    if (rightScope.hasOwnScope === 'Y') {
      scopeNames = scopeNames.length > 0 ? scopeNames + ';' + ownOrgName : ownOrgName;
    }
    if (rightScope.hasOwnLowerScope === 'Y') {
      scopeNames = scopeNames.length > 0 ? scopeNames + ';' + ownLowerOrgName : ownLowerOrgName;
    }
    // 指定组织
    if (rightScope.orgNames) {
      scopeNames = scopeNames.length > 0 ? scopeNames + ';' + rightScope.orgNames : rightScope.orgNames;
      if(rightScope.containLowerOrgId === 'Y'){
        scopeNames = scopeNames.length > 0 ? scopeNames + '(包含下级)' : '';
      }
    }
    if (scopeNames !== '') {
      scopeNames = this.rightScopeTypeNameMap[rightScope.scopeType] + ':[' + scopeNames + ']';
    }
    return scopeNames;
  }

}
