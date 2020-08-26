import { Moment } from 'moment';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { ISubCategory } from 'app/shared/model/sub-category.model';
import { IClassificationRule } from 'app/shared/model/classification-rule.model';

export interface ITransaction {
  id?: number;
  date?: Moment;
  amount?: number;
  label?: string;
  identifier?: string;
  bankAccount?: IBankAccount;
  subCategory?: ISubCategory;
  classificationRule?: IClassificationRule;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public date?: Moment,
    public amount?: number,
    public label?: string,
    public identifier?: string,
    public bankAccount?: IBankAccount,
    public subCategory?: ISubCategory,
    public classificationRule?: IClassificationRule
  ) {}
}
