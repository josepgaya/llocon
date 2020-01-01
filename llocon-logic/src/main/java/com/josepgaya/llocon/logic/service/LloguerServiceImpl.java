/**
 * 
 */
package com.josepgaya.llocon.logic.service;

import org.springframework.stereotype.Service;

import com.josepgaya.base.boot.logic.service.AbstractGenericServiceImpl;
import com.josepgaya.llocon.core.entity.LloguerEntity;
import com.josepgaya.llocon.logic.api.dto.Lloguer;
import com.josepgaya.llocon.logic.api.service.LloguerService;

/**
 * Implementació del servei per a la gestió de lloguers.
 * 
 * @author josepgaya
 */
@Service
public class LloguerServiceImpl extends AbstractGenericServiceImpl<Lloguer, LloguerEntity, Long> implements LloguerService {

	/*@Override
	protected void configureAdditionalData(List<Lloguer> dtos) {
		List<Long> ids = new ArrayList<Long>();
		for (Lloguer dto: dtos) {
			ids.add(dto.getId());
		}
		List<LloguerIdAmbImportPendent> importsPendents = facturaRepository.sumImportBySubministramentLloguerIdAndNotEstat(
				ids,
				FacturaEstatEnum.PAGADA);
		for (Lloguer dto: dtos) {
			boolean found = false;
			for (LloguerIdAmbImportPendent importPendent: importsPendents) {
				if (dto.getId().equals(importPendent.getLloguerId())) {
					dto.setImportPendent(importPendent.getImportPendent());
					found = true;
					break;
				}
			}
			if (!found) {
				dto.setImportPendent(new BigDecimal(0));
			}
		}
	}*/

}
