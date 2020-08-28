import { Component } from '@angular/core';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';

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
    stringValue: [null, [Validators.required]],
  });

  constructor(private fb: FormBuilder, public activeModal: NgbActiveModal) {}

  updateForm(filterRule: IFilterRule): void {
    this.editForm.patchValue({
      id: filterRule.id,
      field: filterRule.field,
      operator: filterRule.operator,
      stringValue: filterRule.stringValue,
    });
  }

  cancel(): void {
    this.activeModal.dismiss();
  }

  close(): void {
    this.activeModal.close(this.createFromForm());
  }

  private createFromForm(): IFilterRule {
    return {
      ...new FilterRule(),
      id: this.editForm.get(['id'])!.value,
      field: this.editForm.get(['field'])!.value,
      operator: this.editForm.get(['operator'])!.value,
      stringValue: this.editForm.get(['stringValue'])!.value,
    };
  }

  trackById(index: number, item: IClassificationRule): any {
    return item.id;
  }
}
