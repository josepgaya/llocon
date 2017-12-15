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

	public void connectar() throws ScrapperException;

	public List<Factura> findDarreresFactures() throws ScrapperException;

	public void descarregarArxiu(
			Factura factura,
			OutputStream out) throws ScrapperException;

}
