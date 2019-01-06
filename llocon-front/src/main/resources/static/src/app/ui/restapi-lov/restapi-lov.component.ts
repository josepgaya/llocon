import { Component, Input, Output, EventEmitter, ElementRef, ViewChild, AfterViewInit } from "@angular/core";
import { FormGroup, FormBuilder } from "@angular/forms";
import { MdcDialog, MdcDialogComponent, MdcDialogRef, MdcTextField } from '@angular-mdc/web';
import { Resource } from 'angular4-hal';

import { RestapiService } from "../../core/restapi/restapi-service";
import { RestapiResource, RestapiField } from "../../core";
import { RestapiLovDialogComponent } from "./restapi-lov-dialog.component";

@Component( {
    selector: 'restapi-lov',
    template: `
<div *ngIf="formGroupConfigured" [formGroup]="formGroup" style="display:flex; justify-content: space-between;width: 100%">
    <ng-container [formGroupName]="formGroupName">
        <input
            #lovHiddenInput
            *ngIf="!twoFields"
            type="hidden"
            formControlName="id"/>
        <mdc-text-field
            *ngIf="twoFields"
            #lovValueInput
            [label]="twoFields ? label: undefined"
            outlined dense
            formControlName="id"
            [required]="required"
            [helperText]="helperText"
            style="flex-basis:30%"
            (blur)="onLovInputBlur($event)"
            (change)="onLovInputChange($event)">
            <mdc-icon  *ngIf="twoFields" mdcTextFieldIcon trailing clickable (click)="onLovIconClick($event)">expand_more</mdc-icon>
        </mdc-text-field>
        <mdc-text-field
            #lovDetailInput
            *ngIf="detailFieldName"
            [label]="!twoFields ? label: undefined"
            [formControlName]="detailFieldName"
            [ngStyle]="{ 'flex-basis': (twoFields === true) ? '69%' : '100%'}"
            [required]="!twoFields ? required: false"
            [helperText]="helperText"
            outlined
            dense>
            <mdc-icon *ngIf="!twoFields" mdcTextFieldIcon trailing clickable (click)="onLovIconClick($event)">expand_more</mdc-icon></mdc-text-field>
    </ng-container>
</div>
<mdc-helper-text
    #helperText
    validation>{{errorMessage}}</mdc-helper-text>`,
    providers: [RestapiService]
} )
export class RestapiLovComponent implements AfterViewInit {

    @Input() label;
    @Input() formGroup: FormGroup;
    @Input()
    set field( field: RestapiField ) {
        this.formGroupName = field.name;
        this.lovResourceName = field.lovResource;
        this.restapiService.configure( field.lovResource ).subscribe(( resourceConfig: RestapiResource ) => {
            this.detailFieldName = field.lovDetailFieldName;
            resourceConfig.fields.forEach(( field: RestapiField ) => {
                if ( !field.hiddenInLov ) {
                    this.dialogColumns.push( {
                        field: field.name,
                        headerName: field.name,
                        editable: false
                    } );
                }
            } );
            this.formControlDisabled = this.formGroup.controls[this.formGroupName].disabled;
            this.updateLovValues( this.formGroup.value[this.formGroupName] );
        } );
        this.required = field.required;
        this.twoFields = field.lovWithDetailInput;
    }
    @Input() parent;

    @Output() lovChange: EventEmitter<any> = new EventEmitter();

    @ViewChild( 'lovHiddenInput' ) lovHiddenInput: ElementRef;
    @ViewChild( 'lovValueInput' ) lovValueInput: MdcTextField;
    @ViewChild( 'lovDetailInput' ) lovDetailInput: MdcTextField;

    private formGroupName: string;
    private formGroupConfigured: boolean = false;
    private formControlDisabled: boolean;
    private lovResourceName: string;
    private detailFieldName: string;
    private dialogColumns: any[] = [];
    private required: boolean;
    private twoFields: boolean = false;
    private canBlur: boolean = true;
    private errorMessage: string;

    ngAfterViewInit() {
        if ( this.lovValueInput ) {
            let valueNativeElement = this.lovValueInput.elementRef.nativeElement;
            let inputElement = valueNativeElement.getElementsByTagName( "INPUT" )[0];
            inputElement.addEventListener( "focus", this.onLovInputFocus );
        }
    }

    onLovInputFocus( event ) {
        event.target.setSelectionRange( 0, event.target.value.length );
    }
    onLovInputBlur( event ) {
        if ( this.lovValueInput.value && this.lovValueInput.value.length ) {
            if ( !this.canBlur ) {
                this.lovValueInput.focus();
            }
        } else {
            this.canBlur = true;
        }
    }
    onLovInputChange( event ) {
        this.lovDetailInput.value = undefined;
        if ( this.lovValueInput.value && this.lovValueInput.value.length ) {
            this.canBlur = false;
            this.restapiService.get( this.lovValueInput.value ).subscribe(
                ( resource: Resource ) => {
                    this.lovDetailInput.value = resource[this.detailFieldName];
                    this.canBlur = true;
                },
                error => {
                    if ( error.status == 404 ) {
                        this.lovValueInput.focus();
                    }
                } );
        }
        return true;
    }

    onLovIconClick() {
        let dialogRef = this.dialog.open( RestapiLovDialogComponent, {
            escapeToClose: true,
            clickOutsideToClose: true,
            data: {
                resourceName: this.lovResourceName,
                columns: this.dialogColumns,
                parent: this.parent
            }
        } );
        dialogRef.afterClosed().subscribe( selectedRowData => {
            if ( selectedRowData !== 'close' ) {
                this.updateLovValues( selectedRowData );
                this.lovChange.emit( selectedRowData );
            }
        } );
    }

    updateLovValues( lovValue?: any ) {
        let valueId = (lovValue) ? lovValue['id'] : null;
        let valueDetail = (lovValue) ? lovValue[this.detailFieldName] : null;
        let lovControls = {};
        lovControls['id'] = {value: valueId, disabled: this.formControlDisabled};
        lovControls[this.detailFieldName] = {value: valueDetail, disabled: this.formControlDisabled || this.twoFields};
        this.formGroup.removeControl( this.formGroupName);
        this.formGroup.setControl( this.formGroupName, this.formBuilder.group( lovControls ) );
        this.formGroupConfigured = true;
        this.canBlur = true;
    }

    public setValid( valid: boolean, errorMessage: string ) {
        if ( this.lovValueInput ) {
            this.lovValueInput.valid = valid;
        }
        if ( this.lovDetailInput ) {
            this.lovDetailInput.valid = valid;
        }
        this.errorMessage = errorMessage;
    }
    public focus() {
        this.lovValueInput.focus();
    }

    constructor(
        private formBuilder: FormBuilder,
        private restapiService: RestapiService,
        private dialog: MdcDialog ) { }

}