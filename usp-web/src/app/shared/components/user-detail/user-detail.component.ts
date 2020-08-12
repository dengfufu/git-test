import {ChangeDetectorRef, Input, Component, OnInit} from '@angular/core';
import {FormBuilder} from '@angular/forms';
import {finalize} from 'rxjs/operators';
import {HttpClient} from '@angular/common/http';
import {FileService} from '@core/service/file.service';
import {UserService} from '@core/user/user.service';

@Component({
  selector: 'app-user-detail',
  templateUrl: 'user-detail.component.html',
  styleUrls: ['user-detail.less']
})
export class UserDetailComponent implements OnInit {

  @Input() userId;

  spinning = false;
  detail: any = {};
  avatarUrl = '';
  previewVisible = false;
  avatarUrlBig = '';
  previewImage: any;
  isSameCorp = false;

  constructor(private formBuilder: FormBuilder,
              private cdf: ChangeDetectorRef,
              private userService: UserService,
              private httpClient: HttpClient) {
  }

  ngOnInit(): void {
    this.queryDetail();
  }

  queryDetail() {
    if (!this.userId) {
      return;
    }
    this.spinning = true;
    this.httpClient
      .get('/api/uas/account/info/' + this.userId)
      .pipe(
        finalize(() => {
          this.spinning = false;
          this.cdf.markForCheck();
        })
      )
      .subscribe((res: any) => {
        this.detail = res.data;
        const corpIdList = this.detail.corpIdList || [];
        this.isSameCorp = corpIdList.includes(this.userService.currentCorp.corpId);
        if (res.data.faceImg && res.data.faceImg !== '0' && res.data.faceImgBig) {
          this.avatarUrl = FileService.BASE_URL + res.data.faceImg;
          this.avatarUrlBig = FileService.BASE_URL + res.data.faceImgBig;
        } else {
          this.avatarUrl = res.sex === '2' ? 'assets/user/female-portrait.svg' :
            (res.sex === '1' ? 'assets/user/male-portrait.svg' : 'assets/user/portrait.svg');
          this.avatarUrlBig = res.sex === '2' ? 'assets/user/female-portrait.svg' :
            (res.sex === '1' ? 'assets/user/male-portrait.svg' : 'assets/user/portrait.svg');
        }
      });
  }
}
