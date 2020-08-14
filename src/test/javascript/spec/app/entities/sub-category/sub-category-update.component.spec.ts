import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { SubCategoryUpdateComponent } from 'app/entities/sub-category/sub-category-update.component';
import { SubCategoryService } from 'app/entities/sub-category/sub-category.service';
import { SubCategory } from 'app/shared/model/sub-category.model';

describe('Component Tests', () => {
  describe('SubCategory Management Update Component', () => {
    let comp: SubCategoryUpdateComponent;
    let fixture: ComponentFixture<SubCategoryUpdateComponent>;
    let service: SubCategoryService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [SubCategoryUpdateComponent],
        providers: [FormBuilder],
      })
        .overrideTemplate(SubCategoryUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SubCategoryUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(SubCategoryService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new SubCategory(123);
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
        const entity = new SubCategory();
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
