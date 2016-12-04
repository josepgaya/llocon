/**
 * 
 */
package com.josepgaya.llocon.dto;

import java.math.BigDecimal;
import java.util.Date;

import com.josepgaya.llocon.entity.FacturaEstatEnum;

/**
 * Objecte de transferència amb informació d'una factura.
 * 
 * @author josepgaya
 */
public class FacturaDto {

	private String numero;
	private Date data;
	private BigDecimal importt;
	private FacturaEstatEnum estat;
	private SubministramentDto subministrament;

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
	public BigDecimal getImportt() {
		return importt;
	}
	public void setImportt(BigDecimal importt) {
		this.importt = importt;
	}
	public FacturaEstatEnum getEstat() {
		return estat;
	}
	public void setEstat(FacturaEstatEnum estat) {
		this.estat = estat;
	}
	public SubministramentDto getSubministrament() {
		return subministrament;
	}
	public void setSubministrament(SubministramentDto subministrament) {
		this.subministrament = subministrament;
	}

}
