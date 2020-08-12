import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {ZonValidators} from '@util/zon-validators';

@Injectable({
  providedIn: 'root'
})
export class DataConfigService {

  constructor(private httpClient: HttpClient) {
  }

  getCorpConfigData(corpId, itemIdList,configItem) {
    return  this.httpClient.post('/api/anyfix/service-config/corp/getConfig',
      {
        corpId,
        itemIdList
      }).toPromise().then((res: Result) => {
        if (res.data && res.data.length >0) {
          res.data.forEach( item => {
            const obj = configItem[item.itemId];
            if (obj) {
               obj.itemValue = item.itemValue;
            }
          });
        }
        return res;
      });
  }

  resetConfigShow(configItem,form) {
    for ( const key of Object.keys(configItem)) {
      const formName = configItem[key].formName;
      if(formName) {
        configItem[key].isShow = false;
        form.controls[formName].setValidators(null);
        form.controls[formName].updateValueAndValidity();
      }
    }
  }

  validateConfigItem(configItem, form) {
    for ( const key of Object.keys(configItem)) {
      if (configItem[key].itemValue === '2' && configItem[key].formName) {
        const formName = configItem[key].formName;
        configItem[key].isShow = true;
        form.controls[formName].setValidators([ZonValidators.required(configItem[key].description)]);
        form.controls[formName].updateValueAndValidity();
      }
    }
  }

}
