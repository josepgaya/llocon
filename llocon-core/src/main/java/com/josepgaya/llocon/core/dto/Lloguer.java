/**
 * 
 */
package com.josepgaya.llocon.core.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.josepgaya.llocon.core.annotations.RestapiField;
import com.josepgaya.llocon.core.annotations.RestapiField.RestapiFieldType;
import com.josepgaya.llocon.core.annotations.RestapiGrid;
import com.josepgaya.llocon.core.annotations.RestapiResource;

/**
 * Informació d'un lloguer.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestapiResource(
	grids = {
			@RestapiGrid("subministrament")}
)
public class Lloguer extends AbstractIdentificable<Long> {

	@NotNull
	@Size(max = 64)
	private String codi;
	@NotNull
	@Size(max = 255)
	private String nom;
	@NotNull
	@Size(max = 255)
	private String adressa;
	@RestapiField(
			type = RestapiFieldType.BIGDECIMAL,
			hiddenInGrid = true,
			hiddenInForm = true,
			hiddenInLov = true)
	private BigDecimal importPendent;

	public String getCodi() {
		return codi;
	}
	public void setCodi(String codi) {
		this.codi = codi;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getAdressa() {
		return adressa;
	}
	public void setAdressa(String adressa) {
		this.adressa = adressa;
	}
	public BigDecimal getImportPendent() {
		return importPendent;
	}
	public void setImportPendent(BigDecimal importPendent) {
		this.importPendent = importPendent;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
