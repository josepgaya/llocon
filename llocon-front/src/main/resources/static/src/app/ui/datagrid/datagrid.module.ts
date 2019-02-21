import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule } from '@angular/forms';

import { AgGridModule } from "ag-grid-angular";

import { ContextTopbarModule } from '../context-topbar/context-topbar.module';
import { AppMaterialModule } from '../../core/material.module';
import { DatagridComponent } from "./datagrid.component";
import { DatagridActionsRendererComponent } from "./datagrid-actions-renderer.component";
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
            DatagridActionsRendererComponent,
            BooleanEditorComponent,
            NumericEditorComponent
        ] )],
    declarations: [
        DatagridComponent,
        DatagridTopBarComponent,
        DatagridHeaderComponent,
        DatagridActionsRendererComponent,
        BooleanEditorComponent,
        NumericEditorComponent,
        DatagridMantenimentDirective],
    exports: [
        DatagridComponent,
        DatagridMantenimentDirective]
} )
export class DatagridModule {}