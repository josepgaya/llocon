import { Component } from "@angular/core";
import { Router } from "@angular/router";
import { registerLocaleData } from '@angular/common';
import localeCa from '@angular/common/locales/ca';

registerLocaleData( localeCa );

@Component( {
    template: `
<infinite-list
    resourceName="lloguer"
    roundedIcons="true"
    [itemTemplate]="lloguerItemTemplate"
    (itemClick)="onListItemClick($event)"></infinite-list>
<ng-template #lloguerItemTemplate let-item="item">
    <mdc-icon mdcListItemGraphic>domain</mdc-icon>
    <mdc-list-item-text secondaryText="{{item.adressa}}">{{item.nom}}</mdc-list-item-text>
    <!--mdc-icon mdcListItemMeta>info</mdc-icon-->
    <p mdcListItemMeta style="color:grey">{{item.importPendent | currency:'EUR':'symbol':'1.2-2':'ca'}}</p>
</ng-template>`
} )
export class LloguerListComponent {

    onListItemClick( item ) {
        let targetRoute = this.router.url;
        targetRoute += '/' + item.id + '/detail';
        this.router.navigate( [targetRoute] );
    }

    constructor(
        private router: Router ) { }

}
