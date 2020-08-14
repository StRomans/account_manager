import { Moment } from 'moment';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { ISubCategory } from 'app/shared/model/sub-category.model';

export interface ITransaction {
  id?: number;
  date?: Moment;
  amount?: number;
  label?: string;
  identifier?: string;
  bankAccount?: IBankAccount;
  subCategory?: ISubCategory;
}

export class Transaction implements ITransaction {
  constructor(
    public id?: number,
    public date?: Moment,
    public amount?: number,
    public label?: string,
    public identifier?: string,
    public bankAccount?: IBankAccount,
    public subCategory?: ISubCategory
  ) {}
}
