import { Component, OnInit, OnDestroy } from '@angular/core';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { Subscription } from 'rxjs';
import { JhiEventManager, JhiParseLinks } from 'ng-jhipster';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IClassificationRule } from 'app/shared/model/classification-rule.model';

import { ITEMS_PER_PAGE } from 'app/shared/constants/pagination.constants';
import { ClassificationRuleService } from './classification-rule.service';
import { ClassificationRuleDeleteDialogComponent } from './classification-rule-delete-dialog.component';

@Component({
  selector: 'jhi-classification-rule',
  templateUrl: './classification-rule.component.html',
})
export class ClassificationRuleComponent implements OnInit, OnDestroy {
  classificationRules: IClassificationRule[];
  eventSubscriber?: Subscription;
  itemsPerPage: number;
  links: any;
  page: number;
  predicate: string;
  ascending: boolean;

  constructor(
    protected classificationRuleService: ClassificationRuleService,
    protected eventManager: JhiEventManager,
    protected modalService: NgbModal,
    protected parseLinks: JhiParseLinks
  ) {
    this.classificationRules = [];
    this.itemsPerPage = ITEMS_PER_PAGE;
    this.page = 0;
    this.links = {
      last: 0,
    };
    this.predicate = 'id';
    this.ascending = true;
  }

  loadAll(): void {
    this.classificationRuleService
      .query({
        page: this.page,
        size: this.itemsPerPage,
        sort: this.sort(),
      })
      .subscribe((res: HttpResponse<IClassificationRule[]>) => this.paginateClassificationRules(res.body, res.headers));
  }

  reset(): void {
    this.page = 0;
    this.classificationRules = [];
    this.loadAll();
  }

  loadPage(page: number): void {
    this.page = page;
    this.loadAll();
  }

  ngOnInit(): void {
    this.loadAll();
    this.registerChangeInClassificationRules();
  }

  ngOnDestroy(): void {
    if (this.eventSubscriber) {
      this.eventManager.destroy(this.eventSubscriber);
    }
  }

  trackId(index: number, item: IClassificationRule): number {
    // eslint-disable-next-line @typescript-eslint/no-unnecessary-type-assertion
    return item.id!;
  }

  registerChangeInClassificationRules(): void {
    this.eventSubscriber = this.eventManager.subscribe('classificationRuleListModification', () => this.reset());
  }

  delete(classificationRule: IClassificationRule): void {
    const modalRef = this.modalService.open(ClassificationRuleDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.classificationRule = classificationRule;
  }

  sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? 'asc' : 'desc')];
    if (this.predicate !== 'id') {
      result.push('id');
    }
    return result;
  }

  protected paginateClassificationRules(data: IClassificationRule[] | null, headers: HttpHeaders): void {
    const headersLink = headers.get('link');
    this.links = this.parseLinks.parse(headersLink ? headersLink : '');
    if (data) {
      for (let i = 0; i < data.length; i++) {
        this.classificationRules.push(data[i]);
      }
    }
  }
}
