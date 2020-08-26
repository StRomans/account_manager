import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { FilterRuleUpdateComponent } from 'app/entities/filter-rule/filter-rule-update.component';
import { FilterRuleService } from 'app/entities/filter-rule/filter-rule.service';
import { FilterRule } from 'app/shared/model/filter-rule.model';

describe('Component Tests', () => {
  describe('FilterRule Management Update Component', () => {
    let comp: FilterRuleUpdateComponent;
    let fixture: ComponentFixture<FilterRuleUpdateComponent>;
    let service: FilterRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [FilterRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(FilterRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(FilterRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(FilterRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new FilterRule(123);
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
        const entity = new FilterRule();
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
