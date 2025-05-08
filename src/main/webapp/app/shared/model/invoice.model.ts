import { type IClient } from '@/shared/model/client.model';
import { type IPartner } from '@/shared/model/partner.model';

export interface IInvoice {
  id?: number;
  invoiceId?: string;
  clientId?: string;
  partnerId?: string;
  invoiceAmount?: number;
  invoiceDate?: Date;
  invoiceType?: string;
  client?: IClient | null;
  partner?: IPartner | null;
}

export class Invoice implements IInvoice {
  constructor(
    public id?: number,
    public invoiceId?: string,
    public clientId?: string,
    public partnerId?: string,
    public invoiceAmount?: number,
    public invoiceDate?: Date,
    public invoiceType?: string,
    public client?: IClient | null,
    public partner?: IPartner | null,
  ) {}
}
