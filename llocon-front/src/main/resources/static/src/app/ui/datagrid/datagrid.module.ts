import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from '@angular/forms';

import { AgGridModule } from "ag-grid-angular";

import { ContextTopbarModule } from '../context-topbar/context-topbar.module';
import { AppMaterialModule } from '../../core/material.module';
import { DatagridComponent } from "./datagrid.component";
import { DatagridRowActionsComponent } from "./datagrid-row-actions.component";
import { BooleanEditorComponent } from "./boolean-editor.component";
import { NumericEditorComponent } from "./numeric-editor.component";
import { DatagridTopBarComponent } from "./datagrid-topbar.component";
import { DatagridHeaderComponent } from "./datagrid-header.component";
import { DatagridMantenimentDirective } from "./datagrid-manteniment.directive";

@NgModule( {
    imports: [
        CommonModule,
        FormsModule,
        ContextTopbarModule,
        AppMaterialModule,
        AgGridModule.withComponents( [
            DatagridHeaderComponent,
            BooleanEditorComponent,
            NumericEditorComponent
        ] )],
    declarations: [
        DatagridComponent,
        DatagridRowActionsComponent,
        DatagridTopBarComponent,
        DatagridHeaderComponent,
        BooleanEditorComponent,
        NumericEditorComponent,
        DatagridMantenimentDirective],
    exports: [
        DatagridComponent,
        DatagridMantenimentDirective]
} )
export class DatagridModule {}