/**
 * 
 */
package com.josepgaya.llocon.entity.projection;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.rest.core.config.Projection;

import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.entity.SubministramentProducteEnum;

/**
 * Projecció per a obtenir el submin. amb l'estructura desitjada de Spring
 * Data Rest.
 * 
 * @author josepgaya
 */
@Projection(name = "ambProveidor", types = { SubministramentEntity.class })
public interface SubministramentAmbProveidor {

	Long getId();
	SubministramentProducteEnum getProducte();
	String getContracteNum();
	Date getDarreraActualitzacio();
	@Value("#{target.connexio.proveidor}")
	String getConnexioProveidor();

}
