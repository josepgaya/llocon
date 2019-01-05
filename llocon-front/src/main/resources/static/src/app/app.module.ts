import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { BrowserAnimationsModule } from "@angular/platform-browser/animations";
import { HttpClientModule } from '@angular/common/http';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RouteReuseStrategy } from '@angular/router';
import { ResponsiveModule } from 'ngx-responsive'

import { CoreModule } from "./core/core.module";
import { AppMaterialModule } from './core/material.module';
import { AppComponent } from './app.component';
import { AppLayout } from './app-layout';
import { AppRoutingModule } from "./app.routing";
import { AppRouteReuseStrategy } from "./core";

@NgModule( {
    imports: [
        BrowserModule,
        BrowserAnimationsModule,
        HttpClientModule,
        FormsModule,
        ReactiveFormsModule,
        AppRoutingModule,
        CoreModule,
        AppMaterialModule,
        ResponsiveModule.forRoot()
    ],
    declarations: [
        AppComponent,
        AppLayout
    ],
    providers: [
        { provide: RouteReuseStrategy, useClass: AppRouteReuseStrategy }],
    bootstrap: [
        AppComponent]
} )
export class AppModule { }
