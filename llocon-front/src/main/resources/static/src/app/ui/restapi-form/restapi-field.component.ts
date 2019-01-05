import { Component, Input, TemplateRef, ViewChild, AfterViewInit } from "@angular/core";
import { FormGroup } from "@angular/forms";

import { MdcTextField, MdcTextarea, MdcSelect } from '@angular-mdc/web';

import { RestapiField } from "../../core";
import { RestapiLovComponent } from "../restapi-lov/restapi-lov.component";

@Component( {
    selector: 'restapi-field',
    template: `
<div [formGroup]="formGroup">
    <ng-container *ngIf="field.type === 'STRING' || field.type === 'PASSWORD'">
        <mdc-text-field
            [type]="field.type === 'PASSWORD' ? 'password' : 'text'"
            [label]="generateInputTitle(field.name)"
            outlined dense
            [minlength]="field.minLength"
            [maxlength]="field.maxLength"
            [formControlName]="field.name"
            [required]="field.required"
            [helperText]="helperText"></mdc-text-field>
        <mdc-helper-text
            #helperText
            validation>{{errorMessage}}</mdc-helper-text>
    </ng-container>
    <ng-container *ngIf="field.type === 'TEXTAREA'">
        <mdc-textarea
            [label]="generateInputTitle(field.name)"
            outlined dense
            [minlength]="field.minLength"
            [maxlength]="field.maxLength"
            [formControlName]="field.name"
            [required]="field.required"
            [helperText]="helperText"></mdc-textarea>
        <mdc-helper-text
            #helperText
            validation>{{errorMessage}}</mdc-helper-text>
    </ng-container>
    <ng-container *ngIf="field.type === 'INTEGER' || field.type ==='FLOAT' || field.type === 'BIGDECIMAL'">
        <mdc-text-field
            type="number"
            [label]="generateInputTitle(field.name)"
            [step]="field.type === 'INTEGER' ? 1 : 0.01"
            outlined dense
            [formControlName]="field.name"
            [required]="field.required"
            [helperText]="helperText"></mdc-text-field>
        <mdc-helper-text
            #helperText
            validation>{{errorMessage}}</mdc-helper-text>
    </ng-container>
    <ng-container *ngIf="field.type === 'DATE'">
        <mdc-text-field
            type="date"
            [label]="generateInputTitle(field.name)"
            outlined dense
            [formControlName]="field.name"
            [required]="field.required"
            [helperText]="helperText">
            <mdc-icon mdcTextFieldIcon trailing>event</mdc-icon>
        </mdc-text-field>
        <mdc-helper-text
            #helperText
            validation>{{errorMessage}}</mdc-helper-text>
    </ng-container>
    <ng-container *ngIf="field.type === 'BOOLEAN'">
        <mdc-form-field>
            <mdc-checkbox
                (input)="change()"
                [formControlName]="field.name"></mdc-checkbox>
            <label>{{generateInputTitle(field.name)}}</label>
        </mdc-form-field>
    </ng-container>
    <ng-container *ngIf="field.type === 'ENUM'">
        <mdc-select
            [placeholder]="generateInputTitle(field.name)"
            outlined dense
            [ngClass]="{'mdc-select--dense': true}"
            [formControlName]="field.name"
            [required]="field.required"
            [helperText]="helperText">
            <option value=""></option>
            <option *ngFor="let enumValue of field.enumValues" [value]="enumValue">{{enumValue}}</option>
        </mdc-select>
        <mdc-helper-text
            #helperText
            validation>{{errorMessage}}</mdc-helper-text>
    </ng-container>
    <ng-container *ngIf="field.type === 'LOV'">
        <restapi-lov
            [label]="generateInputTitle(field.name)"
            [formGroup]="formGroup"
            [field]="field"></restapi-lov>
    </ng-container>
</div>`
} )
export class RestapiFieldComponent implements AfterViewInit {

    @Input() field: RestapiField;
    @Input() formGroup: FormGroup;
    @Input() lovDetail: string;

    @ViewChild( MdcTextField ) mdcTextField: MdcTextField;
    @ViewChild( MdcTextarea ) mdcTextarea: MdcTextarea;
    @ViewChild( MdcSelect ) mdcSelect: MdcSelect;
    @ViewChild( RestapiLovComponent ) lovComponent: RestapiLovComponent;

    private valid: boolean = true;
    private errorMessage: string;

    ngAfterViewInit() {
        // Per a evitar el bug del component MdcSelect que no selecciona de forma
        // automàtica el valor del formControl
        setTimeout(() => {
            let fieldValue = this.formGroup.value[this.field.name];
            if ( this.mdcSelect && fieldValue ) {
                this.mdcSelect.value = fieldValue
            }
            if ( this.field.type === 'DATE' && this.mdcTextField && fieldValue ) {
                this.mdcTextField.value = fieldValue
            }
        });
    }

    generateInputTitle( fieldName ) {
        return fieldName;
    }

    public setValid( valid: boolean, errorMessage: string = null ) {
        if ( this.mdcTextField ) {
            this.mdcTextField.valid = valid;
        } else if ( this.mdcTextarea ) {
            this.mdcTextarea.valid = valid;
        } else if ( this.mdcSelect ) {
            this.mdcSelect.valid = valid;
        } else if ( this.lovComponent ) {
            this.lovComponent.setValid( valid, errorMessage );
        }
        this.errorMessage = errorMessage;
        this.valid = valid;
    }
    public focus() {
        if ( this.mdcTextField ) {
            this.mdcTextField.focus();
        } else if ( this.lovComponent ) {
            this.lovComponent.focus();
        }
    }

}
