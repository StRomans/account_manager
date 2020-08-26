import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormArray, FormBuilder, FormControl, FormGroup, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClassificationRule, ClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from './classification-rule.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { RuleField } from '../../shared/model/enumerations/rule-field.model';
import { FilterRule } from '../../shared/model/filter-rule.model';

type SelectableEntity = IUser | IBankAccount;

@Component({
  selector: 'jhi-classification-rule-update',
  templateUrl: './classification-rule-update.component.html',
})
export class ClassificationRuleUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  bankaccounts: IBankAccount[] = [];
  availableFields: string[] = Object.keys(RuleField);
  rules: any;

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
      filterRules: this.editForm.get(['filterRules'])?.value,
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

  /**
   * Update current Rule
   * @param event
   * @param rule
   */
  changeRule(event: any, rule: any): void {
    rule[event.target?.name] = event.target?.value;
  }

  /**
   * Generate new line in Form
   * @param event
   */
  addRule(event: MouseEvent): void {
    const newRule = new FilterRule();
    newRule.stringValue = '';
    this.editForm.get('filterRules')?.value.push(newRule);
    event.preventDefault();
    event.stopPropagation();
  }

  removeRule(event: MouseEvent, rule: number): void {
    const index = this.editForm.get('filterRules')?.value.indexOf(rule);
    this.editForm.get('filterRules')?.value.splice(index, 1);
  }
}
