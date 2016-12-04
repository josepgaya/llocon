/**
 * 
 */
package com.josepgaya.llocon.service;

import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;

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
	public BigDecimal calcularImportPendent(String lloguerCodi) {
		logger.info("Calcular import pendent (lloguerCodi=" + lloguerCodi + ")");
		BigDecimal importPendent = facturaRepository.sumImportBySubministramentLloguerCodiAndNotEstat(
				lloguerCodi,
				FacturaEstatEnum.PAGADA);
		if (importPendent != null) {
			return importPendent;
		} else {
			return new BigDecimal(0);
		}
	}

	@Transactional
	public FacturaEstatEnum canviEstat(Long id) {
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
		return estat;
	}

	public ArxiuDto descarregar(
			Long facturaId) throws Exception {
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

	/*private void descarregarFacturesNoves(
			SubministramentEntity subministrament,
			FacturaScrapper scrapper,
			String basePath) throws Exception {
		scrapper.connectar();
		List<Factura> factures = scrapper.findDarreresFactures();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		for (Factura factura: factures) {
			FacturaEntity facturaEntity = facturaRepository.findBySubministramentAndNumero(
					subministrament,
					factura.getNumero());
			if (facturaEntity == null) {
				logger.info("Descarregant nova factura (" +
						"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
						"producte=" + subministrament.getProducte() + ", " +
						"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
						"numero=" + factura.getNumero() + ", " +
						"data=" + sdf.format(factura.getData()) + ", " +
						"import=" + factura.getImportt() + ")");
				scrapper.descarregarArxiu(
						factura,
						basePath);
				facturaEntity = FacturaEntity.getBuilder(
						subministrament,
						factura.getNumero(),
						factura.getData(),
						factura.getImportt()).build();
				facturaRepository.save(facturaEntity);
				logger.info("Factura guardada (" +
						"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
						"producte=" + subministrament.getProducte() + ", " +
						"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
						"numero=" + factura.getNumero() + ", " +
						"data=" + sdf.format(factura.getData()) + ", " +
						"import=" + factura.getImportt() + ")");
			} else {
				logger.info("Factura ja existeix a la base de dades (" +
						"lloguerCodi=" + subministrament.getLloguer().getCodi() + ", " +
						"producte=" + subministrament.getProducte() + ", " +
						"proveidor=" + subministrament.getConnexio().getProveidor() + ", " +
						"numero=" + factura.getNumero() + ", " +
						"data=" + sdf.format(factura.getData()) + ", " +
						"import=" + factura.getImportt() + ")");
			}
		}
		subministrament.updateDarreraActualitzacio(new Date());
	}*/

	private static final Logger logger = LoggerFactory.getLogger(FacturaService.class);

}
