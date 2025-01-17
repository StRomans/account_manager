import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { SubCategoryDetailComponent } from 'app/entities/sub-category/sub-category-detail.component';
import { SubCategory } from 'app/shared/model/sub-category.model';

describe('Component Tests', () => {
  describe('SubCategory Management Detail Component', () => {
    let comp: SubCategoryDetailComponent;
    let fixture: ComponentFixture<SubCategoryDetailComponent>;
    const route = ({ data: of({ subCategory: new SubCategory(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [SubCategoryDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(SubCategoryDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(SubCategoryDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load subCategory on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.subCategory).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
