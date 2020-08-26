import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { AccountManagerTestModule } from '../../../test.module';
import { MockEventManager } from '../../../helpers/mock-event-manager.service';
import { MockActiveModal } from '../../../helpers/mock-active-modal.service';
import { ClassificationRuleDeleteDialogComponent } from 'app/entities/classification-rule/classification-rule-delete-dialog.component';
import { ClassificationRuleService } from 'app/entities/classification-rule/classification-rule.service';

describe('Component Tests', () => {
  describe('ClassificationRule Management Delete Component', () => {
    let comp: ClassificationRuleDeleteDialogComponent;
    let fixture: ComponentFixture<ClassificationRuleDeleteDialogComponent>;
    let service: ClassificationRuleService;
    let mockEventManager: MockEventManager;
    let mockActiveModal: MockActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [AccountManagerTestModule],
        declarations: [ClassificationRuleDeleteDialogComponent],
      })
        .overrideTemplate(ClassificationRuleDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ClassificationRuleDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(ClassificationRuleService);
      mockEventManager = TestBed.get(JhiEventManager);
      mockActiveModal = TestBed.get(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.closeSpy).toHaveBeenCalled();
          expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
      });
    });
  });
});