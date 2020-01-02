import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material';
import { Router, ActivatedRoute } from '@angular/router';
import { TranslateService } from '@ngx-translate/core';
import { BngAuthService, BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

import { RegistreService } from '../registre/registre.service';

@Component( {
    template: `
<div [ngClass]="{'formContentDesktop centered': !mobileScreen, 'formContentMobile': mobileScreen}">
	<h1 class="mat-display-1 formTitle">{{(tokenPayload.aud=='validation')?('validate.titol.validar'|translate):('validate.titol.recuperarContrasenya'|translate)}}</h1>
    <form [formGroup]="formGroup">
		<p>{{tokenPayload.name}} {{tokenPayload.llinatges}} &lt;{{tokenPayload.sub}}&gt;</p>
		<ng-container *ngIf="!tokenExpired">
			<mat-form-field appearance="outline" style="width:100%">
				<mat-label>{{'validate.field.contrasenya'|translate}}</mat-label>
				<input matInput
					type="password"
					formControlName="contrasenya"
					maxlength="100"
					autocomplete="off"/>
				<mat-error>{{getErrorMessage('contrasenya')}}</mat-error>
	        </mat-form-field>
			<mat-form-field appearance="outline" style="width:100%">
				<mat-label>{{'validate.field.repeticio'|translate}}</mat-label>
				<input matInput
					type="password"
					formControlName="repeticio"
					maxlength="100"
					autocomplete="off"/>
				<mat-error>{{getErrorMessage('repeticio')}}</mat-error>
	        </mat-form-field>
			<button (click)="onValidateButtonClick()" style="display:none"></button>
		</ng-container>
		<ng-container *ngIf="tokenExpired">
			<br/>
			<div style="padding:1em;color:#d8000c;background-color: #ffbaba; text-align: center">{{'validate.link.expired'|translate}}</div>
			<br/>
		</ng-container>
		<button (click)="onValidateButtonClick()" style="display:none"></button>
		<div style="display: flex; justify-content: space-between">
			<button mat-button (click)="onCancelButtonClick()">{{'validate.button.cancel'|translate}}</button>
			<button mat-raised-button color="primary" (click)="onValidateButtonClick()" [disabled]="tokenExpired">{{'validate.button.validate'|translate}}</button>
		</div>
    </form>
</div>
`,
    styles: [`
.formContentDesktop {
    padding: 2em;
    background-color: white;
    width: 450px;
    border: 1px solid #dadce0;
    border-radius: 8px;
}
.formContentMobile {
    padding: 2em;
    background-color: white;
}
.formTitle {
    text-align: center;
}
`]
} )
export class ValidateComponent {

	token: string;
	tokenPayload: any;
	tokenExpired: boolean;
	formGroup: FormGroup = this.formBuilder.group({
		contrasenya: ['', Validators.required],
		repeticio: ['', [Validators.required]]
	});
	mobileScreen: boolean;

	onCancelButtonClick() {
		this.router.navigate( ['login'] )
	}

	onValidateButtonClick() {
		this.formGroup.updateValueAndValidity();
		if (this.formGroup.valid) {
			this.registreService.userValidate( this.token, this.formGroup ).subscribe(( response: any ) => {
				this.showSnack('validate.snack.validated.ok');
				this.router.navigate( ['login'] );
			}, (error: Error) => {
				let processed = false;
				if (error instanceof HttpErrorResponse) {
					if ((<HttpErrorResponse>error).error.errors) {
						(<HttpErrorResponse>error).error.errors.forEach((fieldError: any) => {
							let fieldName: string = fieldError.field;
							let errorCode: string = fieldError.code;
							let errors = {};
							errors[errorCode] = true;
							this.formGroup.controls[fieldName].setErrors(errors);
						})
						processed = true;
					}
				}
				if (!processed) {
					throw error;
				}
	        });
		}
	}

	showSnack(message: string) {
		 let snackBarRef = this.snackbar.open(
            this.translate.instant(message),
            this.translateKey( 'component.restapi.form.manteniment.button.close' ), {
            } );
		snackBarRef.afterDismissed().subscribe(() => {
		} );
	}

	getErrorMessage(fieldName: string): string {
		if (this.formGroup.get(fieldName).errors) {
			var errorCode = Object.keys(this.formGroup.get(fieldName).errors)[0];
			if (errorCode) {
				return this.translateKey(
					'error.validation.constraint.' + errorCode,
					undefined,
					this.translate.instant('error.validation.unknown'));
			}
		}
	}

	translateKey(key: string, params?: any, defaultValue?: string) {
		let translatedKey = this.translate.instant(key, params);
		if (defaultValue) {
			return (translatedKey !== key) ? translatedKey : defaultValue;
		} else {
			return translatedKey;
		}
	}

	constructor(
		private registreService: RegistreService,
		private router: Router,
		private activatedRoute: ActivatedRoute,
		private formBuilder: FormBuilder,
		private snackbar: MatSnackBar,
		authService: BngAuthService,
		private translate: TranslateService,
		private screenSizeService: BngScreenSizeService ) {
		this.activatedRoute.params.subscribe( params => {
			this.token = params['token'];
			this.tokenPayload = authService.tokenToObject(this.token);
			this.tokenExpired = Date.now() > this.tokenPayload.exp * 1000;
		} );
		this.mobileScreen = this.screenSizeService.isMobile();
		this.screenSizeService.getScreenSizeChangeSubject().subscribe(( event: BngScreenSizeChangeEvent ) => {
			this.mobileScreen = event.mobile
		} );
	}

}