import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TransactionService } from './transaction.service';
import { Component } from '@angular/core';
import { IBankAccount } from '../../shared/model/bank-account.model';
import { ITransaction } from '../../shared/model/transaction.model';
import { UploadTransactionResultDto } from '../../shared/model/dto/upload-transaction-result.dto';

@Component({
  templateUrl: './transaction-upload-dialog.component.html',
  styleUrls: ['./transaction-upload-dialog.component.scss'],
})
export class TransactionUploadDialogComponent {
  bankAccounts: IBankAccount[] = [];
  selectedBankAccount!: IBankAccount;
  selectedFile!: File;
  ignoredTransactions: ITransaction[] = [];
  savedTransactions: ITransaction[] = [];
  isProcessing = false;
  isComplete = false;

  constructor(
    protected transactionService: TransactionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {
    this.ignoredTransactions = [];
    this.savedTransactions = [];
    this.isProcessing = false;
    this.isComplete = false;
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  upload(): void {
    this.isProcessing = true;
    this.transactionService.upload(this.selectedFile, this.selectedBankAccount).subscribe(data => {
      this.isProcessing = false;
      this.isComplete = true;
      const uploadResult = data.body || new UploadTransactionResultDto([], []);
      this.ignoredTransactions = uploadResult.ignoredTransactions;
      this.savedTransactions = uploadResult.savedTransactions;
    });
  }

  trackById(index: number, item: IBankAccount): any {
    return item.id;
  }

  onFileSelected(event: Event): void {
    if (event && event.target) {
      this.selectedFile = event.target['files'].item(0);
    }
  }

  close(): void {
    this.eventManager.broadcast('transactionListModification');
    this.activeModal.close();
  }
}
