import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AppMaterialModule } from "../../core/material.module";
import { DatagridModule } from "../../ui/datagrid/datagrid.module";
import { RestapiFormModule } from "../../ui/restapi-form/restapi-form.module";
import { LloguerRoutingModule } from "./lloguer-routing.module";
import { LloguerGridComponent } from "./pages/lloguer-grid.component";
import { LloguerFormComponent } from "./pages/lloguer-form.component";
import { SubministramentFormComponent } from "./pages/subministrament-form.component";

@NgModule( {
    imports: [
        CommonModule,
        AppMaterialModule,
        DatagridModule,
        RestapiFormModule,
        LloguerRoutingModule],
    declarations: [
        LloguerGridComponent,
        LloguerFormComponent,
        SubministramentFormComponent]
} )
export class LloguerModule {}