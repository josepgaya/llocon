/**
 * 
 */
package com.josepgaya.llocon.core.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotNull;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Informació d'una factura.
 * 
 * @author josepgaya
 */
public class Factura extends AbstractIdentificableWithParent<Long, Long> {

	@NotNull
	private String numero;
	@NotNull
	private Date data;
	@NotNull
	private BigDecimal importt;
	@NotNull
	private FacturaEstatEnum estat;
	private Subministrament subministrament;

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
	public Subministrament getSubministrament() {
		return subministrament;
	}
	public void setSubministrament(Subministrament subministrament) {
		this.subministrament = subministrament;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
