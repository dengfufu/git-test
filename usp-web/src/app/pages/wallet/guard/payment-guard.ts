import {Injectable} from '@angular/core';
import {CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, CanActivateChild} from
    '@angular/router';
import {Router} from '@angular/router';
import {Observable, of} from 'rxjs';
import {PayService, WalletAccountType} from '../service/pay.service';
import {map, tap} from 'rxjs/operators';

@Injectable()
export class PaymentGuard implements CanActivate, CanActivateChild {
  constructor(private payService: PayService,
              private router: Router) {
  }
  
  private process(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    const redirectUrl = state.url;
    const type = WalletAccountType.ENTERPRISE;
    return this.checkModule(type, redirectUrl);
  }
  
  private checkModule(type, redirectUrl): Observable<boolean> {
    let role;
    return this.payService.checkAccountAndProtocol().pipe(
      map(v => {
        role = v.role;
        return v.role === 'passed';
      }),
      tap(v => {
        if (!v) {
          const params = {
            role,
            type,
            redirectUrl
          };
          this.router.navigate(['/wallet/account/check'], {
            queryParams: params
          });
        }
      })
    );
  }
  
  canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.process(route, state);
  }
  
  canActivateChild(childRoute: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<boolean> {
    return this.process(childRoute, state);
  }
}
