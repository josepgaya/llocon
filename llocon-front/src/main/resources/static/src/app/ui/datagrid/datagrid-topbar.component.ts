import { Component, Input, Output, EventEmitter, ViewChild } from "@angular/core";
import { Router } from "@angular/router";
import { Subscription, interval } from 'rxjs';
import { map } from 'rxjs/operators'
import { GridOptions } from 'ag-grid/main';
import { IHeaderGroupAngularComp } from "ag-grid-angular";
import { RestService, Resource } from 'hal-4-angular';
import { MdcTextField } from '@angular-mdc/web';

import { DatagridConfig } from "./datagrid.component";
import { DatagridMessageService, PaginacioInfo } from "./datagrid-message.service";

@Component( {
    selector: 'datagrid-top-bar',
    template: `
<context-topbar [template]="topbarTemplate">
    <ng-template #topbarTemplate>
        <mdc-top-app-bar-section align="start">
            <!--mdc-icon mdcTopAppBarActionItem>arrow_back</mdc-icon-->
            <p style="font-size: 20px;font-weight: 500;padding: 0 .5em">
                {{headerTitle}}
            </p>
            <p style="font-size: 12px;padding-top: 8px;" *ngIf="paginacioInfo">
                {{paginacioInfo.firstRow + 1}} - {{paginacioInfo.lastRow + 1}} / {{paginacioInfo.numElements}}
            </p>
        </mdc-top-app-bar-section>
        <mdc-top-app-bar-section align="end">
            <mdc-icon
                mdcTopAppBarActionItem
                *ngIf="!staticData"
                title="Refrescar contingut"
                (click)="onButtonRefreshClick($event)">refresh</mdc-icon>
            <mdc-icon
                mdcTopAppBarActionItem
                *ngIf="!readOnly"
                title="Crear nou element"
                (click)="onButtonCreateClick($event)">add</mdc-icon>
            <mdc-icon
                mdcTopAppBarActionItem
                *ngIf="details && !readOnly"
                title="Crear nou element fill"
                (click)="onButtonCreateChildClick($event)">add_box</mdc-icon>
            <mdc-icon
                mdcTopAppBarActionItem
                *ngIf="!readOnly && !editable && isSelectedRows()"
                title="Modificar fila seleccionada"
                (click)="onButtonEditClick($event)">edit</mdc-icon>
            <mdc-icon
                mdcTopAppBarActionItem
                title="Esborrar fila seleccionada"
                *ngIf="!readOnly && isSelectedRows()"
                (click)="onButtonDeleteClick($event)">delete</mdc-icon>
            <mdc-icon
                mdcTopAppBarActionItem
                title="Filtrar elements"
                *ngIf="!staticData && !inputFilterVisible"
                (click)="onButtonSearchClick($event)">search</mdc-icon>
            <mdc-text-field
                *ngIf="inputFilterVisible"
                #filterInput
                dense
                class="text-field-search"
                (input)="onFilterChange($event)"
                (blur)="onFilterBlur($event)">
                <mdc-icon mdcTextFieldIcon leading>search</mdc-icon>
            </mdc-text-field>
        </mdc-top-app-bar-section>
    </ng-template>
</context-topbar>`,
    styles: [`,
mdc-top-app-bar {
     position: unset;
     display: block;
}`]
} )
export class DatagridTopBarComponent {

    @Output() buttonRefreshClick: EventEmitter<any> = new EventEmitter();
    @Output() buttonCreateClick: EventEmitter<any> = new EventEmitter();
    @Output() buttonCreateChildClick: EventEmitter<any> = new EventEmitter();
    @Output() buttonEditClick: EventEmitter<any> = new EventEmitter();
    @Output() buttonDeleteClick: EventEmitter<any> = new EventEmitter();

    @Input() headerTitle: string;
    @Input() details: DatagridConfig;
    @Input() readOnly: boolean;
    @Input() editable: boolean;
    @Input() staticData: boolean;
    @Input() resourceName: string;
    @Input() gridOptions: GridOptions;

    @ViewChild( 'filterInput' ) filterInput: MdcTextField;

    private selectionCount = 0;
    private selectionSubscription: Subscription;
    private paginacioInfo: PaginacioInfo;
    private paginacioInfoSubscription: Subscription;
    private inputFilterVisible: boolean = false;

    onButtonRefreshClick() {
        this.buttonRefreshClick.emit();
    }
    onButtonCreateClick() {
        this.buttonCreateClick.emit();
    }
    onButtonCreateChildClick() {
        this.buttonCreateChildClick.emit( this.details.resourceName );
    }
    onButtonEditClick() {
        this.buttonEditClick.emit();
    }
    onButtonDeleteClick() {
        this.buttonDeleteClick.emit();
    }
    onButtonSearchClick() {
        this.inputFilterVisible = true;
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

    onFilterChange( event ) {
        this.messageService.sendTextFiltre( event.target.value );
    }
    onFilterBlur( value ) {
        if ( !value ) {
            this.inputFilterVisible = false;
        }
    }

    isSelectedRows() {
        return this.selectionCount > 0;
    }

    constructor(
        private messageService: DatagridMessageService ) {
        this.selectionSubscription = this.messageService.getSelection().subscribe(
            selection => {
                this.selectionCount = selection ? selection.length : 0;
            } );
        this.paginacioInfoSubscription = this.messageService.getPaginacioInfo().subscribe(
            paginacioInfo => {
                this.paginacioInfo = paginacioInfo;
            } );
    }

}