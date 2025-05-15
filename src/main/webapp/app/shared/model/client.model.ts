import { type IPartner } from '@/shared/model/partner.model';

export interface IClient {
  id?: number;
  clientName?: string;
  partner?: IPartner | null;
}

export class Client implements IClient {
  constructor(
    public id?: number,
    public clientName?: string,
    public partner?: IPartner | null,
  ) {}
}
