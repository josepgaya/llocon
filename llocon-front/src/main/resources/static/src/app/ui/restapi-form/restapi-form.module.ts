import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { FormsModule, ReactiveFormsModule } from '@angular/forms';

import { AppMaterialModule } from '../../core/material.module';
import { RestapiLovModule } from '../restapi-lov/restapi-lov.module';
import { DatagridModule } from '../datagrid/datagrid.module';
import { RestapiFormComponent } from "./restapi-form.component";
import { RestapiFieldComponent } from "./restapi-field.component";
import { RestapiCustomComponent } from "./restapi-custom.component";
import { RestapiMantenimentDirective } from "./restapi-manteniment.directive";

@NgModule( {
    imports: [
        CommonModule,
        FormsModule,
        ReactiveFormsModule,
        AppMaterialModule,
        RestapiLovModule,
        DatagridModule],
    declarations: [
        RestapiFormComponent,
        RestapiFieldComponent,
        RestapiCustomComponent,
        RestapiMantenimentDirective],
    exports: [
        RestapiFormComponent,
        RestapiFieldComponent,
        RestapiCustomComponent,
        RestapiMantenimentDirective],
    entryComponents: [
        RestapiFieldComponent],
    providers: []
} )
export class RestapiFormModule {}