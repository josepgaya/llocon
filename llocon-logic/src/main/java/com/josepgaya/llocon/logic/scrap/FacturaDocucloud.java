/**
 * 
 */
package com.josepgaya.llocon.logic.scrap;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Objecte per a parsejar el JSON d'una factura de Docucloud.
 * 
 * @author Límit Tecnologies <limit@limit.es>
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class FacturaDocucloud {

	@JsonProperty(value="doc_id")
	private String id;
	@JsonProperty(value="doc_num")
	private String numero;
	@JsonProperty(value="doc_dat")
	private String data;
	@JsonProperty(value="scli_des")
	private String contracte;
	@JsonProperty(value="doc_arc")
	private String arxiu;
	@JsonProperty(value="doc_imp")
	private String importt;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getContracte() {
		return contracte;
	}
	public void setContracte(String contracte) {
		this.contracte = contracte;
	}
	public String getArxiu() {
		return arxiu;
	}
	public void setArxiu(String arxiu) {
		this.arxiu = arxiu;
	}
	public String getImportt() {
		return importt;
	}
	public void setImportt(String importt) {
		this.importt = importt;
	}

}
