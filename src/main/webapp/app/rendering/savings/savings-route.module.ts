import { Routes } from '@angular/router';
import { Authority } from '../../shared/constants/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access-service';
import { SavingsLineChartComponent } from './savings-line-chart.component';

export const savingsRoute: Routes = [
  {
    path: 'savings',
    component: SavingsLineChartComponent,
    data: {
      authorities: [Authority.USER],
      pageTitle: 'global.menu.data.analyse.savings',
    },
    canActivate: [UserRouteAccessService],
  },
];
