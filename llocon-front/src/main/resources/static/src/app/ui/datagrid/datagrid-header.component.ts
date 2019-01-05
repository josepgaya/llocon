import { Component, ViewChild, ElementRef } from "@angular/core";
import { Router } from "@angular/router";

import { Subscription, interval } from 'rxjs';
import { map } from 'rxjs/operators'
import { IHeaderGroupAngularComp } from "ag-grid-angular";
import { RestService, Resource } from 'hal-4-angular';
import { MdcTextField } from '@angular-mdc/web';

import { DatagridConfig } from "./datagrid.component";
import { DatagridMessageService, PaginacioInfo } from "./datagrid-message.service";

@Component( {
    selector: 'datagrid-header',
    template: `
<div *ngIf="!lovMode" id="header-content">
    <div>
        <span *ngIf="headerTitle" id="header-title">{{headerTitle}}</span>
        <span id="header-page-info">
            <ng-container *ngIf="paginacioInfo">
                {{paginacioInfo.firstRow + 1}} - {{paginacioInfo.lastRow + 1}} / {{paginacioInfo.numElements}}
            </ng-container>
        </span>
    </div>
    <div id="content-buttons">
        <button
            *ngIf="!staticData"
            mdcIconButton
            title="Refrescar"
            (click)="onButtonRefreshClick($event)">
            <mdc-icon mdcIcon>refresh</mdc-icon>
        </button>
        <button
            *ngIf="!readOnly"
            mdcIconButton
            title="Crear nou element"
            (click)="onButtonCreateClick($event)">
            <mdc-icon mdcIcon>add</mdc-icon>
        </button>
        <button
            *ngIf="details && !readOnly"
            mdcIconButton
            title="Crear nou element fill"
            (click)="onButtonAddChildClick($event)"
            [disabled]="this.params.api.getSelectedRows().length == 0">
            <mdc-icon mdcIcon>add_box</mdc-icon>
        </button>
        <button
            *ngIf="!readOnly && !editable && isSelectedRows()"
            mdcIconButton
            title="Modificar fila seleccionada"
            (click)="onButtonEditClick($event)"
            [disabled]="this.params.api.getSelectedRows().length == 0">
            <mdc-icon mdcIcon>create</mdc-icon>
        </button>
        <button
            *ngIf="!readOnly && isSelectedRows()"
            mdcIconButton
            title="Esborrar fila seleccionada"
            (click)="onButtonDeleteClick($event)"
            [disabled]="this.params.api.getSelectedRows().length == 0">
            <mdc-icon mdcIcon>delete</mdc-icon>
        </button>
        <mdc-text-field
            *ngIf="!staticData"
            #filterInput
            (input)="onFilterChange($event)"
            outlined dense>
            <mdc-icon mdcTextFieldIcon leading>search</mdc-icon>
        </mdc-text-field>
    </div>
</div>
<div *ngIf="lovMode" id="header-lov">
    <mdc-text-field
        #filterInput
        (input)="onFilterChange($event)"
        outlined dense>
        <mdc-icon mdcTextFieldIcon leading>search</mdc-icon>
    </mdc-text-field>
</div>`,
    styles: [`
#header-content {
    display: flex;
    justify-content: space-between;
}
#header-title {
    font-weight: bold;
    font-size: 16px;
    margin-right: .5em;
}
#content-buttons {
    display: flex;
    justify-content: flex-end;
}
#header-lov>mdc-text-field {
    width: 100%;
}
.mdc-icon-button {
    color: rgba(0, 0, 0, 0.87);
}
.mdc-icon-button:disabled {
    color: rgba(0, 0, 0, 0.54);
}
mdc-text-field {
    margin-left: .5em;
}`]
} )
export class DatagridHeaderComponent implements IHeaderGroupAngularComp {

    private params: any;
    private headerTitle: string;
    private lovMode: boolean = false;
    private resourceName: string;
    private details: DatagridConfig;
    private readOnly: boolean = false;
    private editable: boolean = false;
    private staticData: boolean = false;

    private rowSelectionCount = 0;
    private paginacioInfo: PaginacioInfo;
    private paginacioInfoSubscription: Subscription;

    @ViewChild( 'filterInput' ) filterInput: MdcTextField;

    agInit( params ): void {
        this.params = params;
        this.headerTitle = params.headerTitle;
        this.lovMode = params.lovMode;
        this.resourceName = params.resourceName;
        this.details = params.details;
        this.staticData = params.isStaticData;
        this.readOnly = params.readOnly;
        this.editable = params.editable;
        const secondsCounter = interval( 200 );
        secondsCounter.subscribe( n => {
            let selectedRows = this.params.api.getSelectedRows();
            this.rowSelectionCount = selectedRows.length;
        } );
        if ( this.lovMode ) {
            setTimeout(() => {
                if ( this.filterInput ) {
                    this.filterInput.focus();
                } else {
                    setTimeout(() => {
                        if ( this.filterInput ) {
                            this.filterInput.focus();
                        }
                    }, 100 );
                }
            }, 100 );
        }
    }
    onButtonRefreshClick() {
        this.params.context.rootComponent.refresh();
    }
    onButtonCreateClick() {
        this.params.context.rootComponent.onNewElementClicked(
            this.resourceName,
            this.params.api );
    }
    onButtonAddChildClick() {
        this.params.context.rootComponent.onSelectedRowAddChildClicked(
            this.details.resourceName,
            this.resourceName,
            this.params.api );
    }
    onButtonEditClick() {
        this.params.context.rootComponent.onSelectedRowEditClicked(
            this.resourceName,
            this.params.api );
    }
    onButtonDeleteClick() {
        this.params.context.rootComponent.onSelectedRowsDeleteClicked(
            this.resourceName,
            this.params.api );
    }

    onFilterChange( event ) {
        this.messageService.sendTextFiltre( event.target.value );
    }

    isSelectedRows() {
        return this.params.api.getSelectedRows().length > 0;
    }

    constructor(
        private router: Router,
        private messageService: DatagridMessageService ) {
        this.paginacioInfoSubscription = this.messageService.getPaginacioInfo().subscribe(
            paginacioInfo => {
                this.paginacioInfo = paginacioInfo;
            } );
    }

}