import { Injectable } from '@angular/core';
import { ExternalConfigurationHandlerInterface, ExternalConfiguration } from 'angular4-hal';
import { HttpClient } from '@angular/common/http';

import { RestapiConfig } from './restapi/restapi-config';

@Injectable()
export class HalExternalConfiguration implements ExternalConfigurationHandlerInterface {

    getProxyUri(): string {
        return '';
    }

    getRootUri(): string {
        return this.restapiConfig.getServerUrlAmbContext();
    }

    getHttp(): HttpClient {
        return this.http;
    }

    getExternalConfiguration(): ExternalConfiguration {
        return null;
    }

    setExternalConfiguration( externalConfiguration: ExternalConfiguration ) {
    }

    deserialize() { }
    serialize() { }

    constructor( private http: HttpClient, private restapiConfig: RestapiConfig ) {
    }

}