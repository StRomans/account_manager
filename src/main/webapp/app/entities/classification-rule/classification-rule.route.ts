import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { IClassificationRule, ClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from './classification-rule.service';
import { ClassificationRuleComponent } from './classification-rule.component';
import { ClassificationRuleDetailComponent } from './classification-rule-detail.component';
import { ClassificationRuleUpdateComponent } from './classification-rule-update.component';

@Injectable({ providedIn: 'root' })
export class ClassificationRuleResolve implements Resolve<IClassificationRule> {
  constructor(private service: ClassificationRuleService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IClassificationRule> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((classificationRule: HttpResponse<ClassificationRule>) => {
          if (classificationRule.body) {
            return of(classificationRule.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new ClassificationRule());
  }
}

export const classificationRuleRoute: Routes = [
  {
    path: '',
    component: ClassificationRuleComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.classificationRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ClassificationRuleDetailComponent,
    resolve: {
      classificationRule: ClassificationRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.classificationRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ClassificationRuleUpdateComponent,
    resolve: {
      classificationRule: ClassificationRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.classificationRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ClassificationRuleUpdateComponent,
    resolve: {
      classificationRule: ClassificationRuleResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.classificationRule.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
