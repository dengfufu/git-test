import {Injectable} from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AreaService {

  // 根据district 获取 [省, 市, 区] 数组
  getAreaListByDistrict(district: string) {
    if (district && district.length > 0) {
      if (district.length === 2) {
        return [district];
      } else if (district && district.length === 4) {
        return [district.substr(0, 2), district];
      } else if (district && district.length === 6) {
        // 上级市是重庆市
        if (district.substr(0, 4) === '5002') {
          return [district.substr(0, 2), '5001', district];
          // 本身是省直辖市
        } else if (district.match(/^\d{2}9\d{3}$/)) {
          return [district.substr(0, 2), district];
        } else {
          return [district.substr(0, 2), district.substr(0, 4), district];
        }
      }
      return [];
    }
  }

  // 判断sourceDistrict是否为targetDistrict的下级
  isChildren(sourceDistrict: string, targetDistrict: string): boolean {
    if (!sourceDistrict || !targetDistrict) {
      return false;
    }
    if (sourceDistrict.length < targetDistrict.length) {
      return false;
    }
    if (sourceDistrict.indexOf(targetDistrict) === 0) {
      return true;
    } else if (sourceDistrict.indexOf('5002') === 0) {
      if (targetDistrict === '5001') {
        return true;
      }
    }
    return false;
  }

}
