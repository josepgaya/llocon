import { Component, Output, EventEmitter } from '@angular/core';
import { BngAuthService, BngScreenSizeService, BngScreenSizeChangeEvent } from 'base-angular';

import { SelectedIdentificadorEmpresaService, SelectedIdentificadorEmpresa } from './selected-identificador-empresa.service';
import { UsuariIdentificadorEmpresaService } from '../usuari-identificador-empresa.service';

@Component({
	selector: 'selector-identificador-empresa',
	templateUrl: './selector-identificador-empresa.component.html',
	styles: [`
	.btn_adm {
		float: right;
		margin-left:1em;
		padding: 0;
		width: 30px;
		min-width: 40px;
		height:26px;
		line-height: 32px;
		top: 6px;
	}
	.icon_adm {
		font-size:1.2em;
		margin: 0 !important;
		color: #875A7B;
	}
	.nomcom {
		width: 154px;
		white-space: nowrap;
		overflow: hidden;
		text-overflow: ellipsis;
		display:inline-block;
	}
	.iconcom {
		float: left;
    	position: relative;
    	top: 10px;
	}
`]
})
export class SelectorIdentificadorEmpresaComponent {

	@Output() selectedIdentificadorEmpresaChange: EventEmitter<SelectedIdentificadorEmpresa> = new EventEmitter();
	@Output() identificadorAdmin: EventEmitter<any> = new EventEmitter();

	selectionTree: any[];
	selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa;
	mobileScreen: boolean;

	onEmpresaButtonClick(indexIdentificador: number, indexEmpresa: number) {
		let selectedIdentificador: any = this.selectionTree[indexIdentificador];
		let selectedEmpresa: any = selectedIdentificador.empreses[indexEmpresa];
		this.changeSelectedIdentificadorEmpresa({
			identificador: selectedIdentificador,
			empresa: selectedEmpresa
		}, true);
	}

	onAdministrarButtonClick(indexIdentificador: number) {
		let selectedIdentificadorFromTree = this.selectionTree[indexIdentificador];
		let selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa = this.selectedIdentificadorEmpresaService.getSelectedIdentificadorEmpresa();
		let selectedIdentificador: any = selectedIdentificadorEmpresa ? selectedIdentificadorEmpresa.identificador : undefined;
		if (!selectedIdentificador || selectedIdentificador.id !== selectedIdentificadorFromTree.id) {
			this.changeSelectedIdentificadorEmpresa({
				identificador: selectedIdentificadorFromTree,
				empresa: undefined
			}, true);
		}
		this.identificadorAdmin.emit(selectedIdentificadorFromTree);
	}

	private changeSelectedIdentificadorEmpresa(selectedIdentificadorEmpresa: SelectedIdentificadorEmpresa, triggerSessioUpdate: boolean) {
		this.selectedIdentificadorEmpresa = selectedIdentificadorEmpresa;
		if (triggerSessioUpdate) {
			let identificador: any;
			if (selectedIdentificadorEmpresa && selectedIdentificadorEmpresa.identificador) {
				identificador = selectedIdentificadorEmpresa.identificador.id;
			}
			let empresa: any;
			if (selectedIdentificadorEmpresa && selectedIdentificadorEmpresa.empresa) {
				empresa = selectedIdentificadorEmpresa.empresa.id;
			}
			this.authService.sessionSave({
				i: identificador,
				e: empresa
			});
		}
		this.selectedIdentificadorEmpresaService.setSelectedIdentificadorEmpresa(selectedIdentificadorEmpresa);
		this.selectedIdentificadorEmpresaChange.emit(selectedIdentificadorEmpresa);
	}

	private refreshSelectionTree() {
		let authToken: string = this.authService.getAuthToken();
		if (authToken) {
			this.usuariIdentificadorEmpresaService.whenReady().subscribe(() => {
				this.usuariIdentificadorEmpresaService.getSelectionTree().subscribe((resposta: any) => {
					if (resposta && resposta._embedded) {
						this.selectionTree = resposta._embedded.identificadorEmpresaSelectionTreeItems;
					} else {
						this.selectionTree = undefined;
					}
					let selectedIdentificador: any;
					let selectedEmpresa: any;
					let session: any = this.authService.getSession();
					if (session && session.i && this.selectionTree) {
						selectedIdentificador = this.selectionTree.find((identificador: any) => { return identificador.id === session.i });
						if (session.e && selectedIdentificador && selectedIdentificador.empreses) {
							selectedEmpresa = selectedIdentificador.empreses.find((empresa: any) => { return empresa.id === session.e });
						}
					}
					this.changeSelectedIdentificadorEmpresa({
						identificador: selectedIdentificador,
						empresa: selectedEmpresa
					}, true);
				});
			});
		} else {
			this.selectionTree = undefined;
		}
	}

	constructor(
		private authService: BngAuthService,
		private usuariIdentificadorEmpresaService: UsuariIdentificadorEmpresaService,
		private selectedIdentificadorEmpresaService: SelectedIdentificadorEmpresaService,
		private screenSizeService: BngScreenSizeService) {
		/*authService.getAuthTokenChangeEvent().subscribe((tokenPayload: BngAuthTokenPayload) => {
			if (tokenPayload) {
				// Només refresca l'arbre si l'usuari ha canviat
				if (!this.tokenPayload || this.tokenPayload.sub !== tokenPayload.sub) {
					this.refreshSelectionTree();
				}
			}
			this.tokenPayload = tokenPayload;
		});*/
		// Es subscriu al subject de canvi de tamany de la pantalla
		this.mobileScreen = this.screenSizeService.isMobile();
		this.screenSizeService.getScreenSizeChangeSubject().subscribe((event: BngScreenSizeChangeEvent) => {
			this.mobileScreen = event.mobile
		});
		this.refreshSelectionTree();
	}

}