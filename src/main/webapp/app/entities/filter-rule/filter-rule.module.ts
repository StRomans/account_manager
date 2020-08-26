import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { AccountManagerSharedModule } from 'app/shared/shared.module';
import { FilterRuleComponent } from './filter-rule.component';
import { FilterRuleDetailComponent } from './filter-rule-detail.component';
import { FilterRuleUpdateComponent } from './filter-rule-update.component';
import { FilterRuleDeleteDialogComponent } from './filter-rule-delete-dialog.component';
import { filterRuleRoute } from './filter-rule.route';

@NgModule({
  imports: [AccountManagerSharedModule, RouterModule.forChild(filterRuleRoute)],
  declarations: [FilterRuleComponent, FilterRuleDetailComponent, FilterRuleUpdateComponent, FilterRuleDeleteDialogComponent],
  entryComponents: [FilterRuleDeleteDialogComponent],
})
export class AccountManagerFilterRuleModule {}
