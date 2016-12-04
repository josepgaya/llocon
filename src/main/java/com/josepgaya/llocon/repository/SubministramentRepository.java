/**
 * 
 */
package com.josepgaya.llocon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.josepgaya.llocon.entity.LloguerEntity;
import com.josepgaya.llocon.entity.SubministramentEntity;
import com.josepgaya.llocon.entity.SubministramentProducteEnum;
import com.josepgaya.llocon.entity.SubministramentProveidorEnum;
import com.josepgaya.llocon.entity.projection.SubministramentAmbLloguer;

/**
 * Repository per a la gestió de subministraments.
 * 
 * @author josepgaya
 */
@RepositoryRestResource(
		collectionResourceRel = "subministrament",
		path = "subministrament",
		excerptProjection = SubministramentAmbLloguer.class)
public interface SubministramentRepository extends JpaRepository<SubministramentEntity, Long> {

	SubministramentEntity findByLloguerAndProducteAndConnexioProveidor(
			LloguerEntity lloguer,
			SubministramentProducteEnum producte,
			SubministramentProveidorEnum proveidor);

}
