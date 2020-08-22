import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Routes, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { flatMap } from 'rxjs/operators';

import { Authority } from 'app/shared/constants/authority.constants';
import { UserRouteAccessService } from 'app/core/auth/user-route-access-service';
import { ISubCategory, SubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from './sub-category.service';
import { SubCategoryComponent } from './sub-category.component';
import { SubCategoryDetailComponent } from './sub-category-detail.component';
import { SubCategoryUpdateComponent } from './sub-category-update.component';

@Injectable({ providedIn: 'root' })
export class SubCategoryResolve implements Resolve<ISubCategory> {
  constructor(private service: SubCategoryService, private router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubCategory> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        flatMap((subCategory: HttpResponse<SubCategory>) => {
          if (subCategory.body) {
            return of(subCategory.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubCategory());
  }
}

export const subCategoryRoute: Routes = [
  {
    path: '',
    component: SubCategoryComponent,
    data: {
      authorities: [Authority.USER],
      defaultSort: 'label,asc',
      pageTitle: 'accountManagerApp.subCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubCategoryDetailComponent,
    resolve: {
      subCategory: SubCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.subCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubCategoryUpdateComponent,
    resolve: {
      subCategory: SubCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.subCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubCategoryUpdateComponent,
    resolve: {
      subCategory: SubCategoryResolve,
    },
    data: {
      authorities: [Authority.USER],
      pageTitle: 'accountManagerApp.subCategory.home.title',
    },
    canActivate: [UserRouteAccessService],
  },
];
