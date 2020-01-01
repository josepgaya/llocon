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

import com.josepgaya.base.boot.back.controller.AbstractIdentificableApiController;
import com.josepgaya.base.boot.logic.api.controller.GenericController;
import com.josepgaya.llocon.logic.api.dto.Factura;
import com.josepgaya.llocon.logic.api.dto.File;
import com.josepgaya.llocon.logic.api.service.FacturaService;

/**
 * Controlador pel servei REST de gestió de factures.
 * 
 * @author josepgaya
 */
@RestController
@RequestMapping(GenericController.API_PATH + "/factures")
public class FacturaApiController extends AbstractIdentificableApiController<Factura, Long> {

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

}
