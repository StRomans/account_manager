import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Component } from '@angular/core';
import { ITransaction } from '../../../shared/model/transaction.model';

@Component({
  templateUrl: './classification-rule-result-dto-dialog.component.html',
})
export class ClassificationRuleResultDtoDialogComponent {
  unclassifiedTransactions: ITransaction[] = [];

  constructor(public activeModal: NgbActiveModal) {
    this.unclassifiedTransactions = [];
  }

  trackById(index: number, item: ITransaction): any {
    return item.id;
  }

  close(): void {
    this.activeModal.close();
  }
}
