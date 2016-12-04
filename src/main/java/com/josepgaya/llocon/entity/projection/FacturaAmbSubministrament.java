/**
 * 
 */
package com.josepgaya.llocon.entity.projection;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.entity.FacturaEstatEnum;
import com.josepgaya.llocon.entity.SubministramentEntity;

/**
 * Projecció per a obtenir la factura amb l'estructura desitjada de Spring
 * Data Rest.
 * 
 * @author josepgaya
 */
@Projection(name = "facturaAmbSubministrament", types = { FacturaEntity.class })
public interface FacturaAmbSubministrament {

	public Long getId();
	public String getNumero();
	public Date getData();
	public BigDecimal getImportt();
	public FacturaEstatEnum getEstat();
	public SubministramentEntity getSubministrament();

}
