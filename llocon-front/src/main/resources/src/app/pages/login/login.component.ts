import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { TranslateService } from '@ngx-translate/core';
import { BngAuthService, BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

@Component({
	template: `
<div [ngClass]="{'formContentDesktop centered': !mobileScreen, 'formContentMobile': mobileScreen}">
	<h1 class="mat-display-3 formTitle"><mat-icon style="font-size:50px;margin-right:.8em">cloud_queue</mat-icon>Llocon</h1>
	<form [formGroup]="formGroup">
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'login.field.usuari'|translate}}</mat-label>
			<input matInput
				type="text"
				formControlName="user"
				maxlength="100"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('user')}}</mat-error>
        </mat-form-field>
		<mat-form-field appearance="outline" style="width:100%">
			<mat-label>{{'login.field.contrasenya'|translate}}</mat-label>
			<input matInput
				type="password"
				formControlName="pass"
				autocomplete="off"/>
			<mat-error>{{getErrorMessage('pass')}}</mat-error>
        </mat-form-field>
		<button (click)="onEntrarButtonClick()" style="display:none"></button>
        <a mat-button color="accent" routerLink="/registre/recover">{{'login.msg.contrasenya.recover'|translate}}</a>
        <div style="display: flex; justify-content: space-between">
            <a mat-button color="accent" routerLink="/registre/create">{{'login.msg.usuari.create'|translate}}</a>
            <button mat-raised-button color="primary" (click)="onEntrarButtonClick()">{{'login.button.entrar'|translate}}</button>
        </div>
    </form>
</div>`,
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
})
export class LoginComponent {

	formGroup: FormGroup = this.formBuilder.group({
		user: ['', Validators.required],
		pass: ['', [Validators.required]]
	});
	mobileScreen: boolean;

	onEntrarButtonClick() {
		this.formGroup.get('user').setErrors({ 'loginError': null });
		this.formGroup.get('user').updateValueAndValidity();
		this.formGroup.get('pass').setErrors({ 'loginError': null });
		this.formGroup.get('pass').updateValueAndValidity();
		if (this.formGroup.valid) {
			this.authService.login(this.formGroup.get('user').value, this.formGroup.get('pass').value).subscribe((response: any) => {
				if (!response.error) {
					this.router.navigate(['/home']);
				} else {
					let errors = {};
					errors['loginError'] = true;
					this.formGroup.controls['user'].setErrors(errors);
					this.formGroup.controls['pass'].setErrors(errors);
				}
			}, () => {
				let errors = {};
				errors['loginError'] = true;
				this.formGroup.controls['user'].setErrors(errors);
				this.formGroup.controls['pass'].setErrors(errors);
			});
		}
	}

	onSubmit(event: Event) {
		event.preventDefault();
		this.onEntrarButtonClick();
	}

	getErrorMessage(fieldName: string): string {
		if (this.formGroup.get(fieldName).errors) {
			var errorCode = Object.keys(this.formGroup.get(fieldName).errors)[0];
			if (errorCode) {
				if (errorCode == 'loginError') {
					return this.translateKey('login.msg.login.error');
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
		private authService: BngAuthService,
		private router: Router,
		private formBuilder: FormBuilder,
		private translate: TranslateService,
		private screenSizeService: BngScreenSizeService) {
		this.mobileScreen = this.screenSizeService.isMobile();
		this.screenSizeService.getScreenSizeChangeSubject().subscribe((event: BngScreenSizeChangeEvent) => {
			this.mobileScreen = event.mobile
		});
	}

}