/**
 * 
 */
package com.josepgaya.llocon.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.josepgaya.llocon.scrap.Factura;
import com.josepgaya.llocon.service.SubministramentService;

/**
 * Controlador per a les accions personalitzades dels subministraments.
 * 
 * @author Limit Tecnologies <limit@limit.es>
 */
@RepositoryRestController
public class SubministramentCustomController {

	@Autowired
	private SubministramentService subministramentService;

	@RequestMapping(value = "/subministrament/{id}/refrescar")
	@ResponseBody
    public ResponseEntity<?> refrescar(
    		@PathVariable Long id) {
		List<Factura> factures = subministramentService.actualitzarFactures(id);
		List<Resource<Factura>> resources = new ArrayList<Resource<Factura>>();
		for (Factura factura: factures) {
			Resource<Factura> resource = new Resource<Factura>(factura);
			resources.add(resource);
		}
		return ResponseEntity.ok(resources);
	}

}
