/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.josepgaya.llocon.core.dto.Identificable;

import ma.glasnost.orika.MapperFacade;

/**
 * Implementació base del servei de gestió d'entitats que depenen
 * d'un identificador.
 * 
 * @author josepgaya
 */
@Service
public abstract class AbstractGenericServiceImpl<D extends Identificable<ID>, E extends AbstractPersistable<ID>, ID extends Serializable> implements GenericService<D, ID> {

	@Autowired
	protected MapperFacade orikaMapperFacade;

	@Override
	@Transactional
	public D create(D dto) {
		logger.debug("Creant entitat (" +
				"dto=" + dto + ")");
		E entity = buildNewEntity(dto);
		return toDto(
				getRepository().save(entity));
	}

	@Override
	@Transactional
	public D update(ID id, D dto) {
		logger.debug("Modificant entitat (" +
				"id=" + id + ", " +
				"dto=" + dto + ")");
		E entity = getEntity(id);
		updateEntity(entity, dto);
		return toDto(entity);
	}

	@Override
	@Transactional
	public D patch(ID id, Patch patch) {
		logger.debug("Pedaçant entitat (" +
				"id=" + id + ", " +
				"patch=" + patch + ")");
		E entity = getEntity(id);
		D dto = toDto(entity);
		updateEntity(
				entity,
				patch.apply(dto, getDtoClass()));
		return toDto(entity);
	}

	@Override
	@Transactional
	public void delete(ID id) {
		logger.debug("Esborrant entitat (" +
				"id=" + id + ")");
		E entity = getEntity(id);
		getRepository().delete(entity);
	}

	@Override
	@Transactional(readOnly = true)
	public D getOne(ID id) {
		logger.debug("Obtenint entitat (" +
				"id=" + id + ")");
		return toDto(getEntityAsObject(id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<D> findPageByFiltre(String filtre, Pageable pageable) {
		logger.debug("Consulta d'entitats amb filtre i paginació (" +
				"filtre=" + filtre + ", " +
				"pageable=" + pageable + ")");
		Page<? extends Object> resultat = findPageByFiltreImpl(
				filtre,
				pageable);
		return toPaginaDto(resultat, pageable);
	}

	protected abstract E buildNewEntity(D dto);
	protected abstract void updateEntity(E entity, D dto);
	protected abstract Page<? extends Object> findPageByFiltreImpl(
			String filtre,
			Pageable pageable);
	protected abstract JpaRepository<E, ID> getRepository();
	protected abstract Class<D> getDtoClass();

	protected Object getEntityAsObject(ID id) {
		return getEntity(id);
	}
	protected E getEntity(ID id) {
		return getRepository().getOne(id);
	}
	protected List<E> getEntities(ID[] ids) {
		return getRepository().findAllById(
				Arrays.asList(ids));
	}
	protected D toDto(Object entity) {
		D dto = orikaMapperFacade.map(
				entity,
				getDtoClass());
		configureAdditionalData(Arrays.asList(dto));
		return dto;
	}
	protected List<D> toListDto(
			List<? extends Object> entityList) {
		List<D> dtos = orikaMapperFacade.mapAsList(
				entityList,
				getDtoClass());
		configureAdditionalData(dtos);
		return dtos;
	}
	protected Page<D> toPaginaDto(
			Page<? extends Object> entityPage,
			Pageable pageable) {
		Page<D> page = new PageImpl<D>(
				toListDto(entityPage.getContent()),
				pageable,
				entityPage.getTotalElements());
		return page;
	}
	protected void configureAdditionalData(List<D> dtos) {}

	private static final Logger logger = LoggerFactory.getLogger(AbstractGenericServiceImpl.class);

}
