/**
 * 
 */
package com.josepgaya.llocon.logic.service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josepgaya.base.boot.logic.service.AbstractGenericServiceImpl;
import com.josepgaya.llocon.core.entity.FacturaEntity;
import com.josepgaya.llocon.core.entity.SubministramentEntity;
import com.josepgaya.llocon.core.exception.GenericServiceException;
import com.josepgaya.llocon.core.repository.FacturaRepository;
import com.josepgaya.llocon.core.repository.SubministramentRepository;
import com.josepgaya.llocon.core.repository.SubministramentRepository.SubministramentAmbConnexio;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.FacturaEstatEnum;
import com.josepgaya.llocon.logic.api.dto.Subministrament;
import com.josepgaya.llocon.logic.api.service.SubministramentService;
import com.josepgaya.llocon.logic.scrap.ScrapperHelper;

/**
 * Implementació del servei per a la gestió de subministraments.
 * 
 * @author josepgaya
 */
@Service
public class SubministramentServiceImpl extends AbstractGenericServiceImpl<Subministrament, SubministramentEntity, Long> implements SubministramentService {

	@Autowired
	private SubministramentRepository subministramentRepository;
	@Autowired
	private FacturaRepository facturaRepository;
	@Autowired
	private ScrapperHelper scrapperHelper;

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
				List<com.josepgaya.llocon.logic.scrap.Factura> darreresFactures = scrapperHelper.findDarreresFactures(
						subministrament.getConnexio(),
						subministrament.getEmbedded().getContracteNum());
				List<FacturaEntity> noves = new ArrayList<FacturaEntity>();
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				for (com.josepgaya.llocon.logic.scrap.Factura factura: darreresFactures) {
					FacturaEntity facturaEntity = facturaRepository.findBySubministramentAndNumero(
							subministrament,
							factura.getNumero());
					if (facturaEntity == null) {
						logger.debug("Processant nova factura (" +
								"lloguerCodi=" + subministrament.getLloguer().getEmbedded().getCodi() + ", " +
								"producte=" + subministrament.getEmbedded().getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getEmbedded().getProveidor() + ", " +
								"numero=" + factura.getNumero() + ", " +
								"data=" + sdf.format(factura.getData()) + ", " +
								"import=" + factura.getImportt() + ")");
						Factura dto = new Factura();
						dto.setNumero(factura.getNumero());
						dto.setData(factura.getData());
						dto.setImportt(factura.getImportt());
						dto.setEstat(FacturaEstatEnum.PENDENT);
						facturaEntity = FacturaEntity.builder().
								embedded(dto).
								subministrament(subministrament).
								build();
						noves.add(facturaRepository.save(facturaEntity));
						logger.debug("Factura guardada (" +
								"lloguerCodi=" + subministrament.getLloguer().getEmbedded().getCodi() + ", " +
								"producte=" + subministrament.getEmbedded().getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getEmbedded().getProveidor() + ", " +
								"numero=" + factura.getNumero() + ", " +
								"data=" + sdf.format(factura.getData()) + ", " +
								"import=" + factura.getImportt() + ")");
					} else {
						logger.debug("Factura existent a la base de dades (" +
								"lloguerCodi=" + subministrament.getLloguer().getEmbedded().getCodi() + ", " +
								"producte=" + subministrament.getEmbedded().getProducte() + ", " +
								"proveidor=" + subministrament.getConnexio().getEmbedded().getProveidor() + ", " +
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
				throw new GenericServiceException("Error al actualitzar factures", ex);
			}
		}
	}

	private static final Logger logger = LoggerFactory.getLogger(SubministramentServiceImpl.class);

}
