import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';

import { MaterialModule } from '../../shared/material.module';
import { RegistreRoutingModule } from './registre.routing';
import { CreateComponent } from './create.component';
import { RecoverComponent } from './recover.component';
import { ValidateComponent } from './validate.component';

import { RecaptchaModule } from 'ng-recaptcha';

@NgModule( {
    imports: [
		RecaptchaModule.forRoot(),
        CommonModule,
		FormsModule,
		ReactiveFormsModule,
        TranslateModule,
        MaterialModule,
        RegistreRoutingModule
    ],
    declarations: [
        CreateComponent,
        RecoverComponent,
        ValidateComponent
    ]
} )
export class RegistreModule {}