import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { ClassificationRuleUpdateComponent } from 'app/entities/classification-rule/classification-rule-update.component';
import { ClassificationRuleService } from 'app/entities/classification-rule/classification-rule.service';
import { ClassificationRule } from 'app/shared/model/classification-rule.model';

describe('Component Tests', () => {
  describe('ClassificationRule Management Update Component', () => {
    let comp: ClassificationRuleUpdateComponent;
    let fixture: ComponentFixture<ClassificationRuleUpdateComponent>;
    let service: ClassificationRuleService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [ClassificationRuleUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(ClassificationRuleUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ClassificationRuleUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassificationRuleService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new ClassificationRule(123);
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
        const entity = new ClassificationRule();
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
