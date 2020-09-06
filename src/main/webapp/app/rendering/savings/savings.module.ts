import { NgModule } from '@angular/core';

import { RouterModule } from '@angular/router';
import { savingsRoute } from './savings-route.module';

import { AccountManagerSharedModule } from '../../shared/shared.module';
import { SavingsLineChartComponent } from './savings-line-chart.component';
import { NgxEchartsModule } from 'ngx-echarts';

import * as echarts from 'echarts';
// import 'echarts/lib/theme/dark.js';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(savingsRoute), NgxEchartsModule.forRoot({ echarts })],
  declarations: [SavingsLineChartComponent],
  entryComponents: [],
})
export class AccountManagerSavingsModule {}
