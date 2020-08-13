import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { CurrencyDetailComponent } from 'app/entities/currency/currency-detail.component';
import { Currency } from 'app/shared/model/currency.model';

describe('Component Tests', () => {
  describe('Currency Management Detail Component', () => {
    let comp: CurrencyDetailComponent;
    let fixture: ComponentFixture<CurrencyDetailComponent>;
    const route = ({ data: of({ currency: new Currency(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [CurrencyDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(CurrencyDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CurrencyDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load currency on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.currency).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
