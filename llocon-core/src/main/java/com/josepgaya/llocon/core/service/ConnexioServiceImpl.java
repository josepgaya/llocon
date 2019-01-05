/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.security.GeneralSecurityException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.josepgaya.llocon.core.dto.Connexio;
import com.josepgaya.llocon.core.entity.ConnexioEntity;
import com.josepgaya.llocon.core.exception.GenericServiceException;
import com.josepgaya.llocon.core.repository.ConnexioRepository;

/**
 * Implementació del service per a la gestió de connexions.
 * 
 * @author josepgaya
 */
@Service
public class ConnexioServiceImpl extends AbstractGenericServiceImpl<Connexio, ConnexioEntity, Long> implements ConnexioService {

	@Autowired
	private ConnexioRepository connexioRepository;

	@Override
	protected ConnexioEntity buildNewEntity(
			Connexio dto) {
		try {
			return ConnexioEntity.getBuilder(
					dto.getNom(),
					dto.getProveidor(),
					dto.getUsuari(),
					dto.getContrasenya()).
					build();
		} catch (GeneralSecurityException ex) {
			throw new GenericServiceException(
					"Error al encriptar la contrasenya",
					ex);
		}
	}

	@Override
	protected void updateEntity(
			ConnexioEntity entity,
			Connexio dto) {
		try {
			entity.update(
					dto.getNom(),
					dto.getProveidor(),
					dto.getUsuari(),
					dto.getContrasenya());
		} catch (GeneralSecurityException ex) {
			throw new GenericServiceException(
					"Error al encriptar la contrasenya",
					ex);
		}
	}

	@Override
	protected Page<ConnexioEntity> findPageByFiltreImpl(
			String filtre,
			Pageable pageable) {
		return connexioRepository.findByFiltre(
				filtre == null || filtre.isEmpty(),
				filtre,
				pageable);
	}

	@Override
	protected JpaRepository<ConnexioEntity, Long> getRepository() {
		return connexioRepository;
	}

	@Override
	protected Class<Connexio> getDtoClass() {
		return Connexio.class;
	}

}
