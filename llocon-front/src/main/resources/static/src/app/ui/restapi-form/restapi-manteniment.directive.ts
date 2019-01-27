import { Directive, ElementRef, Injector } from '@angular/core';
import { ActivatedRoute } from "@angular/router";
import { Location } from "@angular/common";

import { MdcSnackbar } from '@angular-mdc/web';

import { RestapiFormComponent } from "../../ui/restapi-form/restapi-form.component";

@Directive( {
    selector: '[restapi-mant]'
} )
export class RestapiMantenimentDirective {

    private id: string;

    private location: Location;
    private snackbar: MdcSnackbar;

    onFormActionSave() {
        if ( this.restapiForm.isCreacio() ) {
            this.showMessage(
                "L'element s'ha creat correctament",
                false );
        } else {
            this.showMessage(
                "La informació s'ha guardat correctament",
                false );
        }
        this.tornar();
    }
    onFormActionCancel() {
        this.tornar();
    }

    showMessage( message: string, error: boolean ) {
        const snackbarRef = this.snackbar.open(
            message,
            "Tancar", {
            } );
    }

    tornar() {
        this.location.back();
    }

    constructor(
            private restapiForm: RestapiFormComponent,
            private injectorObj: Injector) {
        restapiForm.actionSave.subscribe(() => this.onFormActionSave());
        restapiForm.actionCancel.subscribe(() => this.onFormActionCancel());
        this.location = <Location>this.injectorObj.get(Location);
        this.snackbar = <MdcSnackbar>this.injectorObj.get(MdcSnackbar);
        let activatedRoute = <ActivatedRoute>this.injectorObj.get(ActivatedRoute);
        activatedRoute.params.subscribe(( params ) => {
            if ( params.id ) {
                restapiForm.id = params.id;
            }
        } );
        activatedRoute.queryParams.subscribe(( queryParams ) => {
            let isEmpty = Object.keys(queryParams).length === 0 && queryParams.constructor === Object;
            if (!isEmpty) {
                restapiForm.pare = queryParams;
            }
        } );
    }

}
