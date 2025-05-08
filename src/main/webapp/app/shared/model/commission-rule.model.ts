import { type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';

export interface ICommissionRule {
  id?: number;
  ruleName?: string;
  description?: string;
  startDay?: number;
  endDay?: number | null;
  commissionPercentage?: number;
  commissionRuleSet?: ICommissionRuleSet | null;
}

export class CommissionRule implements ICommissionRule {
  constructor(
    public id?: number,
    public ruleName?: string,
    public description?: string,
    public startDay?: number,
    public endDay?: number | null,
    public commissionPercentage?: number,
    public commissionRuleSet?: ICommissionRuleSet | null,
  ) {}
}
