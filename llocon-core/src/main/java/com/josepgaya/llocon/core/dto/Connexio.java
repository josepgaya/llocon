/**
 * 
 */
package com.josepgaya.llocon.core.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.josepgaya.llocon.core.annotations.RestapiField;
import com.josepgaya.llocon.core.annotations.RestapiField.RestapiFieldType;
import com.josepgaya.llocon.core.annotations.RestapiResource;

/**
 * Informació d'una connexió.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RestapiResource(
		descriptionField = "nom")
public class Connexio extends AbstractIdentificable<Long> {

	@NotNull
	@Size(max = 255)
	private String nom;
	@NotNull
	private SubministramentProveidorEnum proveidor;
	@NotNull
	@Size(max = 64)
	private String usuari;
	@NotNull
	@Size(max = 64)
	@RestapiField(
			type = RestapiFieldType.PASSWORD,
			hiddenInGrid = true,
			hiddenInLov = true)
	private String contrasenya;

	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public SubministramentProveidorEnum getProveidor() {
		return proveidor;
	}
	public void setProveidor(SubministramentProveidorEnum proveidor) {
		this.proveidor = proveidor;
	}
	public String getUsuari() {
		return usuari;
	}
	public void setUsuari(String usuari) {
		this.usuari = usuari;
	}
	public String getContrasenya() {
		return contrasenya;
	}
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}

}
