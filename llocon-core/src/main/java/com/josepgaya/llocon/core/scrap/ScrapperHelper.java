/**
 * 
 */
package com.josepgaya.llocon.core.scrap;

import java.io.ByteArrayOutputStream;
import java.security.GeneralSecurityException;
import java.util.List;

import org.springframework.stereotype.Component;

import com.josepgaya.llocon.core.dto.File;
import com.josepgaya.llocon.core.dto.SubministramentProducteEnum;
import com.josepgaya.llocon.core.entity.ConnexioEntity;

/**
 * Helper per a obtenir l'scrapper a partir d'un subministrament.
 * 
 * @author josepgaya
 */
@Component
public class ScrapperHelper {

	public List<Factura> findDarreresFactures(
			ConnexioEntity connexio,
			String contracteNum) throws GeneralSecurityException {
		FacturaScrapper scrapper = getFacturaScrapper(connexio, contracteNum);
		scrapper.connectar();
		return scrapper.findDarreresFactures();
	}

	public File descarregarFactura(
			ConnexioEntity connexio,
			SubministramentProducteEnum producte,
			String contracteNum,
			String facturaNum) throws GeneralSecurityException {
		FacturaScrapper scrapper = getFacturaScrapper(connexio, contracteNum);
		scrapper.connectar();
		List<Factura> darreresFactures = scrapper.findDarreresFactures();
		File file = null;
		for (Factura factura: darreresFactures) {
			if (factura.getNumero().equals(facturaNum)) {
				file = new File();
				file.setName(producte + "_" + connexio.getProveidor() + "_" + facturaNum + ".pdf");
				file.setContentType("application/pdf");
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				scrapper.descarregarArxiu(factura, out);
				file.setContent(out.toByteArray());
				break;
			}
		}
		return file;
	}

	private FacturaScrapper getFacturaScrapper(
			ConnexioEntity connexio,
			String contracteNum) throws GeneralSecurityException {
		String usuari = connexio.getUsuari();
		String contrasenya = connexio.getContrasenya();
		FacturaScrapper scrapper = null;
		switch (connexio.getProveidor()) {
		case EMAYA:
			scrapper = new EmayaScrapper(
					usuari,
					contrasenya,
					contracteNum);
			break;
		case ENDESA:
			scrapper = new EndesaScrapper(
					usuari,
					contrasenya,
					contracteNum);
			break;
		case SAM:
			scrapper = new DocucloudScrapper(
					"2880",
					usuari,
					contrasenya,
					contracteNum);
			break;
		}
		return scrapper;
	}

}
