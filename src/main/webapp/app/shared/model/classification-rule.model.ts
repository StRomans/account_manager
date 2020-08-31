import { IUser } from 'app/core/user/user.model';
import { IBankAccount } from 'app/shared/model/bank-account.model';
import { ITransaction } from 'app/shared/model/transaction.model';
import { IFilterRule } from 'app/shared/model/filter-rule.model';
import { ISubCategory } from 'app/shared/model/sub-category.model';

export interface IClassificationRule {
  id?: number;
  priority?: number;
  owner?: IUser;
  bankAccount?: IBankAccount;
  transactions?: ITransaction[];
  filterRules?: IFilterRule[];
  subCategory?: ISubCategory;
}

export class ClassificationRule implements IClassificationRule {
  constructor(
    public id?: number,
    public priority?: number,
    public owner?: IUser,
    public bankAccount?: IBankAccount,
    public transactions?: ITransaction[],
    public filterRules?: IFilterRule[],
    public subCategory?: ISubCategory
  ) {}
}
