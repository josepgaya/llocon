/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.josepgaya.base.boot.logic.api.annotation.RestapiField;
import com.josepgaya.base.boot.logic.api.annotation.RestapiResource;
import com.josepgaya.base.boot.logic.api.dto.ProfileResourceField.RestapiFieldType;
import com.josepgaya.base.boot.logic.api.dto.util.AbstractIdentificable;

import lombok.Getter;
import lombok.Setter;

/**
 * Informació d'una connexió.
 * 
 * @author josepgaya
 */
@Getter @Setter
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

}
