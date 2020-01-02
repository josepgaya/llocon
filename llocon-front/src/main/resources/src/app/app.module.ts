import { NgModule, ErrorHandler, LOCALE_ID } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ReactiveFormsModule } from '@angular/forms';
import { HttpClient, HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';
import { TranslateModule, TranslateLoader } from '@ngx-translate/core';
import { TranslateHttpLoader } from '@ngx-translate/http-loader';
import { AngularHalModule } from 'angular4-hal';
import { NgxMaskModule } from 'ngx-mask';
import { BngJwtInterceptor, BngErrorModule, BngErrorHandler, BngRestapiConfigService } from 'base-angular';

import { MaterialModule } from './shared/material.module';
import { LocaleService } from './shared/locale.service';
import { RestapiConfigService } from './shared/restapi-config.service';
import { SelectorIdentificadorEmpresaComponent } from './shared/selector-empresa/selector-identificador-empresa.component';
import { AppComponent } from './app.component';
import { AppRoutingModule } from './app.routing';

export function createTranslateLoader(http: HttpClient) {
	return new TranslateHttpLoader(http, './assets/i18n/', '.json');
}

@NgModule({
	imports: [
		BrowserModule,
		BrowserAnimationsModule,
		ReactiveFormsModule,
		HttpClientModule,
		MaterialModule,
		TranslateModule.forRoot({
			loader: {
				provide: TranslateLoader,
				useFactory: (createTranslateLoader),
				deps: [HttpClient]
			}
		}),
		AngularHalModule.forRoot(),
		NgxMaskModule.forRoot(),
		BngErrorModule,
		AppRoutingModule
	],
	declarations: [
		AppComponent,
		SelectorIdentificadorEmpresaComponent
	],
	providers: [
		{ provide: HTTP_INTERCEPTORS, useClass: BngJwtInterceptor, multi: true },
		{ provide: ErrorHandler, useClass: BngErrorHandler },
		{ provide: 'ExternalConfigurationService', useClass: RestapiConfigService },
		{ provide: LOCALE_ID, useFactory: () => LocaleService.getCurrentLocale() },
		{ provide: BngRestapiConfigService, useClass: RestapiConfigService }
	],
	bootstrap: [
		AppComponent
	]
})
export class AppModule { }
