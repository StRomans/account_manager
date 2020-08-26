import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFilterRule } from 'app/shared/model/filter-rule.model';

@Component({
  selector: 'jhi-filter-rule-detail',
  templateUrl: './filter-rule-detail.component.html',
})
export class FilterRuleDetailComponent implements OnInit {
  filterRule: IFilterRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ filterRule }) => (this.filterRule = filterRule));
  }

  previousState(): void {
    window.history.back();
  }
}
