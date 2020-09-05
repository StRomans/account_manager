import { Component } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { AbstractControl, FormBuilder, Validators } from '@angular/forms';

import { IFilterRule, FilterRule } from 'app/shared/model/filter-rule.model';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { RuleField } from '../../shared/model/enumerations/rule-field.model';

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

  stringFields = [RuleField.LABEL];
  numberFields = [RuleField.AMOUNT, RuleField.DAY_OF_MONTH];
  dateFields = [RuleField.DATE];

  constructor(private fb: FormBuilder, public activeModal: NgbActiveModal) {}

  /**
   * Activate/Deactivate validators depending on selected 'Field' value
   */
  public refreshValidatorsOnFieldChange(): any {
    const selectedField = this.editForm.get('field')?.value;
    const stringControl = this.editForm.get('stringValue');
    const numberControl = this.editForm.get('numberValue');
    const dateControl = this.editForm.get('dateValue');

    if (!this.stringFields.includes(selectedField)) stringControl?.setValue(undefined);
    if (!this.numberFields.includes(selectedField)) numberControl?.setValue(undefined);
    if (!this.dateFields.includes(selectedField)) dateControl?.setValue(undefined);

    stringControl?.setValidators(null);
    numberControl?.setValidators(null);
    dateControl?.setValidators(null);

    if (this.numberFields.includes(selectedField)) numberControl?.setValidators([Validators.required]);
    else if (this.dateFields.includes(selectedField)) dateControl?.setValidators([Validators.required]);
    else if (this.stringFields.includes(selectedField)) stringControl?.setValidators([Validators.required]);

    stringControl?.updateValueAndValidity();
    numberControl?.updateValueAndValidity();
    dateControl?.updateValueAndValidity();
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
