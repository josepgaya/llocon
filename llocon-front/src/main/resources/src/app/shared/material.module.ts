import { NgModule } from '@angular/core';
import {
	MatToolbarModule,
	MatSidenavModule,
	MatMenuModule,
	MatListModule,
	MatFormFieldModule,
	MatInputModule,
	MatSelectModule,
	MatCheckboxModule,
	MatButtonModule,
	MatIconModule,
	MatDatepickerModule,
	MatSnackBarModule,
	MatProgressSpinnerModule,
	MatAutocompleteModule,
	MatDialogModule,
	MatCardModule,
	MatTabsModule,
	MAT_DATE_FORMATS
} from '@angular/material';

import { MomentDateModule } from '@angular/material-moment-adapter';

@NgModule( {
    exports: [
		MatToolbarModule,
		MatSidenavModule,
		MatMenuModule,
		MatListModule,
        MatFormFieldModule,
        MatInputModule,
        MatSelectModule,
        MatCheckboxModule,
        MatButtonModule,
        MatIconModule,
        MatDatepickerModule,
		MatSnackBarModule,
        MatProgressSpinnerModule,
        MatAutocompleteModule,
		MatDialogModule,
		MatCardModule,
		MatTabsModule,
        MomentDateModule
    ],
    providers: [
        { provide: MAT_DATE_FORMATS, useValue: {
                parse: {
                    dateInput: ['l', 'LL'],
                }, display: {
                    dateInput: 'L',
                    monthYearLabel: 'MMM YYYY',
                    dateA11yLabel: 'LL',
                    monthYearA11yLabel: 'MMMM YYYY',
                }
            }
        }
    ]
} )
export class MaterialModule { }