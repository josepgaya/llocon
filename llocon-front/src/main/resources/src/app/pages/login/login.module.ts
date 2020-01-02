import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { MaterialModule } from '../../shared/material.module';
import { LoginComponent } from './login.component';

@NgModule( {
    imports: [
        CommonModule,
		FormsModule,
		ReactiveFormsModule,
        TranslateModule,
        MaterialModule,
        RouterModule.forChild( [
            { path: '', component: LoginComponent }
        ] )
    ],
    declarations: [
        LoginComponent
    ]
} )
export class LoginModule {}