/**
 * 
 */
package com.josepgaya.llocon.scrap;

import java.io.OutputStream;
import java.util.List;

/**
 * Interfície de l'scrapper de factures.
 * 
 * @author josepgaya
 */
public interface FacturaScrapper {

	public void connectar() throws Exception;

	public List<Factura> findDarreresFactures() throws Exception;

	public void descarregarArxiu(
			Factura factura,
			OutputStream out) throws Exception;

}
