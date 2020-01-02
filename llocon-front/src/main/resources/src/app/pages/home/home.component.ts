import { Component, OnInit } from '@angular/core';
import { BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

import { MenuService, AppMenuItem } from '../../shared/menu.service';

@Component( {
    template: `
<div *ngIf="!mobileScreen" class="mat-display-3 centered" style="text-align: center">
	<p style="margin:0"><mat-icon inline="true" style="font-size:100px;">cloud_queue</mat-icon></p>
    <p style="margin:.4em">{{'home.salutacio'|translate}}</p>
	<!--div class="example-button-row">
		<button mat-raised-button>Basic</button>&nbsp;
		<button mat-raised-button color="primary">Primary</button>&nbsp;
		<button mat-raised-button color="accent">Accent</button>&nbsp;
		<button mat-raised-button color="warn">Warn</button>&nbsp;
		<button mat-raised-button disabled>Disabled</button>&nbsp;
		<a mat-raised-button routerLink=".">Link</a>
	</div-->
</div>
<ng-container *ngIf="mobileScreen">
    <p *ngFor="let item of allowedMenuItems" style="text-align:center">
        <a mat-stroked-button [routerLink]="item.route" color="primary" style="margin:1em; width:200px">
            <mat-icon>{{item.icon}}</mat-icon>{{item.label}}
        </a>
    </p>
</ng-container>`
} )
export class HomeComponent implements OnInit {

    mobileScreen: boolean;
    allowedMenuItems: AppMenuItem[];

    ngOnInit() {
        //this.allowedMenuItems = this.menuService.getAllowedMenuItems();
    }

    constructor(
        private screenSizeService: BngScreenSizeService,
        private menuService: MenuService ) {
        // Es subscriu al subject de canvi de tamany de la pantalla
        this.mobileScreen = this.screenSizeService.isMobile();
        this.screenSizeService.getScreenSizeChangeSubject().subscribe(( event: BngScreenSizeChangeEvent ) => {
            this.mobileScreen = event.mobile
        } );
    }

}