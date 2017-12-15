/**
 * 
 */
package com.josepgaya.llocon.service;

import java.io.ByteArrayOutputStream;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.dto.ArxiuDto;
import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.entity.FacturaEstatEnum;
import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.helper.ScrapperHelper;
import com.josepgaya.llocon.repository.FacturaRepository;
import com.josepgaya.llocon.scrap.Factura;
import com.josepgaya.llocon.scrap.FacturaScrapper;

/**
 * Servei de gestió de factures.
 * 
 * @author josepgaya
 */
@Service
public class FacturaService {

	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private ScrapperHelper scrapperHelper;



	@Transactional
	public FacturaEntity canviEstat(
			Long id,
			FacturaEstatEnum estat) {
		logger.info("Canvi estat factura (id=" + id + ")");
		FacturaEntity factura = facturaRepository.findOne(id);
		factura.update(estat);
		return factura;
	}
	@Transactional
	public FacturaEntity seguentEstat(
			Long id) {
		logger.info("Canvi estat factura (id=" + id + ")");
		FacturaEntity factura = facturaRepository.findOne(id);
		FacturaEstatEnum estat = null;
		if (factura != null) {
			estat = FacturaEstatEnum.PENDENT;
			switch(factura.getEstat()) {
			case ENVIADA:
				estat = FacturaEstatEnum.PAGADA;
				break;
			case PAGADA:
				estat = FacturaEstatEnum.PENDENT;
				break;
			case PENDENT:
				estat = FacturaEstatEnum.ENVIADA;
				break;
			}
			factura.update(estat);
		}
		factura.update(estat);
		return factura;
	}

	public ArxiuDto descarregar(
			Long facturaId) {
		logger.info("Descarregar factura (" +
				"facturaId=" + facturaId + ")");
		FacturaEntity factura = facturaRepository.findOne(facturaId);
		SubministramentEntity subministrament = factura.getSubministrament();
		FacturaScrapper scrapper = scrapperHelper.getFacturaScrapper(subministrament);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		Factura fac = new Factura();
		fac.setNumero(factura.getNumero());
		scrapper.connectar();
		scrapper.descarregarArxiu(fac, baos);
		ArxiuDto arxiu = new ArxiuDto();
		arxiu.setNom(subministrament.getProducte() + "_" + subministrament.getConnexio().getProveidor() + "_" + factura.getNumero() + ".pdf");
		arxiu.setContentType("application/pdf");
		arxiu.setContingut(baos.toByteArray());
		return arxiu;
	}

	private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

}
