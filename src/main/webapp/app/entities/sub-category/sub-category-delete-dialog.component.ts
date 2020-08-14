import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { ISubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from './sub-category.service';

@Component({
  templateUrl: './sub-category-delete-dialog.component.html',
})
export class SubCategoryDeleteDialogComponent {
  subCategory?: ISubCategory;

  constructor(
    protected subCategoryService: SubCategoryService,
    public activeModal: NgbActiveModal,
    protected eventManager: JhiEventManager
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subCategoryService.delete(id).subscribe(() => {
      this.eventManager.broadcast('subCategoryListModification');
      this.activeModal.close();
    });
  }
}
