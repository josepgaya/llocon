/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.core.dto.FacturaEstatEnum;
import com.josepgaya.llocon.core.dto.Lloguer;
import com.josepgaya.llocon.core.entity.LloguerEntity;
import com.josepgaya.llocon.core.repository.FacturaRepository;
import com.josepgaya.llocon.core.repository.FacturaRepository.LloguerIdAmbImportPendent;
import com.josepgaya.llocon.core.repository.LloguerRepository;

/**
 * Implementació del service per a la gestió de lloguers.
 * 
 * @author josepgaya
 */
@Service
public class LloguerServiceImpl extends AbstractGenericServiceImpl<Lloguer, LloguerEntity, Long> implements LloguerService {

	@Autowired
	private LloguerRepository lloguerRepository;
	@Autowired
	private FacturaRepository facturaRepository;

	@Override
	protected LloguerEntity buildNewEntity(
			Lloguer dto) {
		return LloguerEntity.getBuilder(
				dto.getCodi(),
				dto.getNom(),
				dto.getAdressa()).
				build();
	}

	@Override
	protected void updateEntity(
			LloguerEntity entity,
			Lloguer dto) {
		entity.update(
				dto.getCodi(),
				dto.getNom(),
				dto.getAdressa());
	}

	@Override
	protected Page<LloguerEntity> findPageByFiltreImpl(
			String filtre,
			Pageable pageable) {
		 Page<LloguerEntity> page = lloguerRepository.findByFiltre(
				filtre == null || filtre.isEmpty(),
				filtre,
				pageable);
		 return page;
	}

	@Override
	protected JpaRepository<LloguerEntity, Long> getRepository() {
		return lloguerRepository;
	}

	@Override
	protected Class<Lloguer> getDtoClass() {
		return Lloguer.class;
	}

	@Override
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
	}

}
