import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from './classification-rule.service';

@Component({
  templateUrl: './classification-rule-delete-dialog.component.html',
})
export class ClassificationRuleDeleteDialogComponent {
  classificationRule?: IClassificationRule;

  constructor(
    protected classificationRuleService: ClassificationRuleService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.classificationRuleService.delete(id).subscribe(() => {
      this.eventManager.broadcast('classificationRuleListModification');
      this.activeModal.close();
    });
  }
}
