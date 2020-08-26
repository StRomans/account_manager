import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFilterRule } from 'app/shared/model/filter-rule.model';
import { FilterRuleService } from './filter-rule.service';

@Component({
  templateUrl: './filter-rule-delete-dialog.component.html',
})
export class FilterRuleDeleteDialogComponent {
  filterRule?: IFilterRule;

  constructor(
    protected filterRuleService: FilterRuleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.filterRuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('filterRuleListModification');
      this.activeModal.close();
    });
  }
}
