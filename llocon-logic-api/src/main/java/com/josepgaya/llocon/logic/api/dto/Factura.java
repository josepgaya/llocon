/**
 * 
 */
package com.josepgaya.llocon.logic.api.dto;

import java.math.BigDecimal;
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
 * Informació d'una factura.
 * 
 * @author josepgaya
 */
@Getter @Setter
public class Factura extends AbstractIdentificable<Long> {

	@NotNull
	@Size(max = 64)
	private String numero;
	@NotNull
	private Date data;
	@NotNull
	private BigDecimal importt;
	@NotNull
	private FacturaEstatEnum estat;
	@NotNull
	@Transient
	@RestapiField(
			type = RestapiFieldType.LOV,
			disabledForCreate = true,
			disabledForUpdate = true,
			hiddenInGrid = true,
			hiddenInForm = true,
			hiddenInLov = true)
	private GenericReference<Subministrament, Long> subministrament;

}
