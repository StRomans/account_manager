import { NgModule } from '@angular/core';
import { AccountManagerSharedLibsModule } from './shared-libs.module';
import { FindLanguageFromKeyPipe } from './language/find-language-from-key.pipe';
import { AlertComponent } from './alert/alert.component';
import { AlertErrorComponent } from './alert/alert-error.component';
import { LoginModalComponent } from './login/login.component';
import { HasAnyAuthorityDirective } from './auth/has-any-authority.directive';
import { ColorPickerComponent } from 'app/shared/color-picker/color-picker.component';

@NgModule({
  imports: [AccountManagerSharedLibsModule],
  declarations: [
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    ColorPickerComponent,
  ],
  entryComponents: [LoginModalComponent],
  exports: [
    AccountManagerSharedLibsModule,
    FindLanguageFromKeyPipe,
    AlertComponent,
    AlertErrorComponent,
    LoginModalComponent,
    HasAnyAuthorityDirective,
    ColorPickerComponent,
  ],
})
export class AccountManagerSharedModule {}
