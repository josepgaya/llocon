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
	public void connectar() throws IOException {
		loginResponse = login();
		if (loginResponse == null) {
			throw new RuntimeException("Login incorrecte");
		}
	}

	@Override
	public List<Factura> findDarreresFactures() throws Exception {
		if (loginResponse != null) {
			Connection.Response facturaLlistat = Jsoup.connect(
					"https://www.emaya.es/ca/oficina-virtual/comptes-de-contracte/consulta-de-factures/").
					data("cuentaContrato", contracte).
					cookies(loginResponse.cookies()).
					method(Connection.Method.POST).
					timeout(REQUEST_TIMEOUT).
		            execute();
			Document facturaLlistatDocument = facturaLlistat.parse();
			List<Factura> factures = parseFactures(
					facturaLlistatDocument.select("table.consultaDeFacturas tbody tr"));
			return factures;
		} else {
			throw new RuntimeException("Sense connexió");
		}
	}

	@Override
	public void descarregarArxiu(
			Factura factura,
			OutputStream out) throws IOException {
		if (loginResponse != null) {
			Connection.Response fileResponse = Jsoup.connect(
					"https://www.emaya.es/umbraco/Surface/GestionFacturas/CopiaFactura?numFac=" + factura.getNumero()).
					cookies(loginResponse.cookies()).
                    ignoreContentType(true).
					timeout(REQUEST_TIMEOUT).
                    execute();
			out.write(fileResponse.bodyAsBytes());
			out.close();
		} else {
			throw new RuntimeException("Sense connexió");
		}
	}

	private Connection.Response login() throws IOException {
		Connection.Response loginForm = Jsoup.connect(
				"https://www.emaya.es/ca/oficina-virtual/acces-al-sistema").
				method(Connection.Method.GET).
	            execute();
		Document loginFormDocument = loginForm.parse();
		String ufprtValue = loginFormDocument.select("input[name=ufprt]").get(0).attr("value");
		Connection.Response loginResponse = Jsoup.connect(
				"https://www.emaya.es/ca/oficina-virtual/acces-al-sistema").
				data("returnUrl", "/ca/oficina-virtual/inici/").
				data("returnUrlKO", "/ca/oficina-virtual/error-acces-al-sistema/").
				data("returnUrlPwd", "/ca/oficina-virtual/usuaris/canvi-password/").
				data("returnUrlOlvidoPwd", "/ca/oficina-virtual/oblit-password").
	            data("usu", usuari).
	            data("pwd", contrasenya).
	            data("ufprt", ufprtValue).
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
			String importText = columnes.get(2).text().trim();
			fact.setImportt(
					(BigDecimal)decimalFormat.parse(importText.substring(0, importText.length() - 1)));
			facts.add(fact);
		}
		return facts;
	}

}
