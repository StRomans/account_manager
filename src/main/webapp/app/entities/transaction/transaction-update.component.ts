import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ITransaction, Transaction } from 'app/shared/model/transaction.model';
import { TransactionService } from './transaction.service';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { ISubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from 'app/entities/sub-category/sub-category.service';

type SelectableEntity = IBankAccount | ISubCategory;

@Component({
  selector: 'jhi-transaction-update',
  templateUrl: './transaction-update.component.html',
})
export class TransactionUpdateComponent implements OnInit {
  isSaving = false;
  bankaccounts: IBankAccount[] = [];
  subcategories: ISubCategory[] = [];
  dateDp: any;

  editForm = this.fb.group({
    id: [],
    date: [null, [Validators.required]],
    amount: [null, [Validators.required]],
    label: [null, [Validators.required]],
    identifier: [],
    bankAccount: [null, Validators.required],
    subCategory: [],
  });

  constructor(
    protected transactionService: TransactionService,
    protected bankAccountService: BankAccountService,
    protected subCategoryService: SubCategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ transaction }) => {
      this.updateForm(transaction);

      this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankaccounts = res.body || []));

      this.subCategoryService.query().subscribe((res: HttpResponse<ISubCategory[]>) => (this.subcategories = res.body || []));
    });
  }

  updateForm(transaction: ITransaction): void {
    this.editForm.patchValue({
      id: transaction.id,
      date: transaction.date,
      amount: transaction.amount,
      label: transaction.label,
      identifier: transaction.identifier,
      bankAccount: transaction.bankAccount,
      subCategory: transaction.subCategory,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const transaction = this.createFromForm();
    if (transaction.id !== undefined) {
      this.subscribeToSaveResponse(this.transactionService.update(transaction));
    } else {
      this.subscribeToSaveResponse(this.transactionService.create(transaction));
    }
  }

  private createFromForm(): ITransaction {
    return {
      ...new Transaction(),
      id: this.editForm.get(['id'])!.value,
      date: this.editForm.get(['date'])!.value,
      amount: this.editForm.get(['amount'])!.value,
      label: this.editForm.get(['label'])!.value,
      identifier: this.editForm.get(['identifier'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
      subCategory: this.editForm.get(['subCategory'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ITransaction>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: SelectableEntity): any {
    return item.id;
  }
}
