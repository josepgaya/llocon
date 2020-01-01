/**
 * 
 */
package com.josepgaya.llocon.logic.scrap;

import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Implementació de l'scrapper de factures per Docucloud.
 * 
 * @author josepgaya
 */
public class DocucloudScrapper implements FacturaScrapper {

	private String empresa;
	private String usuari;
	private String contrasenya;
	private String contracte;

	private Connection.Response loginResponse;

	public DocucloudScrapper(String empresa, String usuari, String contrasenya, String contracte) {
		super();
		this.empresa = empresa;
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
				Connection.Response getFacturas = Jsoup.connect(
						"https://docucloud.net/clientes/getFacturas").
						data("scl_id", contracte).
						data("doc_id", "T").
						cookies(loginResponse.cookies()).
						method(Connection.Method.POST).
				        execute();
				return parseFactures(getFacturas.body());
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
						"https://docucloud.net/facturas/download&documento=" + factura.getNumero()).
						cookies(loginResponse.cookies()).
	                    ignoreContentType(true).
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
			
			// Create a trust manager that does not validate certificate chains
	        TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
	                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
	                    return null;
	                }
	                public void checkClientTrusted(X509Certificate[] certs, String authType) {
	                }
	                public void checkServerTrusted(X509Certificate[] certs, String authType) {
	                }
	            }
	        };
	        // Install the all-trusting trust manager
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	 
	        // Create all-trusting host name verifier
	        HostnameVerifier allHostsValid = new HostnameVerifier() {
	            public boolean verify(String hostname, SSLSession session) {
	                return true;
	            }
	        };
	        // Install the all-trusting host verifier
	        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
	        
			Connection.Response loginForm = Jsoup.connect(
					"https://docucloud.net/").
					method(Connection.Method.GET).
			        execute();
			Connection.Response loginResponse = Jsoup.connect(
					"https://docucloud.net/login/doLogin").
					data("emp", empresa).
		            data("usu", usuari).
		            data("pwd", contrasenya).
		            data("login", "Login").
		            cookies(loginForm.cookies()).
		            method(Connection.Method.POST).
		            execute();
			String loginResponseBody = loginResponse.body().trim();
			if (!"3".equals(loginResponseBody) && !"4".equals(loginResponseBody)) {
				// Login ok
				return loginForm;
			} else {
				// Login error
				return null;
			}
		} catch (IOException ex) {
			throw new ScrapperException(
					"Error en el login",
					ex);
		} catch (NoSuchAlgorithmException ex) {
			throw new ScrapperException(
					"Error en el login",
					ex);
		} catch (KeyManagementException ex) {
			throw new ScrapperException(
					"Error en el login",
					ex);
		}
	}

	private static List<Factura> parseFactures(
			String resposta) throws JsonProcessingException, IOException, ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		ObjectMapper mapper = new ObjectMapper();
		List<Factura> factures = new ArrayList<Factura>();
		JsonNode root = mapper.readTree(resposta);
		Iterator<JsonNode> nodesIt = root.elements();
		while (nodesIt.hasNext()) {
			FacturaDocucloud fd = mapper.treeToValue(
					nodesIt.next(),
					FacturaDocucloud.class);
			Factura fact = new Factura();
			fact.setNumero(fd.getId());
			fact.setData(sdf.parse(fd.getData()));
			fact.setImportt(new BigDecimal(fd.getImportt()));
			fact.setArxiuNom(fd.getArxiu());
			factures.add(fact);
		}
		return factures;
	}

}
