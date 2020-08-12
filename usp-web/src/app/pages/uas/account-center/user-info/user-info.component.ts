import {Component, OnInit} from '@angular/core';
import {FormBuilder, FormGroup} from '@angular/forms';
import {UserService} from '@core/user/user.service';
import {FileService} from '@core/service/file.service';
import {HttpClient} from '@angular/common/http';
import {Result} from '@core/interceptor/result';
import {NzMessageService, UploadFile, UploadXHRArgs} from 'ng-zorro-antd';
import {ZonValidators} from '@util/zon-validators';
import {environment} from '@env/environment';
import {forkJoin, Observable} from 'rxjs';
import {finalize} from 'rxjs/operators';
import {StartupService} from '@core/startup/startup.service';

/**
 * 个人资料
 */
@Component({
  selector: 'app-user-info',
  templateUrl: 'user-info.component.html',
  styleUrls: ['user-info.component.less']
})
export class UserInfoComponent implements OnInit {

  form: FormGroup;
  isLoading: boolean;
  uploadLoading: boolean;
  faceImg: string;
  faceImgBig: string;
  roleName: string | undefined = '';

  constructor(public formBuilder: FormBuilder,
              public fileService: FileService,
              public httpClient: HttpClient,
              public startupService: StartupService,
              public msg: NzMessageService,
              public userService: UserService) {
    this.form = formBuilder.group({
      nickname: [userService.userInfo.nickname, [ZonValidators.required(), ZonValidators.maxLength(20)]], // 昵称
      // logonId: [userService.userInfo.logonId, [ZonValidators.required(), ZonValidators.maxLength(20)]], // 登录名
      sex: [userService.userInfo.sex, [ZonValidators.required()]], // 性别
      // email: [userService.userInfo.email, [ZonValidators.required(), ZonValidators.isEmail()]], // 我的邮箱
      signature: [userService.userInfo.signature, [ZonValidators.maxLength(30)]], // 个性签名
      faceImg: [userService.userInfo.faceImgBig, []] // 头像
    });
  }

  ngOnInit() {
    this.getRoleUser(this.userService.userInfo.userId);
  }

  submit() {
    this.isLoading = true;
    const param = {
      ...this.form.value,
      userId: this.userService.userInfo.userId
    };
    const info$ = this.httpClient.post('/api/uas/account/update', {
      nickname: param.nickname,
      sex: param.sex,
      // email: param.email,
      signature: param.signature,
      userId: param.userId,
    });
    // const logonId$ = this.httpClient.post('/api/uas/account/updateLoginId', {
    //   logonId: param.logonId,
    //   userId: param.userId,
    // });
    // const email$ = this.httpClient.post('/api/uas/account/updateMail', {emailAddress: param.email});
    forkJoin([info$])
      .pipe(finalize(() => {
        this.isLoading = false;
      }))
      .subscribe((results: any) => {
        this.userService.initUserInfo().then(() => {
          this.msg.success('修改成功');
        });
      });
  }

  getRoleUser(userId?: string) {
    this.httpClient
      .get('/api/uas/sys-role/list/user/' + userId)
      .pipe(
        finalize(() => {
        })
      )
      .subscribe((res: any) => {
        console.log(res);
        let name = '';
        res.data.forEach(item => {
          name = name + item.roleName + ',';
        });
        name = name.substr(0, name.length - 1);
        this.roleName = name;
      });
  }

  beforeUpload = (file: UploadFile): boolean => {
    this.uploadLoading = true;
    this.fileService.uploadFaceImg(file).pipe(finalize(() => {
      this.uploadLoading = false;
    })).subscribe((dataJson: any) => {
      this.faceImgBig = dataJson[0].fileId;
      this.faceImg = dataJson[1].fileId;
      this.form.patchValue({
        faceImg: this.faceImgBig
      });
      this.httpClient.post('/api/uas/account/face', {
        faceImg: this.faceImg,
        faceImgBig: this.faceImgBig
      }).subscribe((res: any) => {
        this.userService.initUserInfo().then();
      });
    });
    return false;
  };

  getFaceImgUrl() {
    return this.userService.userInfo.faceImgUrl;
  }
}
