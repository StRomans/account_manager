import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'rendering',
        loadChildren: () => import('app/rendering/savings/savings.module').then(m => m.AccountManagerSavingsModule),
      },
    ]),
  ],
})
export class AccountManagerRenderingModule {}
