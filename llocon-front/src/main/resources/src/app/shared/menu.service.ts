import { Injectable } from '@angular/core';
import { Router } from '@angular/router';

import { BngAuthService, BngModuleService, BngModuleItem, BngModuleMenuItem } from 'base-angular';
import { IdentificadorsService } from '../pages/identificadors/identificadors.service';

export class AppMenu {
	icon?: string;
	label: string;
	labelKey?: string;
	menuItems: AppMenuItem[]
}
export class AppMenuItem extends BngModuleMenuItem {
	labelKey: string;
};

@Injectable({
	providedIn: 'root'
})
export class MenuService {

	private adminMenu: AppMenu = {
		icon: 'build',
		label: 'Administració',
		labelKey: 'app.menu.admin',
		menuItems: [
			{ icon: 'people', label: 'Usuaris', labelKey: 'app.menu.usuaris', route: '/usuaris' },
			{ icon: 'domain', label: 'Grups d\'empreses', labelKey: 'app.menu.identificadors', route: '/identificadors' }
		]
	}

	private adminIdentificadorMenu: AppMenu = {
		icon: 'build',
		label: '...',
		menuItems: [
			{ icon: 'domain', label: 'Grups d\'empreses', labelKey: 'app.menu.identificador', route: '/identificador' },
			{ icon: 'people', label: 'Usuaris', labelKey: 'app.menu.usuaris', route: '/usuari-identificadors' },
			{ icon: 'business_center', label: 'Empreses', labelKey: 'app.menu.empreses', route: '/empreses' },
			{ icon: 'portrait', label: 'Perfils', labelKey: 'app.menu.perfils', route: '/perfils' },
			{ icon: 'person_pin', label: 'Rols', labelKey: 'app.menu.rols', route: '/rols' }
		]
	}

	public getAdminMenu(): AppMenu {
		return this.adminMenu;
	}

	public getAdminIdentificadorMenu(identificadorNom: string): AppMenu {
		this.adminIdentificadorMenu.label = identificadorNom;
		return this.adminIdentificadorMenu;
	}

	public getModuleMenu(module: string): AppMenu {
		let moduleItem: BngModuleItem = this.moduleService.getModuleItem(module);
		if (moduleItem) {
			return <AppMenu>{
				icon: moduleItem.icon,
				label: moduleItem.label,
				labelKey: 'app.module.' + module,
				menuItems: moduleItem.menuItems
			};
		}
	}

	public getCurrentRouteMenu(): AppMenu {
		let found: boolean = false;
		if (this.router.url.startsWith('/admin-app')) {
			found = true;
		} else {
			this.adminMenu.menuItems.forEach((menuItem: AppMenuItem) => {
				if (menuItem.route && this.router.url.startsWith(menuItem.route)) {
					found = true;
				}
			});
		}
		if (found) {
			return this.adminMenu;
		} else {
			found = false;
			if (this.router.url.startsWith('/admin-identificador')) {
				found = true;
			} else {
				this.adminIdentificadorMenu.menuItems.forEach((menuItem: AppMenuItem) => {
					if (menuItem.route && this.router.url.startsWith(menuItem.route)) {
						found = true;
					}
				});
			}
		}
		if (found) {
			let session: any = this.authService.getSession();
			if (session) {
				this.identificadorsService.whenReady().subscribe(() => {
					this.identificadorsService.get(session.i).subscribe((resposta: any) => {
						this.adminIdentificadorMenu.label = resposta.descripcio;
					});
				});
			}
			return this.adminIdentificadorMenu;
		} else {
			let routerUrl = this.router.url.substring(1);
			let modul = (routerUrl.indexOf("/") != -1) ? routerUrl.substring(0, routerUrl.indexOf("/")) : routerUrl;
			return this.getModuleMenu(modul);
		}
	}

	constructor(
		private router: Router,
		private authService: BngAuthService,
		private moduleService: BngModuleService,
		private identificadorsService: IdentificadorsService) {
	}

}
