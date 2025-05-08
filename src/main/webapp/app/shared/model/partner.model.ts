export interface IPartner {
  id?: number;
  partnerID?: string;
  partnerName?: string;
}

export class Partner implements IPartner {
  constructor(
    public id?: number,
    public partnerID?: string,
    public partnerName?: string,
  ) {}
}
