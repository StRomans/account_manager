import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IFilterRule, FilterRule } from 'app/shared/model/filter-rule.model';
import { FilterRuleService } from './filter-rule.service';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from 'app/entities/classification-rule/classification-rule.service';

@Component({
  selector: 'jhi-filter-rule-update',
  templateUrl: './filter-rule-update.component.html',
})
export class FilterRuleUpdateComponent implements OnInit {
  isSaving = false;
  classificationrules: IClassificationRule[] = [];

  editForm = this.fb.group({
    id: [],
    field: [null, [Validators.required]],
    operator: [null, [Validators.required]],
    stringValue: [null, [Validators.required]],
    classificationRule: [null, Validators.required],
  });

  constructor(
    protected filterRuleService: FilterRuleService,
    protected classificationRuleService: ClassificationRuleService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filterRule }) => {
      this.updateForm(filterRule);

      this.classificationRuleService
        .query()
        .subscribe((res: HttpResponse<IClassificationRule[]>) => (this.classificationrules = res.body || []));
    });
  }

  updateForm(filterRule: IFilterRule): void {
    this.editForm.patchValue({
      id: filterRule.id,
      field: filterRule.field,
      operator: filterRule.operator,
      stringValue: filterRule.stringValue,
      classificationRule: filterRule.classificationRule,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const filterRule = this.createFromForm();
    if (filterRule.id !== undefined) {
      this.subscribeToSaveResponse(this.filterRuleService.update(filterRule));
    } else {
      this.subscribeToSaveResponse(this.filterRuleService.create(filterRule));
    }
  }

  private createFromForm(): IFilterRule {
    return {
      ...new FilterRule(),
      id: this.editForm.get(['id'])!.value,
      field: this.editForm.get(['field'])!.value,
      operator: this.editForm.get(['operator'])!.value,
      stringValue: this.editForm.get(['stringValue'])!.value,
      classificationRule: this.editForm.get(['classificationRule'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFilterRule>>): void {
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

  trackById(index: number, item: IClassificationRule): any {
    return item.id;
  }
}
