import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClassificationRule, ClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from './classification-rule.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { IFilterRule } from '../../shared/model/filter-rule.model';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { FilterRuleDeleteDialogComponent } from '../filter-rule/filter-rule-delete-dialog.component';

type SelectableEntity = IUser | IBankAccount;

@Component({
  selector: 'jhi-classification-rule-update',
  templateUrl: './classification-rule-update.component.html',
})
export class ClassificationRuleUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  bankaccounts: IBankAccount[] = [];

  editForm = this.fb.group({
    id: [],
    owner: [null, Validators.required],
    bankAccount: [null, Validators.required],
    filterRules: [null, Validators.required],
  });

  constructor(
    protected classificationRuleService: ClassificationRuleService,
    protected userService: UserService,
    protected bankAccountService: BankAccountService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationRule }) => {
      this.updateForm(classificationRule);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankaccounts = res.body || []));
    });
  }

  updateForm(classificationRule: IClassificationRule): void {
    this.editForm.patchValue({
      id: classificationRule.id,
      owner: classificationRule.owner,
      bankAccount: classificationRule.bankAccount,
      filterRules: classificationRule.filterRules,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classificationRule = this.createFromForm();
    if (classificationRule.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationRuleService.update(classificationRule));
    } else {
      this.subscribeToSaveResponse(this.classificationRuleService.create(classificationRule));
    }
  }

  private createFromForm(): IClassificationRule {
    return {
      ...new ClassificationRule(),
      id: this.editForm.get(['id'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassificationRule>>): void {
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

  deleteRule(filterRule: IFilterRule): void {
    const modalRef = this.modalService.open(FilterRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.filterRule = filterRule;
    modalRef.result.then(() => this.removeRuleFromList(filterRule));
    this.ngOnInit();
  }

  removeRuleFromList(filterRule: IFilterRule): void {
    const index = this.editForm.get('filterRules')?.value.indexOf(filterRule);
    this.editForm.get('filterRules')?.value.splice(index, 1);
  }
}
