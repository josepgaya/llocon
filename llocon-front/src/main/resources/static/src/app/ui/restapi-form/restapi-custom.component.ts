import { Component, Input, ViewChild, ViewContainerRef } from "@angular/core";

@Component( {
    selector: 'restapi-custom',
    template: '<ng-template #content></ng-template>'
} )
export class RestapiCustomComponent {

    @Input() fieldName: string;
    
    @ViewChild( 'content', { read: ViewContainerRef } ) contentRef: ViewContainerRef;

}
