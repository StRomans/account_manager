import { ITransaction } from '../transaction.model';

export interface IClassificationRuleResultDto {
  transactionsToClassify: ITransaction[];
}

export class ClassificationRuleResultDto implements IClassificationRuleResultDto {
  constructor(public transactionsToClassify: ITransaction[]) {
    this.transactionsToClassify = transactionsToClassify || [];
  }
}
