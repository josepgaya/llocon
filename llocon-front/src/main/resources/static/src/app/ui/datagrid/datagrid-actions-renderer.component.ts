import { Component, Input, Output, EventEmitter, ViewChild, ElementRef } from "@angular/core";
import { Subscription } from 'rxjs';
import { ICellRendererAngularComp } from "ag-grid-angular";
import { RowNode } from 'ag-grid-community/main';

import { DatagridMessageService } from "./datagrid-message.service";

@Component( {
    selector: 'datagrid-actions-renderer',
    template: `
{{paramsValue}}
<div
    #actionButtons
    [hidden]="hidden"
    (mouseover)="onBotonsMouseOver($event)"
    (mouseout)="onBotonsMouseOut($event)"
    style="float: right; position: relative; top: -1px">
    <button
        #editButton
        mdcIconButton
        title="Modificar"
        (click)="onBotoEditClick()">
        <mdc-icon mdcIcon>create</mdc-icon>
    </button>
    <button
        #addChildButton
        mdcIconButton
        title="Crear fill"
        (click)="onBotoAddChildClick()">
        <mdc-icon mdcIcon>save_alt</mdc-icon>
    </button>
    <button
        #deleteButton
        mdcIconButton
        title="Esborrar"
        (click)="onBotoDeleteClick()">
        <mdc-icon mdcIcon>delete</mdc-icon>
    </button>
</div>`
} )
export class DatagridActionsRendererComponent implements ICellRendererAngularComp {

    @Output() botoEditClicked: EventEmitter<any> = new EventEmitter();
    @Output() botoAddChildClicked: EventEmitter<any> = new EventEmitter();
    @Output() botoDeleteClicked: EventEmitter<any> = new EventEmitter();

    @ViewChild( 'actionButtons' ) actionButtonsElement: ElementRef;
    @ViewChild( 'editButton' ) editButtonElement: ElementRef;
    @ViewChild( 'addChildButton' ) addChildButtonElement: ElementRef;
    @ViewChild( 'deleteButton' ) deleteButtonElement: ElementRef;

    private hoverRowSubscription: Subscription;

    private paramsValue: any;
    private rowIndex: any;
    private hidden = true;

    agInit( params: any ): void {
        this.paramsValue = params.value;
        this.rowIndex = params.rowIndex;
    }
    refresh(): boolean {
        return false;
    }

    onBotonsMouseOver( event ) {
    }
    onBotonsMouseOut( event ) {
    }

    onBotoEditClick() {
        this.botoEditClicked.emit();
    }
    onBotoAddChildClick() {
        this.botoAddChildClicked.emit();
    }
    onBotoDeleteClick() {
        this.botoDeleteClicked.emit();
    }

    constructor(
        private messageService: DatagridMessageService ) {
        this.hoverRowSubscription = this.messageService.getHoverRow().subscribe(
            hoverRowNode => {
                if ( hoverRowNode ) {
                    if ( hoverRowNode.rowIndex == this.rowIndex ) {
                        this.hidden = false;
                    }
                } else {
                    this.hidden = true;
                }
            } );
    }

}