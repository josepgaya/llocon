import { Component, Injector } from "@angular/core";

import { DatagridConfig, DatagridEventParams } from "../../../ui/datagrid/datagrid.component";

@Component( {
    template: `
<datagrid
    datagrid-mant
    [config]="datagridConfig"
    (rowClick)="onDatagridRowClick($event)"
    (rowDoubleClick)="onDatagridRowDoubleClick($event)"></datagrid>`
} )
export class ConnexioGridComponent {

    private datagridConfig: DatagridConfig = {
        resourceName: 'connexio',
        headerTitle: 'Connexions',
        showTopBar: true/*,
        columns: [{
            field: 'nom',
            headerName: 'Nom',
            editable: true
        }, {
            field: 'proveidor',
            headerName: 'Proveidor',
            editable: true
        }, {
            field: 'usuari',
            headerName: 'Usuari',
            editable: false
        }]*/
    }

    onDatagridRowClick( params: DatagridEventParams ) {
        //console.log('>>> onDatagridRowClick', params);
    }
    onDatagridRowDoubleClick( params: DatagridEventParams ) {
        //console.log('>>> onDatagridRowDoubleClick', params);
    }

}