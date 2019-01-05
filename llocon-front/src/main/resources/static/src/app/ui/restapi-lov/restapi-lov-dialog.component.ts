import { Component, Input, Output, EventEmitter, ElementRef, ViewChild, Inject } from "@angular/core";

import { RestService, Resource } from 'hal-4-angular';
import { MdcDialog, MdcDialogComponent, MdcDialogRef, MDC_DIALOG_DATA, MdcTextField } from '@angular-mdc/web';

import { DatagridEventParams, DatagridConfig } from "../../ui/datagrid/datagrid.component";

@Component( {
    template: `
<mdc-dialog>
    <mdc-dialog-surface>
        <mdc-dialog-title>Seleccionar {{data.resourceName}}</mdc-dialog-title>
        <mdc-dialog-content>
            <datagrid
                [config]="datagridConfig"
                (selectionChanged)="onDatagridSelectionChanged($event)"
                (rowDoubleClick)="onDatagridRowDoubleClick($event)"></datagrid>
        </mdc-dialog-content>
        <mdc-dialog-actions>
            <button
                mdcDialogButton
                mdcDialogAction="close">Tancar</button>
            <button
                mdcDialogButton
                mdcDialogAction="accept"
                [disabled]="!selectedRowData"
                (click)="propagarSeleccioITancar()">Seleccionar</button>
        </mdc-dialog-actions>
    </mdc-dialog-surface>
</mdc-dialog>`,
    styles: [`
mdc-dialog {
    z-index: 7
}
mdc-dialog-surface {
    width: 80%
}`]
} )
export class RestapiLovDialogComponent {

    private datagridConfig: DatagridConfig;
    private selectedRowData: any;

    onDatagridSelectionChanged( selectedRowsData: any[] ): void {
        if ( selectedRowsData && selectedRowsData.length ) {
            this.selectedRowData = selectedRowsData[0];
        } else {
            this.selectedRowData = null;
        }
    }
    onDatagridRowDoubleClick( params: DatagridEventParams ): void {
        this.selectedRowData = params.rowData;
        this.propagarSeleccioITancar();
    }

    propagarSeleccioITancar(): void {
        this.dialogRef.close( this.selectedRowData );
    }

    constructor(
        public dialogRef: MdcDialogRef<RestapiLovDialogComponent>,
        @Inject( MDC_DIALOG_DATA ) public data ) {
        this.datagridConfig = {
                resourceName: data.resourceName,
                columns: data.columns,
                pare: data.parent,
                lovMode: true
        }
    }

}