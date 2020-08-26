import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';

type EntityResponseType = HttpResponse<IClassificationRule>;
type EntityArrayResponseType = HttpResponse<IClassificationRule[]>;

@Injectable({ providedIn: 'root' })
export class ClassificationRuleService {
  public resourceUrl = SERVER_API_URL + 'api/classification-rules';

  constructor(protected http: HttpClient) {}

  create(classificationRule: IClassificationRule): Observable<EntityResponseType> {
    return this.http.post<IClassificationRule>(this.resourceUrl, classificationRule, { observe: 'response' });
  }

  update(classificationRule: IClassificationRule): Observable<EntityResponseType> {
    return this.http.put<IClassificationRule>(this.resourceUrl, classificationRule, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IClassificationRule>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IClassificationRule[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }
}
