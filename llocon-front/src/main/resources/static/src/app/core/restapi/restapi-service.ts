import { Injectable, Injector } from "@angular/core";
import { HttpClient } from '@angular/common/http';
import { Observable, EMPTY, of } from "rxjs";
import { RestService, Resource } from 'angular4-hal';

import { RestapiConfig } from './restapi-config';
import { RestapiResource } from "./restapi-resource";

class GenericResource extends Resource { }

@Injectable()
export class RestapiService extends RestService<GenericResource> {

    private resourceName: string;
    private apiUrl: string;

    configure( resourceName: string, apiUrl?: string ): Observable<RestapiResource> {
        this.resourceName = resourceName;
        if ( !apiUrl ) {
            return new Observable(( observer ) => {
                this.getForm().subscribe(( resource: RestapiResource ) => {
                    this.configureApiUrl( resource.apiUrl );
                    observer.next( resource );
                    observer.complete();
                } );
            } )
        } else {
            this.configureApiUrl( apiUrl );
            return EMPTY;
        }
    }

    getForm(): Observable<RestapiResource> {
        let serviceUrl = this.restapiConfig.getApiServiceUrl( '/api/forms/' + this.resourceName );
        return this.http.get<RestapiResource>( serviceUrl );
    }

    getHttpClient(): HttpClient {
        return this.http;
    }
    getApiRelativeUrl( url ): string {
        return this.restapiConfig.getApiServiceUrl( '/api/' + url );
    }

    private configureApiUrl( apiUrl: string ) {
        this.apiUrl = apiUrl;
        if ( apiUrl ) {
            if ( apiUrl.startsWith( "/" ) ) {
                this["resource"] = apiUrl.substr( 1, apiUrl.length );
            } else {
                this["resource"] = apiUrl;
            }
        } else {
            this["resource"] = undefined;
        }
    }

    constructor(
        injector: Injector,
        private http: HttpClient,
        private restapiConfig: RestapiConfig ) {
        super(
            GenericResource,
            '',
            injector );
    }

}