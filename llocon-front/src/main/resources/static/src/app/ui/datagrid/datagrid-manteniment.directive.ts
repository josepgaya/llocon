import { Directive, ElementRef, Injector } from '@angular/core';
import { HttpClient, HttpParams } from "@angular/common/http";
import { Router } from "@angular/router";
import { MdcSnackbar } from "@angular-mdc/web";
import { Resource } from "hal-4-angular";

import { DatagridComponent, DatagridEventParams } from './datagrid.component';

@Directive( {
    selector: '[datagrid-mant]'
} )
export class DatagridMantenimentDirective {

    private http: HttpClient;
    private router: Router;
    private snackbar: MdcSnackbar;

    onDatagridActionCreate( params: DatagridEventParams ) {
        let targetRoute = this.getTargetRoute(
            params.resourceName,
            'create' );
        this.navegarAmbPare( targetRoute, params.pare );
    }
    onDatagridActionCreateChild( params: DatagridEventParams ) {
        let targetRoute = this.getTargetRoute(
            params.detailResourceName,
            'create' );
        this.navegarAmbPare( targetRoute, params.pare );
    }
    onDatagridActionEdit( params: DatagridEventParams ) {
        let targetRoute = this.getTargetRoute(
            params.resourceName,
            'update',
            params.rowId );
        this.navegarAmbPare( targetRoute, params.pare );
    }
    onDatagridActionDelete( params: DatagridEventParams ) {
        if ( confirm( "Estau segur que voleu esborrar aquest registre (id=" + params.rowId + ")?" ) ) {
            let linkSelfHref = params.rowData['_links'].self.href;
            this.http.delete( linkSelfHref, {params: params.pare} ).subscribe(( resource: Resource ) => {
                this.showMessage(
                    "El registre s'ha esborrat correctament",
                    false );
                this.datagrid.refresh();
            } );
        }
    }

    showMessage( message: string, error: boolean ) {
        const snackbarRef = this.snackbar.show(
            message,
            "Tancar", {
                align: 'center',
                multiline: false,
                dismissOnAction: true
            } );
    }

    navegarAmbPare( targetRoute: string, pare: any ) {
        if ( pare ) {
            this.router.navigate( [targetRoute], { queryParams: pare } );
        } else {
            this.router.navigate( [targetRoute] );
        }
    }

    getTargetRoute( resourceName, action, id?) {
        let targetRoute = this.router.url;
        let includeResourceName = !this.router.url.startsWith('/' + resourceName);
        targetRoute += (includeResourceName) ? '/' + resourceName : '';
        targetRoute += (id) ? '/' + id : '';
        targetRoute += '/' + action;
        return targetRoute;
    }

    constructor(
        private datagrid: DatagridComponent,
        private injectorObj: Injector ) {
        datagrid.actionCreate.subscribe( params => this.onDatagridActionCreate( params ) );
        datagrid.actionCreateChild.subscribe( params => this.onDatagridActionCreateChild( params ) );
        datagrid.actionEdit.subscribe( params => this.onDatagridActionEdit( params ) );
        datagrid.actionDelete.subscribe( params => this.onDatagridActionDelete( params ) );
        this.http = <HttpClient>this.injectorObj.get( HttpClient );
        this.router = <Router>this.injectorObj.get( Router );
        this.snackbar = <MdcSnackbar>this.injectorObj.get( MdcSnackbar );
    }

}
