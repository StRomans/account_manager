import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import * as moment from 'moment';
import { IFilterRule } from '../../shared/model/filter-rule.model';
import { FilterRuleService } from '../filter-rule/filter-rule.service';
import { map } from 'rxjs/operators';

type EntityResponseType = HttpResponse<IClassificationRule>;
type EntityArrayResponseType = HttpResponse<IClassificationRule[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationRuleService {
  public resourceUrl = SERVER_API_URL + 'api/classification-rules';

  constructor(protected http: HttpClient, protected filterRuleService: FilterRuleService) {}

  create(classificationRule: IClassificationRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classificationRule);
    return this.http
      .post<IClassificationRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(classificationRule: IClassificationRule): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(classificationRule);
    return this.http
      .put<IClassificationRule>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IClassificationRule>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IClassificationRule[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  protected convertDateFromClient(classificationRule: IClassificationRule): IClassificationRule {
    if (classificationRule.filterRules) {
      classificationRule.filterRules.forEach((filterRule: IFilterRule, index, filterRules) => {
        filterRules[index] = this.filterRuleService.convertDateFromClient(filterRule);
      });

      const copy: IClassificationRule = Object.assign({}, classificationRule, {});
      return copy;
    } else {
      return classificationRule;
    }
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.filterRules?.forEach((filterRule: IFilterRule) => {
        filterRule.dateValue = filterRule.dateValue ? moment(filterRule.dateValue) : undefined;
      });
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((classificationRule: IClassificationRule) => {
        classificationRule?.filterRules?.forEach((filterRule: IFilterRule) => {
          filterRule.dateValue = filterRule.dateValue ? moment(filterRule.dateValue) : undefined;
        });
      });
    }
    return res;
  }
}
