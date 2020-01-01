/**
 * 
 */
package com.josepgaya.llocon.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.josepgaya.llocon.core.entity.LloguerEntity;

/**
 * Repository per a la gestió de lloguers.
 * 
 * @author josepgaya
 */
public interface LloguerRepository extends JpaRepository<LloguerEntity, Long> {

	@Query(
			"from " +
			"    LloguerEntity s " +
			"where " +
			"    :esNullFiltre = true " + 
			" or lower(s.codi) like lower('%'|| :filtre ||'%') " +
			" or lower(s.nom) like lower('%'|| :filtre ||'%') ")
	public Page<LloguerEntity> findByFiltre(
			@Param("esNullFiltre") boolean esNullFiltre,
			@Param("filtre") String filtre,
			Pageable pageable);

}
