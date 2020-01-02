import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { BngRestapiConfigService } from 'base-angular';

@Injectable()
export class RestapiConfigService extends BngRestapiConfigService {

	contextPath: string;

	getContextPath(): string {
		if (this.contextPath === undefined) {
			var baseHref: string = (document.querySelector('base') || {})['href'];
			var location = window.location;
			let protocolHostPort = location.protocol + '//' + location.hostname + (location.port ? ':' + location.port : '');
			let fullPath = baseHref + ((baseHref.endsWith('/')) ? 'api' : '/api');
			this.contextPath = fullPath.replace(protocolHostPort, '');
		}
		return this.contextPath;
	}

	constructor(http: HttpClient) {
		super(http);
	}

}
