import { Injectable, Injector } from '@angular/core';
import { FormGroup } from '@angular/forms';
import { HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Resource } from 'angular4-hal';

import { BngRestapiService } from 'base-angular';

export class BngGenericResource extends Resource {}

@Injectable({
    providedIn: 'root',
})
export class RegistreService extends BngRestapiService<BngGenericResource> {

	userCreate(formGroup: FormGroup, captchaResponse: string): Observable<any> {
		return this.getHttpClient().post(
			'api/registres/create', {
				nom: formGroup.value.nom,
				llinatges: formGroup.value.llinatges,
				email: formGroup.value.email,
				captchaResponse: captchaResponse
			}
		);
	}

	userValidate(token: string, formGroup: FormGroup): Observable<any> {
		return this.getHttpClient().post(
			'api/registres/validate', {
				token: token,
				contrasenya: formGroup.value.contrasenya,
				repeticio: formGroup.value.repeticio
			}
		);
	}

	passwordRecover(email: string): Observable<any> {
		return this.getHttpClient().post(
			'api/registres/' + email + '/reset',
			new HttpParams().append('email', email));
	}

	recatpchaSiteKey(): Observable<any> {
		return this.getHttpClient().get('api/registres/recaptchaSiteKey', {responseType: 'text'});
	}

	constructor( injector: Injector ) {
        super( BngGenericResource, undefined, injector );
    }

}