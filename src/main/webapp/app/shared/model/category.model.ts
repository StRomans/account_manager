import { IUser } from 'app/core/user/user.model';

export interface ICategory {
  id?: number;
  label?: string;
  color?: string;
  owner?: IUser;
}

export class Category implements ICategory {
  constructor(public id?: number, public label?: string, public color?: string, public owner?: IUser) {}
}
