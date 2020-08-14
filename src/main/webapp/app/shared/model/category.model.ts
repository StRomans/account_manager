export interface ICategory {
  id?: number;
  label?: string;
  color?: string;
}

export class Category implements ICategory {
  constructor(public id?: number, public label?: string, public color?: string) {}
}
