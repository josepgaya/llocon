import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AppMaterialModule } from "../../core/material.module";
import { DatagridModule } from "../../ui/datagrid/datagrid.module";
import { RestapiFormModule } from "../../ui/restapi-form/restapi-form.module";
import { ConnexioRoutingModule } from "./connexio-routing.module";
import { ConnexioGridComponent } from "./pages/connexio-grid.component";
import { ConnexioFormComponent } from "./pages/connexio-form.component";

@NgModule( {
    imports: [
        CommonModule,
        AppMaterialModule,
        DatagridModule,
        RestapiFormModule,
        ConnexioRoutingModule],
    declarations: [
        ConnexioGridComponent,
        ConnexioFormComponent]
} )
export class ConnexioModule {}