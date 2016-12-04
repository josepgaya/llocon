/**
 * 
 */
package com.josepgaya.llocon.helper;

import org.springframework.stereotype.Component;

import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.scrap.DocucloudScrapper;
import com.josepgaya.llocon.scrap.EmayaScrapper;
import com.josepgaya.llocon.scrap.EndesaScrapper;
import com.josepgaya.llocon.scrap.FacturaScrapper;

/**
 * Helper per a obtenir l'scrapper a partir d'un subministrament.
 * 
 * @author josepgaya
 */
@Component
public class ScrapperHelper {

	public FacturaScrapper getFacturaScrapper(
			SubministramentEntity subministrament) {
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
		return scrapper;
	}

}
