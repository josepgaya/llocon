/**
 * 
 */
package com.josepgaya.llocon.logic.api.service;

import com.josepgaya.base.boot.logic.api.service.GenericService;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.File;

/**
 * Interfície del servei per a la gestió de factures.
 * 
 * @author josepgaya
 */
public interface FacturaService extends GenericService<Factura, Long> {

	public File download(
			Long parentId,
			Long id);

}
