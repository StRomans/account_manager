import { Component, ElementRef, forwardRef, HostListener, Input, ViewChild } from '@angular/core';
import { ControlValueAccessor, NG_VALUE_ACCESSOR } from '@angular/forms';

@Component({
  // source : https://stackoverflow.com/questions/45004293/multiple-datepicker-in-one-form-angular2-with-difference-component
  selector: 'jhi-date-picker',
  template: `<div class="input-group">
    <input
      class="form-control form-control-sm"
      [placeholder]="placeholder"
      [name]="name"
      [(ngModel)]="model"
      (ngModelChange)="onModelChange()"
      ngbDatepicker
      #dp="ngbDatepicker"
    />
    <span class="input-group-append">
      <button type="button" class="btn btn-outline-secondary btn-sm" (click)="dp.toggle()"><fa-icon icon="calendar-alt"></fa-icon></button>
    </span>
  </div>`,
  providers: [
    {
      provide: NG_VALUE_ACCESSOR,
      // eslint-disable-next-line @typescript-eslint/no-use-before-define
      useExisting: forwardRef(() => JhiDatePickerComponent),
      multi: true,
    },
  ],
})
export class JhiDatePickerComponent implements ControlValueAccessor {
  @Input()
  name = '';
  @Input()
  model: any;
  @Input()
  placeholder = 'yyy-mm-dd';
  @ViewChild('dp')
  dp: any;

  constructor(private _eref: ElementRef) {}

  private propagateChange: any = () => {};

  @HostListener('document:click', ['$event'])
  onClick(event: Event): any {
    if (this._eref.nativeElement.contains(event.target)) return true;
    setTimeout(() => this.dp.close(), 10);
  }

  onModelChange(): void {
    this.propagateChange(this.model);
  }

  writeValue(value: any): void {
    this.model = value;
  }

  registerOnChange(fn: any): void {
    this.propagateChange = fn;
  }

  registerOnTouched(): void {}
}
