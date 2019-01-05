/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.io.Serializable;
import java.net.URI;

import javax.persistence.EntityNotFoundException;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.json.patch.JsonPatchPatchConverter;
import org.springframework.data.rest.webmvc.json.patch.Patch;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.josepgaya.llocon.core.dto.IdentificableWithParent;
import com.josepgaya.llocon.core.service.GenericParentService;

/**
 * Mètodes bàsics per als controladors REST que gestionen un
 * recurs del tipus Identificable.
 * 
 * @author josepgaya
 */
public abstract class AbstractParentApiController<D extends IdentificableWithParent<ID, PID>, ID extends Serializable, PID extends Serializable> extends CommonApiController<D, ID> {

	public static final String API_PREFIX = "/api";

	@Autowired
	private ObjectMapper objectMapper;

	@GetMapping(
			value = "/{id}",
			produces = "application/json")
	public ResponseEntity<Resource<D>> getOne(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			@PathVariable final ID id) {
		logger.debug("Obtenint recurs (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		try {
			D dto = getService().getOne(
					parentId,
					id);
			return ResponseEntity.ok(toResource(dto));
		} catch (EntityNotFoundException ex) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping(
			produces = "application/json")
	public ResponseEntity<Resource<D>> create(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			@RequestBody @Valid final D dto,
			@RequestParam(required = false) boolean validate) {
		if (!validate) {
			logger.debug("Creant recurs (" +
					"parentId=" + parentId + ", " +
					"dto=" + dto + ")");
			D creat = getService().create(
					parentId,
					dto);
			final URI uri = MvcUriComponentsBuilder.fromController(getClass()).
					path("/{id}").
		            buildAndExpand(creat.getId()).
		            toUri();
			return ResponseEntity.created(uri).body(
					toResource(creat));
		} else {
			logger.debug("Validant entitat per creació (" +
					"dto=" + dto + ")");
			return ResponseEntity.ok(null);
		}
	}

	@PutMapping(
			value = "/{id}",
			produces = "application/json")
	public ResponseEntity<Resource<D>> update(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			@PathVariable final ID id,
			@RequestBody @Valid final D dto,
			@RequestParam(required = false) boolean validate) {
		if (!validate) {
			logger.debug("Modificant recurs (" +
					"parentId=" + parentId + ", " +
					"id=" + id + ", " +
					"dto=" + dto + ")");
			D modificat = getService().update(
					parentId,
					id,
					dto);
			return ResponseEntity.ok(toResource(modificat));
		} else {
			logger.debug("Validant recurs per modificació (" +
					"id=" + id + ", " +
					"dto=" + dto + ")");
			return ResponseEntity.ok(null);
		}
	}

	@PatchMapping(
			value = "/{id}",
			produces = "application/json")
	public ResponseEntity<Resource<D>> patch(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			@PathVariable final ID id,
			@RequestBody final JsonNode jsonNode) {
		logger.debug("Modificant el recurs de forma parcial (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ", " +
				"jsonNode=" + jsonNode + ")");
		Patch patch = new JsonPatchPatchConverter(objectMapper).convert(jsonNode);
		D modificat = getService().patch(
				parentId,
				id,
				patch);
		return ResponseEntity.ok(toResource(modificat));
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<?> delete(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			@PathVariable final ID id) {
		logger.debug("Esborrant recurs (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		getService().delete(
				parentId,
				id);
		return ResponseEntity.ok().build();
	}

	@GetMapping(
			produces = "application/json")
	public ResponseEntity<PagedResources<Resource<D>>> find(
			@RequestParam(value = "parentId", required = true) final PID parentId,
			final String filtre,
			final Pageable pageable) {
		logger.debug("Consulta de recursos (" +
				"parentId=" + parentId + ", " +
				"filtre=" + filtre + ", " +
				"pageable=" + pageable + ")");
		Page<D> pagina = getService().findPageByFiltre(
				parentId,
				filtre,
				pageable);
		return ResponseEntity.ok(toPagedResources(pagina));
	}

	protected abstract GenericParentService<D, ID, PID> getService();
	protected abstract Class<D> getDtoClass();

	@SuppressWarnings("unchecked")
	protected Link getSelfLink() {
		Link selfLink = linkTo(methodOn(getClass()).getOne(null, null)).withSelfRel();
		return selfLink;
	}

	private static final Logger logger = LoggerFactory.getLogger(AbstractParentApiController.class);

}
