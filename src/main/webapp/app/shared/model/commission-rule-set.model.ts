export interface ICommissionRuleSet {
  id?: number;
  commissionRuleSetId?: string;
  ruleSetName?: string;
}

export class CommissionRuleSet implements ICommissionRuleSet {
  constructor(
    public id?: number,
    public commissionRuleSetId?: string,
    public ruleSetName?: string,
  ) {}
}
