import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ICurrency, Currency } from 'app/shared/model/currency.model';
import { CurrencyService } from './currency.service';
import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';

@Injectable({ providedIn: 'root' })
export class CurrencyResolve implements Resolve<ICurrency> {
  constructor(private service: CurrencyService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICurrency> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((currency: HttpResponse<Currency>) => {
          if (currency.body) {
            return of(currency.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Currency());
  }
}

export const currencyRoute: Routes = [
  {
    path: '',
    component: CurrencyComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.currency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CurrencyDetailComponent,
    resolve: {
      currency: CurrencyResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.currency.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
