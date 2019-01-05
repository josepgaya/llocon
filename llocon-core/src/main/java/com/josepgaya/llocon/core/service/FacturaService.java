/**
 * 
 */
package com.josepgaya.llocon.core.service;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.File;

/**
 * Interfície del service per a la gestió de factures.
 * 
 * @author josepgaya
 */
public interface FacturaService extends GenericParentService<Factura, Long, Long> {

	public File download(
			Long parentId,
			Long id);

}
