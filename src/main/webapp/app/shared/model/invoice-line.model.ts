import { type ILicense } from '@/shared/model/license.model';
import { type IInvoice } from '@/shared/model/invoice.model';

export interface IInvoiceLine {
  id?: number;
  totalAmount?: number;
  license?: ILicense | null;
  invoice?: IInvoice | null;
}

export class InvoiceLine implements IInvoiceLine {
  constructor(
    public id?: number,
    public totalAmount?: number,
    public license?: ILicense | null,
    public invoice?: IInvoice | null,
  ) {}
}
