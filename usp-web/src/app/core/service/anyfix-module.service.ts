import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {UserService} from '@core/user/user.service';


@Injectable({
  providedIn: 'root'
})
export class AnyfixModuleService {

  constructor(private httpClient: HttpClient,
              private userService: UserService) {
  }

  getWorkType() {
    return this.httpClient.post('/api/anyfix/work-type/list', {});
  }

  getDemanderOption() {
    return this.httpClient
      .get('/api/anyfix/demander/list');
  }

  getCustomOption() {
    return this.httpClient
      .get('/api/anyfix/demander-custom/custom/list/' + this.userService.currentCorp.corpId);
  }

  getDeviceSmallClassOption() {
    return this.httpClient
      .post('/api/device/device-small-class/list', {});
  }

  getDeviceModelOption() {
      return this.httpClient
        .post('/api/device/device-model/list', {});
  }

  getDeviceBrandOption() {
    return this.httpClient
      .post('/api/device/device-brand/list', {corpId: this.userService.currentCorp.corpId});
  }

  getDeviceSmallClassOptionByDemanderCorp(demanderCorp) {
    if(demanderCorp !== undefined&&demanderCorp !== null ){
      return this.httpClient
        .post('/api/device/device-small-class/list', {corp: demanderCorp});
    }
  }

  getDeviceModelOptionByDemanderCorp(demanderCorp) {
    if(demanderCorp !== undefined&&demanderCorp !== null ){
      return this.httpClient
        .post('/api/device/device-model/list', {corp: demanderCorp});
    }
  }

  getDeviceBrandOptionByDemanderCorp(demanderCorp) {
    if(demanderCorp !== undefined&&demanderCorp !== null ){
      return this.httpClient
        .post('/api/device/device-brand/list', {corp: demanderCorp});
    }
  }

  getWorkSourceOption() {
    return this.httpClient
      .get('/api/anyfix/work-source/list');
  }

  getAreaInfoOption() {
    return this.httpClient
      .get('/api/uas/area/list');
  }

  getProvinceOption() {
    return this.httpClient
      .get('/api/uas/area/province/list');
  }

  getAtmCaseOption() {
    return this.httpClient.post('/api/anyfix/work-request/findAtmCaseOption', {});
  }

  getEngineerListOption(branch, userName) {
    return this.httpClient.post('/api/anyfix/service-branch-user/list', {branchId: branch, userName});
  }

  getWorkRemindType() {
    return this.httpClient.post('/api/anyfix/work-remind/listWorkRemindType', {});
  }
}
