import { Component, OnInit } from '@angular/core';

import { UsuarisService } from './usuaris.service';

@Component( {
    template: `
    <bng-datagrid
        bng-datagrid-mant
        [config]="datagridConfig"
        [restapiService]="usuarisService"></bng-datagrid>`
} )
export class UsuarisGridComponent implements OnInit {

    datagridConfig = {
        //editable: true,
        columnFiltersEnabled: true
    };

    ngOnInit() {
    }

    constructor(
        public usuarisService: UsuarisService ) { }

}