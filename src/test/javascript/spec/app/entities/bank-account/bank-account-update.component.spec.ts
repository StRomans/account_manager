import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { BankAccountUpdateComponent } from 'app/entities/bank-account/bank-account-update.component';
import { BankAccountService } from 'app/entities/bank-account/bank-account.service';
import { BankAccount } from 'app/shared/model/bank-account.model';

describe('Component Tests', () => {
  describe('BankAccount Management Update Component', () => {
    let comp: BankAccountUpdateComponent;
    let fixture: ComponentFixture<BankAccountUpdateComponent>;
    let service: BankAccountService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [BankAccountUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(BankAccountUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BankAccountUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BankAccountService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new BankAccount(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new BankAccount();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
