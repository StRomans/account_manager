import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IFilterRule } from 'app/shared/model/filter-rule.model';
import { DATE_FORMAT } from '../../shared/constants/input.constants';
import * as moment from 'moment';

type EntityResponseType = HttpResponse<IFilterRule>;
type EntityArrayResponseType = HttpResponse<IFilterRule[]>;

@Injectable({ providedIn: 'root' })
export class FilterRuleService {
  public resourceUrl = SERVER_API_URL + 'api/filter-rules';

  constructor(protected http: HttpClient) {}

  create(filterRule: IFilterRule): Observable<EntityResponseType> {
    return this.http.post<IFilterRule>(this.resourceUrl, filterRule, { observe: 'response' });
  }

  update(filterRule: IFilterRule): Observable<EntityResponseType> {
    return this.http.put<IFilterRule>(this.resourceUrl, filterRule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IFilterRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IFilterRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  public convertDateFromClient(filterRule: IFilterRule): IFilterRule {
    const copy: IFilterRule = Object.assign({}, filterRule, {
      dateValue: filterRule.dateValue && filterRule.dateValue.isValid() ? filterRule.dateValue.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  public convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.dateValue = res.body.dateValue ? moment(res.body.dateValue) : undefined;
    }
    return res;
  }

  public convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((filterRule: IFilterRule) => {
        filterRule.dateValue = filterRule.dateValue ? moment(filterRule.dateValue) : undefined;
      });
    }
    return res;
  }
}
