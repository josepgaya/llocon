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
public class EndesaScrapperTest {
	
	private static String USERNAME = "USER";
	private static String PASSWORD = "PASS";
	private static String CONTRACT = "123456789012";

	private FacturaScrapper facturaScrapper;

	@Before
	public void init() {
		facturaScrapper = new EndesaScrapper(
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
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		facturaScrapper.descarregarArxiu(factures.get(0), baos);
		System.out.println("--- Download: ok (" + baos.size() + " bytes)");
		//baos.writeTo(new FileOutputStream(new File("home/user/factura.pdf")));
		assertNotEquals(baos.size(), 0);
	}

}
