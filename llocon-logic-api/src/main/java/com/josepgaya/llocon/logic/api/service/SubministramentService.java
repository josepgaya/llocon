/**
 * 
 */
package com.josepgaya.llocon.logic.api.service;

import java.util.List;

import com.josepgaya.base.boot.logic.api.service.GenericService;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.Subministrament;

/**
 * Interfície del servei per a la gestió de subministraments.
 * 
 * @author josepgaya
 */
public interface SubministramentService extends GenericService<Subministrament, Long> {

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
