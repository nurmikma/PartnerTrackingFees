import { type ILicense } from '@/shared/model/license.model';

export interface ICommissionFee {
  id?: number;
  commissionAmount?: number;
  license?: ILicense | null;
}

export class CommissionFee implements ICommissionFee {
  constructor(
    public id?: number,
    public commissionAmount?: number,
    public license?: ILicense | null,
  ) {}
}
