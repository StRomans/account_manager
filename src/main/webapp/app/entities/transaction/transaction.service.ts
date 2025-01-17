import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as moment from 'moment';

import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared/util/request-util';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IBankAccount } from '../../shared/model/bank-account.model';
import { IUploadTransactionResultDto } from '../../shared/model/dto/upload-transaction-result.dto';
import { IClassificationRule } from '../../shared/model/classification-rule.model';
import { IClassificationRuleResultDto } from '../../shared/model/dto/classification-rule-result-dto';
import { ClassificationRuleService } from '../classification-rule/classification-rule.service';

type EntityResponseType = HttpResponse<ITransaction>;
type EntityArrayResponseType = HttpResponse<ITransaction[]>;

@Injectable({ providedIn: 'root' })
export class TransactionService {
  public resourceUrl = SERVER_API_URL + 'api/transactions';

  constructor(protected http: HttpClient, protected classificationRuleService: ClassificationRuleService) {}

  create(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .post<ITransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(transaction: ITransaction): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(transaction);
    return this.http
      .put<ITransaction>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<ITransaction>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<ITransaction[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  upload(file: File, bankAccount: IBankAccount): Observable<HttpResponse<IUploadTransactionResultDto>> {
    const formData: FormData = new FormData();
    formData.append('file', file, file.name);
    formData.append('bankAccountId', JSON.stringify(bankAccount.id));
    return this.http.post<IUploadTransactionResultDto>(`${this.resourceUrl}/upload`, formData, { observe: 'response' });
  }

  public estimateClassification(classificationRule: IClassificationRule): Observable<HttpResponse<IClassificationRuleResultDto>> {
    const copy: IClassificationRule = this.classificationRuleService.convertDateFromClient(classificationRule);
    return this.http.post<IClassificationRuleResultDto>(`${this.resourceUrl}/classification/estimate`, copy, { observe: 'response' });
  }

  protected convertDateFromClient(transaction: ITransaction): ITransaction {
    const copy: ITransaction = Object.assign({}, transaction, {
      date: transaction.date && transaction.date.isValid() ? transaction.date.format(DATE_FORMAT) : undefined,
    });
    return copy;
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.date = res.body.date ? moment(res.body.date) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((transaction: ITransaction) => {
        transaction.date = transaction.date ? moment(transaction.date) : undefined;
      });
    }
    return res;
  }
}
