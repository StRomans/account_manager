import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { FilterRuleDetailComponent } from 'app/entities/filter-rule/filter-rule-detail.component';
import { FilterRule } from 'app/shared/model/filter-rule.model';

describe('Component Tests', () => {
  describe('FilterRule Management Detail Component', () => {
    let comp: FilterRuleDetailComponent;
    let fixture: ComponentFixture<FilterRuleDetailComponent>;
    const route = ({ data: of({ filterRule: new FilterRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [FilterRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(FilterRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(FilterRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load filterRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.filterRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
