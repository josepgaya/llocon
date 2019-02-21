import { Component, Input, Output, EventEmitter, ViewChild, ElementRef } from "@angular/core";
import { ICellRendererAngularComp } from "ag-grid-angular";

@Component( {
    selector: 'datagrid-row-actions',
    template: `
<div
    #actionButtons
    (mouseover)="onBotonsMouseOver($event)"
    (mouseout)="onBotonsMouseOut($event)"
    style="text-align:right; float: right">
    <button
        #editButton
        *ngIf="editable"
        mdcIconButton
        title="Modificar"
        (click)="onBotoEditClick()">
        <mdc-icon mdcIcon>create</mdc-icon>
    </button>
    <button
        #addChildButton
        *ngIf="hasChild"
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
export class DatagridRowActionsComponent implements ICellRendererAngularComp {

    /*@Input()
    set rowElement( rowElement ) {
        let botonsElement = this.actionButtonsElement.nativeElement;
        if ( rowElement ) {
            let offsetTop = 0; // -110;
            let offsetLeft = 0; // -212;
            let rowRect = rowElement.getBoundingClientRect();
            //botonsElement.style.display = 'block';
            botonsElement.style.position = 'absolute';
            botonsElement.style.top = (rowRect.top + offsetTop + window.scrollY) + 'px';
            let buttonsRect = botonsElement.getBoundingClientRect();
            botonsElement.style.left = (rowRect.right - buttonsRect.width + offsetLeft) + 'px';
            botonsElement.style.height = ( rowRect.bottom - rowRect.top - 1 ) + 'px';
            this.currentRowElement = rowElement;
            this.updateBotonsBackground();
        } else {
            //botonsElement.style.display = 'none';
        }
    }*/
    @Input() editable: boolean = true;
    @Input() hasChild: boolean = true;

    @Output() botoEditClicked: EventEmitter<any> = new EventEmitter();
    @Output() botoAddChildClicked: EventEmitter<any> = new EventEmitter();
    @Output() botoDeleteClicked: EventEmitter<any> = new EventEmitter();

    @ViewChild( 'actionButtons' ) actionButtonsElement: ElementRef;
    @ViewChild( 'editButton' ) editButtonElement: ElementRef;
    @ViewChild( 'addChildButton' ) addChildButtonElement: ElementRef;
    @ViewChild( 'deleteButton' ) deleteButtonElement: ElementRef;

    //private currentRowElement;
    private params: any;

    agInit( params: any ): void {
        console.log('>>> params', params.getValue(), params)
        this.params = params;
    }
    refresh(): boolean {
        return false;
    }

    onBotonsMouseOver( event ) {
        /*let botonsElement = this.actionButtonsElement.nativeElement;
        let isCursorInBotons = this.isCursorInside( event );
        if ( isCursorInBotons ) {
            let className = this.currentRowElement.className;
            if ( className.indexOf( 'ag-row-selected' ) == -1 ) {
                this.currentRowElement.classList.add( 'ag-row-hover' );
            }
        }*/
    }
    onBotonsMouseOut( event ) {
        /*let botonsElement = this.actionButtonsElement.nativeElement;
        let isCursorInBotons = this.isCursorInside( event );
        let isCursorInRow = false;
        if ( this.currentRowElement ) {
            isCursorInRow = this.isCursorInside( event, this.currentRowElement );
        }
        if ( !isCursorInBotons && !isCursorInRow ) {
            this.actionButtonsElement.nativeElement.style.display = 'none';
            this.currentRowElement.classList.remove( 'ag-row-hover' );
            this.currentRowElement = null;
        }*/
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

    /*updateBotonsBackground() {
        if ( this.currentRowElement ) {
            let botonsElement = this.actionButtonsElement.nativeElement;
            let className = this.currentRowElement.className;
            if ( className.indexOf( 'ag-row-selected' ) != -1 ) {
                botonsElement.style.backgroundColor = '#f5f5f5';
            } else {
                if ( className.indexOf( 'ag-row-hover' ) != -1 ) {
                    botonsElement.style.backgroundColor = '#eeeeee';
                } else {
                    botonsElement.style.backgroundColor = '#ffffff';
                }
            }
        }
    }

    isCursorInside( cursorEvent, element = this.actionButtonsElement.nativeElement ) {
        let bodyRect = element.getBoundingClientRect();
        let cursorX = cursorEvent.pageX;
        let cursorY = cursorEvent.pageY;
        return ( cursorX >= bodyRect.left + window.scrollX &&
            cursorX <= bodyRect.right + window.scrollX &&
            cursorY >= bodyRect.top + window.scrollY &&
            cursorY <= bodyRect.bottom + window.scrollY );
    }*/

}