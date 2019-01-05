import { Component, ViewChild } from "@angular/core";
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";
import { registerLocaleData } from '@angular/common';
import localeCa from '@angular/common/locales/ca';
import { MdcSnackbar } from '@angular-mdc/web';
import { HttpParams } from '@angular/common/http';

import { RestapiService } from "../../../core/restapi/restapi-service";
import { InfiniteListComponent } from "../../../ui/infinite-list/infinite-list.component";

registerLocaleData( localeCa );

@Component( {
    template: `
<context-topbar [template]="topbarTemplate">
    <ng-template #topbarTemplate>
        <mdc-top-app-bar-section align="start" title="{{lloguer?.nom}}">
            <button mdcIconButton mdcTopAppBarNavIcon icon="arrow_back" (click)="goBack($event)"></button>
        </mdc-top-app-bar-section>
        <mdc-top-app-bar-section align="end">
        {{lloguer?.importPendent | currency:'EUR':'symbol':'1.2-2':'ca'}}
        </mdc-top-app-bar-section>
    </ng-template>
</context-topbar>
<mdc-tab-bar
    #sectionTab
    fixed="true">
    <mdc-tab-scroller>
        <mdc-tab label="Factures"></mdc-tab>
        <mdc-tab label="Subministraments"></mdc-tab>
    </mdc-tab-scroller>
</mdc-tab-bar>
<ng-container *ngIf="sectionTab.activeTabIndex == 0">
    <infinite-list
        #facturaList
        resourceName="factura"
        sortProperty="data"
        sortDirection="desc"
        [parent]="{parentId: lloguerId}"
        [interactive]="false"
        [itemTemplate]="facturaItemTemplate"
        roundedIcons="true"
        (selectionChange)="onFacturaSelectionChange($event)">
        <ng-template #facturaItemTemplate let-item="item">
            <mdc-icon mdcListItemGraphic>
                <ng-container *ngIf="facturaSelected[item.id]">done</ng-container>
                <ng-container *ngIf="!facturaSelected[item.id] && item.subministrament.producte === 'LLUM'">flash_on</ng-container>
                <ng-container *ngIf="!facturaSelected[item.id] && item.subministrament.producte === 'AIGUA'">waves</ng-container>
                <ng-container *ngIf="!facturaSelected[item.id] && item.subministrament.producte === 'GAS'">local_gas_station</ng-container>
                <ng-container *ngIf="!facturaSelected[item.id] && item.subministrament.producte === 'TELEFON'">phone</ng-container>
                <ng-container *ngIf="!facturaSelected[item.id] && item.subministrament.producte === 'INTERNET'">language</ng-container>
            </mdc-icon>
            <mdc-list-item-text secondaryText="{{item.data | date : 'dd-MM-yyyy'}}">{{item.subministrament.descripcioCurta}} {{item.numero}}</mdc-list-item-text>
            <div mdcListItemMeta style="color: rgba(0, 0, 0, 0.87); margin-right: .5em">
                {{item.importt | currency:'EUR':'symbol':'1.2-2':'ca'}}
            </div>
            <div mdcListItemMeta style="color: grey; margin-left: 0">
                <button
                    mdcIconButton
                    title="{{item.estat}}"
                    (click)="onFacturaEstatClick(item)">
                    <mdc-icon>
                        <ng-container *ngIf="item.estat === 'PENDENT'">schedule</ng-container>
                        <ng-container *ngIf="item.estat === 'ENVIADA'">mail</ng-container>
                        <ng-container *ngIf="item.estat === 'PAGADA'">check_circle</ng-container>
                    </mdc-icon>
                </button>
            </div>
            <!--div mdcListItemMeta style="color: grey; margin-left: 0">
                <mdc-chip (click)="onFacturaEstatClick(item)" title="{{item.estat}}">
                    <mdc-chip-icon leading>
                        <ng-container *ngIf="item.estat === 'PENDENT'">schedule</ng-container>
                        <ng-container *ngIf="item.estat === 'ENVIADA'">mail</ng-container>
                        <ng-container *ngIf="item.estat === 'PAGADA'">check_circle</ng-container>
                    </mdc-chip-icon>
                    <mdc-chip-text>{{item.estat}}</mdc-chip-text>
                </mdc-chip>
            </div-->
            <div mdcListItemMeta style="color: grey; margin-left: 0">
                <button
                    mdcIconButton
                    title="Descarregar factura"
                    (click)="onDownloadButtonClick(item)">
                    <mdc-icon>get_app</mdc-icon>
                </button>
            </div>
        </ng-template>
    </infinite-list>
</ng-container>
<ng-container *ngIf="sectionTab.activeTabIndex == 1">
    <infinite-list
        #subminList
        resourceName="subministrament"
        [parent]="{parentId: lloguerId}"
        [interactive]="false"
        [itemTemplate]="subminItemTemplate"
        roundedIcons="true">
        <ng-template #subminItemTemplate let-item="item">
            <mdc-icon mdcListItemGraphic *ngIf="item.producte === 'LLUM'">flash_on</mdc-icon>
            <mdc-icon mdcListItemGraphic *ngIf="item.producte === 'AIGUA'">waves</mdc-icon>
            <mdc-icon mdcListItemGraphic *ngIf="item.producte === 'GAS'">local_gas_station</mdc-icon>
            <mdc-icon mdcListItemGraphic *ngIf="item.producte === 'TELEFON'">phone</mdc-icon>
            <mdc-icon mdcListItemGraphic *ngIf="item.producte === 'INTERNET'">language</mdc-icon>
            <mdc-list-item-text
                *ngIf="item.darreraActualitzacio"
                secondaryText="Darrera actualització el {{item.darreraActualitzacio | date : 'dd-MM-yyyy HH:mm:ss'}}">
                {{item.producte}}
            </mdc-list-item-text>
            <mdc-list-item-text
                *ngIf="!item.darreraActualitzacio"
                secondaryText="Cap actualització realitzada">
                {{item.producte}}
            </mdc-list-item-text>
            <button
                mdcIconButton
                mdcListItemMeta
                (click)="onUpdateButtonClick(item)"
                title="Obtenir factures noves"
                style="color: grey">
                <mdc-icon>update</mdc-icon>
            </button>
        </ng-template>
    </infinite-list>
</ng-container>
`,
    styles: [`
/*mdc-top-app-bar {
    position: unset;
    display: block;
}*/`],
    providers: [
        RestapiService]
} )
export class LloguerDetailComponent {

    @ViewChild( 'facturaList' ) facturaList: InfiniteListComponent;
    @ViewChild( 'subminList' ) subminList: InfiniteListComponent;

    private lloguerId: string;
    private lloguer: any;
    private facturaSelected: boolean[] = [];

    goBack() {
        this.location.back();
    }

    onUpdateButtonClick( submin ) {
        const snackbarRef = this.snackbar.show(
            'Obtenint factures noves',
            'Tancar', {
                timeout: 100000000000000000000
            } );
        let resfrescarFacturesUrl = this.lloguerService.getApiRelativeUrl(
            'subministraments/' + submin.id + '/factures' );
        const params = new HttpParams().set( 'parentId', this.lloguerId );
        this.lloguerService.getHttpClient().get( resfrescarFacturesUrl, { params } ).subscribe(
            ( data: any[] ) => {
                let message;
                if ( data && data.length ) {
                    message = 'S\'han trobat ' + data.length + ' factures noves';
                } else {
                    message = 'No s\'han trobat factures noves';
                }
                const snackbarRef2 = this.snackbar.show(
                    message,
                    'Tancar', {
                    } );
                this.subminList.refresh();
            }
        );
        /*console.log( '>>> snackbarRef', snackbarRef )
        //let snackbarTextElement = <HTMLElement>snackbarRef.instance.elementRef.nativeElement.firstChild;
        let snackbarMessage = snackbarRef.instance.message.nativeElement;
        console.log( '>>> snackbarMessage', snackbarMessage )
        let text = this.renderer.createText("Bon dia");
        const newSpan = this.renderer.createElement('span');
        this.renderer.appendChild(snackbarMessage, newSpan);
        console.log( '>>> snackbarMessage 0', snackbarMessage );
        // console.log( '>>> snackbarMessage 0', snackbarMessage.innerHTML )
        //snackbarTextElement.innerHTML = '<span class="fa fa-circle-o-notch fa-spin"></span>';
        //console.log( '>>> snackbarRef 1', snackbarTextElement.innerText)
        //snackbarTextElement.innerHTML = 'Bon dia tot lo dia';
        //console.log( '>>> snackbarRef 2', snackbarTextElement.innerHTML)*/
    }

    onDownloadButtonClick( factura ) {
        let downloadUrl = this.lloguerService.getApiRelativeUrl(
            'factures/' + factura.id + '/file' );
        //let parentId = factura.subministrament.id;
        window.location.href = downloadUrl + '?parentId=' + this.lloguerId;
    }

    onFacturaSelectionChange( event ) {
        this.facturaSelected['' + event.item.id] = event.selected;
    }

    onFacturaEstatClick( factura ) {
        let patchUrl = this.lloguerService.getApiRelativeUrl(
            'factures/' + factura.id );
        let nouEstat;
        if ( factura.estat == 'PENDENT' ) {
            nouEstat = 'ENVIADA';
        } else if ( factura.estat == 'ENVIADA' ) {
            nouEstat = 'PAGADA';
        } else if ( factura.estat == 'PAGADA' ) {
            nouEstat = 'PENDENT';
        }
        let params = new HttpParams().set( 'parentId', this.lloguerId )
        this.lloguerService.getHttpClient().patch(
            patchUrl,
            [{ op: 'replace', path: '/estat', value: nouEstat }],
            { params: params } ).subscribe(( factura ) => {
                this.facturaList.refresh( factura );
                this.lloguerService.get( this.lloguerId ).subscribe(( lloguer ) => {
                    this.lloguer = lloguer;
                } );
            } );
    }

    constructor(
        private activatedRoute: ActivatedRoute,
        private location: Location,
        private lloguerService: RestapiService,
        private snackbar: MdcSnackbar ) {
        activatedRoute.params.subscribe(( params ) => {
            if ( params.id ) {
                this.lloguerId = params.id;
            }
        } );
        lloguerService.configure( 'lloguer' ).subscribe(( formInfo ) => {
            lloguerService.get( this.lloguerId ).subscribe(( lloguer ) => {
                this.lloguer = lloguer;
            } );
        } );
    }

}
