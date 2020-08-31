import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { ClassificationRuleComponent } from './classification-rule.component';
import { ClassificationRuleDetailComponent } from './classification-rule-detail.component';
import { ClassificationRuleUpdateComponent } from './classification-rule-update.component';
import { ClassificationRuleDeleteDialogComponent } from './classification-rule-delete-dialog.component';
import { classificationRuleRoute } from './classification-rule.route';
import { ClassificationRuleResultDtoDialogComponent } from './dto/classification-rule-result-dto-dialog.component';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(classificationRuleRoute)],
  declarations: [
    ClassificationRuleComponent,
    ClassificationRuleDetailComponent,
    ClassificationRuleUpdateComponent,
    ClassificationRuleDeleteDialogComponent,
    ClassificationRuleResultDtoDialogComponent,
  ],
  entryComponents: [ClassificationRuleDeleteDialogComponent, ClassificationRuleResultDtoDialogComponent],
})
export class AccountManagerClassificationRuleModule {}
