import { JhiEventManager } from 'ng-jhipster';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { TransactionService } from './transaction.service';
import { Component } from '@angular/core';
import { IBankAccount } from '../../shared/model/bank-account.model';

@Component({
  templateUrl: './transaction-upload-dialog.component.html',
})
export class TransactionUploadDialogComponent {
  bankAccounts: IBankAccount[] = [];
  selectedBankAccount?: IBankAccount;
  selectedFile!: File;

  constructor(
    protected transactionService: TransactionService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  upload(): void {
    this.transactionService.upload(this.selectedFile).subscribe(() => {
      this.eventManager.broadcast('transactionListModification');
      this.activeModal.close();
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
}
