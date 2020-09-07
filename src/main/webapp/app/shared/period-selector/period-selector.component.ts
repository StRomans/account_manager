import { Component, OnInit } from '@angular/core';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'jhi-period-selector',
  templateUrl: './period-selector.component.html',
})
export class JhiPeriodSelectorComponent implements OnInit {
  bankaccounts: IBankAccount[] = [];

  selectorDisplayValue = '2013';

  constructor(protected bankAccountService: BankAccountService) {}

  ngOnInit(): void {
    this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankaccounts = res.body || []));
  }

  trackById(index: number, item: IBankAccount): any {
    return item.id;
  }
}
