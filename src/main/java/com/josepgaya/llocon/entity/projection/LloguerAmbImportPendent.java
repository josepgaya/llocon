/**
 * 
 */
package com.josepgaya.llocon.entity.projection;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.josepgaya.llocon.entity.LloguerEntity;

/**
 * Projecció per a obtenir el lloguer juntament amb el seu import pendent.
 * 
 * @author josepgaya
 */
@Projection(name = "ambImportPendent", types = { LloguerEntity.class })
public interface LloguerAmbImportPendent {

	@Value("#{target}")
	LloguerEntity getLloguer();
	@Value("#{@facturaRepository.sumImportBySubministramentLloguerCodiAndNotEstat(target.codi, T(com.josepgaya.llocon.entity.FacturaEstatEnum).PAGADA)}")
	BigDecimal getImportPendent();

}
