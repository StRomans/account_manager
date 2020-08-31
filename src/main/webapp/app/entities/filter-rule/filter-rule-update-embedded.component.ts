import { Component } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';

import { IFilterRule, FilterRule } from 'app/shared/model/filter-rule.model';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  templateUrl: './filter-rule-update-embedded.component.html',
})
export class FilterRuleUpdateEmbeddedComponent {
  editForm = this.fb.group({
    id: [],
    field: [null, [Validators.required]],
    operator: [null, [Validators.required]],
    stringValue: [null, null],
    numberValue: [null, null],
    dateValue: [null, null],
  });

  constructor(private fb: FormBuilder, public activeModal: NgbActiveModal) {}

  /**
   * Activate/Deactivate validators depending on selected 'Field' value
   */
  public refreshValidatorsOnFieldChange(): any {
    if (this.editForm.get('field')?.value !== 'LABEL') this.editForm.get('stringValue')?.setValue(null);
    if (this.editForm.get('field')?.value !== 'AMOUNT') this.editForm.get('numberValue')?.setValue(null);
    if (this.editForm.get('field')?.value !== 'DATE') this.editForm.get('dateValue')?.setValue(null);

    this.editForm.get('stringValue')?.setValidators(null);
    this.editForm.get('numberValue')?.setValidators(null);
    this.editForm.get('dateValue')?.setValidators(null);

    if (this.editForm.get('field')?.value === 'AMOUNT') this.editForm.get('numberValue')?.setValidators([Validators.required]);
    else if (this.editForm.get('field')?.value === 'DATE') this.editForm.get('dateValue')?.setValidators([Validators.required]);
    else if (this.editForm.get('field')?.value === 'LABEL') this.editForm.get('stringValue')?.setValidators([Validators.required]);

    this.editForm.controls.stringValue.updateValueAndValidity();
    this.editForm.controls.numberValue.updateValueAndValidity();
    this.editForm.controls.dateValue.updateValueAndValidity();
  }

  updateForm(filterRule: IFilterRule): void {
    this.editForm.patchValue({
      id: filterRule.id,
      field: filterRule.field,
      operator: filterRule.operator,
      stringValue: filterRule.stringValue,
      numberValue: filterRule.numberValue,
      dateValue: filterRule.dateValue,
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  close(): void {
    this.refreshValidatorsOnFieldChange();
    if (this.editForm.valid) {
      this.activeModal.close(this.createFromForm());
    }
  }

  private createFromForm(): IFilterRule {
    return {
      ...new FilterRule(),
      id: this.editForm.get(['id'])!.value,
      field: this.editForm.get(['field'])!.value,
      operator: this.editForm.get(['operator'])!.value,
      stringValue: this.editForm.get(['stringValue'])?.value,
      numberValue: this.editForm.get(['numberValue'])?.value,
      dateValue: this.editForm.get(['dateValue'])?.value,
    };
  }

  trackById(index: number, item: IClassificationRule): any {
    return item.id;
  }
}
