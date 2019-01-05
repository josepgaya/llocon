/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.Subministrament;
import com.josepgaya.llocon.core.entity.FacturaEntity;
import com.josepgaya.llocon.core.entity.SubministramentEntity;
import com.josepgaya.llocon.core.repository.ConnexioRepository;
import com.josepgaya.llocon.core.repository.FacturaRepository;
import com.josepgaya.llocon.core.repository.LloguerRepository;
import com.josepgaya.llocon.core.repository.SubministramentRepository;
import com.josepgaya.llocon.core.repository.SubministramentRepository.SubministramentAmbConnexio;
import com.josepgaya.llocon.core.scrap.ScrapperHelper;

/**
 * Implementació del service per a la gestió de subministraments.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@Service
public class SubministramentServiceImpl extends AbstractGenericParentServiceImpl<Subministrament, SubministramentEntity, Long, Long> implements SubministramentService {

	@Autowired
	private LloguerRepository lloguerRepository;
	@Autowired
	private ConnexioRepository connexioRepository;
	@Autowired
	private SubministramentRepository subministramentRepository;
	@Autowired
	private FacturaRepository facturaRepository;
	@Autowired
	private ScrapperHelper scrapperHelper;

	@Override
	protected SubministramentEntity buildNewEntity(
			Long parentId,
			Subministrament dto) {
		return SubministramentEntity.getBuilder(
				lloguerRepository.getOne(parentId),
				connexioRepository.getOne(dto.getConnexio().getId()),
				dto.getProducte(),
				dto.getContracteNum()).
				build();
	}

	@Override
	protected void updateEntity(
			Long parentId,
			SubministramentEntity entity,
			Subministrament dto) {
		entity.update(
				connexioRepository.getOne(dto.getConnexio().getId()),
				dto.getProducte(),
				dto.getContracteNum());
	}

	@Override
	protected Object getEntityAsObject(
			Long parentId,
			Long id) {
		return subministramentRepository.findByLloguerIdAndId(
				parentId,
				id);
	}
	@Override
	protected Page<SubministramentAmbConnexio> findPageByFiltreImpl(
			Long parentId,
			String filtre,
			Pageable pageable) {
		return subministramentRepository.findByLloguerId(
				parentId,
				pageable);
	}

	@Override
	protected Long getParentId(SubministramentEntity entity) {
		return entity.getLloguerId();
	}

	@Override
	protected JpaRepository<SubministramentEntity, Long> getRepository() {
		return subministramentRepository;
	}

	@Override
	protected Class<Subministrament> getDtoClass() {
		return Subministrament.class;
	}

	@Override
	@Transactional
	public List<Factura> getFactures(Long parentId, Long id) {
		logger.debug("Descarregant factures noves (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		SubministramentAmbConnexio subministramentAmbConnexio = subministramentRepository.findByLloguerIdAndId(
				parentId,
				id);
		if (subministramentAmbConnexio == null) {
			throw new EntityNotFoundException("SubministramentEntity#" + id + "?parentId=" + parentId);
		} else {
			SubministramentEntity subministrament = subministramentRepository.getOne(id);
			try {
				List<com.josepgaya.llocon.core.scrap.Factura> darreresFactures = scrapperHelper.findDarreresFactures(
						subministrament.getConnexio(),
						subministrament.getContracteNum());
				List<FacturaEntity> noves = new ArrayList<FacturaEntity>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				for (com.josepgaya.llocon.core.scrap.Factura factura: darreresFactures) {
					FacturaEntity facturaEntity = facturaRepository.findBySubministramentAndNumero(
							subministrament,
							factura.getNumero());
					if (facturaEntity == null) {
						logger.debug("Processant nova factura (" +
								"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
								"producte=" + subministrament.getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
								"numero=" + factura.getNumero() + ", " +
								"data=" + sdf.format(factura.getData()) + ", " +
								"import=" + factura.getImportt() + ")");
						facturaEntity = FacturaEntity.getBuilder(
								subministrament,
								factura.getNumero(),
								factura.getData(),
								factura.getImportt()).build();
						noves.add(
								facturaRepository.save(facturaEntity));
						logger.debug("Factura guardada (" +
								"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
								"producte=" + subministrament.getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
								"numero=" + factura.getNumero() + ", " +
								"data=" + sdf.format(factura.getData()) + ", " +
								"import=" + factura.getImportt() + ")");
					} else {
						logger.debug("Factura existent a la base de dades (" +
								"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
								"producte=" + subministrament.getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
								"numero=" + factura.getNumero() + ", " +
								"data=" + sdf.format(factura.getData()) + ", " +
								"import=" + factura.getImportt() + ")");
					}
				}
				subministrament.updateDarreraActualitzacio(new Date());
				return orikaMapperFacade.mapAsList(
						noves,
						Factura.class);
			} catch (Exception ex) {
				throw new RuntimeException(
						"Error al actualitzar factures",
						ex);
			}
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(SubministramentServiceImpl.class);

}
