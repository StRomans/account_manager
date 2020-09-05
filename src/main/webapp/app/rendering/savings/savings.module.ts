import { NgModule } from '@angular/core';

import { RouterModule } from '@angular/router';
import { savingsRoute } from './savings-route.module';

import { AccountManagerSharedModule } from '../../shared/shared.module';
import { SavingsLineChartComponent } from './savings-line-chart.component';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(savingsRoute)],
  declarations: [SavingsLineChartComponent],
  entryComponents: [],
})
export class AccountManagerSavingsModule {}
