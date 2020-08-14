import { ICategory } from 'app/shared/model/category.model';

export interface ISubCategory {
  id?: number;
  label?: string;
  category?: ICategory;
}

export class SubCategory implements ISubCategory {
  constructor(public id?: number, public label?: string, public category?: ICategory) {}
}
