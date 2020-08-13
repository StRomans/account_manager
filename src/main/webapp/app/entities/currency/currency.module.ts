import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { CurrencyComponent } from './currency.component';
import { CurrencyDetailComponent } from './currency-detail.component';
import { currencyRoute } from './currency.route';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(currencyRoute)],
  declarations: [CurrencyComponent, CurrencyDetailComponent],
})
export class AccountManagerCurrencyModule {}
