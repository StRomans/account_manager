import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from './sub-category.service';
import { SubCategoryDeleteDialogComponent } from './sub-category-delete-dialog.component';

@Component({
  selector: 'jhi-sub-category',
  templateUrl: './sub-category.component.html',
})
export class SubCategoryComponent implements OnInit, OnDestroy {
  subCategories?: ISubCategory[];
  eventSubscriber?: Subscription;

  constructor(
    protected subCategoryService: SubCategoryService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal
  ) {}

  loadAll(): void {
    this.subCategoryService.query().subscribe((res: HttpResponse<ISubCategory[]>) => (this.subCategories = res.body || []));
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInSubCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ISubCategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInSubCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('subCategoryListModification', () => this.loadAll());
  }

  delete(subCategory: ISubCategory): void {
    const modalRef = this.modalService.open(SubCategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.subCategory = subCategory;
  }
}
