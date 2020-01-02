import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { HttpErrorResponse } from '@angular/common/http';
import { MatSnackBar } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';
import { BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

import { RegistreService } from '../registre/registre.service';

@Component( {
    template: `
<div [ngClass]="{'formContentDesktop centered': !mobileScreen, 'formContentMobile': mobileScreen}">
	<h1 class="mat-display-1 formTitle">{{'create.titol'|translate}}</h1>
	<form [formGroup]="formGroup">
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'create.field.nom'|translate}}</mat-label>
			<input matInput
				type="text"
				formControlName="nom"
				maxlength="100"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('nom')}}</mat-error>
        </mat-form-field>
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'create.field.llinatges'|translate}}</mat-label>
			<input matInput
				type="text"
				formControlName="llinatges"
				maxlength="100"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('llinatges')}}</mat-error>
        </mat-form-field>
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'create.field.correu'|translate}}</mat-label>
			<input matInput
				type="text"
				formControlName="email"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('email')}}</mat-error>
        </mat-form-field>
		<button (click)="onCreateButtonClick()" style="display:none"></button>
		<div *ngIf="recatpchaSiteKey" style="display:block; margin-left: auto; margin-right: auto; width: 70%; margin-top: .5em;">
			<re-captcha (resolved)="captchaResolved($event)" [siteKey]="recatpchaSiteKey"></re-captcha>
		</div>
		<div style="display: flex; justify-content: space-between; margin-top: 20px;">			
			<button mat-button (click)="onCancelButtonClick()">{{'create.button.cancel'|translate}}</button>
			<button [disabled]="createButtonDisabled" mat-raised-button color="primary" (click)="onCreateButtonClick()">{{'create.button.crear'|translate}}</button>
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
export class CreateComponent {

	createButtonDisabled: any = true;
	formGroup: FormGroup = this.formBuilder.group({
		nom: ['', Validators.required],
		llinatges: ['', Validators.required],
		email: ['', [Validators.required, Validators.email]]
	});
	recatpchaSiteKey: string; // = '6LcdScIUAAAAAC7Rgi9lcUU8UbAdkQG7alCNvAam';
	captchaResponse: string;
    mobileScreen: boolean;

	captchaResolved(captchaResponse: string) {
		this.createButtonDisabled = false;		
		this.captchaResponse = captchaResponse;	
	}
	
	onCancelButtonClick() {
		this.router.navigate( ['login'] );
	}

	onCreateButtonClick() {
		this.formGroup.updateValueAndValidity();
		if (this.formGroup.valid) {
			this.registreService.userCreate(this.formGroup, this.captchaResponse).subscribe(( response: any ) => {				
				if (response!=null) {
					this.showSnack('create.snack.created.ok');					
				} else {
					this.showSnack('create.snack.created.ko');
				}
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
			} );
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
		private snackbar: MatSnackBar,
		private formBuilder: FormBuilder,
		private translate: TranslateService,
		private screenSizeService: BngScreenSizeService ) {
		this.mobileScreen = this.screenSizeService.isMobile();
		this.screenSizeService.getScreenSizeChangeSubject().subscribe(( event: BngScreenSizeChangeEvent ) => {
			this.mobileScreen = event.mobile
		} );
		this.registreService.recatpchaSiteKey().subscribe((recatpchaSiteKey: string) => {
			this.recatpchaSiteKey = recatpchaSiteKey;
		});
	}

}