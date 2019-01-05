/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.util.List;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.Subministrament;

/**
 * Interfície del service per a la gestió de subministraments.
 * 
 * @author josepgaya
 */
public interface SubministramentService extends GenericParentService<Subministrament, Long, Long> {

	/**
	 * Refresca les darreres factures del subministrament i les retorna
	 * a dins una llista.
	 * @param parentId identificador del lloguer.
	 * @param id identificador del subministrament.
	 * @return llista amb les darreres factures
	 */
	public List<Factura> getFactures(
			Long parentId,
			Long id);

}
