import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IFilterRule, FilterRule } from 'app/shared/model/filter-rule.model';
import { FilterRuleService } from './filter-rule.service';
import { FilterRuleComponent } from './filter-rule.component';
import { FilterRuleDetailComponent } from './filter-rule-detail.component';
import { FilterRuleUpdateComponent } from './filter-rule-update.component';

@Injectable({ providedIn: 'root' })
export class FilterRuleResolve implements Resolve<IFilterRule> {
  constructor(private service: FilterRuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IFilterRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((filterRule: HttpResponse<FilterRule>) => {
          if (filterRule.body) {
            return of(filterRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new FilterRule());
  }
}

export const filterRuleRoute: Routes = [
  {
    path: '',
    component: FilterRuleComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'id,asc',
      pageTitle: 'accountManagerApp.filterRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: FilterRuleDetailComponent,
    resolve: {
      filterRule: FilterRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.filterRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: FilterRuleUpdateComponent,
    resolve: {
      filterRule: FilterRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.filterRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: FilterRuleUpdateComponent,
    resolve: {
      filterRule: FilterRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.filterRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
