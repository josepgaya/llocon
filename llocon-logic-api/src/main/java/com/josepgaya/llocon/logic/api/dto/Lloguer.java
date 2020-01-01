/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import java.math.BigDecimal;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.josepgaya.base.boot.logic.api.annotation.RestapiField;
import com.josepgaya.base.boot.logic.api.annotation.RestapiGrid;
import com.josepgaya.base.boot.logic.api.annotation.RestapiResource;
import com.josepgaya.base.boot.logic.api.dto.ProfileResourceField.RestapiFieldType;
import com.josepgaya.base.boot.logic.api.dto.util.AbstractIdentificable;

import lombok.Getter;
import lombok.Setter;

/**
 * Informació d'un lloguer.
 * 
 * @author josepgaya
 */
@Getter @Setter
@RestapiResource(
	grids = {
			@RestapiGrid("subministrament")}
)
public class Lloguer extends AbstractIdentificable<Long> {

	@NotNull
	@Size(max = 10)
	private String codi;
	@NotNull
	@Size(max = 100)
	private String nom;
	@NotNull
	@Size(max = 200)
	private String adressa;
	@RestapiField(
			type = RestapiFieldType.BIGDECIMAL,
			hiddenInGrid = true,
			hiddenInForm = true,
			hiddenInLov = true)
	private BigDecimal importPendent;

}
