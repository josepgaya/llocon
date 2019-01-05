/**
 * 
 */
package com.josepgaya.llocon.core.service;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.josepgaya.llocon.core.dto.IdentificableWithParent;
import com.josepgaya.llocon.core.exception.WrongParentIdException;

import ma.glasnost.orika.MapperFacade;

/**
 * Implementació base del servei de gestió d'entitats que depenen
 * d'un identificador.
 * 
 * @author josepgaya
 */
@Service
public abstract class AbstractGenericParentServiceImpl<D extends IdentificableWithParent<ID, PID>, E extends AbstractPersistable<ID>, ID extends Serializable, PID extends Serializable> implements GenericParentService<D, ID, PID> {

	@Autowired
	protected MapperFacade orikaMapperFacade;

	@Override
	@Transactional
	public D create(
			PID parentId,
			D dto) {
		logger.debug("Creant entitat (" +
				"parentId=" + parentId + ", " +
				"dto=" + dto + ")");
		E entity = buildNewEntity(parentId, dto);
		return toDto(
				getRepository().save(entity));
	}

	@Override
	@Transactional
	public D update(
			PID parentId,
			ID id,
			D dto) {
		logger.debug("Modificant entitat (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ", " +
				"dto=" + dto + ")");
		E entity = getEntity(parentId, id);
		updateEntity(parentId, entity, dto);
		return toDto(entity);
	}

	@Override
	@Transactional
	public D patch(
			PID parentId,
			ID id,
			Patch patch) {
		logger.debug("Pedaçant entitat (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ", " +
				"patch=" + patch + ")");
		E entity = getEntity(parentId, id);
		D dto = toDto(entity);
		updateEntity(
				parentId,
				entity,
				patch.apply(dto, getDtoClass()));
		return toDto(entity);
	}

	/*@Override
	@Transactional
	public List<D> patch(
			PID parentId,
			ID[] ids,
			Patch patch) {
		logger.debug("Pedaçant entitats (" +
				"parentId=" + parentId + ", " +
				"ids=" + ids + ", " +
				"patch=" + patch + ")");
		List<E> entities = getEntities(parentId, ids);
		List<D> dtos = new ArrayList<D>();
		for (E entity: entities) {
			D dto = toDto(entity);
			updateEntity(
					parentId,
					entity,
					patch.apply(dto, getDtoClass()));
			dtos.add(toDto(entity));
		}
		return dtos;
	}*/

	@Override
	@Transactional
	public void delete(
			PID parentId,
			ID id) {
		logger.debug("Esborrant entitat (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		E entity = getEntity(parentId, id);
		getRepository().delete(entity);
	}

	/*@Override
	@Transactional
	public void delete(
			PID parentId,
			ID[] ids) {
		logger.debug("Esborrant entitats (" +
				"parentId=" + parentId + ", " +
				"ids=" + ids + ")");
		List<E> entities = getEntities(parentId, ids);
		getRepository().deleteInBatch(entities);
	}*/

	@Override
	@Transactional(readOnly = true)
	public D getOne(
			PID parentId,
			ID id) {
		logger.debug("Obtenint entitat (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		return toDto(getEntityAsObject(parentId, id));
	}

	@Override
	@Transactional(readOnly = true)
	public Page<D> findPageByFiltre(
			PID parentId,
			String filtre,
			Pageable pageable) {
		logger.debug("Consulta d'entitats amb filtre i paginació (" +
				"parentId=" + parentId + ", " +
				"filtre=" + filtre + ", " +
				"pageable=" + pageable + ")");
		Page<? extends Object> resultat = findPageByFiltreImpl(
				parentId,
				filtre,
				pageable);
		return toPageDto(resultat, pageable);
	}

	protected abstract E buildNewEntity(
			PID parentId,
			D dto);
	protected abstract void updateEntity(
			PID parentId,
			E entity,
			D dto);
	protected abstract Page<? extends Object> findPageByFiltreImpl(
			PID parentId,
			String filtre,
			Pageable pageable);
	protected abstract PID getParentId(E dto);
	protected abstract JpaRepository<E, ID> getRepository();
	protected abstract Class<D> getDtoClass();

	protected Object getEntityAsObject(
			PID parentId,
			ID id) {
		return getEntity(parentId, id);
	}
	protected E getEntity(
			PID parentId,
			ID id) {
		E entity = getRepository().getOne(id);
		if (!parentId.equals(getParentId(entity))) {
			throw new WrongParentIdException(
					parentId,
					id,
					getDtoClass());
		}
		return entity;
	}
	protected List<E> getEntities(
			PID parentId,
			ID[] ids) {
		List<E> entities = getRepository().findAllById(
				Arrays.asList(ids));
		for (E entity: entities) {
			if (!parentId.equals(getParentId(entity))) {
				throw new WrongParentIdException(
						parentId,
						entity.getId(),
						getDtoClass());
			}
		}
		return entities;
	}
	protected D toDto(Object entity) {
		D dto = orikaMapperFacade.map(
				entity,
				getDtoClass());
		configurarParentIds(Arrays.asList(dto));
		configureAdditionalData(Arrays.asList(dto));
		return dto;
	}
	protected List<D> toListDto(
			List<? extends Object> entityList) {
		List<D> dtos = orikaMapperFacade.mapAsList(
				entityList,
				getDtoClass());
		configurarParentIds(dtos);
		configureAdditionalData(dtos);
		return dtos;
	}
	protected Page<D> toPageDto(
			Page<? extends Object> entityPage,
			Pageable pageable) {
		Page<D> page = new PageImpl<D>(
				toListDto(entityPage.getContent()),
				pageable,
				entityPage.getTotalElements());
		return page;
	}
	protected void configurarParentIds(List<D> dtos) {
		List<ID> ids = new ArrayList<ID>();
		for (D dto: dtos) {
			ids.add(dto.getId());
		}
		List<E> entities = getRepository().findAllById(ids);
		for (int i = 0; i < entities.size(); i++) {
			dtos.get(i).setParentId(
					getParentId(entities.get(i)));
		}
	}
	protected void configureAdditionalData(List<D> dtos) {}

	private static final Logger logger = LoggerFactory.getLogger(AbstractGenericParentServiceImpl.class);

}
