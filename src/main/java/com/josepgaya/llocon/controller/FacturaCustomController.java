/**
 * 
 */
package com.josepgaya.llocon.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.data.rest.webmvc.support.RepositoryEntityLinks;
import org.springframework.hateoas.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.josepgaya.llocon.dto.ArxiuDto;
import com.josepgaya.llocon.entity.FacturaEntity;
import com.josepgaya.llocon.service.FacturaService;

/**
 * Controlador per a les accions personalitzades de les factures.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RepositoryRestController
public class FacturaCustomController {

	@Autowired
	private FacturaService facturaService;
	@Autowired
	private RepositoryEntityLinks entityLinks;

	@RequestMapping(value = "/factura/{id}/canviEstat")
	@ResponseBody
    public ResponseEntity<?> canviEstat(
    		@PathVariable Long id) {
		FacturaEntity factura = facturaService.seguentEstat(id);
		Resource<FacturaEntity> resource = new Resource<FacturaEntity>(factura);
		resource.add(entityLinks.linkForSingleResource(FacturaEntity.class, id).withSelfRel());
		return ResponseEntity.ok(resource);
	}

	@RequestMapping(value = "/factura/{id}/descarregarRest")
	@ResponseBody
    public ResponseEntity<?> descarregarRest(
    		@PathVariable Long id) {
		ArxiuDto arxiu = facturaService.descarregar(id);
		ByteArrayResource resource = new ByteArrayResource(arxiu.getContingut());
		return ResponseEntity.ok().
        		contentType(MediaType.APPLICATION_PDF).
        		contentLength(arxiu.getContingut().length).
        		header("Content-Disposition", "attachment; filename=" + arxiu.getNom()).
        		body(resource);
	}

	@RequestMapping(value = "/factura/{id}/descarregar")
	public void descarregar(
    		HttpServletResponse response,
    		@PathVariable Long id) throws Exception {
        ArxiuDto arxiu = facturaService.descarregar(id);
        response.setContentType(arxiu.getContentType());
        response.setHeader("Content-Disposition", "attachment; filename=" + arxiu.getNom());
        response.getOutputStream().write(arxiu.getContingut());
    }

}
