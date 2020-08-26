import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { ClassificationRuleComponent } from './classification-rule.component';
import { ClassificationRuleDetailComponent } from './classification-rule-detail.component';
import { ClassificationRuleUpdateComponent } from './classification-rule-update.component';
import { ClassificationRuleDeleteDialogComponent } from './classification-rule-delete-dialog.component';
import { classificationRuleRoute } from './classification-rule.route';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(classificationRuleRoute)],
  declarations: [
    ClassificationRuleComponent,
    ClassificationRuleDetailComponent,
    ClassificationRuleUpdateComponent,
    ClassificationRuleDeleteDialogComponent,
  ],
  entryComponents: [ClassificationRuleDeleteDialogComponent],
})
export class AccountManagerClassificationRuleModule {}
