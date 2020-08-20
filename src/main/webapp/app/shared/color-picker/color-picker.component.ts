import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
  selector: 'jhi-app-color-picker',
  templateUrl: './color-picker.component.html',
  styleUrls: ['./color-picker.component.scss'],
})
export class ColorPickerComponent {
  @Input()
  heading = '';
  @Input()
  color = '#000000';
  @Output() event: EventEmitter<string> = new EventEmitter<string>();

  public show = false;
  /* 7 rows, 4 columns */
  public defaultColors: string[] = [
    /* column 1 */
    '#7BDFF2',
    '#B2F7EF',
    '#EFF7F6',
    '#F7D6E0',
    '#F2B5D4',
    /* column 2 */
    '#FF595E',
    '#FFCA3A',
    '#8AC926',
    '#1982C4',
    '#6A4C93',
    /* column 3 */
    '#011627',
    '#fdfffc',
    '#2ec4b6',
    '#e71d36',
    '#ff9f1c',
    /* column 4 */
    '#390099',
    '#9e0059',
    '#ff0054',
    '#ff5400',
    '#ffbd00',
  ];

  constructor() {}

  /**
   * Change color from default colors
   * @param {string} color
   */
  public changeColor(color: string): void {
    this.color = color;
    this.event.emit(this.color);
    this.show = false;
  }

  /**
   * Change color from input
   * @param {string} color
   */
  public changeColorManual(color: string): void {
    const isValid = /(^#[0-9A-F]{6}$)|(^#[0-9A-F]{3}$)/i.test(color);

    if (isValid) {
      this.color = color;
      this.event.emit(this.color);
    }
  }

  /**
   * Change status of visibility to color picker
   */
  public toggleColors(): void {
    this.show = !this.show;
  }
}
