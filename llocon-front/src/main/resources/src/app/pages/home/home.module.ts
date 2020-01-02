import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { TranslateModule } from '@ngx-translate/core';

import { MaterialModule } from '../../shared/material.module';

import { HomeComponent } from './home.component';

@NgModule( {
    imports: [
        CommonModule,
        TranslateModule,
        MaterialModule,
        RouterModule.forChild( [
            { path: '', component: HomeComponent }
        ] )
    ],
    declarations: [
        HomeComponent
    ]
} )
export class HomeModule {}