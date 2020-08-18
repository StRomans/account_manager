import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IBankAccount, BankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from './bank-account.service';
import { ICurrency } from 'app/shared/model/currency.model';
import { CurrencyService } from 'app/entities/currency/currency.service';
import { IUser, User } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

type SelectableEntity = ICurrency | IUser;

@Component({
  selector: 'jhi-bank-account-update',
  templateUrl: './bank-account-update.component.html',
})
export class BankAccountUpdateComponent implements OnInit {
  isSaving = false;
  currencies: ICurrency[] = [];
  users: IUser[] = [];
  defaultOwner: IUser = new User();

  editForm = this.fb.group({
    id: [],
    label: [null, [Validators.required]],
    currency: [null, Validators.required],
    owner: [null, null],
  });

  constructor(
    protected bankAccountService: BankAccountService,
    protected currencyService: CurrencyService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ bankAccount }) => {
      this.updateForm(bankAccount);
      this.currencyService.query().subscribe((res: HttpResponse<ICurrency[]>) => (this.currencies = res.body || []));
      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(bankAccount: IBankAccount): void {
    this.editForm.patchValue({
      id: bankAccount.id,
      label: bankAccount.label,
      currency: bankAccount.currency,
      owner: bankAccount.owner,
    });
    if (!bankAccount.owner) {
      this.userService.currentUser().subscribe((user: IUser) => this.editForm.patchValue({ owner: user }));
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const bankAccount = this.createFromForm();
    if (bankAccount.id !== undefined) {
      this.subscribeToSaveResponse(this.bankAccountService.update(bankAccount));
    } else {
      this.subscribeToSaveResponse(this.bankAccountService.create(bankAccount));
    }
  }

  private createFromForm(): IBankAccount {
    return {
      ...new BankAccount(),
      id: this.editForm.get(['id'])!.value,
      label: this.editForm.get(['label'])!.value,
      currency: this.editForm.get(['currency'])!.value,
      owner: this.editForm.get(['owner'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBankAccount>>): void {
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
