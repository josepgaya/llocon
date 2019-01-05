/**
 * 
 */
package com.josepgaya.llocon.core.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.File;
import com.josepgaya.llocon.core.entity.FacturaEntity;
import com.josepgaya.llocon.core.entity.SubministramentEntity;
import com.josepgaya.llocon.core.repository.FacturaRepository;
import com.josepgaya.llocon.core.repository.FacturaRepository.FacturaAmbSubministrament;
import com.josepgaya.llocon.core.repository.SubministramentRepository;
import com.josepgaya.llocon.core.scrap.ScrapperHelper;

/**
 * Implementació del service per a la gestió de factures.
 * 
 * @author josepgaya
 */
@Service
public class FacturaServiceImpl extends AbstractGenericParentServiceImpl<Factura, FacturaEntity, Long, Long> implements FacturaService {

	@Autowired
	private SubministramentRepository subministramentRepository;
	@Autowired
	private FacturaRepository facturaRepository;
	@Autowired
	private ScrapperHelper scrapperHelper;

	@Override
	protected FacturaEntity buildNewEntity(
			Long parentId,
			Factura dto) {
		SubministramentEntity subministrament = subministramentRepository.getOne(parentId);
		return FacturaEntity.getBuilder(
				subministrament,
				dto.getNumero(),
				dto.getData(),
				dto.getImportt()).
				build();
	}

	@Override
	protected void updateEntity(
			Long parentId,
			FacturaEntity entity,
			Factura dto) {
		entity.update(
				dto.getEstat());
	}

	@Override
	protected Object getEntityAsObject(
			Long parentId,
			Long id) {
		return facturaRepository.findBySubministramentLloguerIdAndId(
				parentId,
				id);
	}
	@Override
	protected Page<FacturaAmbSubministrament> findPageByFiltreImpl(
			Long parentId,
			String filtre,
			Pageable pageable) {
		Page<FacturaAmbSubministrament> page = facturaRepository.findBySubministramentLloguerId(
				parentId,
				pageable);
		return page;
	}

	@Override
	public File download(
			Long parentId,
			Long id) {
		FacturaEntity factura = getEntity(parentId, id);
		try {
			return scrapperHelper.descarregarFactura(
					factura.getSubministrament().getConnexio(),
					factura.getSubministrament().getProducte(),
					factura.getSubministrament().getContracteNum(),
					factura.getNumero());
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error al descarregar factura",
					ex);
		}
	}

	@Override
	protected Long getParentId(FacturaEntity entity) {
		return entity.getSubministrament().getLloguerId();
	}

	@Override
	protected JpaRepository<FacturaEntity, Long> getRepository() {
		return facturaRepository;
	}

	@Override
	protected Class<Factura> getDtoClass() {
		return Factura.class;
	}

}
