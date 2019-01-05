/**
 * 
 */
package com.josepgaya.llocon.core.dto;

import java.util.Date;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.josepgaya.llocon.core.annotations.RestapiField;
import com.josepgaya.llocon.core.annotations.RestapiField.RestapiFieldType;

/**
 * Informació d'un subministrament.
 * 
 * @author josepgaya
 */
public class Subministrament extends AbstractIdentificableWithParent<Long, Long> {

	@NotNull
	private SubministramentProducteEnum producte;
	@NotNull
	@Size(max = 64)
	private String contracteNum;
	/*@RestapiField(
			hiddenInGrid = true,
			hiddenInForm = true)*/
	private Date darreraActualitzacio;
	@NotNull
	@RestapiField(
			type = RestapiFieldType.LOV)
	private Connexio connexio;

	public SubministramentProducteEnum getProducte() {
		return producte;
	}
	public void setProducte(SubministramentProducteEnum producte) {
		this.producte = producte;
	}
	public String getContracteNum() {
		return contracteNum;
	}
	public void setContracteNum(String contracteNum) {
		this.contracteNum = contracteNum;
	}
	public Date getDarreraActualitzacio() {
		return darreraActualitzacio;
	}
	public void setDarreraActualitzacio(Date darreraActualitzacio) {
		this.darreraActualitzacio = darreraActualitzacio;
	}
	public Connexio getConnexio() {
		return connexio;
	}
	public void setConnexio(Connexio connexio) {
		this.connexio = connexio;
	}

	public String getDescripcio() {
		if (connexio == null) {
			return null;
		} else {
			return producte.name() + " " + connexio.getProveidor().name() + " " + contracteNum;
		}
	}
	public String getDescripcioCurta() {
		if (connexio == null) {
			return null;
		} else {
			return producte.name() + " " + connexio.getProveidor().name();
		}
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
