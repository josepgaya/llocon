import { Component, ViewChild, ViewContainerRef } from "@angular/core";

import { ICellEditorAngularComp } from "ag-grid-angular";

@Component( {
    template: `
<input #input type="checkbox" [(ngModel)]="value" class="form-check-input position-static"/>
`,
    styles: [`
input {
    margin-left: 10px;
    margin-top: 6px;
}
`]
} )
export class BooleanEditorComponent implements ICellEditorAngularComp {

    public value: boolean;

    private params: any;

    @ViewChild( 'input', { read: ViewContainerRef } ) public input;

    agInit( params: any ): void {
        this.params = params;
        this.value = this.params.value;
    }

    getValue(): any {
        return this.value;
    }

    focusIn() {
        this.input.element.nativeElement.focus();
        return true;
    }

}