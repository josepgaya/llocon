import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";

import { AppMaterialModule } from '../../core/material.module';
import { ContextTopbarComponent } from "./context-topbar.component";

@NgModule( {
    imports: [
        CommonModule,
        AppMaterialModule],
    declarations: [
        ContextTopbarComponent],
    exports: [
        ContextTopbarComponent]
} )
export class ContextTopbarModule {}