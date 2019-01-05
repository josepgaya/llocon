/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.Subministrament;
import com.josepgaya.llocon.core.service.GenericParentService;
import com.josepgaya.llocon.core.service.SubministramentService;

/**
 * Controlador per al servei de gestió de subministraments.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping(value = AbstractApiController.API_PREFIX + SubministramentApiController.API_CONTROLLER_PATH)
public class SubministramentApiController extends AbstractParentApiController<Subministrament, Long, Long> {

	public static final String API_CONTROLLER_PATH = "/subministraments";

	@Autowired
	private SubministramentService subministramentService;
	@Autowired
	private FacturaApiController facturaApiController;

	@Override
	protected GenericParentService<Subministrament, Long, Long> getService() {
		return subministramentService;
	}

	@Override
	protected Class<Subministrament> getDtoClass() {
		return Subministrament.class;
	}

	@GetMapping(
			value = "/{id}/factures",
			produces = "application/json")
	public ResponseEntity<Resources<Resource<Factura>>> getFactures(
			HttpServletRequest request,
			@RequestParam(value = "parentId", required = true) final Long parentId,
			@PathVariable final Long id) {
		logger.debug("Obtenint factures del subministrament (" +
				"parentId=" + parentId + ", " +
				"id=" + id + ")");
		List<Factura> factures = subministramentService.getFactures(parentId, id);
		return ResponseEntity.ok(facturaApiController.toResources(factures));
	}

	private static final Logger logger = LoggerFactory.getLogger(SubministramentApiController.class);

}
