/**
 * 
 */
package com.josepgaya.llocon.service;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.dto.ArxiuDto;
import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.entity.FacturaEstatEnum;
import com.josepgaya.llocon.entity.LloguerEntity;
import com.josepgaya.llocon.repository.FacturaRepository;
import com.josepgaya.llocon.repository.LloguerRepository;

/**
 * Servei de gestió de lloguers.
 * 
 * @author josepgaya
 */
@Service
public class LloguerService {

	@Autowired
	private LloguerRepository lloguerRepository;
	@Autowired
	private FacturaRepository facturaRepository;



	@Transactional
	public ArxiuDto generarCsv(
			String lloguerCodi) {
		logger.info("Generant CSV all (" +
				"lloguerCodi=" + lloguerCodi + ")");
		LloguerEntity lloguer = lloguerRepository.findByCodi(lloguerCodi);
		if (lloguer == null) {
			throw new RuntimeException("Lloguer no trobat (lloguerCodi=" + lloguerCodi + ")");
		}
		StringBuilder csv = new StringBuilder();
		List<FacturaEntity> factures = facturaRepository.findBySubministramentLloguerOrderByDataDesc(lloguer);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("0.00");
		csv.append("LLOGUER;PRODUCTE;PROVEIDOR;DATA;IMPORT;NUMERO;ESTAT\n");
		for (FacturaEntity factura: factures) {
			csv.append(lloguer.getCodi());
			csv.append(";");
			csv.append(factura.getSubministrament().getProducte());
			csv.append(";");
			csv.append(factura.getSubministrament().getConnexio().getProveidor());
			csv.append(";");
			csv.append(sdf.format(factura.getData()));
			csv.append(";");
			csv.append(df.format(factura.getImportt()) + " €");
			csv.append(";");
			csv.append(factura.getNumero());
			csv.append(";");
			csv.append(factura.getEstat());
			csv.append("\n");
		}
		ArxiuDto arxiu = new ArxiuDto();
		arxiu.setNom(lloguer.getCodi() + "_" + FacturaEstatEnum.PENDENT + ".csv");
		arxiu.setContentType("text/csv");
		arxiu.setContingut(csv.toString().getBytes());
		return arxiu;
	}

	public ArxiuDto generarCsvPendent(
			String lloguerCodi) {
		logger.info("Generant CSV pendent (" +
				"lloguerCodi=" + lloguerCodi + ")");
		LloguerEntity lloguer = lloguerRepository.findByCodi(lloguerCodi);
		if (lloguer == null) {
			throw new RuntimeException("Lloguer no trobat (lloguerCodi=" + lloguerCodi + ")");
		}
		StringBuilder csv = new StringBuilder();
		List<FacturaEntity> factures = facturaRepository.findBySubministramentLloguerAndEstatOrderByDataDesc(
				lloguer,
				FacturaEstatEnum.PENDENT);
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormat df = new DecimalFormat("0.00");
		csv.append("LLOGUER;PRODUCTE;PROVEIDOR;DATA;IMPORT;NUMERO;ESTAT\n");
		for (FacturaEntity factura: factures) {
			csv.append(lloguer.getCodi());
			csv.append(";");
			csv.append(factura.getSubministrament().getProducte());
			csv.append(";");
			csv.append(factura.getSubministrament().getConnexio().getProveidor());
			csv.append(";");
			csv.append(sdf.format(factura.getData()));
			csv.append(";");
			csv.append(df.format(factura.getImportt()) + " €");
			csv.append(";");
			csv.append(factura.getNumero());
			csv.append(";");
			csv.append(factura.getEstat());
			csv.append("\n");
		}
		ArxiuDto arxiu = new ArxiuDto();
		arxiu.setNom(lloguer.getCodi() + "_" + FacturaEstatEnum.PENDENT + ".csv");
		arxiu.setContentType("text/csv");
		arxiu.setContingut(csv.toString().getBytes());
		return arxiu;
	}

	private static final Logger logger = LoggerFactory.getLogger(LloguerService.class);

}
