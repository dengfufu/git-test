import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';


@Injectable()
export class AutomationConfigService {


  customCorp = 0;

  constructor(private httpClient: HttpClient) {
  }


  /**
   * 得到所选数据的id以逗号分隔的字符串
   */
  getStrNameFromArray(ids: Array<string>): any {

    let idString = '';
    const actualIds = [];
    if (Array.isArray(ids) && ids.length > 0) {
      ids.forEach(id => {
        // 不为空
        if (id != null && id.trim().length > 0) {
          actualIds.push(id);
        }
      });
      idString = actualIds.join();

    }
    return idString;
  }


}
