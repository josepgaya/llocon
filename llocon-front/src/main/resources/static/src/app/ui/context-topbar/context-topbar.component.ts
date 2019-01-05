import { Component, Input, TemplateRef, OnDestroy } from "@angular/core";

import { AppService } from '../../core';

@Component( {
    selector: 'context-topbar',
    template: `
<mdc-top-app-bar [ngClass]="{'context-app-bar': true, 'secondary': !isScreenSmall()}" fixed="false" prominent="false" dense="false">
    <mdc-top-app-bar-row>
        <ng-container *ngTemplateOutlet="template"></ng-container>
    </mdc-top-app-bar-row>
</mdc-top-app-bar>`,
    styles: [`
mdc-top-app-bar.secondary {
    position: unset;
    display: block;
}`]
} )
export class ContextTopbarComponent implements OnDestroy {

    @Input() template: TemplateRef<any>;

    ngOnDestroy() {
        this.appService.setContextTopBar( false );
    }

    isScreenSmall(): boolean {
        return this.appService.isScreenSmall();
    }

    constructor(
        private appService: AppService ) {
        appService.setContextTopBar( true );
    }

}
