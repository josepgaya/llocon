import { Component, Input, Injector, Inject, ViewChild, ElementRef, OnInit } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { MatDialog, MatDialogRef, MAT_DIALOG_DATA, MatSnackBar } from '@angular/material';
import { BngFormBaseField } from 'base-angular';

import { UsuarisService } from './usuaris.service';

@Component( {
    selector: 'usuari-contrasenya',
    template: `
<div>
	<button mat-raised-button color="accent" (click)="onButtonClick($event)">{{'page.usuaris.canvi.contrasenya.titol'|translate}}</button>
</div>
`, styles: [`
`]
} )
export class UsuarisContrasenyaFieldComponent extends BngFormBaseField {

	@Input() usuariId: number;

	onButtonClick(event: Event) {
		event.stopPropagation();
		event.preventDefault();
		const dialogRef = this.dialog.open(UsuarisContrasenyaDialog, {
			//width: '250px',
			data: {
				usuariId: this.usuariId
			}
		});
		dialogRef.afterClosed().subscribe((/*result*/) => {
		});
	}

    public setErrors( errors?: any ) {
    }

    getFieldComponent() {
    }

	constructor(
        public injector: Injector,
		translate: TranslateService,
		public dialog: MatDialog ) {
		super(translate);
    }

}

@Component( {
    template: `
<h1 mat-dialog-title>{{'page.usuaris.canvi.contrasenya.titol'|translate}}</h1>
<div mat-dialog-content>
	<form [formGroup]="formGroup">
		<button (click)="onButtonCanviarClick()" style="display:none"></button>
	    <mat-form-field style="width:100%">
			<mat-label>{{'page.usuaris.canvi.contrasenya.field.contrasenya'|translate}}</mat-label>
			<input #inputPassword matInput formControlName="password" type="password" autocomplete="off" required/>
			<mat-error>{{fieldError}}</mat-error>
		</mat-form-field>
		<mat-form-field style="width:100%">
			<mat-label>{{'page.usuaris.canvi.contrasenya.field.confirmacio'|translate}}</mat-label>
			<input #inputConfirmation matInput formControlName="confirmation" type="password" autocomplete="off" required/>
			<mat-error>{{fieldError}}</mat-error>
		</mat-form-field>
	</form>
</div>
<div mat-dialog-actions style="display:flex;justify-content:space-between">
	<button mat-button (click)="onCancelButtonClick()">{{'page.usuaris.canvi.contrasenya.button.cancelar'|translate}}</button>
	<button mat-raised-button color="primary" (click)="onButtonCanviarClick()" style="">{{'page.usuaris.canvi.contrasenya.button.canviar'|translate}}</button>
</div>`
} )
export class UsuarisContrasenyaDialog implements OnInit {

	@ViewChild( 'inputPassword', { static: false } ) passwordField: ElementRef;

	formGroup: FormGroup;
	fieldError: string;

	ngOnInit() {
		this.formGroup = this.formBuilder.group({
			password: [''],
			confirmation: ['']
		});
	}

	onCancelButtonClick() {
		this.dialogRef.close();
	}

	onButtonCanviarClick() {
		let password = this.formGroup.get('password').value;
		let confirmation = this.formGroup.get('confirmation').value;
		if (!password && !confirmation) {
			this.passwordField.nativeElement.focus();
			this.fieldError = this.translate.instant('error.validation.constraint.NotNull');
			this.formGroup.get('password').markAsTouched();
			this.formGroup.get('password').setErrors({'error': true});
			this.formGroup.get('confirmation').markAsTouched();
			this.formGroup.get('confirmation').setErrors({'error': true});
		} else {
			if (password == confirmation) {
				let patchUrl = this.usuarisService.getApiBaseUrl('/' + this.data.usuariId);
				let patchOperation: any = { op: 'replace', path: '/contrasenya', value: password };
				this.usuarisService.getHttpClient().patch(patchUrl, [patchOperation]).subscribe((usuari: any) => {
					this.snackbar.open(
						this.translate.instant('page.usuaris.canvi.contrasenya.ok'),
						this.translate.instant('component.restapi.form.manteniment.button.close'));
					this.dialogRef.close();
				});
			} else {
				this.fieldError = this.translate.instant('page.usuaris.canvi.contrasenya.no.coincideix');
				this.formGroup.get('password').markAsTouched();
				this.formGroup.get('password').setErrors({'error': true});
				this.formGroup.get('confirmation').markAsTouched();
				this.formGroup.get('confirmation').setErrors({'error': true});
			}
		}
	}

	constructor(
		private dialogRef: MatDialogRef<UsuarisContrasenyaDialog>,
		@Inject(MAT_DIALOG_DATA) public data: any,
		private formBuilder: FormBuilder,
		private snackbar: MatSnackBar,
		private translate: TranslateService,
		private usuarisService: UsuarisService) {
        this.data = data;
	}

}