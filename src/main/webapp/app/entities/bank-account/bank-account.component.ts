import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from './bank-account.service';
import { BankAccountDeleteDialogComponent } from './bank-account-delete-dialog.component';

@Component({
  selector: 'jhi-bank-account',
  templateUrl: './bank-account.component.html',
})
export class BankAccountComponent implements OnInit, OnDestroy {
  bankAccounts?: IBankAccount[];
  eventSubscriber?: Subscription;
  currentSearch = '';

  constructor(
    protected bankAccountService: BankAccountService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    if (this.currentSearch) {
      this.bankAccountService
        .query({ 'label.contains': this.currentSearch })
        .subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankAccounts = res.body || []));
    } else {
      this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankAccounts = res.body || []));
    }
  }

  search(query: string): void {
    this.currentSearch = query;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInBankAccounts();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IBankAccount): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInBankAccounts(): void {
    this.eventSubscriber = this.eventManager.subscribe('bankAccountListModification', () => this.loadAll());
  }

  delete(bankAccount: IBankAccount): void {
    const modalRef = this.modalService.open(BankAccountDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.bankAccount = bankAccount;
  }
}
