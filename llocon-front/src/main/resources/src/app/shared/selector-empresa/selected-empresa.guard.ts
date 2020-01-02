import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot } from '@angular/router';

import { SelectedIdentificadorEmpresaService, SelectedIdentificadorEmpresa } from './selected-identificador-empresa.service';

@Injectable({
	providedIn: 'root',
})
export class SelectedEmpresaGuard implements CanActivate {

	canActivate(route: ActivatedRouteSnapshot, state: RouterStateSnapshot) {
		let selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa = this.selectedIdentificadorEmpresaService.getSelectedIdentificadorEmpresa();
		let anyEmpresaSelected = selectedIdentificadorEmpresa && selectedIdentificadorEmpresa.empresa && selectedIdentificadorEmpresa.empresa.id !== undefined;
		if (!anyEmpresaSelected) {
			// TODO: Mostrar missatge explicant que per accedir a una pàgina d'un mòdul hi ha d'haver alguna empresa seleccionada
			console.error("Per accedir a una pàgina d'un mòdul hi ha d'haver alguna empresa seleccionada");
			return this.router.navigateByUrl('/home');
		} else {
			return true;
		}
	}

	constructor(
		private router: Router,
		private selectedIdentificadorEmpresaService: SelectedIdentificadorEmpresaService) {
	}

}
