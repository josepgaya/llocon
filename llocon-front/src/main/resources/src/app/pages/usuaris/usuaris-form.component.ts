import { Component } from '@angular/core';
import { BngFormConfig } from 'base-angular';

import { UsuarisService } from './usuaris.service';

@Component( {
    template: `
<bng-form
	bng-form-mant
	[config]="formConfig"
	[restapiService]="usuarisService"
	(resourceChange)="onResourceChange($event)">
	<ng-container ngProjectAs="headerActions">
		<usuari-contrasenya *ngIf="usuariId" [usuariId]="usuariId"></usuari-contrasenya>
	</ng-container>
	<div style="display: flex">
		<div style="width: 50%; padding-right: 1em">
			<bng-custom-field name="codi"></bng-custom-field>
		</div>
		<div style="width: 50%; padding-left: 1em">
		</div>
	</div>
	<div style="display: flex">
		<div style="width: 50%; padding-right: 1em">
			<bng-custom-field name="nom"></bng-custom-field>
			<bng-custom-field name="llinatges"></bng-custom-field>
		</div>
		<div style="width: 50%; padding-left: 1em">
			<bng-custom-field name="email"></bng-custom-field>
			<bng-custom-field name="imatgeUrl"></bng-custom-field>
		</div>
	</div>
	<div style="display: flex">
		<bng-custom-field name="authorities" style="width: 100%"></bng-custom-field>
	</div>
	<div style="display: flex">
		<div style="width: 50%; padding-right: 1em">
			<bng-custom-field name="actiu"></bng-custom-field>
		</div>
		<div style="width: 50%; padding-left: 1em">
			<bng-custom-field name="validat"></bng-custom-field>
		</div>
	</div>
</bng-form>
`
} )
export class UsuarisFormComponent {

    formConfig: BngFormConfig = {
		readOnlyStateEnabled: true
	};
	usuariId: number;

	onResourceChange(usuari: any) {
		this.usuariId = usuari.id;
	}

    constructor(
        public usuarisService: UsuarisService ) { }

}