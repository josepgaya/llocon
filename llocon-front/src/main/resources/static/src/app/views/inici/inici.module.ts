import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { ContextTopbarModule } from "../../ui/context-topbar/context-topbar.module";
import { InfiniteListModule } from "../../ui/infinite-list/infinite-list.module";
import { AppMaterialModule } from "../../core/material.module";
import { IniciRoutingModule } from "./inici-routing.module";
import { LloguerListComponent } from "./pages/lloguer-list.component";
import { LloguerDetailComponent } from "./pages/lloguer-detail.component";

@NgModule( {
    imports: [
        CommonModule,
        AppMaterialModule,
        ContextTopbarModule,
        InfiniteListModule,
        IniciRoutingModule],
    declarations: [
        LloguerListComponent,
        LloguerDetailComponent]
} )
export class IniciModule { }
