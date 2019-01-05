/**
 * 
 */
package com.josepgaya.llocon.core.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.josepgaya.llocon.core.dto.SubministramentProducteEnum;
import com.josepgaya.llocon.core.dto.SubministramentProveidorEnum;
import com.josepgaya.llocon.core.entity.SubministramentEntity;

/**
 * Repository per a la gestió de subministraments.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface SubministramentRepository extends JpaRepository<SubministramentEntity, Long> {

	SubministramentAmbConnexio findByLloguerIdAndId(
			Long lloguerId,
			Long id);

	Page<SubministramentAmbConnexio> findByLloguerId(
			Long lloguerId,
			Pageable pageable);

	interface SubministramentAmbConnexio {
		Long getId();
		SubministramentProducteEnum getProducte();
		String getContracteNum();
		Date getDarreraActualitzacio();
		Connexio getConnexio();
	}
	interface Connexio {
		Long getId();
		String getNom();
		SubministramentProveidorEnum getProveidor();
	}

}
