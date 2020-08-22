import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { TransactionComponent } from './transaction.component';
import { TransactionDetailComponent } from './transaction-detail.component';
import { TransactionUpdateComponent } from './transaction-update.component';
import { TransactionDeleteDialogComponent } from './transaction-delete-dialog.component';
import { TransactionUploadDialogComponent } from './transaction-upload-dialog.component';
import { transactionRoute } from './transaction.route';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(transactionRoute)],
  declarations: [
    TransactionComponent,
    TransactionDetailComponent,
    TransactionUpdateComponent,
    TransactionDeleteDialogComponent,
    TransactionUploadDialogComponent,
  ],
  entryComponents: [TransactionDeleteDialogComponent, TransactionUploadDialogComponent],
})
export class AccountManagerTransactionModule {}
