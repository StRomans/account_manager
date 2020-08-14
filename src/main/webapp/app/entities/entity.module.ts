import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'bank-account',
        loadChildren: () => import('./bank-account/bank-account.module').then(m => m.AccountManagerBankAccountModule),
      },
      {
        path: 'currency',
        loadChildren: () => import('./currency/currency.module').then(m => m.AccountManagerCurrencyModule),
      },
      {
        path: 'category',
        loadChildren: () => import('./category/category.module').then(m => m.AccountManagerCategoryModule),
      },
      {
        path: 'sub-category',
        loadChildren: () => import('./sub-category/sub-category.module').then(m => m.AccountManagerSubCategoryModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class AccountManagerEntityModule {}
