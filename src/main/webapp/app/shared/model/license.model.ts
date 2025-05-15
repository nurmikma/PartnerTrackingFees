import { type IClient } from '@/shared/model/client.model';
import { type IPartner } from '@/shared/model/partner.model';
import { type ICommissionRuleSet } from '@/shared/model/commission-rule-set.model';

export interface ILicense {
  id?: number;
  licenseRuleName?: string;
  licenseStartDate?: Date;
  licenseEndDate?: Date | null;
  licenseQuantity?: number;
  pricePerLicense?: number;
  totalLicenseAmount?: number | null;
  client?: IClient | null;
  partner?: IPartner | null;
  commissionRuleSet?: ICommissionRuleSet | null;
}

export class License implements ILicense {
  constructor(
    public id?: number,
    public licenseRuleName?: string,
    public licenseStartDate?: Date,
    public licenseEndDate?: Date | null,
    public licenseQuantity?: number,
    public pricePerLicense?: number,
    public totalLicenseAmount?: number | null,
    public client?: IClient | null,
    public partner?: IPartner | null,
    public commissionRuleSet?: ICommissionRuleSet | null,
  ) {}
}
