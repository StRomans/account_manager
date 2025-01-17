import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { TransactionDetailComponent } from 'app/entities/transaction/transaction-detail.component';
import { Transaction } from 'app/shared/model/transaction.model';

describe('Component Tests', () => {
  describe('Transaction Management Detail Component', () => {
    let comp: TransactionDetailComponent;
    let fixture: ComponentFixture<TransactionDetailComponent>;
    const route = ({ data: of({ transaction: new Transaction(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [TransactionDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(TransactionDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TransactionDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load transaction on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.transaction).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
