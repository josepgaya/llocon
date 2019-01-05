/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.io.Serializable;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.json.patch.Patch;

import com.josepgaya.llocon.core.dto.Identificable;
import com.josepgaya.llocon.core.exception.PermissionDeniedException;

/**
 * Mètodes bàsics per als serveis que gestionen entitats que NO
 * estan relacionades amb cap identificador o empresa.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
public interface GenericService<D extends Identificable<ID>, ID extends Serializable> {

	/**
	 * Crea una nova entitat.
	 * 
	 * @param dto
	 *            informació de l'entitat.
	 * @return l'entitat creada.
	 * @throws PermissionDeniedException
	 *             si l'usuari no te permisos per realitzar aquesta acció.
	 */
	public D create(
			D dto) throws PermissionDeniedException;

	/**
	 * Actualitza la informació d'una entitat.
	 * 
	 * @param id
	 *            identificació de l'entitat.
	 * @param dto
	 *            informació de l'entitat.
	 * @return l'entitat modificada.
	 * @throws EntityNotFoundException
	 *             si no s'ha trobat l'entitat especificada.
	 * @throws PermissionDeniedException
	 *             si l'usuari no te permisos per realitzar aquesta acció.
	 */
	public D update(
			ID id,
			D dto) throws EntityNotFoundException, PermissionDeniedException;

	/**
	 * Modifica una entitat aplicant un pedaç.
	 * 
	 * @param id
	 *            identificació de l'entitat.
	 * @param patch
	 *            el pedaç a aplicar.
	 * @return l'entitat modificada.
	 * @throws EntityNotFoundException
	 *             si no s'ha trobat l'entitat especificada.
	 * @throws PermissionDeniedException
	 *             si l'usuari no te permisos per realitzar aquesta acció.
	 */
	public D patch(
			ID id,
			Patch patch) throws EntityNotFoundException, PermissionDeniedException;

	/**
	 * Esborra l'entitat donada la seva identificació.
	 * 
	 * @param id
	 *            identificació de l'entitat.
	 * @throws EntityNotFoundException
	 *             si no s'ha trobat l'entitat especificada.
	 * @throws PermissionDeniedException
	 *             si l'usuari no te permisos per realitzar aquesta acció.
	 */
	public void delete(
			ID id) throws EntityNotFoundException, PermissionDeniedException;

	/**
	 * Consulta una entitat donada la seva identificació.
	 * 
	 * @param id
	 *            identificació de l'entitat.
	 * @return l'entitat amb la identificació especificada.
	 * @throws EntityNotFoundException
	 *             si no s'ha trobat l'entitat especificada.
	 */
	public D getOne(
			ID id) throws EntityNotFoundException;

	/**
	 * Llistat genèric d'entitats amb filtre textual.
	 * 
	 * @param filtre
	 *            text per a filtrar els resultats.
	 * @param pageable
	 *            paràmetres per a paginar i ordenar el llistat.
	 * @return la llista d'entitats.
	 */
	public Page<D> findPageByFiltre(
			String filtre,
			Pageable pageable);

}
