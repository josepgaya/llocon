import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppMaterialModule } from '../../core/material.module';
import { DatagridModule } from "../../ui/datagrid/datagrid.module";
import { RestapiLovComponent } from "./restapi-lov.component";
import { RestapiLovDialogComponent } from "./restapi-lov-dialog.component";

@NgModule( {
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        AppMaterialModule,
        DatagridModule],
    declarations: [
        RestapiLovComponent,
        RestapiLovDialogComponent],
    entryComponents: [
        RestapiLovDialogComponent
    ],
    exports: [
        RestapiLovComponent],
    providers: []
} )
export class RestapiLovModule {}