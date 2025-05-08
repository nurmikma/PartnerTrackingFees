import { type IPartner } from '@/shared/model/partner.model';

export interface IClient {
  id?: number;
  clientId?: string;
  clientName?: string;
  partnerId?: string;
  partner?: IPartner | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public clientId?: string,
    public clientName?: string,
    public partnerId?: string,
    public partner?: IPartner | null,
  ) {}
}
