/**
 * 
 */
package com.josepgaya.llocon.core.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.josepgaya.llocon.core.entity.ConnexioEntity;

/**
 * Repository per a la gestió de connexions.
 * 
 * @author josepgaya
 */
public interface ConnexioRepository extends JpaRepository<ConnexioEntity, Long> {

	@Query(
			"from " +
			"    ConnexioEntity c " +
			"where " +
			"    :esNullFiltre = true " + 
			" or lower(c.nom) like lower('%'|| :filtre ||'%') ")
	public Page<ConnexioEntity> findByFiltre(
			@Param("esNullFiltre") boolean esNullFiltre,
			@Param("filtre") String filtre,
			Pageable pageable);

}
