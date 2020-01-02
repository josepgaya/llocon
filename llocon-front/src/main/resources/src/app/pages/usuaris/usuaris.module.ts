import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { TranslateModule } from '@ngx-translate/core';
import { BngModule, BngFormExitGuard } from 'base-angular';

import { MaterialModule } from '../../shared/material.module';

import { UsuarisGridComponent } from './usuaris-grid.component';
import { UsuarisFormComponent } from './usuaris-form.component';
import { UsuarisContrasenyaFieldComponent, UsuarisContrasenyaDialog } from './usuaris-contrasenya-field.component';
import { UsuarisService } from './usuaris.service';

@NgModule({
	imports: [
		CommonModule,
		FormsModule,
		ReactiveFormsModule,
		TranslateModule,
		BngModule,
		MaterialModule,
		RouterModule.forChild([
			{ path: '', component: UsuarisGridComponent },
			{ path: 'create', component: UsuarisFormComponent, canDeactivate: [BngFormExitGuard] },
			{ path: 'update/:id', component: UsuarisFormComponent, canDeactivate: [BngFormExitGuard] }
		])
	],
	declarations: [
		UsuarisGridComponent,
		UsuarisFormComponent,
		UsuarisContrasenyaFieldComponent,
		UsuarisContrasenyaDialog
	],
	providers: [
		UsuarisService
	],
	entryComponents: [
		UsuarisContrasenyaDialog
	]
})
export class UsuarisModule { }