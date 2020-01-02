import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { MatSnackBar } from '@angular/material';
import { TranslateService } from '@ngx-translate/core';
import { BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

import { RegistreService } from '../registre/registre.service';

@Component( {
    template: `
<div [ngClass]="{'formContentDesktop centered': !mobileScreen, 'formContentMobile': mobileScreen}">
	<h1 class="mat-display-1 formTitle">{{'recover.titol'|translate}}</h1>
    <form [formGroup]="formGroup">
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'recover.field.correu'|translate}}</mat-label>
			<input matInput
				type="text"
				formControlName="email"
				maxlength="100"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('email')}}</mat-error>
        </mat-form-field>
		<button (click)="onRecuperarButtonClick()" style="display:none"></button>
		<div style="display: flex; justify-content: space-between">
			<button mat-button (click)="onCancelButtonClick()">{{'recover.button.cancel'|translate}}</button>
			<button mat-raised-button color="primary" (click)="onRecuperarButtonClick()">{{'recover.button.recuperar'|translate}}</button>
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
export class RecoverComponent {

    formGroup: FormGroup = this.formBuilder.group({
		email: ['', [Validators.required, Validators.email]]
	});
    mobileScreen: boolean;

	onCancelButtonClick() {
		this.router.navigate( ['login'] );
	}

    onRecuperarButtonClick() {
        this.formGroup.updateValueAndValidity();
		if (this.formGroup.valid) {
	        this.registreService.passwordRecover(this.formGroup.get('email').value).subscribe(( response ) => {
				this.showSnack('recover.notify.recover');
				this.router.navigate( ['login'] );
			}, () => {
				let errors = {};
				errors['recoverError'] = true;
				this.formGroup.controls['email'].setErrors(errors);
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
				if (errorCode == 'recoverError') {
					return this.translateKey('recover.msg.recover.error');
				} else {
					return this.translateKey(
						'error.validation.constraint.' + errorCode,
						undefined,
						this.translate.instant('error.validation.unknown'));
				}
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
        private router: Router,
		private snackbar: MatSnackBar,
		private formBuilder: FormBuilder,
        private registreService: RegistreService,
        private translate: TranslateService,
        private screenSizeService: BngScreenSizeService ) {
        this.mobileScreen = this.screenSizeService.isMobile();
        this.screenSizeService.getScreenSizeChangeSubject().subscribe(( event: BngScreenSizeChangeEvent ) => {
            this.mobileScreen = event.mobile
        } );
    }

}