/**
 * 
 */
package com.josepgaya.llocon.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.josepgaya.llocon.entity.SubministramentEntity;

/**
 * Repository per a la gestió de subministraments.
 * 
 * @author josepgaya
 */
@RepositoryRestResource(
		collectionResourceRel = "subministrament",
		path = "subministrament")
public interface SubministramentRepository extends JpaRepository<SubministramentEntity, Long> {

	Page<SubministramentEntity> findByLloguerCodi(
			@Param("lloguerCodi") String lloguerCodi,
			Pageable pageable);

}
