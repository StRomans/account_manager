import { ICurrency } from 'app/shared/model/currency.model';
import { IUser } from 'app/core/user/user.model';

export interface IBankAccount {
  id?: number;
  label?: string;
  currency?: ICurrency;
  owner?: IUser;
}

export class BankAccount implements IBankAccount {
  constructor(public id?: number, public label?: string, public currency?: ICurrency, public owner?: IUser) {}
}
