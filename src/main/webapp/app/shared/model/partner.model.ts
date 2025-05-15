export interface IPartner {
  id?: number;
  partnerName?: string;
}

export class Partner implements IPartner {
  constructor(
    public id?: number,
    public partnerName?: string,
  ) {}
}
