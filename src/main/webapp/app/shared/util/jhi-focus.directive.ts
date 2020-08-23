import { Directive, Input, ElementRef, OnChanges, SimpleChanges, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[jhiFocus]',
})
export class JhiFocusDirective implements OnChanges, AfterViewInit {
  @Input()
  jhiFocus = true;

  constructor(private element: ElementRef) {}

  ngAfterViewInit(): void {
    if (this.jhiFocus) this.element.nativeElement.focus();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  public ngOnChanges(changes: SimpleChanges): void {
    if (this.jhiFocus) this.element.nativeElement.focus();
  }
}
