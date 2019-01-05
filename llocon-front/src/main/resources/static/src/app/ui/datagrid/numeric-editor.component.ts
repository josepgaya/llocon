import { Component, ViewChild, ViewContainerRef } from "@angular/core";

import { ICellEditorAngularComp } from "ag-grid-angular";

@Component( {
    template: `
<input #input (keydown)="onKeyDown($event)" [(ngModel)]="value" class="ag-cell-edit-input text-right"/>
`
} )
export class NumericEditorComponent implements ICellEditorAngularComp {

    private params: any;
    public value: number;
    private cancelBeforeStart: boolean = false;

    @ViewChild( 'input', { read: ViewContainerRef } ) public input;

    agInit( params: any ): void {
        this.params = params;
        this.value = this.params.value;
        this.cancelBeforeStart = params.charPress && ( '1234567890.'.indexOf( params.charPress ) < 0 );
    }

    getValue(): any {
        return this.value;
    }

    focusIn() {
        this.input.element.nativeElement.focus();
        return true;
    }

    isCancelBeforeStart(): boolean {
        return this.cancelBeforeStart;
    }

    // will reject the number if it greater than 1,000,000
    // not very practical, but demonstrates the method.
    isCancelAfterEnd(): boolean {
        return this.value > 1000000;
    };

    onKeyDown( event ): void {
        if ( !this.isKeyPressedNumeric( event ) ) {
            if ( event.preventDefault ) event.preventDefault();
        }
    }

    // dont use afterGuiAttached for post gui events - hook into ngAfterViewInit instead for this
    /*ngAfterViewInit() {
        setTimeout(() => {
            this.input.element.nativeElement.focus();
        } )
    }*/

    private getCharCodeFromEvent( event ): any {
        event = event || window.event;
        return ( typeof event.which == "undefined" ) ? event.keyCode : event.which;
    }

    private isCharNumeric( charStr ): boolean {
        return !!/\d/.test( charStr );
    }

    private isKeyPressedNumeric( event ): boolean {
        const charCode = this.getCharCodeFromEvent( event );
        const charStr = event.key ? event.key : String.fromCharCode( charCode );
        return this.isCharNumeric( charStr );
    }

}