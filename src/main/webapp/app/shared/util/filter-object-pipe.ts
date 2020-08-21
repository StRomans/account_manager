import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'filterObject',
  pure: false,
})
export class FilterObjectPipe implements PipeTransform {
  transform(items: any[], filterProperty: string, filterValue: Object): any {
    if (!items || items.length === 0 || !filterProperty || !filterValue) {
      return items;
    }
    // filter items array, items which match and return true will be
    // kept, false will be filtered out
    return items.filter(item => this.tryFilteringThroughProperties(item, filterProperty) === filterValue);
  }

  /**
   *  Return objet property
   *  Ex: return value for item.prop1.prop2.prop3
   * @param item the object to explore
   * @param filterProperty the properties dot separated prop1.prop2.prop3 ...
   */
  tryFilteringThroughProperties(item: any, filterProperty: string): any {
    const propertyHierarchy: string[] = filterProperty.split('.');
    while (propertyHierarchy.length && item) {
      const currentProperty: string = propertyHierarchy[0];
      propertyHierarchy.shift();
      item = item[currentProperty];
    }
    return item;
  }
}
