import { ErrorHandler, Injectable } from '@angular/core';
import { HttpErrorResponse } from '@angular/common/http';
import { MdcSnackbar } from '@angular-mdc/web';

@Injectable()
export class GlobalErrorHandler implements ErrorHandler {

    handleError( error ) {
        let title = error.name;
        let description;
        if ( error instanceof HttpErrorResponse ) {
            // Server or connection error happened
            if ( !navigator.onLine ) {
                // Handle offline error
                description = '[OFFLINE] ' + error.message;
            } else {
                // Handle Http Error (error.status === 403, 404...)
                description = '[HTTP] ' + error.message;
            }
            console.log( 'Error catched!', error );
            this.showSnackbarError( title, description );
        } else {
            // Handle Client Error (Angular Error, ReferenceError...)
            description = '[CLIENT] ' + error.message;
            this.showSnackbarError( title, description );
            throw error;
        }
    }

    showSnackbarError( title, description ) {
        const snackbarRef = this.snackbar.open(
            title + ": " + description,
            "Tancar", {
                timeoutMs: 10000
            } );
    }

    constructor(
        private snackbar: MdcSnackbar ) {
    }

}