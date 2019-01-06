import { Component, Injector, OnInit, ApplicationRef, ComponentFactoryResolver, EmbeddedViewRef, ComponentFactory, ComponentRef, ViewRef, ElementRef, Input, Output, EventEmitter, ViewChild, ViewContainerRef, ViewChildren, ContentChildren, QueryList } from "@angular/core";
import { HttpClient, HttpParams, HttpErrorResponse } from '@angular/common/http';
import { Validators, FormGroup, FormBuilder } from "@angular/forms";

import { Resource, ResourceHelper } from 'angular4-hal';
import { MdcSnackbar, MdcTabBar, MdcTabActivatedEvent } from '@angular-mdc/web';
import * as moment from "moment";

import {
    RestapiResource,
    RestapiField,
    RestapiGrid
} from "../../core";
import { RestapiService } from "../../core/restapi/restapi-service"
import { RestapiFieldComponent } from "./restapi-field.component"
import { RestapiCustomComponent } from "./restapi-custom.component"

@Component( {
    selector: 'restapi-form',
    template: `
<mdc-top-app-bar class="context-app-bar" fixed="true" prominent="false" dense="false">
    <mdc-top-app-bar-row>
        <mdc-top-app-bar-section align="start" title="{{id ? 'Modificar' : 'Crear'}} {{resourceName}}">
            <mdc-icon mdcTopAppBarActionItem (click)="onButtonCancelClick()">arrow_back</mdc-icon>
        </mdc-top-app-bar-section>
        <mdc-top-app-bar-section align="end">
            <mdc-icon mdcTopAppBarActionItem (click)="onButtonSaveClick()">save</mdc-icon>
        </mdc-top-app-bar-section>
    </mdc-top-app-bar-row>
</mdc-top-app-bar>
<form [formGroup]="formGroup">
    <mdc-tab-bar
        #formTabs
        *ngIf="showTabs"
        fixed="true"
        (activated)="logTab($event)">
        <mdc-tab-scroller>
            <mdc-tab label="Dades"></mdc-tab>
            <mdc-tab
                *ngFor="let grid of grids"
                [label]="grid.resourceName"></mdc-tab>
        </mdc-tab-scroller>
    </mdc-tab-bar>
    <div id="form-content">
        <div *ngIf="activeTab == 0">
            <ng-template #fieldsContainer></ng-template>
            <ng-content></ng-content>
        </div>
        <div
            *ngFor="let grid of grids; index as i">
            <div *ngIf="activeTab == i + 1">
                <datagrid
                    datagrid-mant
                    [config]="{resourceName: grid.resourceName, pare: {parentId: id}, height: 400}"></datagrid>
            </div>
        </div>
    </div>
</form>`,
    styles: [`
mdc-top-app-bar {
    position: unset;
    display: block;
}
#form-content {
    margin-top: 1em;
}
#actions {
    display: flex;
    flex-direction: row-reverse;
}`],
    providers: [RestapiService]
} )
export class RestapiFormComponent implements OnInit {

    @Input() resourceName: string;
    @Input() pare: any;
    @Input() id: string;
    @Input()
    set data( data: any ) {
        this.refrescarFormGroup( data );
    }

    @Output() actionSave: EventEmitter<any> = new EventEmitter();
    @Output() actionCancel: EventEmitter<any> = new EventEmitter();

    @ViewChild( 'fieldsContainer', { read: ViewContainerRef } ) fieldsContainer: ViewContainerRef;
    @ContentChildren( RestapiCustomComponent ) customInputs: QueryList<RestapiCustomComponent>;
    @ViewChild( 'formTabs' ) formTabs: MdcTabBar;

    private resourceId: any;
    private resourceInstance: any;
    private resourceConfig: RestapiResource;
    private formGroup: FormGroup;
    private inputComponentFactory: ComponentFactory<RestapiFieldComponent>;
    private inputFields: RestapiFieldComponent[];

    private showTabs: boolean = false;
    private activeTab: number = 0;
    private grids: RestapiGrid[];

    ngOnInit() {
        this.refreshFields();
    }

    onButtonSaveClick() {
        let baseValues;
        if ( this.resourceInstance ) {
            baseValues = {
                _links: this.resourceInstance._links,
                id: this.resourceInstance.id
            }
        } else {
            baseValues = {}
        }
        let values = Object.assign( baseValues, this.formGroup.value );
        this.resourceConfig.fields.forEach( field => {
            if ( field.type === 'LOV' ) {
                if ( !values[field.name].id ) {
                    delete values[field.name];
                }
            }
        } );
        if ( this.isCreacio() ) {
            let uri = ResourceHelper.getURL() + this.restapiService["resource"];
            let params = new HttpParams( { fromObject: this.pare } );
            this.http.post( uri, values, { params: params } ).subscribe(( resource: Resource ) => {
                this.actionSave.emit();
            }, errorResponse => {
                this.processSaveErrors( errorResponse );
            } );
        } else {
            this.restapiService.update( values ).subscribe(( resource: Resource ) => {
                this.refrescarFormGroup( resource );
                //this.formatDateValues();
                this.actionSave.emit();
            }, errorResponse => {
                this.processSaveErrors( errorResponse );
            } );
        }
    }
    onButtonCancelClick() {
        this.actionCancel.emit();
    }

    refreshFields() {
        this.restapiService.configure( this.resourceName ).subscribe(( restapiResource: RestapiResource ) => {
            this.resourceConfig = restapiResource;
            if ( restapiResource.grids && this.id ) {
                this.showTabs = true;
                this.grids = restapiResource.grids;
            }
            if ( this.id == null ) {
                this.refrescarFormGroup();
                this.createInputs( restapiResource.fields );
            } else {
                this.restapiService.get( this.generateResourceGetParams( this.id, this.pare ) ).subscribe(( resource: Resource ) => {
                    this.refrescarFormGroup( resource );
                    this.createInputs( restapiResource.fields );
                } );
            }
        } );
    }
    createInputs( fields: RestapiField[] ) {
        this.inputFields = [];
        if ( fields && fields.length ) {
            fields.forEach( field => {
                if ( !field.hiddenInForm ) {
                    let inputRef: ComponentRef<RestapiFieldComponent>;
                    if ( this.customInputs && this.customInputs.length ) {
                        let customInput = this.customInputs.toArray().find( function( customInput ) {
                            return customInput.fieldName === field.name;
                        } );
                        if ( customInput ) {
                            inputRef = this.inputComponentFactory.create( this.injector );
                            customInput.contentRef.clear();
                            customInput.contentRef.insert( inputRef.hostView );
                        }
                    } else {
                        inputRef = this.fieldsContainer.createComponent( this.inputComponentFactory );
                    }
                    if ( inputRef ) {
                        inputRef.instance.field = field;
                        inputRef.instance.formGroup = this.formGroup;
                        this.inputFields.push( inputRef.instance );
                    }
                }
            } );
        }
    }

    refrescarFormGroup( resourceInstance?: any ) {
        if ( resourceInstance ) {
            this.resourceInstance = resourceInstance;
        } else {
            this.resourceInstance = {};
        }
        this.formGroup = this.createFormGroup( this.resourceInstance, this.resourceConfig );
    }

    createFormGroup( resourceInstance, resourceConfig: RestapiResource ): FormGroup {
        let formControls = {};
        for ( let field of resourceConfig.fields ) {
            let validators = [];
            if ( field.minLength ) {
                validators.push( Validators.minLength( field.minLength ) );
            }
            if ( field.maxLength ) {
                validators.push( Validators.maxLength( field.maxLength ) );
            }
            if ( field.required ) {
                validators.push( Validators.required );
            }
            let fieldDisabled = (this.isCreacio) ? field.disabledForCreate : field.disabledForUpdate;
            formControls[field.name] = [{ value: resourceInstance[field.name], disabled: fieldDisabled }, validators];
        }
        return this.formBuilder.group( formControls );
    }
    generateResourceGetParams( id, pare ) {
        let urlParams = '';
        if ( pare ) {
            urlParams = '?' + JSON.stringify( pare ).replace( /\"|\}|\{/g, '' ).replace( /\:/g, '=' ).replace( /\,/g, '&' );
        }
        return id + urlParams;
    }

    processSaveErrors( errorResponse: HttpErrorResponse ) {
        if ( errorResponse.error.errors ) {
            for ( let error of errorResponse.error.errors ) {
                this.inputFields.forEach( inputField => {
                    if ( inputField.field.name === error.field ) {
                        inputField.setValid( false, error.defaultMessage );
                    }
                } );
            }
            let isFirst = true;
            this.inputFields.forEach( inputField => {
                if ( isFirst ) {
                    inputField.focus();
                    isFirst = false;
                }
            } );
        }
    }

    public isCreacio(): boolean {
        return this.id == null;
    }

    logTab( event: MdcTabActivatedEvent ): void {
        this.activeTab = event.index;
    }

    /*formatDateValues() {
        if ( this.fields ) {
            this.fields.forEach( field => {
                if ( field.type == "DATE" ) {
                    let val = this.resourceInstance[field.name];
                    if ( val ) {
                        this.resourceInstance[field.name] = moment( val ).format( 'YYYY-MM-DD' );
                    }
                }
            } );
        }
    }*/

    constructor(
        private factoryResolver: ComponentFactoryResolver,
        private formBuilder: FormBuilder,
        private injector: Injector,
        private appRef: ApplicationRef,
        private http: HttpClient,
        private restapiService: RestapiService,
        private snackbar: MdcSnackbar ) {
        this.inputComponentFactory = this.factoryResolver.resolveComponentFactory( RestapiFieldComponent );
        this.formGroup = this.formBuilder.group( {} );
    }

}
