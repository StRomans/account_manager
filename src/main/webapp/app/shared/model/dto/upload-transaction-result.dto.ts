import { ITransaction } from '../transaction.model';

export interface IUploadTransactionResultDto {
  ignoredTransactions: ITransaction[];
  savedTransactions: ITransaction[];
}

export class UploadTransactionResultDto implements IUploadTransactionResultDto {
  constructor(public ignoredTransactions: ITransaction[], public savedTransactions: ITransaction[]) {
    this.ignoredTransactions = ignoredTransactions || [];
    this.savedTransactions = savedTransactions || [];
  }
}
