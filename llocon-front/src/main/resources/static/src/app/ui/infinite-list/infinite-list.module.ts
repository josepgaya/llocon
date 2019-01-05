import { NgModule } from "@angular/core";
import { CommonModule } from "@angular/common";
import { InfiniteScrollModule } from 'ngx-infinite-scroll'

import { AppMaterialModule } from '../../core/material.module';
import { InfiniteListComponent } from "./infinite-list.component";

@NgModule( {
    imports: [
        CommonModule,
        AppMaterialModule,
        InfiniteScrollModule],
    declarations: [
        InfiniteListComponent],
    exports: [
        InfiniteListComponent],
    entryComponents: [
        InfiniteListComponent],
    providers: []
} )
export class InfiniteListModule {}