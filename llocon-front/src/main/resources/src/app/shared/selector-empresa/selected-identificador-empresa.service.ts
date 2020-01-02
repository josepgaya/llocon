import { Injectable } from '@angular/core';
import { Subject } from 'rxjs';
import { BngAuthService } from 'base-angular';

export class SelectedIdentificadorEmpresa {
	identificador: any;
	empresa: any;
}

@Injectable({
	providedIn: 'root',
})
export class SelectedIdentificadorEmpresaService {

	selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa;

	private selectedIdentificadorEmpresaChangeEvent: Subject<SelectedIdentificadorEmpresa> = new Subject<SelectedIdentificadorEmpresa>();

	public setSelectedIdentificadorEmpresa(selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa) {
		this.selectedIdentificadorEmpresa = selectedIdentificadorEmpresa;
	}

	public getSelectedIdentificadorEmpresa(): SelectedIdentificadorEmpresa {
		return this.selectedIdentificadorEmpresa;
	}

	public getSelectedIdentificadorEmpresaChangeEvent(): Subject<SelectedIdentificadorEmpresa> {
		return this.selectedIdentificadorEmpresaChangeEvent;
	}

	constructor(
		authService: BngAuthService) {
		let session: any = authService.getSession();
		if (session) {
			// let identificador = session.i ? { id: session.i } : undefined;
			// let empresa = session.e ? { id: session.e } : undefined;
			this.selectedIdentificadorEmpresa = {
				identificador: session.i ? { id: session.i } : undefined,
				empresa: session.e ? { id: session.e } : undefined
			}
		}
	}

}
