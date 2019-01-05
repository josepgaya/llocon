/**
 * 
 */
package com.josepgaya.llocon.core.scrap;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

/**
 * Informació d'una factura obtinguda des d'un scrapper.
 * 
 * @author josepgaya
 */
public class Factura {

	private String numero;
	private Date data;
	private String arxiuNom;
	private BigDecimal importt;
	private Map<String, String> descarregaParams;

	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}
	public String getArxiuNom() {
		return arxiuNom;
	}
	public void setArxiuNom(String arxiuNom) {
		this.arxiuNom = arxiuNom;
	}
	public BigDecimal getImportt() {
		return importt;
	}
	public void setImportt(BigDecimal importt) {
		this.importt = importt;
	}
	public Map<String, String> getDescarregaParams() {
		return descarregaParams;
	}
	public void setDescarregaParams(Map<String, String> descarregaParams) {
		this.descarregaParams = descarregaParams;
	}

}
