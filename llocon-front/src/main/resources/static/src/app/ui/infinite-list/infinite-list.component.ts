import { Component, OnInit, Input, Output, EventEmitter, TemplateRef } from "@angular/core";

import { RestapiResource } from "../../core";
import { RestapiService } from "../../core/restapi/restapi-service"

@Component( {
    selector: 'infinite-list',
    template: `
<ng-template #defaultItemTemplate let-item="item">
    <mdc-list-item-text >{{item}}</mdc-list-item-text>
</ng-template>
<div [ngClass]="{'items-list': true, 'clickable': false}"
    infiniteScroll
    [infiniteScrollDistance]="1"
    [infiniteScrollThrottle]="150"
    (scrolled)="onScroll()">
    <mdc-list
        [twoLine]="twoLine"
        [avatar]="avatar"
        [border]="border"
        [interactive]="interactive"
        (selectionChange)="onSelectionChange($event)"
        [class]="roundedIcons ? 'rounded-icons' : ''">
        <mdc-list-item *ngFor="let item of listItems" (click)="onClick($event)">
            <ng-container *ngTemplateOutlet="itemTemplate ? itemTemplate: defaultItemTemplate; context: {item: item}"></ng-container>
        </mdc-list-item>
    </mdc-list>
</div>`,
    styles: [`
mdc-list {
   padding: 0;
}
mdc-list-item {
   border-left: none !important;
   border-right: none !important;
}
.items-list.clickable {
    cursor: pointer;
}
`],
    providers: [
        RestapiService]
} )
export class InfiniteListComponent implements OnInit {

    @Input() resourceName: string;
    @Input() sortProperty: string;
    @Input() sortDirection: string = 'asc';
    @Input() parent: any;
    @Input() twoLine: boolean = true;
    @Input() avatar: boolean = true;
    @Input() border: boolean = true;
    @Input() interactive: boolean = true;
    @Input() pageSize: number = 20;
    @Input() selectable: boolean = false;
    @Input() roundedIcons: boolean = false;
    @Input() itemTemplate: TemplateRef<any>;

    @Output() itemClick: EventEmitter<any> = new EventEmitter();
    @Output() selectionChange: EventEmitter<any> = new EventEmitter();

    private currentPageNum: number = 0;
    private listItems = [];

    ngOnInit() {
        this.restapiService.configure( this.resourceName ).subscribe(() => {
            this.loadNextPage();
        } );
    }

    onScroll() {
        this.loadNextPage();
    }

    onSelectionChange( event ) {
        if ( this.selectable ) {
            let node = event.option.elementRef.nativeElement;
            for ( var i = 0; ( node = node.previousSibling ); i++ );
            let index = i - 1;
            //console.log('>>> onSelectionChange', i, event.option.elementRef);
            event.option.selected = !event.option.selected;
            let item = this.listItems[index];
            /*if (!seleccionat) {
                console.log('>>> Seleccionat', i, this.selectedItems[i]);
            } else {
                console.log('>>> Deseleccionat', i, this.selectedItems[i]);
            }*/
            this.selectionChange.emit( { item: item, selected: event.option.selected, index: index } );
        }
    }

    onClick( event ) {
        let itemElement = event.target.closest( 'mdc-list-item' );
        let parentList = itemElement.closest( 'mdc-list' );
        let parentItems = Array.prototype.slice.call( parentList.children );
        let index = parentItems.indexOf( itemElement );
        let item = this.listItems[index];
        this.itemClick.emit( item );
    }

    loadNextPage() {
        let requestParams = [{
            key: 'page',
            value: '' + this.currentPageNum
        }];
        if ( this.sortProperty ) {
            requestParams.push( {
                key: 'sort',
                value: this.sortProperty + ',' + this.sortDirection
            } );
        } else {
            requestParams.push( {
                key: 'sort',
                value: 'id,' + this.sortDirection
            } );
        }
        if ( this.parent ) {
            for ( var property in this.parent ) {
                if ( this.parent.hasOwnProperty( property ) ) {
                    requestParams.push( { key: property, value: this.parent[property] } );
                }
            }
        }
        this.restapiService.getAll( {
            size: this.pageSize,
            params: requestParams,
        } ).subscribe(( resources: any ) => {
            resources.forEach(( element ) => {
                this.listItems.push( element );
            } );
            this.currentPageNum++;
        }, error => {
            // Suposarem que l'error és causat per no retornar cap resultat
        } );
    }

    public refresh( data?: any, index?: number ) {
        if ( data && this.listItems ) {
            if ( index !== undefined ) {
                this.listItems[index] = data;
            } else if ( data.id ) {
                for ( var i = 0; i < this.listItems.length; i++ ) {
                    if ( data.id === this.listItems[i].id ) {
                        this.listItems[i] = data;
                        break;
                    }
                }
            }
        } else {
            this.currentPageNum = 0;
            this.listItems = [];
            this.loadNextPage();
        }
    }

    constructor(
        private restapiService: RestapiService ) {
    }

}
