import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AccountManagerTestModule } from '../../../test.module';
import { ClassificationRuleDetailComponent } from 'app/entities/classification-rule/classification-rule-detail.component';
import { ClassificationRule } from 'app/shared/model/classification-rule.model';

describe('Component Tests', () => {
  describe('ClassificationRule Management Detail Component', () => {
    let comp: ClassificationRuleDetailComponent;
    let fixture: ComponentFixture<ClassificationRuleDetailComponent>;
    const route = ({ data: of({ classificationRule: new ClassificationRule(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [ClassificationRuleDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }],
      })
        .overrideTemplate(ClassificationRuleDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassificationRuleDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load classificationRule on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.classificationRule).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
