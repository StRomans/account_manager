import { IClassificationRule } from 'app/shared/model/classification-rule.model';
import { RuleField } from 'app/shared/model/enumerations/rule-field.model';
import { RuleOperator } from 'app/shared/model/enumerations/rule-operator.model';

export interface IFilterRule {
  id?: number;
  field?: RuleField;
  operator?: RuleOperator;
  stringValue?: string;
  classificationRule?: IClassificationRule;
}

export class FilterRule implements IFilterRule {
  constructor(
    public id?: number,
    public field?: RuleField,
    public operator?: RuleOperator,
    public stringValue?: string,
    public classificationRule?: IClassificationRule
  ) {}
}
