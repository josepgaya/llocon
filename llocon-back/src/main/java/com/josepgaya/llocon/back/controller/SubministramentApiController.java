/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.base.boot.back.controller.AbstractIdentificableApiController;
import com.josepgaya.base.boot.back.controller.ApiControllerHelper.SelfLinkBuilder;
import com.josepgaya.base.boot.logic.api.controller.GenericController;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.Subministrament;
import com.josepgaya.llocon.logic.api.service.SubministramentService;

import lombok.extern.slf4j.Slf4j;

/**
 * Controlador pel servei REST de gestió de subministraments.
 * 
 * @author josepgaya
 */
@Slf4j
@RestController
@RequestMapping(GenericController.API_PATH + "/subministraments")
public class SubministramentApiController extends AbstractIdentificableApiController<Subministrament, Long> {
	public static final String API_CONTROLLER_PATH = "/subministraments";

	@Autowired
	private SubministramentService subministramentService;

	@GetMapping(
			value = "/{id}/factures",
			produces = "application/json")
	public ResponseEntity<CollectionModel<EntityModel<Factura>>> getFactures(
			HttpServletRequest request,
			@RequestParam(value = "parentId", required = true) final Long parentId,
			@PathVariable final Long id) {
		log.debug("Obtenint factures del subministrament (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		List<Factura> factures = subministramentService.getFactures(parentId, id);
		return ResponseEntity.ok(
				toResources(
						factures,
						FacturaApiController.class,
						new SelfLinkBuilder() {
							@Override
							public Link build(Class<?> apiControllerClass, Object... params) {
								return getSelfLink(params);
							}
						},
						getApiLink(IanaLinkRelations.SELF),
						getProfileLink("profile")));
	}

}
