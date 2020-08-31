import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';
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
import { FilterRuleUpdateEmbeddedComponent } from '../filter-rule/filter-rule-update-embedded.component';
import { ISubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from 'app/entities/sub-category/sub-category.service';
import { ICategory } from '../../shared/model/category.model';
import { CategoryService } from '../category/category.service';

type SelectableEntity = IUser | IBankAccount | ISubCategory | ICategory;

@Component({
  selector: 'jhi-classification-rule-update',
  templateUrl: './classification-rule-update.component.html',
})
export class ClassificationRuleUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];
  bankaccounts: IBankAccount[] = [];
  subcategories: ISubCategory[] = [];
  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    priority: [null, [Validators.required, Validators.min(1), Validators.max(100)]],
    owner: [null, null],
    bankAccount: [null, Validators.required],
    filterRules: [null, ClassificationRuleUpdateComponent.filterRulesRequired],
    subCategory: [null, Validators.required],
  });

  constructor(
    protected classificationRuleService: ClassificationRuleService,
    protected userService: UserService,
    protected bankAccountService: BankAccountService,
    protected subCategoryService: SubCategoryService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    protected modalService: NgbModal,
    private fb: FormBuilder
  ) {}

  /**
   * Make sure filterRules are required
   * @param control filterRules form control
   */
  public static filterRulesRequired(control: AbstractControl): any {
    return control.value?.length > 0 ? null : { required: false };
  }

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationRule }) => {
      this.updateForm(classificationRule);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));

      this.bankAccountService.query().subscribe((res: HttpResponse<IBankAccount[]>) => (this.bankaccounts = res.body || []));

      this.subCategoryService.query().subscribe((res: HttpResponse<ISubCategory[]>) => (this.subcategories = res.body || []));

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(classificationRule: IClassificationRule): void {
    this.editForm.patchValue({
      id: classificationRule.id,
      priority: classificationRule.priority,
      owner: classificationRule.bankAccount?.owner,
      bankAccount: classificationRule.bankAccount,
      filterRules: classificationRule.filterRules || [],
      subCategory: classificationRule.subCategory,
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
      priority: this.editForm.get(['priority'])!.value,
      owner: this.editForm.get(['owner'])!.value,
      bankAccount: this.editForm.get(['bankAccount'])!.value,
      filterRules: this.editForm.get(['filterRules'])!.value,
      subCategory: this.editForm.get(['subCategory'])!.value,
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
   * Start Editing session with : opening modal an initializing form
   * @param filterRule the filterRule to edit
   */
  editFilerRule(filterRule: IFilterRule): void {
    const modalRef = this.modalService.open(FilterRuleUpdateEmbeddedComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.updateForm(filterRule);
    modalRef.result.then(
      // on close
      resultFilterRule => this.updateEditedFilterRule(filterRule, resultFilterRule),
      // on dismiss
      () => {}
    );
  }

  /**
   * Close Editing session with : Update filterRule list according to editing result
   * @param originalfilterRule the filterRule before editing
   * @param editedFilterRule  the filterRule after editing
   */
  updateEditedFilterRule(originalfilterRule: IFilterRule, editedFilterRule: IFilterRule): void {
    const allFilterRules = this.editForm.get('filterRules')?.value;
    const index = allFilterRules.indexOf(originalfilterRule);

    allFilterRules[index] = editedFilterRule;
    this.editForm.patchValue({ filterRules: allFilterRules });
    this.editForm.controls.filterRules.updateValueAndValidity();
  }

  /**
   * Open modal with empty Filter Rule Form
   */
  addFilterRule(): void {
    const modalRef = this.modalService.open(FilterRuleUpdateEmbeddedComponent, { size: 'lg', backdrop: 'static' });
    modalRef.result.then(
      // on close
      filterRule => this.appendCreatedFilterRule(filterRule),
      // on dismiss
      () => {}
    );
  }

  /**
   * Append created filter Rule
   * @param createdFilterRule the filter Rule to append to the list
   */
  appendCreatedFilterRule(createdFilterRule: IFilterRule): void {
    this.editForm.get('filterRules')?.value.push(createdFilterRule);
    this.editForm.controls.filterRules.updateValueAndValidity();
  }

  /**
   * Remove selected Filter Rule from list
   * @param filterRule the filter Rule to remove
   */
  removeFilterRule(filterRule: IFilterRule): void {
    const index = this.editForm.get('filterRules')?.value.indexOf(filterRule);
    this.editForm.get('filterRules')?.value.splice(index, 1);
    this.editForm.controls.filterRules.updateValueAndValidity();
  }

  setOwnerFromBankAccount(bankAccount: IBankAccount): void {
    this.editForm.patchValue({ owner: bankAccount?.owner });
  }
}
