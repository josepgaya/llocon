import { Injectable } from '@angular/core';
import { ExternalConfigurationHandlerInterface, ExternalConfiguration } from 'hal-4-angular';
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