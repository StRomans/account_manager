import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { IClassificationRule, ClassificationRule } from 'app/shared/model/classification-rule.model';
import { ClassificationRuleService } from './classification-rule.service';
import { IUser } from 'app/core/user/user.model';
import { UserService } from 'app/core/user/user.service';

@Component({
  selector: 'jhi-classification-rule-update',
  templateUrl: './classification-rule-update.component.html',
})
export class ClassificationRuleUpdateComponent implements OnInit {
  isSaving = false;
  users: IUser[] = [];

  editForm = this.fb.group({
    id: [],
    owner: [null, Validators.required],
  });

  constructor(
    protected classificationRuleService: ClassificationRuleService,
    protected userService: UserService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ classificationRule }) => {
      this.updateForm(classificationRule);

      this.userService.query().subscribe((res: HttpResponse<IUser[]>) => (this.users = res.body || []));
    });
  }

  updateForm(classificationRule: IClassificationRule): void {
    this.editForm.patchValue({
      id: classificationRule.id,
      owner: classificationRule.owner,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const classificationRule = this.createFromForm();
    if (classificationRule.id !== undefined) {
      this.subscribeToSaveResponse(this.classificationRuleService.update(classificationRule));
    } else {
      this.subscribeToSaveResponse(this.classificationRuleService.create(classificationRule));
    }
  }

  private createFromForm(): IClassificationRule {
    return {
      ...new ClassificationRule(),
      id: this.editForm.get(['id'])!.value,
      owner: this.editForm.get(['owner'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IClassificationRule>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IUser): any {
    return item.id;
  }
}
