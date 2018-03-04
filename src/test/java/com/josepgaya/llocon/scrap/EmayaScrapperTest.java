/**
 * 
 */
package com.josepgaya.llocon.scrap;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

/**
 * Test per a l'scrapper d'Endesa
 * 
 * @author josepgaya
 *
 */
public class EmayaScrapperTest {
	
	private static String USERNAME = "user";
	private static String PASSWORD = "passwd";
	private static String CONTRACT = "123456789012";

	private FacturaScrapper facturaScrapper;

	@Before
	public void init() {
		facturaScrapper = new EmayaScrapper(
				USERNAME,
				PASSWORD,
				CONTRACT);
	}

	@Test
	public void test() throws Exception {
		facturaScrapper.connectar();
		System.out.println("--- Login: ok");
		List<Factura> factures = facturaScrapper.findDarreresFactures();
		System.out.println("--- Find: ok");
		assertNotNull(factures);
		System.out.println("---    Núm. factures: " + factures.size());
		assertNotEquals(factures.size(), 0);
		System.out.println("---    Darrera factura (" +
				"núm.=" + factures.get(0).getNumero() + ", " +
				"data=" + factures.get(0).getData() + ", " +
				"import=" + factures.get(0).getImportt() + ")");
		System.out.println("---    Descarregant darrera factura...");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		facturaScrapper.descarregarArxiu(factures.get(0), baos);
		//baos.writeTo(new FileOutputStream(new File("home/user/factura.pdf")));
		assertNotEquals(baos.size(), 0);
		System.out.println("--- Descarregada amb èxit (" + baos.size() + " bytes)");
	}

}
