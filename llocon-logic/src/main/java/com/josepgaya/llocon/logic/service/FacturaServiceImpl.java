/**
 * 
 */
package com.josepgaya.llocon.logic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.josepgaya.base.boot.logic.service.AbstractGenericServiceImpl;
import com.josepgaya.llocon.core.entity.FacturaEntity;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.File;
import com.josepgaya.llocon.logic.api.service.FacturaService;
import com.josepgaya.llocon.logic.scrap.ScrapperHelper;

/**
 * Implementació del servei per a la gestió de factures.
 * 
 * @author josepgaya
 */
@Service
public class FacturaServiceImpl extends AbstractGenericServiceImpl<Factura, FacturaEntity, Long> implements FacturaService {

	@Autowired
	private ScrapperHelper scrapperHelper;

	@Override
	public File download(
			Long parentId,
			Long id) {
		FacturaEntity factura = this.getEntity(id);
		try {
			return scrapperHelper.descarregarFactura(
					factura.getSubministrament().getConnexio(),
					factura.getSubministrament().getEmbedded().getProducte(),
					factura.getSubministrament().getEmbedded().getContracteNum(),
					factura.getEmbedded().getNumero());
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error al descarregar factura",
					ex);
		}
	}

}
