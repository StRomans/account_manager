import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IClassificationRule } from 'app/shared/model/classification-rule.model';

@Component({
  selector: 'jhi-classification-rule-detail',
  templateUrl: './classification-rule-detail.component.html',
})
export class ClassificationRuleDetailComponent implements OnInit {
  classificationRule: IClassificationRule | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationRule }) => (this.classificationRule = classificationRule));
  }

  previousState(): void {
    window.history.back();
  }
}
