/**
 * 
 */
package com.josepgaya.llocon.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.josepgaya.llocon.entity.LloguerEntity;

/**
 * Repository per a la gestió de lloguers.
 * 
 * @author josepgaya
 */
@RepositoryRestResource(collectionResourceRel = "lloguer", path = "lloguer")
public interface LloguerRepository extends JpaRepository<LloguerEntity, Long> {

	LloguerEntity findByCodi(String codi);

}
