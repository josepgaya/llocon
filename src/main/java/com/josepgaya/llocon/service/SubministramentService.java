/**
 * 
 */
package com.josepgaya.llocon.service;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.repository.FacturaRepository;
import com.josepgaya.llocon.repository.SubministramentRepository;
import com.josepgaya.llocon.scrap.DocucloudScrapper;
import com.josepgaya.llocon.scrap.EmayaScrapper;
import com.josepgaya.llocon.scrap.EndesaScrapper;
import com.josepgaya.llocon.scrap.Factura;
import com.josepgaya.llocon.scrap.FacturaScrapper;

/**
 * Servei de gestió de subministraments.
 * 
 * @author josepgaya
 */
@Service
public class SubministramentService {

	@Autowired
	private SubministramentRepository subministramentRepository;
	@Autowired
	private FacturaRepository facturaRepository;

	@Autowired
	private Environment environment;



	@Transactional
	public List<Factura> actualitzarFactures(
			Long subministramentId) {
		logger.info("Actualitzant factures (" +
				"subministramentId=" + subministramentId + ")");
		SubministramentEntity subministrament = subministramentRepository.findOne(
				subministramentId);
		if (subministrament == null) {
			throw new RuntimeException("Subministrament no trobat (" +
					"subministramentId=" + subministramentId + ")");
		}
		String basePath = environment.getProperty("llocon.base.path") + "/" + subministrament.getLloguer().getCodi() + "_" + subministrament.getProducte() + "_" + subministrament.getConnexio().getProveidor();
		new File(basePath).mkdirs();
		String usuari = subministrament.getConnexio().getUsuari();
		String contrasenya = subministrament.getConnexio().getContrasenya();
		String contracte = subministrament.getContracteNum();
		FacturaScrapper scrapper = null;
		switch (subministrament.getConnexio().getProveidor()) {
		case EMAYA:
			scrapper = new EmayaScrapper(
					usuari,
					contrasenya,
					contracte);
			break;
		case ENDESA:
			scrapper = new EndesaScrapper(
					usuari,
					contrasenya,
					contracte);
			break;
		case SAM:
			scrapper = new DocucloudScrapper(
					"2880",
					usuari,
					contrasenya,
					contracte);
			break;
		}
		try {
			return descarregarFacturesNoves(
					subministrament,
					scrapper,
					basePath);
		} catch (Exception ex) {
			throw new RuntimeException(
					"Error al actualitzar factures",
					ex);
		}
	}



	private List<Factura> descarregarFacturesNoves(
			SubministramentEntity subministrament,
			FacturaScrapper scrapper,
			String basePath) throws Exception {
		scrapper.connectar();
		List<Factura> factures = scrapper.findDarreresFactures();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		List<Factura> noves = new ArrayList<Factura>();
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
				String arxiuNom = factura.getNumero() + ".pdf";
				FileOutputStream out = (
						new FileOutputStream(
								new File(basePath + "/" + arxiuNom)));
				scrapper.descarregarArxiu(
						factura,
						out);
				facturaEntity = FacturaEntity.getBuilder(
						subministrament,
						factura.getNumero(),
						factura.getData(),
						factura.getImportt()).build();
				facturaRepository.save(facturaEntity);
				noves.add(factura);
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
		return noves;
	}

	private static final Logger logger = LoggerFactory.getLogger(SubministramentService.class);

}
