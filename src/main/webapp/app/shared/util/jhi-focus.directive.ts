import { Directive, Input, ElementRef, OnChanges, SimpleChanges, AfterViewInit } from '@angular/core';

@Directive({
  selector: '[jhiFocus]',
})
export class JhiFocusDirective implements OnChanges, AfterViewInit {
  @Input()
  focus = true;

  constructor(private element: ElementRef) {}

  ngAfterViewInit(): void {
    if (this.focus) this.element.nativeElement.focus();
  }

  // eslint-disable-next-line @typescript-eslint/no-unused-vars
  public ngOnChanges(changes: SimpleChanges): void {
    if (this.focus) this.element.nativeElement.focus();
  }
}
