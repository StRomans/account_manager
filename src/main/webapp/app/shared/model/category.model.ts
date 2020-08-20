import { IUser } from 'app/core/user/user.model';

export interface ICategory {
  id?: number;
  label?: string;
  primaryColor?: string;
  secondaryColor?: string;
  owner?: IUser;
}

export class Category implements ICategory {
  constructor(
    public id?: number,
    public label?: string,
    public primaryColor?: string,
    public secondaryColor?: string,
    public owner?: IUser
  ) {}
}
