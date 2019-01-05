/**
 * 
 */
package com.josepgaya.llocon.back.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.josepgaya.llocon.core.dto.Factura;
import com.josepgaya.llocon.core.dto.File;
import com.josepgaya.llocon.core.service.FacturaService;
import com.josepgaya.llocon.core.service.GenericParentService;

/**
 * Controlador per al servei de gestió de factures.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping(value = AbstractApiController.API_PREFIX + FacturaApiController.API_CONTROLLER_PATH)
public class FacturaApiController extends AbstractParentApiController<Factura, Long, Long> {

	public static final String API_CONTROLLER_PATH = "/factures";

	@Autowired
	private FacturaService facturaService;

	@GetMapping(value = "/{id}/file")
	public ResponseEntity<ByteArrayResource> download(
			@RequestParam(value = "parentId", required = true) final Long parentId,
			@PathVariable final Long id) throws IOException {
		File file = facturaService.download(parentId, id);
	    ByteArrayResource resource = new ByteArrayResource(file.getContent());
	    HttpHeaders headers = new HttpHeaders();
	    headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + file.getName());
	    return ResponseEntity.ok()
	            .headers(headers)
	            .contentLength(file.getLength())
	            .contentType(MediaType.parseMediaType(file.getContentType()))
	            .body(resource);
	}

	@Override
	protected GenericParentService<Factura, Long, Long> getService() {
		return facturaService;
	}

	@Override
	protected Class<Factura> getDtoClass() {
		return Factura.class;
	}

}
