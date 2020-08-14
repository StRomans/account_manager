import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISubCategory, SubCategory } from 'app/shared/model/sub-category.model';
import { SubCategoryService } from './sub-category.service';
import { ICategory } from 'app/shared/model/category.model';
import { CategoryService } from 'app/entities/category/category.service';

@Component({
  selector: 'jhi-sub-category-update',
  templateUrl: './sub-category-update.component.html',
})
export class SubCategoryUpdateComponent implements OnInit {
  isSaving = false;
  categories: ICategory[] = [];

  editForm = this.fb.group({
    id: [],
    label: [null, [Validators.required]],
    category: [null, Validators.required],
  });

  constructor(
    protected subCategoryService: SubCategoryService,
    protected categoryService: CategoryService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subCategory }) => {
      this.updateForm(subCategory);

      this.categoryService.query().subscribe((res: HttpResponse<ICategory[]>) => (this.categories = res.body || []));
    });
  }

  updateForm(subCategory: ISubCategory): void {
    this.editForm.patchValue({
      id: subCategory.id,
      label: subCategory.label,
      category: subCategory.category,
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subCategory = this.createFromForm();
    if (subCategory.id !== undefined) {
      this.subscribeToSaveResponse(this.subCategoryService.update(subCategory));
    } else {
      this.subscribeToSaveResponse(this.subCategoryService.create(subCategory));
    }
  }

  private createFromForm(): ISubCategory {
    return {
      ...new SubCategory(),
      id: this.editForm.get(['id'])!.value,
      label: this.editForm.get(['label'])!.value,
      category: this.editForm.get(['category'])!.value,
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubCategory>>): void {
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

  trackById(index: number, item: ICategory): any {
    return item.id;
  }
}
