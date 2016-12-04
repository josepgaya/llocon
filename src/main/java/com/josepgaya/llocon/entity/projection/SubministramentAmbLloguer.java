/**
 * 
 */
package com.josepgaya.llocon.entity.projection;

import java.util.Date;

import org.springframework.data.rest.core.config.Projection;

import com.josepgaya.llocon.entity.ConnexioEntity;
import com.josepgaya.llocon.entity.LloguerEntity;
import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.entity.SubministramentProducteEnum;

/**
 * Projecció per a obtenir el submin. amb l'estructura desitjada de Spring
 * Data Rest.
 * 
 * @author josepgaya
 */
@Projection(name = "subministramentAmbLloguer", types = { SubministramentEntity.class })
public interface SubministramentAmbLloguer {

	public Long getId();
	public SubministramentProducteEnum getProducte();
	public String getContracteNum();
	public Date getDarreraActualitzacio();
	public LloguerEntity getLloguer();
	public ConnexioEntity getConnexio();

}
