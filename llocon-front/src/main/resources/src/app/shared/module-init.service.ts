import { Injectable } from '@angular/core';

import { BngModuleService } from 'base-angular';

@Injectable( {
    providedIn: 'root'
} )
export class ModuleInitService {

    constructor(moduleService: BngModuleService) {
		moduleService.register({
			code: 'fact',
			icon: 'assignment',
			label: 'Facturació',
			menuItems: [
				{ icon: 'room', label: 'Articles', route: '/fact/articles' },
				{ icon: 'room', label: 'Articles família', route: '/fact/articlesFamilia' },
				{ icon: 'room', label: 'Articles família empresa', route: '/fact/articlesFamiliaEmpresa' },
				{ icon: 'room', label: 'Articles gamma', route: '/fact/articlesGamma' },
				{ icon: 'room', label: 'Articles marca', route: '/fact/articlesMarca' },
				{ icon: 'room', label: 'Articles model', route: '/fact/articlesModel' },
				{ icon: 'room', label: 'Codis postals', route: '/fact/codisPostal' },
				{ icon: 'room', label: 'Departaments', route: '/fact/departaments' },
				{ icon: 'room', label: 'Divises', route: '/fact/divises' },
				{ icon: 'room', label: 'Documents de pagament/cobrament', route: '/fact/documentsPagamentCobrament' },
				{ icon: 'room', label: 'Empreses (Facturació)', route: '/fact/empreses' },
				{ icon: 'room', label: 'Famílies cost', route: '/fact/familiesCost' },
				{ icon: 'room', label: 'Famílies proveidor', route: '/fact/familiesProveidor' },
				{ icon: 'room', label: 'Iva', route: '/fact/ives' },
				{ icon: 'room', label: 'Magatzems', route: '/fact/magatzems' },
				{ icon: 'room', label: 'Magatzems període', route: '/fact/magatzemsPeriode' },
				{ icon: 'room', label: 'Naturaleses de pagament/cobrament', route: '/fact/naturalesesPagamentCobrament' },
				{ icon: 'room', label: 'Països', route: '/fact/paisos' },
				{ icon: 'room', label: 'Peus de document', route: '/fact/peusDocument' },
				{ icon: 'room', label: 'Proveïdors', route: '/fact/proveidors' },
				{ icon: 'room', label: 'Províncies', route: '/fact/provincies' },			
				{ icon: 'room', label: 'Règims d\'iva', route: '/fact/regimsIva' },			
				{ icon: 'room', label: 'Seccions empresa', route: '/fact/seccionsEmpresa' },		
				{ icon: 'room', label: 'Sèries de compra', route: '/fact/seccionsCompra' },
				{ icon: 'room', label: 'Sèries intracomunitàries', route: '/fact/seriesIntracomunitaria' },
				{ icon: 'room', label: 'Sèries de venda', route: '/fact/seriesVenda' },
				{ icon: 'room', label: 'Situacions comercials', route: '/fact/situacionsComercial' },
				{ icon: 'room', label: 'Situacions inicials', route: '/fact/situacionsInicial' },
				{ icon: 'room', label: 'Subvencions', route: '/fact/subvencions' },
				{ icon: 'room', label: 'Tarifes', route: '/fact/tarifes' },
				{ icon: 'room', label: 'Tarifes descompte', route: '/fact/tarifesDescompte' },
				{ icon: 'room', label: 'Tipus de comissió', route: '/fact/tipusComissions' },
				{ icon: 'room', label: 'Tipus de facturació', route: '/fact/tipusFacturacions' },
				{ icon: 'room', label: 'Tipus d\'incidencia factura', route: '/fact/tipusIncidenciesFactura' },
				{ icon: 'room', label: 'Tipus de proveïdor/client', route: '/fact/tipusProveidorsClient' },
				{ icon: 'room', label: 'Tipus de riscos', route: '/fact/tipusRiscos' },
				{ icon: 'room', label: 'Tipus de venciments', route: '/fact/tipusVenciments' },
				{ icon: 'room', label: 'Transportistes', route: '/fact/transportistes' },
				{ icon: 'room', label: 'Ubicacions', route: '/fact/ubicacions' },
				{ icon: 'room', label: 'Ubicacions articles', route: '/fact/ubicacionsArticle' },
				{ icon: 'room', label: 'Unitats tipus', route: '/fact/unitatsTipus' },
				{ icon: 'room', label: 'Vehicles', route: '/fact/vehicles' },				
				{ icon: 'room', label: 'Zones (Facturació)', route: '/fact/zonesFact' }					
				
			]
		});
		moduleService.register({
			code: 'comp',
			icon: 'assessment',
			label: 'Comptabilitat'
		});
		moduleService.register({
			code: 'rrhh',
			icon: 'people_alt',
			label: 'Recursos humans',
			menuItems: [
				{ icon: 'room', label: 'Calendaris', route: '/rrhh/calendaris' },
				{ icon: 'room', label: 'Categories', route: '/rrhh/categories' },
				{ icon: 'room', label: 'Empreses', route: '/rrhh/empresesRrhh' },
				{ icon: 'room', label: 'Horaris', route: '/rrhh/horaris' },
				{ icon: 'room', label: 'Identificadors (Recursos humans)', route: '/rrhh/identificadorsRrhh' },
				{ icon: 'room', label: 'Nodes', route: '/rrhh/nodes' },
				{ icon: 'room', label: 'Operaris (Recursos humans)', route: '/rrhh/operarisRrhh' },
				{ icon: 'room', label: 'Parametres', route: '/rrhh/parametres' },
				{ icon: 'room', label: 'Recursos Grup', route: '/rrhh/recursosGrup' },
				{ icon: 'room', label: 'Regims', route: '/rrhh/regims' },
				{ icon: 'room', label: 'Registres Diaris', route: '/rrhh/registresDiari' },
				{ icon: 'room', label: 'Seccions', route: '/rrhh/seccions' },
				{ icon: 'room', label: 'Seccions Grup', route: '/rrhh/seccionsGrup' },
				{ icon: 'room', label: 'Servidors', route: '/rrhh/servidors' },
				{ icon: 'room', label: 'Subcategories', route: '/rrhh/subcategorias' },
				{ icon: 'room', label: 'Tipus Dia', route: '/rrhh/tipusDies' },
				{ icon: 'room', label: 'Tipus Transaccio', route: '/rrhh/tipusTransaccions' },
				{ icon: 'room', label: 'Transaccions', route: '/rrhh/transaccions' },
				{ icon: 'room', label: 'Zones', route: '/rrhh/zones' }									]
		});
		moduleService.register({
			code: 'rrmm',
			icon: 'commute',
			label: 'Recursos de maquinària'
		});
		moduleService.register({
			code: 'banc',
			icon: 'account_balance',
			label: 'Gestió bancària'
		});
		moduleService.register({
			code: 'lici',
			icon: 'work',
			label: 'Licitacions'
		});
		moduleService.register({
			code: 'marc',
			icon: 'touch_app',
			label: 'Marcatges',
			menuItems: [
				{ icon: 'people_alt', label: 'Operaris', route: '/marc/operaris' },
		    	{ icon: 'timer', label: 'Marcatges', route: '/marc/marcatges' }
			]
		});
		moduleService.refreshAllowedModuleItems();
    }

}
