/**
 * 
 */
package com.josepgaya.llocon.logic.scrap;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Implementació de l'scrapper de factures per Endesa.
 * 
 * @author josepgaya
 */
public class EndesaScrapper implements FacturaScrapper {

	private String usuari;
	private String contrasenya;
	private String contracte;
	private String authkey;

	private boolean loggedIn = false;
	private Map<String, String> cookiesSaved;
	
	private int requestTimeout = 30000;

	public EndesaScrapper(String usuari, String contrasenya, String contracte) {
		super();
		this.usuari = usuari;
		this.contrasenya = contrasenya;
		this.contracte = contracte;
	}

	@Override
	public void connectar() {
		try {
			Connection.Response loginResponse = Jsoup.connect(
					"https://www.endesaclientes.com/sso/login").
					data("location", "ib-es").
					data("loginurl", "/Login.html").
					data("service", "/oficina/gestion-online.html").
		            data("alias", usuari).
		            data("password", contrasenya).
		            data("loginButton", "Acceder").
					timeout(requestTimeout).
		            method(Connection.Method.POST).
		            execute();
			Document loginResponseDocument = loginResponse.parse();
			Elements loginTextElements = loginResponseDocument.select("meta[content=0;url=/oficina/gestion-online.html?neolpostlogin=true]");
			if (loginTextElements.size() > 0) {
				cookiesSaved = loginResponse.cookies();
				loggedIn = true;
				Jsoup.connect(
						"https://www.endesaclientes.com/oficina/gestiononline?neolpostlogin=true").
			            cookies(cookiesSaved).
						timeout(requestTimeout).
			            execute();
				String authKeyUrl = "https://www.endesaclientes.com/ss/Satellite?pagename=SiteEntry/NEOL/Site/Page/WrapperPage/SendParameterAuthkey";
				Connection.Response authKeyResponse = Jsoup.connect(authKeyUrl).cookies(cookiesSaved).
						timeout(requestTimeout).
			            execute();
				authkey = authKeyResponse.body();
			} else {
				throw new ScrapperException("Login incorrecte");
			}
		} catch (IOException ex) {
			throw new ScrapperException(
					"Error en el login",
					ex);
		}
	}

	@Override
	public List<Factura> findDarreresFactures() {
		if (loggedIn) {
			try {
				String url = "https://www.endesaclientes.com/ss/Satellite?c=Page&pagename=SiteEntry_IB_ES%2FBill_Search%2FSearch_List&rand=" + randomValue() + "&rand=" + randomValue() + "";
				Connection.Response facturaLlistat = Jsoup.connect(url).
						data("address", contracte).
						data("state", "Todos").
						data("cid", "1383140478917").
						data("locale", "1383137899042").
						data("_authkey_", authkey).
						cookies(cookiesSaved).
						method(Connection.Method.POST).
						timeout(requestTimeout).
			            execute();
				return parseFactures(
						Jsoup.parseBodyFragment(facturaLlistat.body()));
			} catch (Exception ex) {
				throw new ScrapperException(
						"Error en la consulta de les darreres factures",
						ex);
			}
		} else {
			throw new RuntimeException("Sense connexió");
		}
	}

	@Override
	public void descarregarArxiu(
			Factura factura,
			OutputStream out) {
		Map<String, String> params = factura.getDescarregaParams();
		if (params == null) {
			List<Factura> factures = findDarreresFactures();
			for (Factura fact: factures) {
				if (fact.getNumero().equals(factura.getNumero())) {
					params = fact.getDescarregaParams();
					break;
				}
			}
		}
		String jsonDownloadPdf = "{\"billSearch\":{\"billNumber\":\"" + params.get("billNumEncrypted") + "  \",\"secBill\":\"" + params.get("secBill") + "\",\"contractNumber\":\"\",\"holderCompanyCode\":\"" + params.get("holderCompanyCode") + "\",\"businessLine\":\"" + params.get("businessLine") + "\",\"numscct\":\"\",\"refBill\":\"" + contracte + "\"}}";
		if (loggedIn) {
			try {
				Connection.Response locationResponse = Jsoup.connect(
						"https://www.endesaclientes.com/ss/Satellite?rand=" + randomValue() + "&rand=" + randomValue()).
						data("pagename", "SiteEntry_IB_ES/Bill_Search/ValidateClientDownloadBill").
						data("locale", "null").
						data("jsonDownloadPdf", jsonDownloadPdf).
						data("statePay", params.get("statePay")).
						data("amount", params.get("amount")).
						data("bondDivision", params.get("bondDivision")).
						data("emissionDate", params.get("emissionDate")).
						data("ch", params.get("ch")).
						data("billNum", params.get("billNumEncrypted")).
						data("barCode", params.get("barCode")).
						data("contractNumber", params.get("refBill")).
						data("_authkey_", authkey).
						cookies(cookiesSaved).
						method(Connection.Method.POST).
						timeout(requestTimeout).
	                    execute();
				Elements noscript = locationResponse.parse().select("noscript");
				if (noscript.size() == 0) {
					Document locationDocument = Jsoup.parseBodyFragment(locationResponse.body());
					String locationJs = null;
					for (Element script: locationDocument.select("script")) {
						if (script.html().indexOf("location.href") != -1) {
							locationJs = script.html().trim();
							break;
						}
					}
					if (locationJs != null) {
						String downloadUrl = locationJs.split("'")[1];
						Connection.Response fileResponse = Jsoup.connect(
								"https://www.endesaclientes.com" + downloadUrl).
								cookies(cookiesSaved).
			                    ignoreContentType(true).
								timeout(requestTimeout).
			                    execute();
						out.write(fileResponse.bodyAsBytes());
						out.close();
					} else {
						throw new ScrapperException(
								"No s'ha trobat la URL de descàrrega al document: \n" + locationResponse.body());
					}
				} else {
					throw new RuntimeException("La resposta a la petició per localitzar la factura no és correcta: " + locationResponse.body());
				}
			} catch (IOException ex) {
				throw new ScrapperException(
						"Error al descarregar arxiu",
						ex);
			}
		} else {
			throw new RuntimeException("Sense connexió");
		}
	}

	private static List<Factura> parseFactures(
			Document llistatDocument) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss z yyyy",
				Locale.ENGLISH);
		SimpleDateFormat sdFormat = new SimpleDateFormat("dd/MM/yyyy");
		DecimalFormatSymbols symbols = new DecimalFormatSymbols();
		symbols.setGroupingSeparator('.');
		symbols.setDecimalSeparator(',');
		String pattern = "#,##0.0#";
		DecimalFormat decimalFormat = new DecimalFormat(pattern, symbols);
		decimalFormat.setParseBigDecimal(true);
		List<Factura> facts = new ArrayList<Factura>();
		Elements factures = llistatDocument.select("tbody.invoices_body tr");
		Iterator<Element> facturesIt = factures.iterator();
		int facturaIndex = 0;
		while (facturesIt.hasNext()) {
			Element factura = facturesIt.next();
			Elements columnes = factura.select("td");
			String primerTdId = columnes.get(0).attr("id");
			if (!primerTdId.startsWith("tdDetailBill")) {
				Factura fact = new Factura();
				fact.setNumero(
						columnes.get(0).select("span.sld_selector").get(0).attr("databillnumber").trim());
				fact.setData(
						sdf.parse(columnes.get(2).text().trim()));
				fact.setImportt(
						(BigDecimal)decimalFormat.parse(columnes.get(3).text().trim().split(" ")[0]));
				Map<String, String> descarregaParams = new HashMap<String, String>();
				descarregaParams.put(
						"secBill",
						llistatDocument.select("tbody.invoices_body input[name=secBill_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"holderCompanyCode",
						llistatDocument.select("tbody.invoices_body input[name=holderCompanyCode_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"businessLine",
						llistatDocument.select("tbody.invoices_body input[name=businessLine_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"statePay",
						llistatDocument.select("tbody.invoices_body input[name=numBill_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"amount",
						llistatDocument.select("tbody.invoices_body input[name=amount_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"bondDivision",
						llistatDocument.select("tbody.invoices_body input[name=bondDivisionId_" + facturaIndex + "]").attr("value"));
				String emissionDate = llistatDocument.select("tbody.invoices_body input[name=emissionDate_" + facturaIndex + "]").attr("value");
				descarregaParams.put(
						"emissionDate",
						sdFormat.format(sdf.parse(emissionDate)));
				descarregaParams.put(
						"ch",
						"false");
				descarregaParams.put(
						"billNum",
						llistatDocument.select("tbody.invoices_body input[name=numBill_" + facturaIndex + "]").attr("value"));
				descarregaParams.put(
						"billNumEncrypted",
						llistatDocument.select("tbody.invoices_body input[name=numBillEncrypted_" + facturaIndex + "]").attr("value"));
				String statePay = llistatDocument.select("tbody.invoices_body input[name=statePay_" + facturaIndex + "]").attr("value");
				descarregaParams.put(
						"statePay",
						statePay);
				String barCode = "N";
				if (statePay != null) {
					Integer statePayInt = Integer.parseInt(statePay);
					if (statePayInt > 4 && statePayInt < 16) {
						barCode = "S";
					}
				}
				descarregaParams.put(
						"barCode",
						barCode);
				descarregaParams.put(
						"refBill",
						llistatDocument.select("tbody.invoices_body input[name=refBill_" + facturaIndex + "]").attr("value"));
				fact.setDescarregaParams(descarregaParams);
				facts.add(fact);
				facturaIndex++;
			}
		}
		return facts;
	}

	private String randomValue() {
		return Integer.valueOf(Double.valueOf(Math.floor(Math.random() * 99999)).intValue()).toString();
		
	}

}
