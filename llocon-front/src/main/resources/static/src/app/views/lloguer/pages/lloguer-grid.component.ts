import { Component, Injector } from "@angular/core";

import { DatagridConfig, DatagridEventParams } from "../../../ui/datagrid/datagrid.component";

@Component( {
    template: `
<!--mdc-top-app-bar class="context-app-bar" fixed="false" prominent="false" dense="false">
    <mdc-top-app-bar-row>
        <mdc-top-app-bar-section align="start">
            <button mdcIconButton mdcTopAppBarNavIcon icon="arrow_back"></button>
            <p>Lloguers</p>&nbsp;
            <p class="small">1 - 2 / 2</p>
        </mdc-top-app-bar-section>
        <mdc-top-app-bar-section align="end">
            <mdc-icon mdcTopAppBarActionItem>refresh</mdc-icon>
            <mdc-icon mdcTopAppBarActionItem>add</mdc-icon>
            <mdc-icon mdcTopAppBarActionItem>add_box</mdc-icon>
            <mdc-icon mdcTopAppBarActionItem>edit</mdc-icon>
            <mdc-icon mdcTopAppBarActionItem>delete</mdc-icon>
            <mdc-text-field
                label="Filtre"
                class="text-field-search"
                outlined dense>
                <mdc-icon mdcTextFieldIcon leading>search</mdc-icon>
            </mdc-text-field>
        </mdc-top-app-bar-section>
    </mdc-top-app-bar-row>
</mdc-top-app-bar-->
<datagrid
    datagrid-mant
    [config]="datagridConfig"
    (rowClick)="onDatagridRowClick($event)"
    (rowDoubleClick)="onDatagridRowDoubleClick($event)"></datagrid>`,
    styles: [`
mdc-top-app-bar-section p {
    font-size: 20px;
    font-weight: 500
}
mdc-top-app-bar-section p.small {
    font-size: 12px;
}
mdc-top-app-bar {
    position: unset;
    display: block;
}`]
} )
export class LloguerGridComponent {

    private datagridConfig: DatagridConfig = {
        resourceName: 'lloguer',
        headerTitle: 'Lloguers',
        showTopBar: true
        /*columns: [{
            field: 'codi',
            headerName: 'Codi',
            editable: true
        }, {
            field: 'nom',
            headerName: 'Nom',
            editable: true
        }, {
            field: 'adressa',
            headerName: 'Adreça',
            editable: true
        }, {
            headerName: 'Subministraments',
            sortable: false,
            editable: false
        }, {
            headerName: 'Pendent',
            sortable: false,
            editable: false
        }],
        readOnly: false,
        editable: false*/
    }

    onDatagridRowClick( params: DatagridEventParams ) {
        //console.log('>>> onDatagridRowClick', params);
    }
    onDatagridRowDoubleClick( params: DatagridEventParams ) {
        //console.log('>>> onDatagridRowDoubleClick', params);
    }

}