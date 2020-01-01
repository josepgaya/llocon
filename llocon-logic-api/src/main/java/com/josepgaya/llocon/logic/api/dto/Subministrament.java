/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import java.util.Date;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.josepgaya.base.boot.logic.api.annotation.RestapiField;
import com.josepgaya.base.boot.logic.api.dto.ProfileResourceField.RestapiFieldType;
import com.josepgaya.base.boot.logic.api.dto.util.AbstractIdentificable;
import com.josepgaya.base.boot.logic.api.dto.util.GenericReference;

import lombok.Getter;
import lombok.Setter;

/**
 * Informació d'un subministrament.
 * 
 * @author josepgaya
 */
@Getter @Setter
public class Subministrament extends AbstractIdentificable<Long> {

	@NotNull
	private SubministramentProducteEnum producte;
	@NotNull
	@Size(max = 64)
	private String contracteNum;
	private Date darreraActualitzacio;
	@NotNull
	@Transient
	@RestapiField(
			type = RestapiFieldType.LOV)
	private GenericReference<Connexio, Long> connexio;
	@NotNull
	@Transient
	@RestapiField(
			type = RestapiFieldType.LOV,
			disabledForCreate = true,
			disabledForUpdate = true,
			hiddenInGrid = true,
			hiddenInForm = true,
			hiddenInLov = true)
	private GenericReference<Lloguer, Long> lloguer;

}
