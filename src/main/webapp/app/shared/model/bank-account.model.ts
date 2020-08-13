import { ICurrency } from 'app/shared/model/currency.model';

export interface IBankAccount {
  id?: number;
  label?: string;
  currency?: ICurrency;
}

export class BankAccount implements IBankAccount {
  constructor(public id?: number, public label?: string, public currency?: ICurrency) {}
}
