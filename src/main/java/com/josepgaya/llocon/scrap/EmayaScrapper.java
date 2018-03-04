/**
 * 
 */
package com.josepgaya.llocon.scrap;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Implementació de l'scrapper de factures per EMAYA.
 * 
 * @author josepgaya
 */
public class EmayaScrapper implements FacturaScrapper {

	private static final int REQUEST_TIMEOUT = 20000;

	private String usuari;
	private String contrasenya;
	private String contracte;

	private Connection.Response loginResponse;

	public EmayaScrapper(String usuari, String contrasenya, String contracte) {
		super();
		this.usuari = usuari;
		this.contrasenya = contrasenya;
		this.contracte = contracte;
	}

	@Override
	public void connectar() {
		loginResponse = login();
		if (loginResponse == null) {
			throw new ScrapperException("Login incorrecte");
		}
	}

	@Override
	public List<Factura> findDarreresFactures() {
		if (loginResponse != null) {
			try {
				Connection.Response facturaLlistat = Jsoup.connect(
						"https://www.emaya.es/ca/oficina-virtual/comptes-de-contracte/consulta-de-factures/").
						data("cuentaContrato", contracte).
						cookies(loginResponse.cookies()).
						method(Connection.Method.POST).
						timeout(REQUEST_TIMEOUT).
			            execute();
				System.out.println(">>> " + facturaLlistat.body());
				Document facturaLlistatDocument = facturaLlistat.parse();
				List<Factura> factures = parseFactures(
						facturaLlistatDocument.select("table.consultaDeFacturas tbody tr"));
				return factures;
			} catch (Exception ex) {
				throw new ScrapperException(
						"Error en la consulta de les darreres factures",
						ex);
			}
		} else {
			throw new ScrapperException("Sense connexió");
		}
	}

	@Override
	public void descarregarArxiu(
			Factura factura,
			OutputStream out) {
		if (loginResponse != null) {
			try {
				Connection.Response fileResponse = Jsoup.connect(
						"https://www.emaya.es/umbraco/Surface/GestionFacturas/CopiaFactura?numFac=" + factura.getNumero()).
						cookies(loginResponse.cookies()).
	                    ignoreContentType(true).
						timeout(REQUEST_TIMEOUT).
	                    execute();
				out.write(fileResponse.bodyAsBytes());
				out.close();
			} catch (IOException ex) {
				throw new ScrapperException(
						"Error al descarregar arxiu",
						ex);
			}
		} else {
			throw new RuntimeException("Sense connexió");
		}
	}

	private Connection.Response login() {
		try {
			Connection.Response loginForm = Jsoup.connect(
					"https://www.emaya.es/ca/oficina-virtual/").
					method(Connection.Method.GET).
		            execute();
			Connection.Response loginResponse = Jsoup.connect(
					"https://www.emaya.es/umbraco/Surface/MemberLoginSurface/MemberLogin").
					data("returnUrl", "https://www.emaya.es/ca/oficina-virtual/inici/").
					data("returnUrlKO", "#").
					data("returnUrlPwd", "https://www.emaya.es/ca/oficina-virtual/usuaris/canvi-password/").
					data("returnUrlOlvidoPwd", "https://www.emaya.es/ca/oficina-virtual/oblit-password").
		            data("usu", usuari).
		            data("pwd", contrasenya).
		            cookies(loginForm.cookies()).
		            method(Connection.Method.POST).
					timeout(REQUEST_TIMEOUT).
		            execute();
			Document loginResponseDocument = loginResponse.parse();
			Elements loginTextElements = loginResponseDocument.select("font[class=OraInstructionText]");
			if (loginTextElements.size() == 0) {
				// Login ok
				return loginResponse;
			} else {
				// Login error
				return null;
			}
		} catch (IOException ex) {
			throw new ScrapperException(
					"Error en el login",
					ex);
		}
	}

	private static List<Factura> parseFactures(
			Elements factures) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		List<Factura> facts = new ArrayList<Factura>();
		Iterator<Element> facturesIt = factures.iterator();
		while (facturesIt.hasNext()) {
			Element factura = facturesIt.next();
			Elements columnes = factura.select("td");
			Factura fact = new Factura();
			fact.setNumero(
					columnes.get(0).select("form input[name=numFac]").get(0).attr("value"));
			fact.setData(
					sdf.parse(columnes.get(1).text().trim()));
			String importText = columnes.get(3).text().trim();
			fact.setImportt(
					(BigDecimal)decimalFormat.parse(importText.substring(0, importText.length() - 1)));
			facts.add(fact);
		}
		return facts;
	}

}
