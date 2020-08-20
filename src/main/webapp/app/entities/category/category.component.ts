import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from './category.service';
import { CategoryDeleteDialogComponent } from './category-delete-dialog.component';

@Component({
  selector: 'jhi-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.scss'],
})
export class CategoryComponent implements OnInit, OnDestroy {
  categories?: ICategory[];
  eventSubscriber?: Subscription;
  labelSearch = '';
  fullCriteria = {};

  constructor(protected categoryService: CategoryService, protected eventManager: JhiEventManager, protected modalService: NgbModal) {}

  loadAll(): void {
    this.categoryService.query(this.fullCriteria).subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
  }

  search(jpaFilter: string, query: string): void {
    if (!jpaFilter) {
      this.fullCriteria = {};
    } else {
      if (query) {
        this.fullCriteria[jpaFilter] = query;
      } else {
        delete this.fullCriteria[jpaFilter];
      }
    }
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInCategories();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: ICategory): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInCategories(): void {
    this.eventSubscriber = this.eventManager.subscribe('categoryListModification', () => this.loadAll());
  }

  delete(category: ICategory): void {
    const modalRef = this.modalService.open(CategoryDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.category = category;
  }
}
