export interface ICurrency {
  id?: number;
  label?: string;
  code?: string;
}

export class Currency implements ICurrency {
  constructor(public id?: number, public label?: string, public code?: string) {}
}
