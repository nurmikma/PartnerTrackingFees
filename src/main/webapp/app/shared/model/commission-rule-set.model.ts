export interface ICommissionRuleSet {
  id?: number;
  ruleSetName?: string;
}

export class CommissionRuleSet implements ICommissionRuleSet {
  constructor(
    public id?: number,
    public ruleSetName?: string,
  ) {}
}
