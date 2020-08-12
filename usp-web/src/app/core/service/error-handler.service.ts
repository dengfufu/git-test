import {ErrorHandler, Injectable} from '@angular/core';
// import * as Sentry from '@sentry/browser';
// import {environment} from '@env/environment';
import {UserService} from '@core/user/user.service';

@Injectable({
  providedIn: 'root'
})
export class ErrorHandlerService implements ErrorHandler {

  constructor(private userService: UserService) {
    // if (environment.production) {
    //   Sentry.init({
    //     dsn: 'https://6b21068eb7174a1d8a4d796f27f63765@sentry.io/1836971',
    //     environment: environment.production ? 'prod' : 'dev'
    //   });
    // }
  }

  handleError(error) {
    // if (environment.production) {
    //   try {
    //     Sentry.configureScope(scope => {
    //       scope.setUser({
    //         userId: this.userService.userInfo.userId,
    //         username: this.userService.userInfo.username,
    //         mobile: this.userService.userInfo.mobile,
    //         email: this.userService.userInfo.email,
    //       });
    //     });
    //     Sentry.captureException(error.originalError || error);
    //   } catch (e) {
    //     console.error(e);
    //   }
    // } else {
    console.error(error);
    // }
  }
}
